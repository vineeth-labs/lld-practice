import model.User;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class UserService {

    Map<Integer, User> users;
    public UserService() {
        users = new HashMap<Integer, User>();
    }
    public List<User> getUsers() {
        return users.values().stream().toList();
    }
    public User getUser(int id) {
        return users.get(id);
    }

    public void addUser(User user) {
        users.put(user.getId(), user);
    }

}
