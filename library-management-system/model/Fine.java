package model;
public class Fine {
    private String id;
    private String studentId;
    private double amount;

    public Fine(String id, String studentId, double amount) {
        this.id = id;
        this.studentId = studentId;
        this.amount = amount;
    }

    // Getters and setters
    public String getId() {
        return id;
    }

    public String getStudentId() {
        return studentId;
    }

    public void setStudentId(String studentId) {
        this.studentId = studentId;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }
}