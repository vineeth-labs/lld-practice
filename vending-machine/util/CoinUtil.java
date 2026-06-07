package util;

import model.CashInventory;
import model.Coin;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CoinUtil {

    public static Map<Coin, Integer> convertToChange(Map<Coin, Integer> coins, int amount) {
        Map<Coin, Integer> coinsToProvide = new HashMap<Coin, Integer>();
        List<Map.Entry<Coin, Integer>> coinList = coins.entrySet().stream().toList();

        coinList.sort((c1, c2) -> {
            return Integer.compare(c2.getKey().getValue(), c1.getKey().getValue());
        });

        for (Map.Entry<Coin, Integer> entry : coinList) {
            int denominationValue = entry.getKey().getValue();
            int count = entry.getValue();
            if (amount >= denominationValue) {
                int coinsNeeded = amount / denominationValue;
                int coinsCounted = Math.min(coinsNeeded, count);
                amount -= coinsCounted * denominationValue;
                coinsToProvide.put(entry.getKey(), coinsCounted);
            }
        }
        if (amount == 0) {
            return coinsToProvide;
        }
        return null;
    }

    public static boolean canProvideChange(Map<Coin, Integer> coins, int amount) {
        Map<Coin, Integer> coinMap = convertToChange(coins, amount);
        return calculateAmount(coinMap) >= amount;
    }

    public static Integer calculateAmount(Map<Coin, Integer> coins) {
        int total = 0;
        for (Map.Entry<Coin, Integer> entry : coins.entrySet()) {
            total += entry.getValue() * entry.getKey().getValue();
        }
        return total;
    }

}
