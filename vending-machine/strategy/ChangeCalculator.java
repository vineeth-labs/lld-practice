package strategy;

import model.CashInventory;
import model.Coin;

import java.util.Map;

public interface ChangeCalculator {
    public Map<Coin, Integer> convertToChange(CashInventory cashInventory, int amount);
}
