import model.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicInteger;

public class Main {

    public static void main(String[] args) throws InterruptedException {
        BookingService bookingService = new BookingService(new PaymentGateway());

        // --- Bootstrap a show with 5 seats ---
        Movie movie = new Movie("m1", "Inception");
        Screen screen = new Screen("scr1", "Screen 1");
        Show show = new Show("show1", screen, movie, LocalDateTime.now().plusHours(3));

        List<String> allSeatIds = new ArrayList<>();
        for (int i = 1; i <= 5; i++) {
            Seat seat = new Seat("seat" + i, "A" + i, SeatType.REGULAR);
            screen.addSeat(seat);
            ShowSeat showSeat = new ShowSeat("ss" + i, seat, show, new BigDecimal("250.00"));
            show.addShowSeat(showSeat);
            allSeatIds.add(showSeat.getId());
        }
        bookingService.registerShow(show);

        // --- Contention: N users all try to grab the SAME two seats ---
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

        // --- Report final seat states ---
        System.out.println("\n--- Final seat states for show1 ---");
        for (ShowSeat s : show.getShowSeats()) {
            System.out.println(s.getSeat().getSeatNumber() + " (" + s.getId() + "): " + s.getStatus());
        }
        System.out.println("\nConfirmed bookings: " + confirmed.get() + " (expected exactly 1)");
    }
}
