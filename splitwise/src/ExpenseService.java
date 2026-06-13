import model.*;
import strategy.EqualSplitStrategy;
import strategy.PercentageSplitStrategy;
import strategy.SplitStrategy;
import strategy.UnequalSplitStrategy;

import java.util.ArrayList;
import java.util.Collections;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class ExpenseService {

    // map of (groupId, expenses)
    private final Map<String, List<Expense>> expenses;
    private final Map<SplitType, SplitStrategy> splitStrategies;

    public ExpenseService() {
        this.expenses = new HashMap<>();
        this.splitStrategies = new EnumMap<>(SplitType.class);
        splitStrategies.put(SplitType.EQUAL, new EqualSplitStrategy());
        splitStrategies.put(SplitType.PERCENTAGE, new PercentageSplitStrategy());
        splitStrategies.put(SplitType.UNEQUAL, new UnequalSplitStrategy());
    }

    public Expense recordExpense(User paidBy, List<User> usersInvolved, Double amount,
                                 SplitType splitType, Map<User, Double> splitValues, Group group) {
        validateExpense(paidBy, usersInvolved, amount, splitType, group);

        SplitStrategy splitStrategy = splitStrategies.get(splitType);
        List<Split> splits = splitStrategy.calculateSplits(usersInvolved, amount, splitValues);
        Expense expense = new Expense(paidBy, amount, splitType, splits, group);
        expenses.computeIfAbsent(group.getId(), ignored -> new ArrayList<>()).add(expense);
        return expense;
    }

    public List<Expense> getExpenses() {
        List<Expense> allExpenses = new ArrayList<>();
        for (List<Expense> groupExpenses : expenses.values()) {
            allExpenses.addAll(groupExpenses);
        }
        return Collections.unmodifiableList(allExpenses);
    }

    public List<Expense> getExpenses(String groupId) {
        List<Expense> groupExpenses = expenses.get(groupId);
        if (groupExpenses == null) {
            return Collections.emptyList();
        }
        return Collections.unmodifiableList(new ArrayList<>(groupExpenses));
    }

    public List<Balance> createDebtChanges(Expense expense) {
        Objects.requireNonNull(expense, "expense cannot be null");

        List<Balance> debtChanges = new ArrayList<>();
        for (Split split : expense.getSplits()) {
            if (!isSameUser(split.getOwedBy(), expense.getPaidBy()) && split.getAmount() > 0) {
                debtChanges.add(new Balance(
                        split.getOwedBy(),
                        expense.getPaidBy(),
                        split.getAmount()
                ));
            }
        }
        return debtChanges;
    }

    private void validateExpense(User paidBy, List<User> usersInvolved, Double amount,
                                 SplitType splitType, Group group) {
        Objects.requireNonNull(paidBy, "paidBy cannot be null");
        Objects.requireNonNull(usersInvolved, "usersInvolved cannot be null");
        Objects.requireNonNull(amount, "amount cannot be null");
        Objects.requireNonNull(splitType, "splitType cannot be null");
        Objects.requireNonNull(group, "group cannot be null");
        Objects.requireNonNull(group.getId(), "group id cannot be null");

        if (usersInvolved.isEmpty()) {
            throw new IllegalArgumentException("usersInvolved cannot be empty");
        }
        if (!Double.isFinite(amount) || amount <= 0) {
            throw new IllegalArgumentException("amount must be a positive finite number");
        }
        for (User user : usersInvolved) {
            Objects.requireNonNull(user, "usersInvolved cannot contain null");
        }
    }

    private boolean isSameUser(User first, User second) {
        if (first == second) {
            return true;
        }
        return first != null && second != null && Objects.equals(first.getId(), second.getId());
    }
}
