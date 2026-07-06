package structural.adapter;

public class LegacyPaymentGateway {
    public void makePayment(String accountNumber, int amountInCents) {
        System.out.println("Legacy gateway charged " + amountInCents + " cents to account " + accountNumber);
    }
}
