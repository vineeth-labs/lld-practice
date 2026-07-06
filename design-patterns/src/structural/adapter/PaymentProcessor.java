package structural.adapter;

public interface PaymentProcessor {
    void pay(String customerId, double amount);
}
