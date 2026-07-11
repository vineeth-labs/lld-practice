import model.LockResult;
import model.Show;
import model.ShowSeat;

import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Owns seat-hold concurrency: indexes bookable ShowSeats and provides
 * all-or-nothing lock / release / confirm over them. This is the contention core;
 * in a distributed system it would be backed by Redis/Zookeeper.
 */
public class SeatLockManager {
    private final ConcurrentHashMap<String, ConcurrentHashMap<String, ShowSeat>> showSeats =
            new ConcurrentHashMap<>();
    private final Duration lockTtl;

    public SeatLockManager(Duration lockTtl) {
        this.lockTtl = lockTtl;
    }

    /** Indexes a show's bookable seats by seatId. */
    public void registerShow(Show show) {
        ConcurrentHashMap<String, ShowSeat> seatMap = new ConcurrentHashMap<>();
        for (ShowSeat showSeat : show.getShowSeats()) {
            seatMap.put(showSeat.getId(), showSeat);
        }
        showSeats.put(show.getId(), seatMap);
    }

    /** Attempts to lock all seats for a user; releases any partial locks on failure. */
    public LockResult lock(String userId, String showId, List<String> seatIds) {
        boolean ok = false;
        List<String> ordered = seatIds.stream().distinct().sorted().toList();
        List<ShowSeat> acquired = new ArrayList<>();
        try {
            for (String seatId : ordered) {
                ShowSeat showSeat = showSeats.get(showId).get(seatId);
                if (!showSeat.tryLock(userId, lockTtl, Instant.now())) {
                    return LockResult.failure(List.of(seatId));
                }
                acquired.add(showSeat);
            }
            ok = true;
            return LockResult.success(acquired);
        } finally {
            if (!ok) acquired.forEach(s -> s.release(userId));
        }
    }

    public void release(String userId, List<ShowSeat> seats) {
        seats.forEach(s -> s.release(userId));
    }

    /** Promotes all seats LOCKED -> BOOKED. Returns false if any lock was lost. */
    public boolean confirmAll(String userId, List<ShowSeat> seats) {
        for (ShowSeat seat : seats) {
            if (!seat.confirm(userId)) return false;
        }
        return true;
    }
}
