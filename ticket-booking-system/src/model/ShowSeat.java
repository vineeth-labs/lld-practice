package model;

import java.math.BigDecimal;
import java.time.Duration;
import java.time.Instant;
import java.util.concurrent.atomic.AtomicReference;

public class ShowSeat {
    // bookable, THE contention point
    String id;
    Seat seat;
    Show show;
    BigDecimal price;

    private final AtomicReference<SeatState> status =
        new AtomicReference<>(new SeatState(SeatStatus.AVAILABLE, null, null));

    /** Atomic AVAILABLE (or expired LOCK) -> LOCKED. Returns false if genuinely taken. */
    public boolean tryLock(String userId, Duration ttl, Instant now) {
        while (true) {
            SeatState curr = status.get();
            boolean acquirable = curr.status == SeatStatus.AVAILABLE || curr.isExpired(now);
            if (!acquirable) return false;
            SeatState next = new SeatState(SeatStatus.LOCKED, userId, now.plus(ttl));
            if (status.compareAndSet(curr, next)) return true;
            // else another thread changed the state; re-read and retry
        }
    }

    /** LOCKED -> AVAILABLE. Only the lock holder may release. */
    public boolean release(String userId) {
        while (true) {
            SeatState curr = status.get();
            if (curr.status != SeatStatus.LOCKED || !userId.equals(curr.heldBy)) return false;
            if (status.compareAndSet(curr, new SeatState(SeatStatus.AVAILABLE, null, null)))
                return true;
        }
    }

    /** LOCKED -> BOOKED. Called only on payment success, by the lock holder. */
    public boolean confirm(String userId) {
        while (true) {
            SeatState curr = status.get();
            if (curr.status != SeatStatus.LOCKED || !userId.equals(curr.heldBy)) return false;
            if (status.compareAndSet(curr, new SeatState(SeatStatus.BOOKED, userId, null)))
                return true;
        }
    }

    public SeatStatus getStatus() { return status.get().status; }
    public String getId()         { return id; }
    public BigDecimal getPrice()  { return price; }
}