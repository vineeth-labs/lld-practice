package model;

import java.time.Instant;
import java.util.List;

public class Booking {
    private String id;
    private String userId;
    private String showId;
    private List<ShowSeat> showSeats;
    private Instant createdAt;
    private Instant expiresAt;
    private Payment payment;
    private BookingStatus bookingStatus;

    public Booking(String id, String userId, String showId, List<ShowSeat> showSeats, BookingStatus bookingStatus, Instant createdAt, Instant expiresAt) {
        this.id = id;
        this.userId = userId;
        this.showId = showId;
        this.showSeats = showSeats;
        this.createdAt = createdAt;
        this.expiresAt = expiresAt;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
    public List<ShowSeat> getShowSeats() {
        return showSeats;
    }
    public void setShowSeats(List<ShowSeat> showSeats) {
        this.showSeats = showSeats;
    }

    public Payment getPayment() {
        return payment;
    }

    public void setPayment(Payment payment) {
        this.payment = payment;
    }
}
