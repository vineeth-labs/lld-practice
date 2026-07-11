import model.Booking;
import model.Payment;
import model.PaymentStatus;

import java.math.BigDecimal;

public class PaymentGateway {

    static Payment makePayment(String userId, Booking booking, BigDecimal price) {
        try {
            System.out.println("Payment initiated");
            Thread.sleep(2000);
            System.out.println("Payment done");
            return new Payment("id1", booking, price, PaymentStatus.SUCCESS, "");
        } catch (InterruptedException e) {
            e.printStackTrace();
            return new Payment("id2", booking, price, PaymentStatus.FAILED, "");
        }
    }
}
