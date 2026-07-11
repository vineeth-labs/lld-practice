package model;

public class Seat {
    private String id;
    private String seatNumber;
    private SeatType seatType;

    public Seat(String id, String seatNumber, SeatType seatType) {
        this.id = id;
        this.seatNumber = seatNumber;
        this.seatType = seatType;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSeatNumber() {
        return seatNumber;
    }

    public void setSeatNumber(String seatNumber) {
        this.seatNumber = seatNumber;
    }

    public SeatType getSeatType() {
        return seatType;
    }

    public void setSeatType(SeatType seatType) {
        this.seatType = seatType;
    }
}
