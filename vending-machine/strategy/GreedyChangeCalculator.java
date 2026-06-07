package strategy;

import model.CashInventory;
import model.Coin;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GreedyChangeCalculator implements ChangeCalculator {
    public Map<Coin, Integer> convertToChange(CashInventory cashInventory, int amount) {
        Map<Coin, Integer> coinsToProvide = new HashMap<>();
        List<Map.Entry<Coin, Integer>> coinList = new ArrayList<>(cashInventory.getCoins().entrySet().stream().toList());

        coinList.sort((c1, c2) -> Integer.compare(c2.getKey().getValue(), c1.getKey().getValue()));

        for (Map.Entry<Coin, Integer> entry : coinList) {
            int denominationValue = entry.getKey().getValue();
            int count = entry.getValue();
            if (amount >= denominationValue) {
                int coinsNeeded = amount / denominationValue;
                int coinsCounted = Math.min(coinsNeeded, count);
                amount -= coinsCounted * denominationValue;
                if (coinsCounted > 0) {
                    coinsToProvide.put(entry.getKey(), coinsCounted);
                }
            }
        }
        if (amount == 0) {
            return coinsToProvide;
        }
        return null;
    }
}
