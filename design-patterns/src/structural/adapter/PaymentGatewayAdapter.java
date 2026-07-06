package structural.adapter;

public class PaymentGatewayAdapter implements PaymentProcessor {
    private final LegacyPaymentGateway legacyPaymentGateway;

    public PaymentGatewayAdapter(LegacyPaymentGateway legacyPaymentGateway) {
        this.legacyPaymentGateway = legacyPaymentGateway;
    }

    @Override
    public void pay(String customerId, double amount) {
        int amountInCents = (int) Math.round(amount * 100);
        legacyPaymentGateway.makePayment(customerId, amountInCents);
    }
}
