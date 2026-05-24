import java.util.Arrays;
import model.Book;
import model.BookCopy;
import model.Student;

public class Main {
    public static void main(String[] args) {
        LibrarySystem system = new LibrarySystem();

        Student alice = new Student("S001", "Alice", "alice@example.com");
        Student bob = new Student("S002", "Bob", "bob@example.com");
        system.registerStudent(alice);
        system.registerStudent(bob);

        Book javaBook = new Book("B001", "Java Design Patterns", "Jane Doe", 2023, "978-1234567890", Arrays.asList("Programming", "Design"));
        Book cleanCode = new Book("B002", "Clean Code", "Robert C. Martin", 2008, "978-0132350884", Arrays.asList("Programming", "Best Practices"));

        system.addBook(javaBook);
        system.addBook(cleanCode);

        system.addBookCopy(new BookCopy(javaBook, "C001"));
        system.addBookCopy(new BookCopy(javaBook, "C002"));
        system.addBookCopy(new BookCopy(cleanCode, "C003"));

        System.out.println("=== Library simulation started ===");
        System.out.println("Alice borrows Java Design Patterns copy C001");
        system.borrowBook(alice.getId(), javaBook.getBookId(), "C001");

        System.out.println("Bob borrows Clean Code copy C003");
        system.borrowBook(bob.getId(), cleanCode.getBookId(), "C003");

        System.out.println("Alice returns Java Design Patterns copy C001");
        system.returnBook(alice.getId(), javaBook.getBookId(), "C001");

        System.out.println("Bob returns Clean Code copy C003");
        system.returnBook(bob.getId(), cleanCode.getBookId(), "C003");

        System.out.println("=== Transactions ===");
        system.getLibraryService().getStudentTransactions(alice.getId())
                .forEach(transaction -> System.out.println("Transaction: " + transaction.getTransactionId()
                        + " book=" + transaction.getBookId()
                        + " copy=" + transaction.getCopyId()
                        + " returned=" + (transaction.getReturnDate() != null)));

        System.out.println("=== Fines ===");
        system.getLibraryService().getFinesForStudent(bob.getId()).forEach(fine ->
                System.out.println("Fine: " + fine.getId() + " amount= " + fine.getAmount()));

        System.out.println("=== Library simulation complete ===");
    }
}
