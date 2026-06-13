import model.Balance;
import model.BalanceKey;
import model.User;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class BalanceService {
    private static final double ZERO_TOLERANCE = 0.000001;

    // map of (groupId, map(pair(userA, userB), balance))
    private final Map<String, Map<BalanceKey, Balance>> balances = new HashMap<>();

    public void applyDebts(List<Balance> debtChanges, String groupId) {
        debtChanges.forEach(debt -> applyDebt(debt, groupId));
    }

    public void applyDebt(Balance debtChange, String groupId) {
        BalanceKey key = new BalanceKey(debtChange.getDebitor(), debtChange.getCreditor());
        Map<BalanceKey, Balance> groupBalances = balances.computeIfAbsent(groupId, ignored -> new HashMap<>());
        Balance current = groupBalances.get(key);

        if (current == null) {
            groupBalances.put(key, debtChange);
            return;
        }

        // net the new debt against the existing one; keep the direction of whoever owes more
        double netAmount;
        User netDebitor;
        User netCreditor;
        if (isSameDirection(current, debtChange)) {
            netAmount = current.getAmount() + debtChange.getAmount();
            netDebitor = current.getDebitor();
            netCreditor = current.getCreditor();
        } else if (current.getAmount() >= debtChange.getAmount()) {
            netAmount = current.getAmount() - debtChange.getAmount();
            netDebitor = current.getDebitor();
            netCreditor = current.getCreditor();
        } else {
            netAmount = debtChange.getAmount() - current.getAmount();
            netDebitor = debtChange.getDebitor();
            netCreditor = debtChange.getCreditor();
        }

        if (netAmount <= ZERO_TOLERANCE) {
            groupBalances.remove(key);
        } else {
            groupBalances.put(key, new Balance(netDebitor, netCreditor, netAmount));
        }
    }

    public List<Balance> getBalances(String groupId) {
        return new ArrayList<>(balances.getOrDefault(groupId, Map.of()).values());
    }

    public List<Balance> getBalances() {
        List<Balance> allBalances = new ArrayList<>();
        balances.values().forEach(groupBalances -> allBalances.addAll(groupBalances.values()));
        return allBalances;
    }

    public Balance getBalance(User first, User second, String groupId) {
        Map<BalanceKey, Balance> groupBalances = balances.get(groupId);
        return groupBalances == null ? null : groupBalances.get(new BalanceKey(first, second));
    }

    private boolean isSameDirection(Balance first, Balance second) {
        return Objects.equals(first.getDebitor().getId(), second.getDebitor().getId());
    }
}
