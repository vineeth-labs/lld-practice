import model.Balance;
import model.BalanceKey;
import model.User;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BalanceService {
    // map of (groupId, map(pair(userA, userB), settlement))
    Map<String, Map<BalanceKey, Balance>> balances;

    public BalanceService() {
        balances = new HashMap<>();
    }

    public void applyDebts(List<Balance> balances, String groupId) {
        for (Balance bal: balances) {
            applyDebt(bal, groupId);
        }
    }

    public void applyDebt(Balance balance, String groupId) {
        // TODO: Implement this
        // apply
        // find the current balance between the user pair
        // if after adding this if makes teh amount negative, then reverse and add creditor and debitor to the map
    }



}
