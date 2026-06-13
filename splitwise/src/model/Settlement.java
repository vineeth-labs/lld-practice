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
}
