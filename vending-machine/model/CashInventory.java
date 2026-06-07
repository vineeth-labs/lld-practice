package model;

import java.util.Map;

public class CashInventory {
    private Map<Coin, Integer> coins;

    public CashInventory() {
        this.coins = new java.util.HashMap<>();
    }

    public CashInventory(Map<Coin, Integer> initialCoins) {
        this.coins = initialCoins == null ? new java.util.HashMap<>() : initialCoins;
    }

    public Map<Coin, Integer> getCoins() {
        return coins;
    }

    public void addCoins(Map<Coin, Integer> coinsToAdd) {
        if (coinsToAdd == null) {
            return;
        }
        for (Map.Entry<Coin, Integer> entry : coinsToAdd.entrySet()) {
            int currentCount = coins.getOrDefault(entry.getKey(), 0);
            coins.put(entry.getKey(), currentCount + (entry.getValue() == null ? 0 : entry.getValue()));
        }
    }

    public void decreaseCoins(Map<Coin, Integer> coinsToDecrease) {
        if (coinsToDecrease == null) {
            return;
        }
        for (Map.Entry<Coin, Integer> entry : coinsToDecrease.entrySet()) {
            int currentCount = coins.getOrDefault(entry.getKey(), 0);
            int dec = entry.getValue() == null ? 0 : entry.getValue();
            if (currentCount - dec >= 0) {
                coins.put(entry.getKey(), currentCount - dec);
            } else {
                throw new RuntimeException("Insufficient funds");
            }
        }
    }
}

