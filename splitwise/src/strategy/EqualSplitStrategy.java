package strategy;

import model.Split;
import model.User;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class EqualSplitStrategy implements SplitStrategy {

    @Override
    public List<Split> calculateSplits(List<User> usersInvolved, double amount,
                                       Map<User, Double> splitValues) {
        double individualAmount = amount / usersInvolved.size();
        List<Split> splits = new ArrayList<>();

        for (User user : usersInvolved) {
            splits.add(new Split(user, individualAmount));
        }
        return splits;
    }
}
