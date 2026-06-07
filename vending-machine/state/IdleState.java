package state;

import model.Coin;
import model.Product;
import model.VendingMachine;
import strategy.GreedyChangeCalculator;

import java.util.Map;

public class IdleState implements VendingMachineState {

    @Override
    public void selectProduct(VendingMachine vendingMachine, Integer productId) {
        Product product = vendingMachine.getInventory().getProduct(productId);
        if (product == null || product.getQuantity() <= 0) {
            return;
        }
        vendingMachine.setCurrentProduct(product);
        vendingMachine.setState(new PaymentState(new GreedyChangeCalculator()));
    }

    @Override
    public void insertMoney(VendingMachine vendingMachine, Map<Coin, Integer> coins) {
        System.out.println("Please select item before inserting money");
    }

    @Override
    public void dispenseProduct(VendingMachine vendingMachine, Map<Coin, Integer> coinsToAdd, Map<Coin, Integer> coinsToReturn) {
        System.out.println("Please select item before dispensing");
    }

    @Override
    public void cancelTransaction(VendingMachine vendingMachine) {
        if (vendingMachine.getCurrentProduct() != null) {
            System.out.println("Cancelling transaction");
            vendingMachine.resetState();
        }
        System.out.println("Invalid operation");
    }
}