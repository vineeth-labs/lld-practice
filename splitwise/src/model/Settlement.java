package model;

public class Settlement {
    User paidBy;
    User paidTo;
    Double amount;

    public Settlement(User paidBy, User paidTo, Double amount) {
        this.paidBy = paidBy;
        this.paidTo = paidTo;
        this.amount = amount;
    }

    public User getPaidBy() {
        return paidBy;
    }

    public User getPaidTo() {
        return paidTo;
    }

    public Double getAmount() {
        return amount;
    }
}
