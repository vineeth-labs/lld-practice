package state;

import model.*;
import strategy.ChangeCalculator;
import util.CoinUtil;

import java.time.LocalDateTime;
import java.util.Map;

public class PaymentState implements VendingMachineState {

    private ChangeCalculator changeCalculator;

    public PaymentState(ChangeCalculator changeCalculator) {
        this.changeCalculator = changeCalculator;
    }
    @Override
    public void selectProduct(VendingMachine vendingMachine, Integer productId) {
        System.out.println("Product already selected");
    }

    @Override
    public void insertMoney(VendingMachine vendingMachine, Map<Coin, Integer> coins) {
        Product currentProduct = vendingMachine.getCurrentProduct();
        if (currentProduct == null) {
            System.out.println("No product selected");
            return;
        }
        // Add inserted coins to the machine's coin collection
        vendingMachine.addCoinsBeingHeld(coins);
        int amount = CoinUtil.calculateAmount(vendingMachine.getCoinsBeingHeld());

        // Check if the inserted amount is sufficient
        if (amount < currentProduct.getPrice()) {
            System.out.println("Insufficient funds. Please insert additional coins.");
            return;
        }

        // Calculate change needed
        int diff = amount - currentProduct.getPrice();
        Map<Coin, Integer> coinsToReturn = changeCalculator.convertToChange(vendingMachine.getCashInventory(), diff);
        
        if (coinsToReturn == null) {
            System.out.println("Not enough change to return. Please insert additional coins.");
            return;
        }

        // Set the state to DispensingState and proceed with dispensing
        vendingMachine.setState(new DispensingState());
        vendingMachine.dispenseProduct(coinsToReturn);
    }

    @Override
    public void dispenseProduct(VendingMachine vendingMachine, Map<Coin, Integer> coinsToAdd, Map<Coin, Integer> coinsToReturn) {
        System.out.println("Please insert money to continue");
    }

    @Override
    public void cancelTransaction(VendingMachine vendingMachine) {
        int amount = CoinUtil.calculateAmount(vendingMachine.getCoinsBeingHeld());
        if (amount > 0) {
            Transaction tx = new Transaction(0, vendingMachine.getCurrentProduct() == null ? 0 : vendingMachine.getCurrentProduct().getId(), TransactionStatus.CANCELLED,
                    amount, LocalDateTime.now());
            vendingMachine.addTransaction(tx);
            System.out.println("Returning coins (Total amount): " + amount);
        }
        System.out.println("Cancelling transaction");
        vendingMachine.resetState();
    }
}