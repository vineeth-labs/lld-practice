import model.Booking;

import java.util.Collection;

/** Persistence boundary for bookings. In-memory today, a DB tomorrow. */
public interface BookingRepository {
    void save(Booking booking);
    Booking findById(String id);
    Collection<Booking> findAll();
}
