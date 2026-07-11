import model.Booking;
import model.BookingStatus;

import java.time.Duration;
import java.time.Instant;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * Background job: reclaims seats from PENDING_PAYMENT bookings whose hold window
 * elapsed. Separate lifecycle from the booking flow — depends only on the
 * repository and the lock manager.
 */
public class BookingExpiryWorker {
    private final BookingRepository bookingRepository;
    private final SeatLockManager seatLockManager;
    private final ScheduledExecutorService scheduler =
            Executors.newSingleThreadScheduledExecutor(r -> {
                Thread t = new Thread(r, "booking-expiry-worker");
                t.setDaemon(true);
                return t;
            });

    public BookingExpiryWorker(BookingRepository bookingRepository, SeatLockManager seatLockManager) {
        this.bookingRepository = bookingRepository;
        this.seatLockManager = seatLockManager;
    }

    public void start(Duration interval) {
        long millis = interval.toMillis();
        scheduler.scheduleAtFixedRate(this::sweep, millis, millis, TimeUnit.MILLISECONDS);
    }

    void sweep() {
        Instant now = Instant.now();
        for (Booking booking : bookingRepository.findAll()) {
            // Per-booking lock: never race a confirmBooking that's mid-flight.
            synchronized (booking) {
                if (booking.getBookingStatus() == BookingStatus.PENDING_PAYMENT
                        && now.isAfter(booking.getExpiresAt())) {
                    seatLockManager.release(booking.getUserId(), booking.getShowSeats());
                    booking.setBookingStatus(BookingStatus.EXPIRED);
                }
            }
        }
    }

    public void stop() {
        scheduler.shutdownNow();
    }
}
