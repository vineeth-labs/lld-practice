import model.*;
import model.exception.*;

import java.math.BigDecimal;
import java.time.Duration;
import java.time.Instant;
import java.util.List;
import java.util.UUID;

/**
 * Orchestrates the booking lifecycle. Delegates seat concurrency to
 * {@link SeatLockManager}, persistence to the repositories, and payment to the
 * {@link PaymentGateway}. Expiry is handled out-of-band by {@link BookingExpiryWorker}.
 */
public class BookingService {
    private final PaymentGateway paymentGateway;
    private final SeatLockManager seatLockManager;
    private final BookingRepository bookingRepository;
    private final ShowRepository showRepository;
    private final Duration lockTtl;

    public BookingService(PaymentGateway paymentGateway) {
        this(paymentGateway, Duration.ofMinutes(10));
    }

    /** Convenience wiring with default in-memory collaborators. */
    public BookingService(PaymentGateway paymentGateway, Duration lockTtl) {
        this(paymentGateway, new SeatLockManager(lockTtl),
                new InMemoryBookingRepository(), new InMemoryShowRepository(), lockTtl);
    }

    public BookingService(PaymentGateway paymentGateway,
                          SeatLockManager seatLockManager,
                          BookingRepository bookingRepository,
                          ShowRepository showRepository,
                          Duration lockTtl) {
        this.paymentGateway = paymentGateway;
        this.seatLockManager = seatLockManager;
        this.bookingRepository = bookingRepository;
        this.showRepository = showRepository;
        this.lockTtl = lockTtl;
    }

    public void registerShow(Show show) {
        showRepository.save(show);
        seatLockManager.registerShow(show);
    }

    public Booking createBooking(String userId, String showId, List<String> seatIds) {
        LockResult result = seatLockManager.lock(userId, showId, seatIds);
        if (!result.success) {
            throw new SeatUnavailableException(result.unavailableSeatIds);
        }
        Booking booking = new Booking(UUID.randomUUID().toString(), userId, showId, result.lockedSeats,
                BookingStatus.PENDING_PAYMENT,
                Instant.now(), Instant.now().plus(lockTtl));
        bookingRepository.save(booking);
        return booking;
    }

    public Booking confirmBooking(String bookingId) {
        Booking booking = bookingRepository.findById(bookingId);
        if (booking == null) throw new BookingNotFoundException(bookingId);

        // Per-booking lock: serialize with the expiry worker so it can't reclaim a
        // booking out from under us while payment is in flight.
        synchronized (booking) {
            // idempotency / state guard
            if (booking.getBookingStatus() != BookingStatus.PENDING_PAYMENT)
                throw new InvalidBookingStateException(booking.getBookingStatus());

            // hold window guard — if TTL elapsed, treat as expired
            if (Instant.now().isAfter(booking.getExpiresAt())) {
                seatLockManager.release(booking.getUserId(), booking.getShowSeats());
                booking.setBookingStatus(BookingStatus.EXPIRED);
                throw new BookingExpiredException(bookingId);
            }

            BigDecimal total = booking.getShowSeats().stream()
                    .map(ShowSeat::getPrice)
                    .reduce(BigDecimal.ZERO, BigDecimal::add);

            Payment payment = paymentGateway.makePayment(booking.getUserId(), booking, total);
            booking.setPayment(payment);

            if (payment.getStatus() != PaymentStatus.SUCCESS) {
                seatLockManager.release(booking.getUserId(), booking.getShowSeats());
                booking.setBookingStatus(BookingStatus.CANCELLED);
                throw new PaymentFailedException("Payment declined", payment);
            }

            if (!seatLockManager.confirmAll(booking.getUserId(), booking.getShowSeats())) {
                seatLockManager.release(booking.getUserId(), booking.getShowSeats());
                booking.setBookingStatus(BookingStatus.CANCELLED);
                // TODO(refund): payment succeeded but seats lost -> paymentGateway.refund(payment)
                throw new PaymentFailedException("Lost seat lock after charge; refund required", payment);
            }

            booking.setBookingStatus(BookingStatus.CONFIRMED);
            return booking;
        }
    }
}
