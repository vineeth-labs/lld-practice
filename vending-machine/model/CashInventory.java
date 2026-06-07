package model;

import java.util.Map;

public class CashInventory {
    Map<Coin, Integer> coins;

    public Map<Coin, Integer> getCoins() {
        return coins;
    }

    public void addCoins(Map<Coin, Integer> coinsToAdd) {
        for (Map.Entry<Coin, Integer> entry : coinsToAdd.entrySet()) {
            int currentCount = coins.get(entry.getKey());
            coins.put(entry.getKey(), currentCount + entry.getValue());
        }
    }

    public void decreaseCoins(Map<Coin, Integer> coinsToDecrease) {
        for (Map.Entry<Coin, Integer> entry : coinsToDecrease.entrySet()) {
            int currentCount = coins.get(entry.getKey());
            if (currentCount - entry.getValue() >= 0) {
                coins.put(entry.getKey(), currentCount - entry.getValue());
            } else {
                throw new RuntimeException("Insufficient funds");
            }
        }
    }
}

