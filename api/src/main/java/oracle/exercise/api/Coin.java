package oracle.exercise.api;

/**
 * Coin object for tracking denomination and value of the coins in the vending
 * machine
 */
public class Coin implements Comparable<Coin> {
    private final CoinsEnum value;
    private int count;

    public Coin(CoinsEnum value, int count) {
        this.value = value;
        this.count = count;
    }

    public void removeCoin() {
        --count;
    }

    public void addCount(int count) {
        this.count += count;
    }

    @Override
    public int compareTo(Coin otherCoin) {
        return (int) Math.round(value.getDenomination() * 100 - otherCoin.value.getDenomination() * 100);
    }

    public CoinsEnum getValue() {
        return value;
    }

    public int getCount() {
        return count;
    }

}
