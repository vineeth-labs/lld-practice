package model.exception;

import model.Payment;

/** Payment was declined, or seats were lost after a successful charge (refund needed). */
public class PaymentFailedException extends BookingException {
    private final Payment payment;

    public PaymentFailedException(String message, Payment payment) {
        super(message);
        this.payment = payment;
    }

    public Payment getPayment() {
        return payment;
    }
}
