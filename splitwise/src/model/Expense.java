package model;

import java.util.List;

public class Expense {
    User paidBy;
    Double amount;
    SplitType splitType;
    List<Split> splits;
    Scope scope;

    public Expense(User paidBy, Double amount, SplitType splitType, List<Split> splits, Scope scope) {
        this.paidBy = paidBy;
        this.amount = amount;
        this.splitType = splitType;
        this.splits = splits;
        this.scope = scope;
    }

    public User getPaidBy() {
        return paidBy;
    }

    public Double getAmount() {
        return amount;
    }

    public SplitType getSplitType() {
        return splitType;
    }

    public List<Split> getSplits() {
        return splits;
    }

    public Scope getScope() {
        return scope;
    }
}
