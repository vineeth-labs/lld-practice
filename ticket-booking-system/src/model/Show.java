package model;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class Show {
    private String id;
    private Screen screen;
    private Movie movie;
    private LocalDateTime timing;
    private List<ShowSeat> showSeats;

    public Show(String id, Screen screen, Movie movie, LocalDateTime timing) {
        this.id = id;
        this.screen = screen;
        this.movie = movie;
        this.timing = timing;
        this.showSeats = new ArrayList<>();
    }

    public Show(String id, Screen screen, Movie movie, LocalDateTime timing, List<ShowSeat> showSeats) {
        this.id = id;
        this.screen = screen;
        this.movie = movie;
        this.timing = timing;
        this.showSeats = showSeats;
    }

    public void addShowSeat(ShowSeat showSeat) {
        this.showSeats.add(showSeat);
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Screen getScreen() {
        return screen;
    }

    public void setScreen(Screen screen) {
        this.screen = screen;
    }

    public Movie getMovie() {
        return movie;
    }

    public void setMovie(Movie movie) {
        this.movie = movie;
    }

    public LocalDateTime getTiming() {
        return timing;
    }

    public void setTiming(LocalDateTime timing) {
        this.timing = timing;
    }

    public List<ShowSeat> getShowSeats() {
        return showSeats;
    }

    public void setShowSeats(List<ShowSeat> showSeats) {
        this.showSeats = showSeats;
    }
}
