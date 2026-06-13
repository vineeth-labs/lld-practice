import model.Balance;
import model.BalanceKey;
import model.User;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class BalanceService {
    private static final double ZERO_TOLERANCE = 0.000001;

    // map of (groupId, map(pair(userA, userB), balance))
    private final Map<String, Map<BalanceKey, Balance>> balances;

    public BalanceService() {
        balances = new HashMap<>();
    }

    public void applyDebts(List<Balance> debtChanges, String groupId) {
        Objects.requireNonNull(debtChanges, "debtChanges cannot be null");
        for (Balance balance : debtChanges) {
            applyDebt(balance, groupId);
        }
    }

    public void applyDebt(Balance debtChange, String groupId) {
        validateDebtChange(debtChange, groupId);

        BalanceKey key = new BalanceKey(
                debtChange.getDebitor(),
                debtChange.getCreditor()
        );
        Map<BalanceKey, Balance> groupBalances =
                balances.computeIfAbsent(groupId, ignored -> new HashMap<>());
        Balance currentBalance = groupBalances.get(key);

        if (currentBalance == null) {
            groupBalances.put(key, debtChange);
            return;
        }

        double netAmount;
        User netDebitor;
        User netCreditor;
        if (isSameDirection(currentBalance, debtChange)) {
            netAmount = currentBalance.getAmount() + debtChange.getAmount();
            netDebitor = currentBalance.getDebitor();
            netCreditor = currentBalance.getCreditor();
        } else {
            netAmount = currentBalance.getAmount() - debtChange.getAmount();
            if (netAmount >= 0) {
                netDebitor = currentBalance.getDebitor();
                netCreditor = currentBalance.getCreditor();
            } else {
                netAmount = -netAmount;
                netDebitor = debtChange.getDebitor();
                netCreditor = debtChange.getCreditor();
            }
        }

        if (netAmount <= ZERO_TOLERANCE) {
            groupBalances.remove(key);
            if (groupBalances.isEmpty()) {
                balances.remove(groupId);
            }
            return;
        }

        groupBalances.put(key, new Balance(netDebitor, netCreditor, netAmount));
    }

    public List<Balance> getBalances(String groupId) {
        Objects.requireNonNull(groupId, "groupId cannot be null");
        Map<BalanceKey, Balance> groupBalances = balances.get(groupId);
        if (groupBalances == null) {
            return Collections.emptyList();
        }
        return Collections.unmodifiableList(new ArrayList<>(groupBalances.values()));
    }

    public List<Balance> getBalances() {
        List<Balance> allBalances = new ArrayList<>();
        for (Map<BalanceKey, Balance> groupBalances : balances.values()) {
            allBalances.addAll(groupBalances.values());
        }
        return Collections.unmodifiableList(allBalances);
    }

    public Balance getBalance(User first, User second, String groupId) {
        Objects.requireNonNull(groupId, "groupId cannot be null");
        BalanceKey key = new BalanceKey(first, second);
        Map<BalanceKey, Balance> groupBalances = balances.get(groupId);
        return groupBalances == null ? null : groupBalances.get(key);
    }

    private void validateDebtChange(Balance debtChange, String groupId) {
        Objects.requireNonNull(debtChange, "debtChange cannot be null");
        Objects.requireNonNull(groupId, "groupId cannot be null");
        Objects.requireNonNull(debtChange.getDebitor(), "debitor cannot be null");
        Objects.requireNonNull(debtChange.getCreditor(), "creditor cannot be null");
        Objects.requireNonNull(debtChange.getAmount(), "amount cannot be null");
        Objects.requireNonNull(debtChange.getDebitor().getId(), "debitor id cannot be null");
        Objects.requireNonNull(debtChange.getCreditor().getId(), "creditor id cannot be null");

        if (Objects.equals(
                debtChange.getDebitor().getId(),
                debtChange.getCreditor().getId()
        )) {
            throw new IllegalArgumentException("debitor and creditor must be different users");
        }
        if (!Double.isFinite(debtChange.getAmount()) || debtChange.getAmount() <= 0) {
            throw new IllegalArgumentException("amount must be a positive finite number");
        }
    }

    private boolean isSameDirection(Balance first, Balance second) {
        return Objects.equals(
                first.getDebitor().getId(),
                second.getDebitor().getId()
        );
    }
}
