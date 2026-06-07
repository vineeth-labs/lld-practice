package model;

public class BookCopy {
    private Book book;
    private String copyId;
    private BookStatus status;

    public BookCopy(Book book, String copyId) {
        this(book, copyId, BookStatus.AVAILABLE);
    }

    public BookCopy(Book book, String copyId, BookStatus status) {
        this.book = book;
        this.copyId = copyId;
        this.status = status;
    }

    public Book getBook() {
        return book;
    }

    public String getCopyId() {
        return copyId;
    }

    public boolean isAvailable() {
        return status.isAvailable();
    }


    public BookStatus getStatus() {
        return status;
    }

    public void setStatus(BookStatus status) {
        this.status = status;
    }
}
