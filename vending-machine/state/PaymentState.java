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
        vendingMachine.addCoinsBeingHeld(coins);
        int amount = CoinUtil.calculateAmount(coins);
        if (amount >= currentProduct.getPrice()) {
            int diff = amount - currentProduct.getPrice();
            Map<Coin, Integer> coinsToReturn = changeCalculator.convertToChange(vendingMachine.getCashInventory(), diff);
            if (coinsToReturn != null) {
                vendingMachine.setState(new DispensingState());
                vendingMachine.dispenseProduct(coinsToReturn);
            } else {
                System.out.println("Not enough change to return, Please insert diff amount");
            }
        }
    }

    @Override
    public void dispenseProduct(VendingMachine vendingMachine, Map<Coin, Integer> coinsToAdd, Map<Coin, Integer> coinsToReturn) {
        System.out.println("Please insert money to continue");
    }

    @Override
    public void cancelTransaction(VendingMachine vendingMachine) {
        if (vendingMachine.getCoinsBeingHeld() != null && CoinUtil.calculateAmount(vendingMachine.getCoinsBeingHeld()) > 0) {
            vendingMachine.getTransactions().add(
                    new Transaction(0, vendingMachine.getCurrentProduct().getId(), TransactionStatus.CANCELLED,
                            CoinUtil.calculateAmount(vendingMachine.getCoinsBeingHeld()),LocalDateTime.now()));
            System.out.println("Returning coins (Total amount): " + CoinUtil.calculateAmount(vendingMachine.getCoinsBeingHeld()));
        }
        System.out.println("Cancelling transaction");
        vendingMachine.resetState();
    }
}