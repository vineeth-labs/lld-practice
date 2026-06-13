import model.Balance;
import model.Expense;
import model.Group;
import model.Settlement;
import model.SplitType;
import model.User;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

public class SplitwiseSystem {
    private static final double AMOUNT_TOLERANCE = 0.000001;

    private final UserService userService;
    private final ExpenseService expenseService;
    private final BalanceService balanceService;
    private final SettlementService settlementService;
    private final Map<String, Group> groups;

    public SplitwiseSystem() {
        this(
                new UserService(),
                new ExpenseService(),
                new BalanceService(),
                new SettlementService()
        );
    }

    public SplitwiseSystem(UserService userService, ExpenseService expenseService,
                           BalanceService balanceService,
                           SettlementService settlementService) {
        this.userService = Objects.requireNonNull(userService, "userService cannot be null");
        this.expenseService =
                Objects.requireNonNull(expenseService, "expenseService cannot be null");
        this.balanceService =
                Objects.requireNonNull(balanceService, "balanceService cannot be null");
        this.settlementService =
                Objects.requireNonNull(settlementService, "settlementService cannot be null");
        this.groups = new HashMap<>();
    }

    public User addUser(Integer id, String name, String email) {
        if (id == null) {
            throw new NullPointerException("user id cannot be null");
        }
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("user name cannot be blank");
        }
        if (email == null || email.isBlank()) {
            throw new IllegalArgumentException("user email cannot be blank");
        }

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

    public Group createGroup(String groupId, String groupName, List<User> members) {
        validateGroupDetails(groupId, groupName);
        if (groups.containsKey(groupId)) {
            throw new IllegalArgumentException("group id already exists: " + groupId);
        }

        List<User> initialMembers = members == null
                ? Collections.emptyList()
                : new ArrayList<>(members);
        validateDistinctRegisteredUsers(initialMembers);

        Group group = new Group(groupId, groupName, initialMembers);
        groups.put(groupId, group);
        return group;
    }

    public Group createGroup(String groupId, String groupName) {
        return createGroup(groupId, groupName, Collections.emptyList());
    }

    public void addUserToGroup(int userId, String groupId) {
        User user = requireUser(userId);
        Group group = requireGroup(groupId);
        group.addUser(user);
    }

    public Group getGroup(String groupId) {
        return groups.get(groupId);
    }

    public List<Group> getGroups() {
        return Collections.unmodifiableList(new ArrayList<>(groups.values()));
    }

    public Expense recordExpense(User paidBy, List<User> usersInvolved, Double amount,
                                 SplitType splitType, Map<User, Double> splitValues,
                                 Group group) {
        Group registeredGroup = requireRegisteredGroup(group);
        requireGroupMember(paidBy, registeredGroup);
        validateDistinctRegisteredUsers(usersInvolved);
        for (User user : usersInvolved) {
            requireGroupMember(user, registeredGroup);
        }

        Expense expense = expenseService.recordExpense(
                paidBy,
                usersInvolved,
                amount,
                splitType,
                splitValues,
                registeredGroup
        );
        balanceService.applyDebts(
                expenseService.createDebtChanges(expense),
                registeredGroup.getId()
        );
        return expense;
    }

    public Settlement recordSettlement(User paidBy, User paidTo, Double amount, Group group) {
        Group registeredGroup = requireRegisteredGroup(group);
        requireGroupMember(paidBy, registeredGroup);
        requireGroupMember(paidTo, registeredGroup);
        validateSettlementAgainstBalance(paidBy, paidTo, amount, registeredGroup.getId());

        Settlement settlement = settlementService.addSettlement(
                paidBy,
                paidTo,
                amount,
                registeredGroup.getId()
        );
        balanceService.applyDebts(
                settlementService.createDebtChanges(settlement),
                registeredGroup.getId()
        );
        return settlement;
    }

    public List<Expense> getExpenses() {
        return expenseService.getExpenses();
    }

    public List<Expense> getExpenses(String groupId) {
        requireGroup(groupId);
        return expenseService.getExpenses(groupId);
    }

    public List<Settlement> getSettlements() {
        return settlementService.getSettlements();
    }

    public List<Settlement> getSettlements(String groupId) {
        requireGroup(groupId);
        return settlementService.getSettlements(groupId);
    }

