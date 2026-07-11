import model.Booking;
import model.Payment;
import model.PaymentStatus;

import java.math.BigDecimal;
import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;

/**
 * Pseudo gateway for local runs: simulates network latency and an optional
 * random decline rate. Deterministic-success by default.
 */
public class SimulatedPaymentGateway implements PaymentGateway {

    private final double failureRate;   // 0.0 = always succeed, 1.0 = always fail

    public SimulatedPaymentGateway() {
        this(0.0);
    }

    public SimulatedPaymentGateway(double failureRate) {
        if (failureRate < 0 || failureRate > 1)
            throw new IllegalArgumentException("failureRate must be in [0, 1]");
        this.failureRate = failureRate;
    }

    @Override
    public Payment makePayment(String userId, Booking booking, BigDecimal amount) {
        String paymentId = UUID.randomUUID().toString();
        System.out.println("Payment initiated for " + userId + ", amount " + amount);

        try {
            // Simulate provider round-trip latency (100-400ms).
            Thread.sleep(ThreadLocalRandom.current().nextInt(100, 400));
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();   // restore interrupt flag
            return new Payment(paymentId, booking, amount, PaymentStatus.FAILED, null);
        }

        boolean declined = ThreadLocalRandom.current().nextDouble() < failureRate;
        if (declined) {
            System.out.println("Payment declined for " + userId);
            return new Payment(paymentId, booking, amount, PaymentStatus.FAILED, null);
        }

        String txnId = "txn_" + UUID.randomUUID();
        System.out.println("Payment done for " + userId + ", txn " + txnId);
        return new Payment(paymentId, booking, amount, PaymentStatus.SUCCESS, txnId);
    }
}
