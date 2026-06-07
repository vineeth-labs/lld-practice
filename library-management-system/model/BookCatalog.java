package model;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class BookCatalog {
    // Key: bookId, Value: Book
    private Map<String, Book> books;
    public BookCatalog() {
        this.books = new HashMap<>();
    }

    public void addBook(Book book) {
        books.put(book.bookId, book);
    }

    public void addBookCopy(BookCopy bookCopy) {
        String bookId = bookCopy.getBook().bookId;
        Book book = books.get(bookId);
        if (book == null) {
            throw new IllegalArgumentException("Book not found for copy: " + bookId);
        }
        book.addCopy(bookCopy);
    }

    public List<BookCopy> getBookCopiesByBookId(String bookId) {
    return Optional.ofNullable(books.get(bookId))
            .map(book -> book.copies)
            .orElse(Collections.emptyList());
    }

    public Map<String, Book> getBooks() {
        return Collections.unmodifiableMap(books);
    }

}
