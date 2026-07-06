package structural.adapter;

public class Main {
    public static void main(String[] args) {
        LegacyPaymentGateway legacyPaymentGateway = new LegacyPaymentGateway();
        PaymentProcessor paymentProcessor = new PaymentGatewayAdapter(legacyPaymentGateway);

        CheckoutService checkoutService = new CheckoutService(paymentProcessor);
        checkoutService.checkout("CUST-101", 2499.75);
    }
}
