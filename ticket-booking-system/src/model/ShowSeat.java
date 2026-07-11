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

    private volatile String lockedBy;         // userId holding the lock
    private volatile Instant lockedUntil;

    /** Atomic AVAILABLE -> LOCKED. Returns false if someone beat us. */
    public boolean tryLock(String userId, Duration ttl, Instant now) {
        while (true) {
            SeatState curr =  status.get() ;
            boolean acquirable = curr.status == SeatStatus.AVAILABLE || curr.isExpired(now);
            SeatState next = new SeatState(SeatStatus.LOCKED, userId, now.plus(ttl));
            if (status.compareAndSet(curr, next)) {
                return true;
            }
        }
    }

    /** LOCKED -> AVAILABLE. Only the lock holder may release. */
    public boolean release(String userId) {
        if (!userId.equals(lockedBy)) return false;
        this.lockedBy = null;
        this.lockedUntil = null;
        return status.compareAndSet(SeatStatus.LOCKED, SeatStatus.AVAILABLE);
    }

    /** LOCKED -> BOOKED. Called only on payment success. */
    boolean confirm(String userId) {
        if (!userId.equals(lockedBy)) return false;
        return status.compareAndSet(SeatStatus.LOCKED, SeatStatus.BOOKED);
    }

    public SeatStatus getStatus() { return status.get(); }
    public String getId()         { return id; }
}