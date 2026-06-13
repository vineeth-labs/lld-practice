import model.User;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class UserService {

    private final Map<Integer, User> users;

    public UserService() {
        users = new HashMap<>();
    }

    public List<User> getUsers() {
        return Collections.unmodifiableList(new ArrayList<>(users.values()));
    }

    public User getUser(int id) {
        return users.get(id);
    }

    public void addUser(User user) {
        Objects.requireNonNull(user, "user cannot be null");
        Objects.requireNonNull(user.getId(), "user id cannot be null");
        if (users.containsKey(user.getId())) {
            throw new IllegalArgumentException("user id already exists: " + user.getId());
        }
        users.put(user.getId(), user);
    }
}
