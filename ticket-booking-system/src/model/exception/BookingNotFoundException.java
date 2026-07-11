package model.exception;

/** No booking exists for the given id. */
public class BookingNotFoundException extends BookingException {
    public BookingNotFoundException(String bookingId) {
        super("Booking not found: " + bookingId);
    }
}
