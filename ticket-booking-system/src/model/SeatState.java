package model;

import java.time.Instant;

public final class SeatState {
    public final SeatStatus status;
    public final String heldBy;        // null when AVAILABLE
    public final Instant heldUntil;    // null when AVAILABLE/BOOKED
    SeatState(SeatStatus s, String by, Instant until) {
        this.status = s;
        this.heldBy = by;
        this.heldUntil = until;
    }
    boolean isExpired(Instant now) {
        return status == SeatStatus.LOCKED && heldUntil != null && now.isAfter(heldUntil);
    }
}