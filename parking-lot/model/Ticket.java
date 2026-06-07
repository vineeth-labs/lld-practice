package model;

import java.time.LocalDateTime;

public class Ticket {
    LocalDateTime entranceTime;
    LocalDateTime exitTime;
    ParkingSpot parkingSpot;
    String vehicleNumber;
    Double price;

    public Ticket(String vehicleNumber, ParkingSpot parkingSpot) {
        this.parkingSpot = parkingSpot;
        this.vehicleNumber = vehicleNumber;
        this.entranceTime = LocalDateTime.now();
        this.exitTime = LocalDateTime.now();
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public ParkingSpot getParkingSpot() {
        return parkingSpot;
    }
}
