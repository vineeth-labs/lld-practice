package model;
import java.util.Collections;
import java.util.List;

public class Book {
    String bookId;
    String title;
    String author;
    int publicationYear;
    String isbn;
    List<String> genres;
    List<BookCopy> copies;

    public Book(String bookId, String title, String author, int publicationYear, String isbn, List<String> genres) {
        this.bookId = bookId;
        this.title = title;
        this.author = author;
        this.publicationYear = publicationYear;
        this.isbn = isbn;
        this.genres = genres;
        this.copies = new java.util.ArrayList<>();
    }

    public String getBookId() {
        return bookId;
    }

    public String getTitle() {
        return title;
    }

    public String getAuthor() {
        return author;
    }

    public int getPublicationYear() {
        return publicationYear;
    }

    public String getIsbn() {
        return isbn;
    }

    public List<String> getGenres() {
        return Collections.unmodifiableList(genres);
    }

    public List<BookCopy> getCopies() {
        return Collections.unmodifiableList(copies);
    }


    public void addCopy(BookCopy copy) {
        copies.add(copy);
    }
}