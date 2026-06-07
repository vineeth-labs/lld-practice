package model;

import java.time.LocalDateTime;

public class Transaction {
    Integer id;
    Integer productId;
    TransactionStatus status;
    Integer price;
    LocalDateTime timestamp;

    public Transaction(Integer id, Integer productId, TransactionStatus status, Integer price, LocalDateTime timestamp) {
        this.id = id;
        this.productId = productId;
        this.status = status;
        this.price = price;
        this.timestamp = timestamp;
    }
}
