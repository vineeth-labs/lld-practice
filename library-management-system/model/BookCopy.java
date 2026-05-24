package model;

public class BookCopy {
    private Book book;
    private String copyId;
    private boolean isAvailable;
    private BookStatus status;

    public BookCopy(Book book, String copyId) {
        this.book = book;
        this.copyId = copyId;
        this.isAvailable = true; // By default, a new copy is available
        this.status = BookStatus.AVAILABLE;
    }

    public Book getBook() {
        return book;
    }

    public String getCopyId() {
        return copyId;
    }

    public boolean isAvailable() {
        return isAvailable;
    }

    public void setAvailable(boolean available) {
        isAvailable = available;
    }

    public BookStatus getStatus() {
        return status;
    }

    public void setStatus(BookStatus status) {
        this.status = status;
    }
}
