package strategy;

import model.Split;
import model.User;

import java.util.List;
import java.util.Map;

public interface SplitStrategy {
    List<Split> calculateSplits(List<User> usersInvolved, double amount,
                                Map<User, Double> splitValues);
}
