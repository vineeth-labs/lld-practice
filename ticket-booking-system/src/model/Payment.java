package model;

import java.math.BigDecimal;

public class Payment {
    private String id;
    private Booking booking;
    private BigDecimal amount;
    private PaymentStatus status;
    private String txnId;

    public Payment(String id, Booking booking, BigDecimal amount, PaymentStatus status, String txnId) {
        this.id = id;
        this.booking = booking;
        this.amount = amount;
        this.status = status;
        this.txnId = txnId;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Booking getBooking() {
        return booking;
    }

    public void setBooking(Booking booking) {
        this.booking = booking;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public PaymentStatus getStatus() {
        return status;
    }

    public void setStatus(PaymentStatus status) {
        this.status = status;
    }

    public String getTxnId() {
        return txnId;
    }

    public void setTxnId(String txnId) {
        this.txnId = txnId;
    }
}
