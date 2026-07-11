import model.Booking;
import model.Payment;

import java.math.BigDecimal;

/** Abstraction over a payment provider so it can be swapped or mocked. */
public interface PaymentGateway {
    Payment makePayment(String userId, Booking booking, BigDecimal amount);
}
