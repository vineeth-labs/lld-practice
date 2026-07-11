package model.exception;

/** The seat-hold window elapsed before payment completed. */
public class BookingExpiredException extends BookingException {
    public BookingExpiredException(String bookingId) {
        super("Booking hold expired: " + bookingId);
    }
}
