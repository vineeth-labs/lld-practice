package model;

public class Split {
    User owedBy;
    Double amount;

    public Split(User owedBy, Double amount) {
        this.owedBy = owedBy;
        this.amount = amount;
    }

    public User getOwedBy() {
        return owedBy;
    }

    public Double getAmount() {
        return amount;
    }
}
