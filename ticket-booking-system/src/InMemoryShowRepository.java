import model.Show;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class InMemoryShowRepository implements ShowRepository {
    private final Map<String, Show> store = new ConcurrentHashMap<>();

    @Override
    public void save(Show show) {
        store.put(show.getId(), show);
    }

    @Override
    public Show findById(String id) {
        return store.get(id);
    }
}
