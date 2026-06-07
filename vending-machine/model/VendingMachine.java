package model;

import state.IdleState;
import state.OutOfServiceState;
import state.VendingMachineState;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class VendingMachine {
    private Inventory inventory;
    private VendingMachineState state;
    private CashInventory cashInventory;
    private List<Transaction> transactions;
    private Product currentProduct;
    private Map<Coin, Integer> coinsBeingHeld;

    public VendingMachine(Inventory inventory) {
        this(inventory, new CashInventory());
    }

    public VendingMachine(Inventory inventory, CashInventory cashInventory) {
        this.inventory = inventory;
        this.cashInventory = cashInventory == null ? new CashInventory() : cashInventory;
        this.state = new IdleState();
        this.transactions = new java.util.ArrayList<>();
        this.coinsBeingHeld = new java.util.HashMap<>();
    }

    public void selectProduct(int productId) {
        state.selectProduct(this, productId);
    }

    public void insertMoney(Map<Coin, Integer> coins) {
        state.insertMoney(this, coins);
    }

    public void dispenseProduct(Map<Coin, Integer> coinsToGive) {
        state.dispenseProduct(this, this.getCoinsBeingHeld(), coinsToGive);
    }

    public void cancelTransaction() {
        state.cancelTransaction(this);
    }

    public Inventory getInventory() {
        return inventory;
    }

    public void setState(VendingMachineState state) {
        this.state = state;
    }

    public CashInventory getCashInventory() {
        return cashInventory;
    }

    public void setCashInventory(CashInventory cashInventory) {
        this.cashInventory = cashInventory;
    }


    public Product getCurrentProduct() {
        return currentProduct;
    }

    public void setCurrentProduct(Product currentProduct) {
        this.currentProduct = currentProduct;
    }

    public Map<Coin, Integer> getCoinsBeingHeld() {
        return coinsBeingHeld;
    }

    public void setCoinsBeingHeld(Map<Coin, Integer> coinsBeingHeld) {
        this.coinsBeingHeld = coinsBeingHeld;
    }

    public void addCoinsBeingHeld(Map<Coin, Integer> coinsToAdd) {
        if (coinsToAdd == null) {
            return;
        }
        if (this.coinsBeingHeld == null) {
            this.coinsBeingHeld = new java.util.HashMap<>();
        }
        coinsToAdd.forEach((coin, count) -> {
            int quantity = this.coinsBeingHeld.getOrDefault(coin, 0);
            quantity += (count == null ? 0 : count);
            this.coinsBeingHeld.put(coin, quantity);
        });
    }

    public void resetState() {
        state = new IdleState();
        currentProduct = null;
        coinsBeingHeld = new HashMap<Coin, Integer>();
    }

    public void putMachineUnderOutOfService() {
        state = new OutOfServiceState();
    }

    public List<Transaction> getTransactions() {
        return transactions;
    }

    public void addTransaction(Transaction tx) {
        if (this.transactions == null) {
            this.transactions = new java.util.ArrayList<>();
        }
        this.transactions.add(tx);
    }

}