package model;

public class Transaction {
    private String transactionId;
    private String studentId;
    private String bookId;
    private String copyId;
    private java.time.LocalDateTime borrowDate;
    private java.time.LocalDateTime returnDate;

    public Transaction(String transactionId, String studentId, String bookId, String copyId) {
        this.transactionId = transactionId;
        this.studentId = studentId;
        this.bookId = bookId;
        this.copyId = copyId;
        this.borrowDate = java.time.LocalDateTime.now();
    }

    // Getters and setters
    public void setReturnDate(java.time.LocalDateTime returnDate) {
        this.returnDate = returnDate;
    }

    public String getTransactionId() {
        return transactionId;
    }

    public String getStudentId() {
        return studentId;
    }

    public String getBookId() {
        return bookId;
    }

    public String getCopyId() {
        return copyId;
    }

    public java.time.LocalDateTime getBorrowDate() {
        return borrowDate;
    }

    public java.time.LocalDateTime getReturnDate() {
        return returnDate;
    }

    

}