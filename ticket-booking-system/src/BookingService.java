import model.*;
import model.exception.*;

import java.math.BigDecimal;
import java.time.Duration;
import java.time.Instant;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class BookingService {
    Map<String, Show> shows;
    List<Seat> seats;
    Map<String, Booking> bookings;
    ConcurrentHashMap<String, ConcurrentHashMap<String, ShowSeat>> showSeats;
    private final Duration lockTtl;

    private final PaymentGateway paymentGateway;
    private final ScheduledExecutorService sweeper =
            Executors.newSingleThreadScheduledExecutor(r -> {
                Thread t = new Thread(r, "booking-expiry-sweeper");
                t.setDaemon(true);
                return t;
            });

    public BookingService(PaymentGateway paymentGateway) {
        this(paymentGateway, Duration.ofMinutes(10));
    }

    public BookingService(PaymentGateway paymentGateway, Duration lockTtl) {
        this.paymentGateway = paymentGateway;
        this.lockTtl = lockTtl;
        this.shows = new ConcurrentHashMap<>();
        this.bookings = new ConcurrentHashMap<>();
        this.showSeats = new ConcurrentHashMap<>();
    }

    /** Starts the background sweeper that expires stale PENDING_PAYMENT bookings. */
    public void startSweeper(Duration interval) {
        long millis = interval.toMillis();
        sweeper.scheduleAtFixedRate(this::sweepExpired, millis, millis, TimeUnit.MILLISECONDS);
    }

    /** Releases seat locks for any PENDING_PAYMENT booking whose hold window has elapsed. */
    void sweepExpired() {
        Instant now = Instant.now();
        for (Booking booking : bookings.values()) {
            // Per-booking lock: never race a confirmBooking that's mid-flight.
            synchronized (booking) {
                if (booking.getBookingStatus() == BookingStatus.PENDING_PAYMENT
                        && now.isAfter(booking.getExpiresAt())) {
                    releaseAll(booking);
                    booking.setBookingStatus(BookingStatus.EXPIRED);
                }
            }
        }
    }

    public void shutdown() {
        sweeper.shutdownNow();
    }

    /** Registers a show and indexes its show-seats by seatId for lookup during booking. */
    public void registerShow(Show show) {
        shows.put(show.getId(), show);
        ConcurrentHashMap<String, ShowSeat> seatMap = new ConcurrentHashMap<>();
        for (ShowSeat showSeat : show.getShowSeats()) {
            seatMap.put(showSeat.getId(), showSeat);
        }
        showSeats.put(show.getId(), seatMap);
    }

    private LockResult lockSeat(String userId, String showId, List<String> seatIds) {
        boolean ok = false;
        List<String> ordered = seatIds.stream().distinct().sorted().toList();
        List<ShowSeat> acquired = new ArrayList<>();
        try {
            for (String seatId : ordered) {
                ShowSeat showSeat = showSeats.get(showId).get(seatId);
                if(!showSeat.tryLock(userId, lockTtl, Instant.now())) {
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
            throw new SeatUnavailableException(result.unavailableSeatIds);
        }
        Booking booking = new Booking(UUID.randomUUID().toString(), userId, showId, result.lockedSeats,
                BookingStatus.PENDING_PAYMENT,
                Instant.now(), Instant.now().plus(lockTtl));
        bookings.put(booking.getId(), booking);
        return booking;
    }

    public Booking confirmBooking(String bookingId) {
        Booking booking = bookings.get(bookingId);
        if (booking == null) throw new BookingNotFoundException(bookingId);

        // Per-booking lock: serialize with the sweeper so it can't expire a booking
        // out from under us while payment is in flight.
        synchronized (booking) {
            // idempotency / state guard
            if (booking.getBookingStatus() != BookingStatus.PENDING_PAYMENT)
                throw new InvalidBookingStateException(booking.getBookingStatus());

            // hold window guard — if TTL elapsed, treat as expired
            if (Instant.now().isAfter(booking.getExpiresAt())) {
                releaseAll(booking);
                booking.setBookingStatus(BookingStatus.EXPIRED);
                throw new BookingExpiredException(bookingId);
            }

            BigDecimal total = booking.getShowSeats().stream()
                    .map(ShowSeat::getPrice)
                    .reduce(BigDecimal.ZERO, BigDecimal::add);

            Payment payment = paymentGateway.makePayment(booking.getUserId(), booking, total);
            booking.setPayment(payment);

            if (payment.getStatus() != PaymentStatus.SUCCESS) {
                releaseAll(booking);
                booking.setBookingStatus(BookingStatus.CANCELLED);
                throw new PaymentFailedException("Payment declined", payment);
            }

            // LOCKED -> BOOKED for every seat. If any confirm() fails, our lock was lost
            // (expired/stolen). Abort: release what we still hold, cancel, flag refund.
            for (ShowSeat s : booking.getShowSeats()) {
                if (!s.confirm(booking.getUserId())) {
                    releaseAll(booking);
                    booking.setBookingStatus(BookingStatus.CANCELLED);
                    // TODO(refund): payment succeeded but seats lost -> paymentGateway.refund(payment)
                    throw new PaymentFailedException("Lost seat lock after charge; refund required", payment);
                }
            }

            booking.setBookingStatus(BookingStatus.CONFIRMED);
            return booking;
        }
    }

    private void releaseAll(Booking booking) {
        booking.getShowSeats().forEach(s -> s.release(booking.getUserId()));
    }
}
