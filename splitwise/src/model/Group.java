package model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class Group {
    String id;
    String groupName;
    List<User> users;

    public Group(String id, String groupName, List<User> users) {
        this.id = Objects.requireNonNull(id, "group id cannot be null");
        this.groupName = Objects.requireNonNull(groupName, "group name cannot be null");
        this.users = new ArrayList<>(Objects.requireNonNull(users, "users cannot be null"));
    }

    public String getId() {
        return id;
    }

    public String getGroupName() {
        return groupName;
    }

    public List<User> getUsers() {
        return Collections.unmodifiableList(new ArrayList<>(users));
    }

    public void addUser(User user) {
        Objects.requireNonNull(user, "user cannot be null");
        boolean alreadyPresent = users.stream()
                .anyMatch(existing -> Objects.equals(existing.getId(), user.getId()));
        if (alreadyPresent) {
            throw new IllegalArgumentException("user is already a member of the group");
        }
        users.add(user);
    }
}
