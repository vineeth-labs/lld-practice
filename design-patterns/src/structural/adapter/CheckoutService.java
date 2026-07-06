package structural.adapter;

public class CheckoutService {
    private final PaymentProcessor paymentProcessor;

    public CheckoutService(PaymentProcessor paymentProcessor) {
        this.paymentProcessor = paymentProcessor;
    }

    public void checkout(String customerId, double totalAmount) {
        System.out.println("Checking out order for customer " + customerId);
        paymentProcessor.pay(customerId, totalAmount);
    }
}
