import model.Balance;
import model.Expense;
import model.Group;
import model.Settlement;
import model.SplitType;
import model.User;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SplitwiseSystem {

    private final UserService userService;
    private final ExpenseService expenseService;
    private final BalanceService balanceService;
    private final SettlementService settlementService;
    private final Map<String, Group> groups = new HashMap<>();

    public SplitwiseSystem() {
        this(new UserService(), new ExpenseService(), new BalanceService(), new SettlementService());
    }

    public SplitwiseSystem(UserService userService, ExpenseService expenseService,
                           BalanceService balanceService, SettlementService settlementService) {
        this.userService = userService;
        this.expenseService = expenseService;
        this.balanceService = balanceService;
        this.settlementService = settlementService;
    }

    // ----- Users -----

    public User addUser(Integer id, String name, String email) {
        User user = new User(id, name, email);
        userService.addUser(user);
        return user;
    }

    public User getUser(int userId) {
        return userService.getUser(userId);
    }

    public List<User> getUsers() {
        return userService.getUsers();
    }

    // ----- Groups -----

    public Group createGroup(String groupId, String groupName, List<User> members) {
        Group group = new Group(groupId, groupName, members == null ? List.of() : members);
        groups.put(groupId, group);
        return group;
    }

    public Group createGroup(String groupId, String groupName) {
        return createGroup(groupId, groupName, List.of());
    }

    public void addUserToGroup(int userId, String groupId) {
        groups.get(groupId).addUser(userService.getUser(userId));
    }

    public Group getGroup(String groupId) {
        return groups.get(groupId);
    }

    public List<Group> getGroups() {
        return new ArrayList<>(groups.values());
    }

    // ----- Expenses & settlements -----

    public Expense recordExpense(User paidBy, List<User> usersInvolved, Double amount,
                                 SplitType splitType, Map<User, Double> splitValues, Group group) {
        Expense expense = expenseService.recordExpense(paidBy, usersInvolved, amount, splitType, splitValues, group);
        balanceService.applyDebts(expenseService.createDebtChanges(expense), group.getId());
        return expense;
    }

    public Settlement recordSettlement(User paidBy, User paidTo, Double amount, Group group) {
        Settlement settlement = settlementService.addSettlement(paidBy, paidTo, amount, group.getId());
        balanceService.applyDebts(settlementService.createDebtChanges(settlement), group.getId());
        return settlement;
    }

    public List<Expense> getExpenses() {
        return expenseService.getExpenses();
    }

    public List<Expense> getExpenses(String groupId) {
        return expenseService.getExpenses(groupId);
    }

    public List<Settlement> getSettlements() {
        return settlementService.getSettlements();
    }

    public List<Settlement> getSettlements(String groupId) {
        return settlementService.getSettlements(groupId);
    }

    // ----- Balances -----

    public List<Balance> getBalances() {
        return balanceService.getBalances();
    }

    public List<Balance> getBalances(String groupId) {
        return balanceService.getBalances(groupId);
    }

    public List<Balance> getBalances(User user) {
        return balancesFor(user, balanceService.getBalances());
    }

    public List<Balance> getBalances(User user, String groupId) {
        return balancesFor(user, balanceService.getBalances(groupId));
    }

    public Balance getBalance(User first, User second, String groupId) {
        return balanceService.getBalance(first, second, groupId);
    }

    private List<Balance> balancesFor(User user, List<Balance> balances) {
        List<Balance> result = new ArrayList<>();
        for (Balance balance : balances) {
            if (balance.getDebitor().getId().equals(user.getId())
                    || balance.getCreditor().getId().equals(user.getId())) {
                result.add(balance);
            }
        }
        return result;
    }
}
