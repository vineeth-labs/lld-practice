import model.*;
import util.CoinUtil;
import model.Coin;

import java.util.HashMap;
import java.util.Map;

public class Main {
    public static void main(String[] args) {
        // Prepare products and inventory
        Product soda = new Product("Soda", 1, 65);
        Product chips = new Product("Chips", 2, 50);

        Inventory inventory = new Inventory();
        inventory.putProduct(soda.getId(), soda);
        inventory.putProduct(chips.getId(), chips);

        // Prepare cash inventory with change available (20+10+5 = 35 for change)
        Map<Coin, Integer> initialCash = new HashMap<>();
        initialCash.put(Coin.TWENTY, 1);
        initialCash.put(Coin.TEN, 1);
        initialCash.put(Coin.FIVE, 1);

        CashInventory cashInventory = new CashInventory(initialCash);

        // Create vending machine
        VendingMachine vm = new VendingMachine(inventory, cashInventory);

        try {
            System.out.println("Selecting product id 1 (Soda), price " + soda.getPrice());
            vm.selectProduct(1);

            // Insert a single 100 coin
            Map<Coin, Integer> inserted = new HashMap<>();
            inserted.put(Coin.HUNDRED, 1);
            System.out.println("Inserting coins: " + CoinUtil.calculateAmount(inserted));
            vm.insertMoney(inserted);

            System.out.println("Final cash inventory:");
            vm.getCashInventory().getCoins().forEach((coin, cnt) -> System.out.println(coin + " -> " + cnt));

            System.out.println("Transactions:");
            vm.getTransactions().forEach(tx -> System.out.println(tx));

        } catch (Exception ex) {
            System.out.println("Error during vending operation: " + ex.getMessage());
            ex.printStackTrace();
        }
    }
}
