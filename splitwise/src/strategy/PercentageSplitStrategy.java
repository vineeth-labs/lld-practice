package strategy;

import model.Split;
import model.User;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class PercentageSplitStrategy implements SplitStrategy {

    private static final double TOTAL_PERCENTAGE = 1.0;
    private static final double TOLERANCE = 0.000001;

    @Override
    public List<Split> calculateSplits(List<User> usersInvolved, double amount,
                                       Map<User, Double> splitValues) {
        validate(usersInvolved, splitValues);

        List<Split> splits = new ArrayList<>();
        for (User user : usersInvolved) {
            splits.add(new Split(user, splitValues.get(user) * amount));
        }
        return splits;
    }

    private void validate(List<User> usersInvolved, Map<User, Double> splitValues) {
        Objects.requireNonNull(splitValues, "percentage values are required");

        double total = 0;
        for (User user : usersInvolved) {
            Double percentage = splitValues.get(user);
            if (percentage == null || !Double.isFinite(percentage) || percentage < 0) {
                throw new IllegalArgumentException(
                        "each involved user must have a non-negative percentage fraction"
                );
            }
            total += percentage;
        }

        if (Math.abs(total - TOTAL_PERCENTAGE) > TOLERANCE) {
            throw new IllegalArgumentException("percentage fractions must add up to 1.0");
        }
    }
}
