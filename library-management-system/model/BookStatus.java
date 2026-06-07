package model;

public enum BookStatus {
    AVAILABLE(true),
    CHECKED_OUT(false),
    RESERVED(false),
    LOST(false);

    private final boolean available;

    BookStatus(boolean available) {
        this.available = available;
    }

    public boolean isAvailable() {
        return available;
    }
}
