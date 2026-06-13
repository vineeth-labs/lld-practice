import model.Balance;
import model.User;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

// combines the separate per-scope ledgers (groups + global) into an overall view
public class BalanceSheet {

    private final BalanceService ledgers;

    public BalanceSheet(BalanceService ledgers) {
        this.ledgers = ledgers;
    }

    // net amount owed to each counterparty across all scopes (+ = user owes them, - = they owe user)
    public Map<User, Double> overallFor(User user) {
        Map<User, Double> net = new HashMap<>();
        for (Balance balance : ledgers.getBalances()) {
            if (isSameUser(balance.getDebitor(), user)) {
                net.merge(balance.getCreditor(), balance.getAmount(), Double::sum);
            } else if (isSameUser(balance.getCreditor(), user)) {
                net.merge(balance.getDebitor(), -balance.getAmount(), Double::sum);
            }
        }
        return net;
    }

    // net amount between two users across all scopes (+ = a owes b, - = b owes a)
    public double between(User a, User b) {
        return overallFor(a).getOrDefault(b, 0.0);
    }

    private boolean isSameUser(User first, User second) {
        return Objects.equals(first.getId(), second.getId());
    }
}
