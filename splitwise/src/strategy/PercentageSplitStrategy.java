package strategy;

import model.Split;
import model.User;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class PercentageSplitStrategy implements SplitStrategy {

    private static final double TOLERANCE = 0.000001;

    @Override
    public List<Split> calculateSplits(List<User> usersInvolved, double amount,
                                       Map<User, Double> splitValues) {
        double totalFraction = splitValues.values().stream().mapToDouble(Double::doubleValue).sum();
        if (Math.abs(totalFraction - 1.0) > TOLERANCE) {
            throw new IllegalArgumentException("percentage fractions must add up to 1.0");
        }

        List<Split> splits = new ArrayList<>();
        for (User user : usersInvolved) {
            splits.add(new Split(user, splitValues.get(user) * amount));
        }
        return splits;
    }
}