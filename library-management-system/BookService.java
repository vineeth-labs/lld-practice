import java.util.List;
import java.util.stream.Collectors;

import model.Book;
import model.BookCatalog;
import model.BookCopy;
import model.BookStatus;

public class BookService {
    private final BookCatalog bookCatalog;

    public BookService(BookCatalog bookCatalog) {
        if (bookCatalog == null) {
            throw new IllegalArgumentException("BookCatalog cannot be null");
        }
        this.bookCatalog = bookCatalog;
    }

    public void addBook(Book book) {
        if (book == null) {
            throw new IllegalArgumentException("Book cannot be null");
        }
        bookCatalog.addBook(book);
    }

    public void addBookCopy(BookCopy copy) {
        if (copy == null) {
            throw new IllegalArgumentException("Book copy cannot be null");
        }
        bookCatalog.addBookCopy(copy);
    }

    public Book getBook(String bookId) {
        if (bookId == null) {
            throw new IllegalArgumentException("Book ID cannot be null");
        }
        Book book = bookCatalog.getBooks().get(bookId);
        if (book == null) {
            throw new IllegalArgumentException("Book not found: " + bookId);
        }
        return book;
    }

    public BookCopy getBookCopy(String bookId, String copyId) {
        return bookCatalog.getBookCopiesByBookId(bookId).stream()
                .filter(copy -> copy.getCopyId().equals(copyId))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Book copy not found: " + copyId));
    }

    public BookCopy getAvailableBookCopy(String bookId, String copyId) {
        BookCopy copy = getBookCopy(bookId, copyId);
        if (!copy.isAvailable()) {
            throw new IllegalArgumentException("Book copy not available: " + copyId);
        }
        return copy;
    }

    public List<BookCopy> getAvailableCopiesByBook(String bookId) {
        return bookCatalog.getBookCopiesByBookId(bookId).stream()
                .filter(BookCopy::isAvailable)
                .collect(Collectors.toList());
    }

    public void markCopyAsCheckedOut(String bookId, String copyId) {
        BookCopy copy = getBookCopy(bookId, copyId);
        copy.setStatus(BookStatus.CHECKED_OUT);
    }

    public void markCopyAsAvailable(String bookId, String copyId) {
        BookCopy copy = getBookCopy(bookId, copyId);
        copy.setStatus(BookStatus.AVAILABLE);
    }

    public int getAvailableCopyCount(String bookId) {
        return (int) bookCatalog.getBookCopiesByBookId(bookId).stream()
                .filter(BookCopy::isAvailable)
                .count();
    }
}
