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
        Booking booking = new Booking("id", userId, showId, result.lockedSeats,
                BookingStatus.PENDING_PAYMENT,
                Instant.now(), Instant.now().plus(LOCK_TTL));
        bookings.put(booking.getId(), booking);
        return booking;
    }


}
