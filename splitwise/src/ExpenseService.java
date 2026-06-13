import model.*;
import strategy.EqualSplitStrategy;
import strategy.PercentageSplitStrategy;
import strategy.SplitStrategy;
import strategy.UnequalSplitStrategy;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class ExpenseService {

    // map of (scopeId, expenses)
    private final Map<String, List<Expense>> expenses = new HashMap<>();
    private final Map<SplitType, SplitStrategy> splitStrategies = new EnumMap<>(SplitType.class);

    public ExpenseService() {
        splitStrategies.put(SplitType.EQUAL, new EqualSplitStrategy());
        splitStrategies.put(SplitType.PERCENTAGE, new PercentageSplitStrategy());
        splitStrategies.put(SplitType.UNEQUAL, new UnequalSplitStrategy());
    }

    public Expense recordExpense(User paidBy, List<User> usersInvolved, Double amount,
                                 SplitType splitType, Map<User, Double> splitValues, Scope scope) {
        List<Split> splits = splitStrategies.get(splitType).calculateSplits(usersInvolved, amount, splitValues);
        Expense expense = new Expense(paidBy, amount, splitType, splits, scope);
        expenses.computeIfAbsent(scope.id(), ignored -> new ArrayList<>()).add(expense);
        return expense;
    }

    public List<Expense> getExpenses() {
        List<Expense> allExpenses = new ArrayList<>();
        expenses.values().forEach(allExpenses::addAll);
        return allExpenses;
    }

    public List<Expense> getExpenses(String groupId) {
        return new ArrayList<>(expenses.getOrDefault(groupId, List.of()));
    }

    public List<Balance> createDebtChanges(Expense expense) {
        List<Balance> debtChanges = new ArrayList<>();
        for (Split split : expense.getSplits()) {
            User owedBy = split.getOwedBy();
            if (!Objects.equals(owedBy.getId(), expense.getPaidBy().getId()) && split.getAmount() > 0) {
                debtChanges.add(new Balance(owedBy, expense.getPaidBy(), split.getAmount()));
            }
        }
        return debtChanges;
    }
}
