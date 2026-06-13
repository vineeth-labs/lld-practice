import model.Balance;
import model.Scope;
import model.Settlement;
import model.User;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.HashMap;

public class SettlementService {
    // map of (scopeId, settlements)
    private final Map<String, List<Settlement>> settlements = new HashMap<>();

    public List<Settlement> getSettlements() {
        List<Settlement> allSettlements = new ArrayList<>();
        settlements.values().forEach(allSettlements::addAll);
        return allSettlements;
    }

    public List<Settlement> getSettlements(String groupId) {
        return new ArrayList<>(settlements.getOrDefault(groupId, List.of()));
    }

    public Settlement addSettlement(User paidBy, User paidTo, Double amount, Scope scope) {
        Settlement settlement = new Settlement(paidBy, paidTo, amount, scope);
        settlements.computeIfAbsent(scope.id(), ignored -> new ArrayList<>()).add(settlement);
        return settlement;
    }

    public List<Balance> createDebtChanges(Settlement settlement) {
        // settling up means the payer's debt to the payee goes down
        return List.of(new Balance(settlement.getPaidTo(), settlement.getPaidBy(), settlement.getAmount()));
    }
}
