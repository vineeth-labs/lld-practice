import model.Balance;
import model.Settlement;
import model.User;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class SettlementService {
    // map of (groupId, settlements)
    private final Map<String, List<Settlement>> settlements;

    public SettlementService() {
        this.settlements = new HashMap<>();
    }

    public List<Settlement> getSettlements() {
        List<Settlement> allSettlements = new ArrayList<>();
        for (List<Settlement> groupSettlements : settlements.values()) {
            allSettlements.addAll(groupSettlements);
        }
        return Collections.unmodifiableList(allSettlements);
    }

    public List<Settlement> getSettlements(String groupId) {
        Objects.requireNonNull(groupId, "groupId cannot be null");

        List<Settlement> groupSettlements = settlements.get(groupId);
        if (groupSettlements == null) {
            return Collections.emptyList();
        }
        return Collections.unmodifiableList(new ArrayList<>(groupSettlements));
    }

    public Settlement addSettlement(User paidBy, User paidTo, Double amount, String groupId) {
        validateSettlement(paidBy, paidTo, amount, groupId);

        Settlement settlement = new Settlement(paidBy, paidTo, amount);
        settlements.computeIfAbsent(groupId, ignored -> new ArrayList<>()).add(settlement);
        return settlement;
    }

    public List<Balance> createDebtChanges(Settlement settlement) {
        Objects.requireNonNull(settlement, "settlement cannot be null");

        return Collections.singletonList(new Balance(
                settlement.getPaidTo(),
                settlement.getPaidBy(),
                settlement.getAmount()
        ));
    }

    private void validateSettlement(User paidBy, User paidTo, Double amount, String groupId) {
        Objects.requireNonNull(paidBy, "paidBy cannot be null");
        Objects.requireNonNull(paidTo, "paidTo cannot be null");
        Objects.requireNonNull(amount, "amount cannot be null");
        Objects.requireNonNull(groupId, "groupId cannot be null");

        if (isSameUser(paidBy, paidTo)) {
            throw new IllegalArgumentException("paidBy and paidTo must be different users");
        }
        if (!Double.isFinite(amount) || amount <= 0) {
            throw new IllegalArgumentException("amount must be a positive finite number");
        }
    }

    private boolean isSameUser(User first, User second) {
        if (first == second) {
            return true;
        }
        return Objects.equals(first.getId(), second.getId());
    }
}
