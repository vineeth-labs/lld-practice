package model;

import java.util.Collections;
import java.util.List;

public class LockResult {
    public boolean success;
    public List<ShowSeat> lockedSeats;         // populated on success
    public List<String> unavailableSeatIds;   // populated on failure

    private LockResult(boolean success,
                       List<ShowSeat> lockedSeats,
                       List<String> unavailableSeatIds) {
        this.success = success;
        this.lockedSeats = lockedSeats;
        this.unavailableSeatIds = unavailableSeatIds;
    }

    public static LockResult success(List<ShowSeat> seats) {
        return new LockResult(
                true,
                seats,
                Collections.emptyList()
        );
    }
    public static LockResult failure(List<String> taken)   {
        return new LockResult(
                false,
                Collections.emptyList(),
                taken
        );
    }
}