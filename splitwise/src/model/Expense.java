package model;

import java.util.List;

public class Expense {
    User paidBy;
    Double amount;
    SplitType splitType;
    List<Split> splits;
    Group group;

    public Expense(User paidBy, Double amount, SplitType splitType, List<Split> splits, Group group) {
        this.paidBy = paidBy;
        this.amount = amount;
        this.splitType = splitType;
        this.splits = splits;
        this.group = group;
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

    public Group getGroup() {
        return group;
    }
}
