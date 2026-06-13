import model.Balance;
import model.Expense;
import model.Settlement;
import model.User;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SettlementService {
    // map of (groupId, settlements)
    Map<String, List<Settlement>> settlements;

    public SettlementService() {
        this.settlements = new HashMap<>();
    }

    public List<Settlement> getSettlements() {
        return null;
    }

    public List<Settlement> getSettlements(String groupId) {
        return settlements.get(groupId);
    }

    public Settlement addSettlement(String groupId) {
        // TODO: create settlement and add
        return null;
    }

    // a function to convert expense to list
    public List<Balance> createDebtChanges(Expense expense) {
        // TODO: implement this
        return null;
    }

}
