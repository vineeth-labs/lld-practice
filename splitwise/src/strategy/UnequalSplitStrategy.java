package strategy;

import model.Split;
import model.User;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class UnequalSplitStrategy implements SplitStrategy {

    private static final double TOLERANCE = 0.000001;

    @Override
    public List<Split> calculateSplits(List<User> usersInvolved, double amount,
                                       Map<User, Double> splitValues) {
        validate(usersInvolved, amount, splitValues);

        List<Split> splits = new ArrayList<>();
        for (User user : usersInvolved) {
            splits.add(new Split(user, splitValues.get(user)));
        }
        return splits;
    }

    private void validate(List<User> usersInvolved, double amount,
                          Map<User, Double> splitValues) {
        Objects.requireNonNull(splitValues, "unequal amounts are required");

        double total = 0;
        for (User user : usersInvolved) {
            Double splitAmount = splitValues.get(user);
            if (splitAmount == null || !Double.isFinite(splitAmount) || splitAmount < 0) {
                throw new IllegalArgumentException(
                        "each involved user must have a non-negative split amount"
                );
            }
            total += splitAmount;
        }

        if (Math.abs(total - amount) > TOLERANCE) {
            throw new IllegalArgumentException(
                    "unequal amounts must add up to the expense amount"
            );
        }
    }
}
