package model.exception;

/** Base type for all booking-domain failures. Unchecked. */
public class BookingException extends RuntimeException {
    public BookingException(String message) {
        super(message);
    }
}