    public List<Balance> getBalances() {
        return balanceService.getBalances();
    }

    public List<Balance> getBalances(String groupId) {
        requireGroup(groupId);
        return balanceService.getBalances(groupId);
    }

    public List<Balance> getBalances(User user) {
        requireRegisteredUser(user);
        return filterBalancesForUser(balanceService.getBalances(), user);
    }

    public List<Balance> getBalances(User user, String groupId) {
        Group group = requireGroup(groupId);
        requireGroupMember(user, group);
        return filterBalancesForUser(balanceService.getBalances(groupId), user);
    }

    public Balance getBalance(User first, User second, String groupId) {
        Group group = requireGroup(groupId);
        requireGroupMember(first, group);
        requireGroupMember(second, group);
        return balanceService.getBalance(first, second, groupId);
    }

    private User requireUser(int userId) {
        User user = userService.getUser(userId);
        if (user == null) {
            throw new IllegalArgumentException("unknown user id: " + userId);
        }
        return user;
    }

    private Group requireGroup(String groupId) {
        Objects.requireNonNull(groupId, "groupId cannot be null");
        Group group = groups.get(groupId);
        if (group == null) {
            throw new IllegalArgumentException("unknown group id: " + groupId);
        }
        return group;
    }

    private Group requireRegisteredGroup(Group group) {
        Objects.requireNonNull(group, "group cannot be null");
        Group registeredGroup = requireGroup(group.getId());
        if (registeredGroup != group) {
            throw new IllegalArgumentException("group must be managed by this system");
        }
        return registeredGroup;
    }

    private void requireGroupMember(User user, Group group) {
        requireRegisteredUser(user);
        boolean isMember = group.getUsers().stream()
                .anyMatch(member -> Objects.equals(member.getId(), user.getId()));
        if (!isMember) {
            throw new IllegalArgumentException(
                    "user " + user.getId() + " is not a member of group " + group.getId()
            );
        }
    }

    private void validateDistinctRegisteredUsers(List<User> users) {
        Objects.requireNonNull(users, "users cannot be null");
        Set<Integer> userIds = new HashSet<>();
        for (User user : users) {
            requireRegisteredUser(user);
            if (!userIds.add(user.getId())) {
                throw new IllegalArgumentException("users cannot contain duplicates");
            }
        }
    }

    private void requireRegisteredUser(User user) {
        Objects.requireNonNull(user, "user cannot be null");
        Objects.requireNonNull(user.getId(), "user id cannot be null");
        if (userService.getUser(user.getId()) != user) {
            throw new IllegalArgumentException("user must be registered with this system");
        }
    }

    private void validateSettlementAgainstBalance(User paidBy, User paidTo, Double amount,
                                                  String groupId) {
        Objects.requireNonNull(amount, "amount cannot be null");
        if (!Double.isFinite(amount) || amount <= 0) {
            throw new IllegalArgumentException("amount must be a positive finite number");
        }

        Balance balance = balanceService.getBalance(paidBy, paidTo, groupId);
        if (balance == null) {
            throw new IllegalArgumentException("no outstanding balance exists between users");
        }
        if (!Objects.equals(balance.getDebitor().getId(), paidBy.getId())
                || !Objects.equals(balance.getCreditor().getId(), paidTo.getId())) {
            throw new IllegalArgumentException("settlement must be paid by the current debitor");
        }
        if (amount - balance.getAmount() > AMOUNT_TOLERANCE) {
            throw new IllegalArgumentException("settlement exceeds the outstanding balance");
        }
    }

    private List<Balance> filterBalancesForUser(List<Balance> balances, User user) {
        List<Balance> userBalances = new ArrayList<>();
        for (Balance balance : balances) {
            if (Objects.equals(balance.getDebitor().getId(), user.getId())
                    || Objects.equals(balance.getCreditor().getId(), user.getId())) {
                userBalances.add(balance);
            }
        }
        return Collections.unmodifiableList(userBalances);
    }

    private void validateGroupDetails(String groupId, String groupName) {
        if (groupId == null || groupId.isBlank()) {
            throw new IllegalArgumentException("group id cannot be blank");
        }
        if (groupName == null || groupName.isBlank()) {
            throw new IllegalArgumentException("group name cannot be blank");
        }
    }
}
