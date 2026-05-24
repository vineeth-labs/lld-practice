import model.BookCatalog;
import model.Student;

public class LibrarySystem {
    private final BookCatalog bookCatalog;
    private final LibraryService libraryService;

    public LibrarySystem() {
        this.bookCatalog = new BookCatalog();
        this.libraryService = new LibraryService(bookCatalog);
    }

    public void registerStudent(Student student) {
        libraryService.registerStudent(student);
    }

    public void addBook(model.Book book) {
        libraryService.addBook(book);
    }

    public void addBookCopy(model.BookCopy copy) {
        libraryService.addBookCopy(copy);
    }

    public void borrowBook(String studentId, String bookId, String copyId) {
        libraryService.borrowBook(studentId, bookId, copyId);
    }

    public void returnBook(String studentId, String bookId, String copyId) {
        libraryService.returnBook(studentId, bookId, copyId);
    }

    public LibraryService getLibraryService() {
        return libraryService;
    }
}