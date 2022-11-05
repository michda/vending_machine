package oracle.exercise.api;

import java.util.List;
import java.util.Map;

/**
 * POJO for returning the change details
 */
public class Change {
    private final List<Coin> changeCoins;

    private final double total;

    /**
     * Simple constructor to build the change object
     * 
     * @param total     The total ammount of change expected
     * @param changeMap The coins actually expected to be returned
     */
    public Change(final double total, final Map<CoinsEnum, Integer> changeMap) {
        this.changeCoins = changeMap.keySet().stream().map(k -> {
            return new Coin(k, changeMap.get(k));
        }).sorted((c1, c2) -> c1.compareTo(c2)).toList();
        this.total = total;
    }

    public List<Coin> getChangeCoins() {
        return changeCoins;
    }

    public double getTotal() {
        return total;
    }
}
