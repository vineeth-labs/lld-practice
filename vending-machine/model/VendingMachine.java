package model;

import state.IdleState;
import state.OutOfServiceState;
import state.VendingMachineState;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class VendingMachine {
    Inventory inventory;
    VendingMachineState state;
    CashInventory cashInventory;
    List<Transaction> transactions;
    Product currentProduct;
    Map<Coin, Integer> coinsBeingHeld;

    VendingMachine(Inventory inventory) {
        this.inventory = inventory;
        state = new IdleState();
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

    public List<Transaction> getTransactions() {
        return transactions;
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
        coinsToAdd.forEach((coin, count) -> {
            int quantity = this.coinsBeingHeld.get(coin);
            quantity += count;
            this.coinsBeingHeld.put(coin, quantity);
        });
    }

    public void resetState() {
        state = new IdleState();
        currentProduct = null;
        coinsBeingHeld = new HashMap<Coin, Integer>();
    }

    public void putMachineUnderOutOfService(VendingMachine vendingMachine) {
        state = new OutOfServiceState();
    }

}