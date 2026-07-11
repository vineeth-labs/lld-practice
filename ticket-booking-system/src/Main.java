import model.*;

import java.math.BigDecimal;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicInteger;

public class Main {

    public static void main(String[] args) throws InterruptedException {
        contentionDemo();
        System.out.println("\n==================================================\n");
        expiryDemo();
    }

    /** Builds a show with `numSeats` REGULAR seats (ids ss1..ssN) and registers it. */
    private static Show buildShow(BookingService service, String showId, int numSeats) {
        Movie movie = new Movie("m1", "Inception");
        Screen screen = new Screen("scr1", "Screen 1");
        Show show = new Show(showId, screen, movie, LocalDateTime.now().plusHours(3));
        for (int i = 1; i <= numSeats; i++) {
            Seat seat = new Seat("seat" + i, "A" + i, SeatType.REGULAR);
            screen.addSeat(seat);
            show.addShowSeat(new ShowSeat("ss" + i, seat, show, new BigDecimal("250.00")));
        }
        service.registerShow(show);
        return show;
    }

    private static void printSeats(Show show) {
        for (ShowSeat s : show.getShowSeats()) {
            System.out.println("  " + s.getSeat().getSeatNumber() + " (" + s.getId() + "): " + s.getStatus());
        }
    }

    /** N users race for the SAME two seats — exactly one should win. */
    private static void contentionDemo() throws InterruptedException {
        System.out.println("--- Contention demo: 8 users, same 2 seats ---");
        BookingService bookingService = new BookingService(new PaymentGateway());
        Show show = buildShow(bookingService, "show1", 5);

        List<String> contestedSeats = List.of("ss1", "ss2");
        int numUsers = 8;
        CountDownLatch start = new CountDownLatch(1);
        CountDownLatch done = new CountDownLatch(numUsers);
        AtomicInteger confirmed = new AtomicInteger();

        for (int u = 0; u < numUsers; u++) {
            final String userId = "user" + u;
            new Thread(() -> {
                try {
                    start.await();                  // release all threads at once
                    Booking booking = bookingService.createBooking(userId, "show1", contestedSeats);
                    Booking result = bookingService.confirmBooking(booking.getId());
                    confirmed.incrementAndGet();
                    System.out.println("✓ " + userId + " booked "
                            + booking.getId().substring(0, 8) + " -> " + result.getBookingStatus());
                } catch (Exception e) {
                    System.out.println("✗ " + userId + " failed: " + e.getMessage());
                } finally {
                    done.countDown();
                }
            }).start();
        }
        start.countDown();
        done.await();

        System.out.println("Final seat states:");
        printSeats(show);
        System.out.println("Confirmed bookings: " + confirmed.get() + " (expected exactly 1)");
    }

    /** A user locks seats but never pays; the sweeper releases the hold. */
    private static void expiryDemo() throws InterruptedException {
        System.out.println("--- Expiry demo: hold seats, never pay, let sweeper reclaim ---");
        // Short TTL so we don't wait 10 minutes; sweep frequently.
        BookingService bookingService = new BookingService(new PaymentGateway(), Duration.ofSeconds(1));
        Show show = buildShow(bookingService, "show1", 3);
        bookingService.startSweeper(Duration.ofMillis(500));

        Booking abandoned = bookingService.createBooking("lazyUser", "show1", List.of("ss1", "ss2"));
        System.out.println("Created booking " + abandoned.getId().substring(0, 8)
                + " -> " + abandoned.getBookingStatus());
        System.out.println("Seats right after locking:");
        printSeats(show);

        // Wait past TTL + a sweep cycle without ever calling confirmBooking.
        Thread.sleep(2000);

        System.out.println("Booking status after TTL elapses: " + abandoned.getBookingStatus());
        System.out.println("Seats after sweeper ran:");
        printSeats(show);

        // Seats are free again — another user can now book them.
        try {
            Booking rebook = bookingService.createBooking("promptUser", "show1", List.of("ss1", "ss2"));
            Booking result = bookingService.confirmBooking(rebook.getId());
            System.out.println("promptUser re-booked reclaimed seats -> " + result.getBookingStatus());
        } catch (Exception e) {
            System.out.println("promptUser failed: " + e.getMessage());
        }
        printSeats(show);

        bookingService.shutdown();
    }
}
