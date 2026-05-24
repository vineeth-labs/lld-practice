import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.UUID;

import model.Book;
import model.BookCatalog;
import model.BookCopy;
import model.Fine;
import model.Student;
import model.Transaction;

public class LibraryService {
    private static final double FIXED_FINE_PER_DAY = 5.0;
    private static final int BORROW_PERIOD_DAYS = 14;

    private final BookCatalog bookCatalog;
    private final Map<String, Student> students;
    private final List<Transaction> transactions;
    private final List<Fine> fines;

    public LibraryService(BookCatalog bookCatalog) {
        this.bookCatalog = bookCatalog;
        this.students = new HashMap<>();
        this.transactions = new ArrayList<>();
        this.fines = new ArrayList<>();
    }

    public void registerStudent(Student student) {
        if (student == null) {
            throw new IllegalArgumentException("Student cannot be null");
        }
        if (students.containsKey(student.getId())) {
            throw new IllegalArgumentException("Student already registered: " + student.getId());
        }
        students.put(student.getId(), student);
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

    public void borrowBook(String studentId, String bookId, String copyId) {
        validateStudent(studentId);

        BookCopy copy = bookCatalog.getBookCopiesByBookId(bookId).stream()
                .filter(bookCopy -> bookCopy.getCopyId().equals(copyId) && bookCopy.isAvailable())
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Book copy not available: " + copyId));

        copy.setAvailable(false);
        copy.setStatus(model.BookStatus.CHECKED_OUT);
        transactions.add(new Transaction(UUID.randomUUID().toString(), studentId, bookId, copyId));
    }

    public void returnBook(String studentId, String bookId, String copyId) {
        validateStudent(studentId);

        Transaction activeTransaction = transactions.stream()
                .filter(transaction -> transaction.getStudentId().equals(studentId)
                        && transaction.getBookId().equals(bookId)
                        && transaction.getCopyId().equals(copyId)
                        && transaction.getReturnDate() == null)
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("No active borrow transaction found for this book copy."));

        BookCopy copy = bookCatalog.getBookCopiesByBookId(bookId).stream()
                .filter(bookCopy -> bookCopy.getCopyId().equals(copyId))
                .findFirst()
                .orElseThrow(() -> new IllegalStateException("Book copy not found in catalog: " + copyId));

        copy.setAvailable(true);
        copy.setStatus(model.BookStatus.AVAILABLE);

        LocalDateTime returnDate = LocalDateTime.now();
        LocalDateTime borrowDate = activeTransaction.getBorrowDate();
        if (borrowDate.plusDays(BORROW_PERIOD_DAYS).isBefore(returnDate)) {
            collectFine(studentId, borrowDate, returnDate);
        }

        activeTransaction.setReturnDate(returnDate);
    }

    private void collectFine(String studentId, LocalDateTime borrowDate, LocalDateTime returnDate) {
        long overdueDays = ChronoUnit.DAYS.between(borrowDate.plusDays(BORROW_PERIOD_DAYS), returnDate);
        if (overdueDays <= 0) {
            return;
        }

        Fine fine = new Fine(UUID.randomUUID().toString(), studentId, overdueDays * FIXED_FINE_PER_DAY);
        fines.add(fine);
    }

    public List<Transaction> getStudentTransactions(String studentId) {
        validateStudent(studentId);
        return Collections.unmodifiableList(transactions.stream()
            .filter(transaction -> transaction.getStudentId().equals(studentId))
            .collect(Collectors.toList()));
    }

    public List<Fine> getFinesForStudent(String studentId) {
        validateStudent(studentId);
        return Collections.unmodifiableList(fines.stream()
            .filter(fine -> fine.getStudentId().equals(studentId))
            .collect(Collectors.toList()));
    }

    private void validateStudent(String studentId) {
        if (studentId == null || !students.containsKey(studentId)) {
            throw new IllegalArgumentException("Student is not registered: " + studentId);
        }
    }
}
