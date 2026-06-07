package state;

import model.Coin;
import model.VendingMachine;

import java.util.Map;

public interface VendingMachineState {
    void selectProduct(VendingMachine vendingMachine, Integer productId);

    void insertMoney(VendingMachine vendingMachine, Map<Coin, Integer> coins);

    void dispenseProduct(VendingMachine vendingMachine, Map<Coin, Integer> coinsToAdd, Map<Coin, Integer> coinsToReturn);

    void cancelTransaction(VendingMachine vendingMachine);
}