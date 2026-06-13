package model;

import java.util.Objects;

public class BalanceKey {
    private final Integer firstUserId;
    private final Integer secondUserId;

    public BalanceKey(User first, User second) {
        Objects.requireNonNull(first, "first user cannot be null");
        Objects.requireNonNull(second, "second user cannot be null");
        Objects.requireNonNull(first.getId(), "first user id cannot be null");
        Objects.requireNonNull(second.getId(), "second user id cannot be null");

        if (first.getId().equals(second.getId())) {
            throw new IllegalArgumentException("balance users must be different");
        }

        if (first.getId() < second.getId()) {
            this.firstUserId = first.getId();
            this.secondUserId = second.getId();
        } else {
            this.firstUserId = second.getId();
            this.secondUserId = first.getId();
        }
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) {
            return true;
        }
        if (!(object instanceof BalanceKey)) {
            return false;
        }
        BalanceKey other = (BalanceKey) object;
        return firstUserId.equals(other.firstUserId)
                && secondUserId.equals(other.secondUserId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(firstUserId, secondUserId);
    }
}
