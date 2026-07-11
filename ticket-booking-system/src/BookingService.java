import model.*;

import java.math.BigDecimal;
import java.time.Duration;
import java.time.Instant;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class BookingService {
    Map<String, Show> shows;
    List<Seat> seats;
    Map<String, Booking> bookings;
    ConcurrentHashMap<String, ConcurrentHashMap<String, ShowSeat>> showSeats;
    private final Duration LOCK_TTL = Duration.ofMinutes(10);

    private final PaymentGateway paymentGateway;

    public BookingService(PaymentGateway paymentGateway) {
        this.paymentGateway = paymentGateway;
        this.shows = new ConcurrentHashMap<>();
        this.bookings = new ConcurrentHashMap<>();
        this.showSeats = new ConcurrentHashMap<>();
    }

    private LockResult lockSeat(String userId, String showId, List<String> seatIds) {
        boolean ok = false;
        List<String> ordered = seatIds.stream().distinct().sorted().toList();
        List<ShowSeat> acquired = new ArrayList<>();
        try {
            for (String seatId : ordered) {
                ShowSeat showSeat = showSeats.get(showId).get(seatId);
                if(!showSeat.tryLock(userId, LOCK_TTL, Instant.now())) {
                    return LockResult.failure(List.of(seatId));
                }
                acquired.add(showSeat);
            }
            ok = true;
            return LockResult.success(acquired);
        } finally {
            if(!ok) acquired.forEach(s -> s.release(userId));
        }
    }

    public Booking createBooking(String userId, String showId, List<String> seatIds) {

        LockResult result = lockSeat(userId, showId, seatIds);
        if (!result.success) {
            throw new RuntimeException("Could not create booking");
        }
        Booking booking = new Booking(UUID.randomUUID().toString(), userId, showId, result.lockedSeats,
                BookingStatus.PENDING_PAYMENT,
                Instant.now(), Instant.now().plus(LOCK_TTL));
        bookings.put(booking.getId(), booking);
        return booking;
    }

    public Booking confirmBooking(String bookingId) {
        Booking booking = bookings.get(bookingId);
        if (booking == null) throw new RuntimeException("Booking not found");

        // idempotency / state guard
        if (booking.getBookingStatus() != BookingStatus.PENDING_PAYMENT)
            throw new RuntimeException("Booking not payable: " + booking.getBookingStatus());

        // hold window guard — if TTL elapsed, treat as expired
        if (Instant.now().isAfter(booking.getExpiresAt())) {
            releaseAll(booking);
            booking.setBookingStatus(BookingStatus.CANCELLED);
            throw new RuntimeException("Booking hold expired");
        }

        BigDecimal total = booking.getShowSeats().stream()
                .map(ShowSeat::getPrice)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        Payment payment = PaymentGateway.makePayment(booking.getUserId(), booking, total);
        booking.setPayment(payment);

        if (payment.getStatus() != PaymentStatus.SUCCESS) {
            releaseAll(booking);
            booking.setBookingStatus(BookingStatus.CANCELLED);
            throw new RuntimeException("Payment failed");
        }

        // LOCKED -> BOOKED for every seat. If any confirm() fails, our lock was lost
        // (expired/stolen). Abort: release what we still hold, cancel, flag refund.
        for (ShowSeat s : booking.getShowSeats()) {
            if (!s.confirm(booking.getUserId())) {
                releaseAll(booking);
                booking.setBookingStatus(BookingStatus.CANCELLED);
                // TODO(refund): payment succeeded but seats lost -> paymentGateway.refund(payment)
                throw new RuntimeException("Lost seat lock during confirm");
            }
        }

        booking.setBookingStatus(BookingStatus.CONFIRMED);
        return booking;
    }

    private void releaseAll(Booking booking) {
        booking.getShowSeats().forEach(s -> s.release(booking.getUserId()));
    }
}
