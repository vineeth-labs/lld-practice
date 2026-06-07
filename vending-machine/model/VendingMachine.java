package model;

import state.IdleState;
import state.PaymentState;
import state.VendingMachineState;
import util.CoinUtil;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

public class VendingMachine {
    Inventory inventory;
    VendingMachineState state;
    CashInventory cashInventory;
    List<Transaction> transactions;
    Product currentProduct;
    List<Coin> coins;

    VendingMachine(Inventory inventory) {
        this.inventory = inventory;
        state = new IdleState();
    }

    void selectProduct(int productId) {
        // check
        // fetch the product from inventory
        if (state instanceof IdleState) {
            Product product = inventory.getProduct(productId);
            // if positive itemCount , move forward else return
            if (product == null || product.quantity <= 0) {
                return;
            }
            currentProduct = product;
            state = new PaymentState();
        } else {
            System.out.println("Product selection doesnt work");
        }
    }

    void insertMoney(Map<Coin, Integer> coins) {
        // calculate money
        if (state instanceof PaymentState) {
            int amount = CoinUtil.calculateAmount(coins);
            if (amount >= currentProduct.getPrice()) {
                int diff = amount-currentProduct.getPrice();
                Map<Coin, Integer> coinsToGive = CoinUtil.convertToChange(coins, diff);

                if (coinsToGive != null) {
                    dispenseProduct(coinsToGive);
                } else {
                    System.out.println("Not enough change to return, Please insert diff amount");
                }
            }

        }

    }

    private void dispenseProduct(Map<Coin, Integer> coinsToGive) {
        System.out.println("Dispensing product " + currentProduct.name);
        try {
            Thread.sleep(5000);
            inventory.decreaseProduct(currentProduct.id);
            cashInventory.decreaseCoins(coinsToGive);
            transactions.add(
                    new Transaction(0, currentProduct.id, TransactionStatus.PASSED, currentProduct.price, LocalDateTime.now())
            );
            state = new IdleState();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    void cancelTransaction() {

    }


}