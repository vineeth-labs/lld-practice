package strategy;

import model.Split;
import model.User;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class UnequalSplitStrategy implements SplitStrategy {

    private static final double TOLERANCE = 0.000001;

    @Override
    public List<Split> calculateSplits(List<User> usersInvolved, double amount,
                                       Map<User, Double> splitValues) {
        double total = splitValues.values().stream().mapToDouble(Double::doubleValue).sum();
        if (Math.abs(total - amount) > TOLERANCE) {
            throw new IllegalArgumentException("unequal amounts must add up to the expense amount");
        }

        List<Split> splits = new ArrayList<>();
        for (User user : usersInvolved) {
            splits.add(new Split(user, splitValues.get(user)));
        }
        return splits;
    }
}