import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import model.Transaction;

public class TransactionService {
    private static final int BORROW_PERIOD_DAYS = 14;
    private static final int BOOKS_BORROWED_LIMIT = 5;

    private final List<Transaction> transactions;
    private final FineService fineService;
    private final BookService bookService;
    private final UserService userService;

    public TransactionService(FineService fineService, BookService bookService, UserService userService) {
        if (fineService == null || bookService == null || userService == null) {
            throw new IllegalArgumentException("Dependencies cannot be null");
        }
        this.fineService = fineService;
        this.bookService = bookService;
        this.userService = userService;
        this.transactions = new ArrayList<>();
    }

    public void borrowBook(String studentId, String bookId, String copyId) {
        userService.validateStudent(studentId);

        if (isBorrowLimitExceeded(studentId)) {
            throw new IllegalStateException("Borrow limit exceeded for student: " + studentId);
        }

        bookService.getAvailableBookCopy(bookId, copyId);
        bookService.markCopyAsCheckedOut(bookId, copyId);

        Transaction transaction = new Transaction(UUID.randomUUID().toString(), studentId, bookId, copyId);
        transactions.add(transaction);
    }

    public void returnBook(String studentId, String bookId, String copyId) {
        userService.validateStudent(studentId);

        Transaction activeTransaction = transactions.stream()
                .filter(transaction -> transaction.getStudentId().equals(studentId)
                        && transaction.getBookId().equals(bookId)
                        && transaction.getCopyId().equals(copyId)
                        && transaction.getReturnDate() == null)
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("No active borrow transaction found for this book copy."));

        bookService.markCopyAsAvailable(bookId, copyId);

        LocalDateTime returnDate = LocalDateTime.now();
        LocalDateTime borrowDate = activeTransaction.getBorrowDate();

        if (borrowDate.plusDays(BORROW_PERIOD_DAYS).isBefore(returnDate)) {
            fineService.collectFine(studentId, borrowDate, returnDate);
        }

        activeTransaction.setReturnDate(returnDate);
    }

    private boolean isBorrowLimitExceeded(String studentId) {
        long activeBorrows = transactions.stream()
                .filter(transaction -> transaction.getStudentId().equals(studentId)
                        && transaction.getReturnDate() == null)
                .count();
        return activeBorrows >= BOOKS_BORROWED_LIMIT;
    }

    public List<Transaction> getStudentTransactions(String studentId) {
        userService.validateStudent(studentId);
        return Collections.unmodifiableList(transactions.stream()
                .filter(transaction -> transaction.getStudentId().equals(studentId))
                .collect(Collectors.toList()));
    }

    public List<Transaction> getActiveTransactions(String studentId) {
        userService.validateStudent(studentId);
        return Collections.unmodifiableList(transactions.stream()
                .filter(transaction -> transaction.getStudentId().equals(studentId)
                        && transaction.getReturnDate() == null)
                .collect(Collectors.toList()));
    }

    public List<Transaction> getAllTransactions() {
        return Collections.unmodifiableList(new ArrayList<>(transactions));
    }
}
