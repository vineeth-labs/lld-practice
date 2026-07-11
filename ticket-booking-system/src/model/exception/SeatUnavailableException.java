package model.exception;

import java.util.List;

/** One or more requested seats could not be locked. Carries the offending seat ids. */
public class SeatUnavailableException extends BookingException {
    private final List<String> unavailableSeatIds;

    public SeatUnavailableException(List<String> unavailableSeatIds) {
        super("Seats unavailable: " + unavailableSeatIds);
        this.unavailableSeatIds = List.copyOf(unavailableSeatIds);
    }

    public List<String> getUnavailableSeatIds() {
        return unavailableSeatIds;
    }
}
