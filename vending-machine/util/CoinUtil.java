package util;

import model.Coin;
import java.util.Map;

public class CoinUtil {
    public static Integer calculateAmount(Map<Coin, Integer> coins) {
        if  (coins == null) {return 0;}
        int total = 0;
        for (Map.Entry<Coin, Integer> entry : coins.entrySet()) {
            total += entry.getValue() * entry.getKey().getValue();
        }
        return total;
    }

}
