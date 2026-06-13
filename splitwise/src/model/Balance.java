package model;

public class Balance {
    User debitor;
    User creditor;
    Double amount;
    Group group;

    public Balance(User debitor, User creditor, Double amount) {
        this.debitor = debitor;
        this.creditor = creditor;
        this.amount = amount;
    }

    public User getDebitor() {
        return debitor;
    }

    public User getCreditor() {
        return creditor;
    }

    public Double getAmount() {
        return amount;
    }
}
