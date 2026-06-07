package state;

import model.Coin;
import model.VendingMachine;

import java.util.Map;

public class OutOfServiceState implements VendingMachineState {

    @Override
    public void selectProduct(VendingMachine vendingMachine, Integer productId) {
        System.out.println("Out of service. Operation not supported");
    }

    @Override
    public void insertMoney(VendingMachine vendingMachine, Map<Coin, Integer> coins) {
        System.out.println("Out of service. Operation not supported");
    }

    @Override
    public void dispenseProduct(VendingMachine vendingMachine, Map<Coin, Integer> coinsToAdd, Map<Coin, Integer> coinsToReturn) {
        System.out.println("Out of service. Operation not supported");
    }

    @Override
    public void cancelTransaction(VendingMachine vendingMachine) {
        System.out.println("Out of service. Operation not supported");
    }
}