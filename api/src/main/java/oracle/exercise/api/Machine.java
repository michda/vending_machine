package oracle.exercise.api;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import oracle.exercise.api.exceptions.InsufficientFundsExcetion;

/**
 * This class handles most of the business logic of the change calculation
 */
public class Machine {

    private final List<Coin> coinReserve = new ArrayList<>();

    private final List<Coin> coinHopper = new ArrayList<>();

    /**
     * The coins to add to the Float
     * 
     * @param initialCoins
     */
    public Machine(final List<Coin> initialCoins) {
        coinReserve.addAll(initialCoins);
    }

    public List<Coin> getCoinReserve() {
        return coinReserve;
    }

    public List<Coin> getCoinHopper() {
        return coinHopper;
    }

    /**
     * Used to calculate what coins of those available to return after a purchase.
     * NOTE: If there is insufficient funds for change then the transactions is
     * complected regardless
     * 
     * @param cost    The total cost of the purchase.
     * @param payment The list of coins used as payment.
     * @return The change object showing how much change was due as well as the
     *         coins returned.
     * @throws InsufficientFundsExcetion If an attempt to pay with less coins is
     *                                   made, this Exception is throw.
     */
    public Change pay(final double cost, final List<Coin> payment) throws InsufficientFundsExcetion {
        final Map<CoinsEnum, Integer> changeMap = new HashMap<>();
        final double total = payment.stream().map(e -> e.getValue().getDenomination() * e.getCount())
                .reduce(Double::sum)
                .orElse(0.0d);
        double changeAmmount = total - cost;
        if (changeAmmount < 0)
            throw new InsufficientFundsExcetion(
                    String.format("Value was %.2f but only paid %.2f", cost, total));
        // Remove coins without any count and sort in reverse order to pay with larger
        // coins first
        final Iterator<Coin> coinIterator = coinReserve.stream().filter(e -> e.getCount() > 0)
                .sorted((c1, c2) -> c2.compareTo(c1))
                .iterator();
        if (coinIterator.hasNext()) {
            Coin currentCoin = coinIterator.next();
            while (changeAmmount > 0) {
                // Single calculation. Done seperately as it is used several times.
                final Double check = round(changeAmmount - currentCoin.getValue().getDenomination(), 2);
                if (check >= 0.0d) {
                    changeMap.put(currentCoin.getValue(),
                            changeMap.getOrDefault(currentCoin.getValue(), 0) + 1);
                    currentCoin.removeCoin();
                    changeAmmount -= currentCoin.getValue().getDenomination();
                }
                if (coinIterator.hasNext()
                        && (currentCoin.getCount() == 0
                                || check <= 0)) {
                    currentCoin = coinIterator.next();
                } else if (check >= 0
                        && currentCoin.getCount() > 0) {
                    continue;
                } else if (!coinIterator.hasNext()) {
                    break;
                }
            }
        }
        addPaymentToHopper(payment);
        // Convert Map to List of Coins
        final Change change = new Change(total - cost, changeMap);
        return change;
    }

    /**
     * Add the payment to payed coin hopper
     * 
     * @param payment the list of coins to receive
     */
    protected void addPaymentToHopper(final List<Coin> payment) {
        coinHopper.addAll(payment.parallelStream().map(e -> {
            Coin coin = coinHopper.stream().filter(c -> c.getValue() == e.getValue()).findFirst()
                    .orElse(new Coin(e.getValue(), 0));
            coin.addCount(e.getCount());
            return coin;
        }).collect(Collectors.toList()));
    }

    /**
     * Easy calculate the round to specified decimal places
     * 
     * @param value The value to round
     * @param scale The number decimal places to round to
     * @return The rounded double
     */
    protected double round(final double value, final int scale) {
        return Math.round(value * Math.pow(10, scale)) / Math.pow(10, scale);
    }

}
