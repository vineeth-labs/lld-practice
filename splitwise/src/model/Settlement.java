package model;

public class Settlement {
    User paidBy;
    User paidTo;
    Double amount;
    Scope scope;

    public Settlement(User paidBy, User paidTo, Double amount, Scope scope) {
        this.paidBy = paidBy;
        this.paidTo = paidTo;
        this.amount = amount;
        this.scope = scope;
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

    public Scope getScope() {
        return scope;
    }
}
