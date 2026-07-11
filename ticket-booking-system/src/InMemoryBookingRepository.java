import model.Booking;

import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class InMemoryBookingRepository implements BookingRepository {
    private final Map<String, Booking> store = new ConcurrentHashMap<>();

    @Override
    public void save(Booking booking) {
        store.put(booking.getId(), booking);
    }

    @Override
    public Booking findById(String id) {
        return store.get(id);
    }

    @Override
    public Collection<Booking> findAll() {
        return store.values();
    }
}
