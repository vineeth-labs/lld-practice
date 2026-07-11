package model.exception;

import model.BookingStatus;

/** The booking is not in a state that permits the attempted operation. */
public class InvalidBookingStateException extends BookingException {
    private final BookingStatus currentStatus;

    public InvalidBookingStateException(BookingStatus currentStatus) {
        super("Booking not payable, current status: " + currentStatus);
        this.currentStatus = currentStatus;
    }

    public BookingStatus getCurrentStatus() {
        return currentStatus;
    }
}
