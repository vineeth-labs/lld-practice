package model;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public class Ticket {
    private String id;
    private User user;
    private List<ShowSeat> showSeats;
    private LocalDateTime timing;
    private BigDecimal price;

    public Ticket(String id, User user, List<ShowSeat> showSeats, LocalDateTime timing, BigDecimal price) {
        this.id = id;
        this.user = user;
        this.showSeats = showSeats;
        this.timing = timing;
        this.price = price;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public List<ShowSeat> getShowSeats() {
        return showSeats;
    }

    public void setShowSeats(List<ShowSeat> showSeats) {
        this.showSeats = showSeats;
    }

    public LocalDateTime getTiming() {
        return timing;
    }

    public void setTiming(LocalDateTime timing) {
        this.timing = timing;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }
}
