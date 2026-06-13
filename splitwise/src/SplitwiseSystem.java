import model.*;

import java.util.List;
import java.util.Map;

public class SplitwiseSystem {
    UserService userService = new UserService();
    ExpenseService expenseService;
    BalanceService balanceService;
    SettlementService settlementService;

    public void recordExpense(User paidBy, List<User> usersInvolved, Double amount, SplitType splitType, Map<User, Double> unequalAmounts, Group group) {
        Expense expense = expenseService.recordExpense(paidBy, usersInvolved, amount, splitType, unequalAmounts, group);
        List<Balance> balances  = expenseService.createDebtChanges(expense);

        balanceService.applyDebts(balances, group.getId());
    }

    public void recordSettlement(User paidBy, User paidTo, Double amount, Group group) {
        Settlement settlement = settlementService.addSettlement(
                paidBy,
                paidTo,
                amount,
                group.getId()
        );
        List<Balance> balances = settlementService.createDebtChanges(settlement);
        balanceService.applyDebts(balances, group.getId());
    }

}
