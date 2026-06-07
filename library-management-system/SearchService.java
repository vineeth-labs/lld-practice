import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import model.Book;
import model.BookCatalog;

public class SearchService {
    private final BookCatalog bookCatalog;

    public SearchService(BookCatalog bookCatalog) {
        if (bookCatalog == null) {
            throw new IllegalArgumentException("BookCatalog cannot be null");
        }
        this.bookCatalog = bookCatalog;
    }

    /**
     * Search books by title with case-insensitive partial matching.
     *
     * @param title partial or full title to search for
     * @return list of matching books, empty if none found
     */
    public List<Book> searchByTitle(String title) {
        if (title == null || title.trim().isEmpty()) {
            return Collections.emptyList();
        }

        String searchTerm = title.toLowerCase().trim();
        return bookCatalog.getBooks().values().stream()
                .filter(book -> book.getTitle().toLowerCase().contains(searchTerm))
                .collect(Collectors.toList());
    }

    /**
     * Search books by author with case-insensitive partial matching.
     *
     * @param author partial or full author name to search for
     * @return list of matching books, empty if none found
     */
    public List<Book> searchByAuthor(String author) {
        if (author == null || author.trim().isEmpty()) {
            return Collections.emptyList();
        }

        String searchTerm = author.toLowerCase().trim();
        return bookCatalog.getBooks().values().stream()
                .filter(book -> book.getAuthor().toLowerCase().contains(searchTerm))
                .collect(Collectors.toList());
    }

    /**
     * Search books by genre with case-insensitive partial matching.
     *
     * @param genre partial or full genre to search for
     * @return list of matching books, empty if none found
     */
    public List<Book> searchByGenre(String genre) {
        if (genre == null || genre.trim().isEmpty()) {
            return Collections.emptyList();
        }

        String searchTerm = genre.toLowerCase().trim();
        return bookCatalog.getBooks().values().stream()
                .filter(book -> book.getGenres().stream()
                        .anyMatch(g -> g.toLowerCase().contains(searchTerm)))
                .collect(Collectors.toList());
    }

    /**
     * Search books by ISBN with exact matching (case-insensitive).
     *
     * @param isbn exact ISBN to search for
     * @return list of matching books (0 or 1 result), empty if not found
     */
    public List<Book> searchByIsbn(String isbn) {
        if (isbn == null || isbn.trim().isEmpty()) {
            return Collections.emptyList();
        }

        String searchTerm = isbn.toLowerCase().trim();
        return bookCatalog.getBooks().values().stream()
                .filter(book -> book.getIsbn().toLowerCase().equals(searchTerm))
                .collect(Collectors.toList());
    }

    /**
     * Search books by any combination of criteria.
     * All non-null criteria must match (AND logic).
     *
     * @param title optional title (partial, case-insensitive)
     * @param author optional author (partial, case-insensitive)
     * @param genre optional genre (partial, case-insensitive)
     * @param isbn optional ISBN (exact, case-insensitive)
     * @return list of matching books
     */
    public List<Book> searchByMultipleCriteria(String title, String author, String genre, String isbn) {
        return bookCatalog.getBooks().values().stream()
                .filter(book -> matchesCriteria(book, title, author, genre, isbn))
                .collect(Collectors.toList());
    }

    private boolean matchesCriteria(Book book, String title, String author, String genre, String isbn) {
        if (title != null && !title.trim().isEmpty()) {
            if (!book.getTitle().toLowerCase().contains(title.toLowerCase().trim())) {
                return false;
            }
        }

        if (author != null && !author.trim().isEmpty()) {
            if (!book.getAuthor().toLowerCase().contains(author.toLowerCase().trim())) {
                return false;
            }
        }

        if (genre != null && !genre.trim().isEmpty()) {
            String genreSearch = genre.toLowerCase().trim();
            if (!book.getGenres().stream()
                    .anyMatch(g -> g.toLowerCase().contains(genreSearch))) {
                return false;
            }
        }

        if (isbn != null && !isbn.trim().isEmpty()) {
            if (!book.getIsbn().toLowerCase().equals(isbn.toLowerCase().trim())) {
                return false;
            }
        }

        return true;
    }
}
