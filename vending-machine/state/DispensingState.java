package state;

import model.*;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

class DispensingState implements VendingMachineState {

    private DispensingState dispensingState;

    @Override
    public void selectProduct(VendingMachine vendingMachine, Integer productId) {
        System.out.println("You cannot select a product while product is dispensing");
    }

    @Override
    public void insertMoney(VendingMachine vendingMachine, Map<Coin, Integer> coins) {
        System.out.println("You cannot insert money while product is dispensing");
    }

    @Override
    public void dispenseProduct(VendingMachine vendingMachine, Map<Coin, Integer> coinsToAdd, Map<Coin, Integer> coinsToReturn) {
        Product currentProduct = vendingMachine.getCurrentProduct();
        System.out.println("Dispensing product " + currentProduct.getName());

        vendingMachine.getInventory().decreaseProduct(currentProduct.getId());

        vendingMachine.getCashInventory().addCoins(vendingMachine.getCoinsBeingHeld());
        vendingMachine.setCoinsBeingHeld(new HashMap<>());

        vendingMachine.getCashInventory().decreaseCoins(coinsToReturn);
        vendingMachine.getTransactions().add(
                new Transaction(0, currentProduct.getId(), TransactionStatus.COMPLETED, currentProduct.getPrice(), LocalDateTime.now())
        );
        vendingMachine.resetState();
    }

    @Override
    public void cancelTransaction(VendingMachine vendingMachine) {
        System.out.println("You cannot cancel transaction while product is dispensing");
    }
}