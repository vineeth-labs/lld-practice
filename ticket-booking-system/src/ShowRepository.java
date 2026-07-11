import model.Show;

/** Persistence boundary for shows. */
public interface ShowRepository {
    void save(Show show);
    Show findById(String id);
}
