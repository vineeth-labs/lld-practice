import model.Book;
import model.BookCatalog;
import model.BookCopy;
import model.Student;

public class LibraryService {
    private final UserService userService;
    private final BookService bookService;
    private final TransactionService transactionService;
    private final FineService fineService;

    public LibraryService(BookCatalog bookCatalog) {
        this.userService = new UserService();
        this.bookService = new BookService(bookCatalog);
        this.fineService = new FineService(userService);
        this.transactionService = new TransactionService(fineService, bookService, userService);
    }

    public void registerStudent(Student student) {
        userService.registerStudent(student);
    }

    public void addBook(Book book) {
        bookService.addBook(book);
    }

    public void addBookCopy(BookCopy copy) {
        bookService.addBookCopy(copy);
    }

    public void borrowBook(String studentId, String bookId, String copyId) {
        transactionService.borrowBook(studentId, bookId, copyId);
    }

    public void returnBook(String studentId, String bookId, String copyId) {
        transactionService.returnBook(studentId, bookId, copyId);
    }

    // Delegated getters for transaction and fine queries
    public java.util.List<model.Transaction> getStudentTransactions(String studentId) {
        return transactionService.getStudentTransactions(studentId);
    }

    public java.util.List<model.Fine> getFinesForStudent(String studentId) {
        return fineService.getFinesForStudent(studentId);
    }

    // Expose services for advanced operations
    public UserService getUserService() {
        return userService;
    }

    public BookService getBookService() {
        return bookService;
    }

    public TransactionService getTransactionService() {
        return transactionService;
    }

    public FineService getFineService() {
        return fineService;
    }
}
