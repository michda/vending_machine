package oracle.exercise.api;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;

import oracle.exercise.api.exceptions.InsufficientFundsExcetion;

public class MachineTest {

    @Test
    void pay_NoCoinsInvolved() throws InsufficientFundsExcetion {
        List<Coin> initialCoins = new ArrayList<>();
        List<Coin> payment = new ArrayList<>();
        Machine machine = new Machine(initialCoins);
        Change change = machine.pay(0.0d, payment);
        assertEquals(0.0d, change.getTotal());
    }

    @Test
    void pay_OnePennyPayment_NoChange() throws InsufficientFundsExcetion {
        List<Coin> initialCoins = new ArrayList<>();
        List<Coin> payment = new ArrayList<>();
        payment.add(new Coin(CoinsEnum.ONE, 1));
        Machine machine = new Machine(initialCoins);
        Change change = machine.pay(CoinsEnum.ONE.getDenomination(), payment);
        assertEquals(0.0d, change.getTotal());
    }

    @Test
    void pay_FivePennyOwed_NoPayment() throws InsufficientFundsExcetion {
        List<Coin> initialCoins = new ArrayList<>();
        List<Coin> payment = new ArrayList<>();
        payment.add(new Coin(CoinsEnum.ONE, 1));
        Machine machine = new Machine(initialCoins);
        Exception exception = assertThrows(InsufficientFundsExcetion.class, () -> {
            machine.pay(CoinsEnum.FIVE.getDenomination(), payment);
        });
        String expectedErrorString = String.format("Value was %.2f but only paid %.2f",
                CoinsEnum.FIVE.getDenomination(), CoinsEnum.ONE.getDenomination());
        assertEquals(expectedErrorString, exception.getMessage());
    }

    @Test
    void pay_OnePennyOwed_OnePennyPayment_OnePennyChange() throws InsufficientFundsExcetion {
        List<Coin> initialCoins = new ArrayList<>();
        initialCoins.add(new Coin(CoinsEnum.ONE, 1));
        List<Coin> payment = new ArrayList<>();
        payment.add(new Coin(CoinsEnum.TWO, 1));
        Machine machine = new Machine(initialCoins);
        Change change = machine.pay(CoinsEnum.ONE.getDenomination(), payment);
        assertEquals(CoinsEnum.ONE.getDenomination(), change.getTotal());
        Coin coin = change.getChangeCoins().get(0);
        assertEquals(1, coin.getCount());
        assertEquals(CoinsEnum.ONE, coin.getValue());
    }

    @Test
    void pay_OnePoundOwed_TwoPoundPayment_OnePoundChange() throws InsufficientFundsExcetion {
        List<Coin> initialCoins = new ArrayList<>();
        initialCoins.add(new Coin(CoinsEnum.ONE, 10));
        initialCoins.add(new Coin(CoinsEnum.TWO, 10));
        initialCoins.add(new Coin(CoinsEnum.HUNDERED, 10));
        initialCoins.add(new Coin(CoinsEnum.TWO_HUNDRED, 10));
        List<Coin> payment = new ArrayList<>();
        payment.add(new Coin(CoinsEnum.TWO_HUNDRED, 1));
        Machine machine = new Machine(initialCoins);
        Change change = machine.pay(CoinsEnum.HUNDERED.getDenomination(), payment);
        assertEquals(CoinsEnum.HUNDERED.getDenomination(), change.getTotal());
        Coin coin = change.getChangeCoins().get(0);
        assertEquals(1, coin.getCount());
        assertEquals(CoinsEnum.HUNDERED, coin.getValue());
    }

    @Test
    void pay_OnePennyOwed_OnePennyPayment_OnePennyChange_FiftyPenceOption() throws InsufficientFundsExcetion {
        List<Coin> initialCoins = new ArrayList<>();
        initialCoins.add(new Coin(CoinsEnum.ONE, 1));
        initialCoins.add(new Coin(CoinsEnum.FIFTY, 1));
        List<Coin> payment = new ArrayList<>();
        payment.add(new Coin(CoinsEnum.TWO, 1));
        Machine machine = new Machine(initialCoins);
        Change change = machine.pay(CoinsEnum.ONE.getDenomination(), payment);
        assertEquals(CoinsEnum.ONE.getDenomination(), change.getTotal());
        Coin coin = change.getChangeCoins().get(0);
        assertEquals(1, coin.getCount());
        assertEquals(CoinsEnum.ONE, coin.getValue());
    }

    @Test
    void pay_OnePennyOwed_OnePennyPayment_OnePennyChange_InsufficientChange() throws InsufficientFundsExcetion {
        List<Coin> initialCoins = new ArrayList<>();
        initialCoins.add(new Coin(CoinsEnum.FIFTY, 1));
        List<Coin> payment = new ArrayList<>();
        payment.add(new Coin(CoinsEnum.TWO, 1));
        Machine machine = new Machine(initialCoins);
        Change change = machine.pay(CoinsEnum.ONE.getDenomination(), payment);
        assertEquals(CoinsEnum.ONE.getDenomination(), change.getTotal());
        assertEquals(0, change.getChangeCoins().size());
    }

    @Test
    void pay_ThreePennyOwed_FivePennyPayment_TwoPennyChange() throws InsufficientFundsExcetion {
        List<Coin> initialCoins = new ArrayList<>();
        initialCoins.add(new Coin(CoinsEnum.ONE, 2));
        List<Coin> payment = new ArrayList<>();
        payment.add(new Coin(CoinsEnum.FIVE, 1));
        Machine machine = new Machine(initialCoins);
        Change change = machine.pay(0.03d, payment);
        assertEquals(0.02d, machine.round(change.getTotal(), 2));
        Coin coin = change.getChangeCoins().get(0);
        assertEquals(2, coin.getCount());
        assertEquals(CoinsEnum.ONE, coin.getValue());
    }

    @Test
    void pay_TwoPenceOwed_FivePencePayment_ThreePenceChange() throws InsufficientFundsExcetion {
        List<Coin> initialCoins = new ArrayList<>();
        initialCoins.add(new Coin(CoinsEnum.ONE, 1));
        initialCoins.add(new Coin(CoinsEnum.TWO, 1));
        List<Coin> payment = new ArrayList<>();
        payment.add(new Coin(CoinsEnum.FIVE, 1));
        Machine machine = new Machine(initialCoins);
        Change change = machine.pay(0.02d, payment);
        assertEquals(0.03d, machine.round(change.getTotal(), 2));
        assertEquals(2, change.getChangeCoins().size());
        List<Coin> changeCoins = new ArrayList<>();
        changeCoins.addAll(change.getChangeCoins());
        Coin coin = change.getChangeCoins().get(0);
        assertEquals(1, coin.getCount());
        assertEquals(CoinsEnum.ONE, coin.getValue());
        coin = change.getChangeCoins().get(1);
        assertEquals(1, coin.getCount());
        assertEquals(CoinsEnum.TWO, coin.getValue());
    }

    @Test
    void pay_OnePenceOwed_TwoPencePayment_OnePenceChange_OtherCoinsInChange() throws InsufficientFundsExcetion {
        List<Coin> initialCoins = new ArrayList<>();
        initialCoins.add(new Coin(CoinsEnum.ONE, 1));
        initialCoins.add(new Coin(CoinsEnum.HUNDERED, 1));
        List<Coin> payment = new ArrayList<>();
        payment.add(new Coin(CoinsEnum.TWO, 1));
        Machine machine = new Machine(initialCoins);
        Change change = machine.pay(0.01d, payment);
        assertEquals(0.01d, machine.round(change.getTotal(), 2));
    }

    @Test
    void pay_ThreePenceOwed_FivePencePayment_NotEnoughChange() throws InsufficientFundsExcetion {
        List<Coin> initialCoins = new ArrayList<>();
        initialCoins.add(new Coin(CoinsEnum.ONE, 1));
        List<Coin> payment = new ArrayList<>();
        payment.add(new Coin(CoinsEnum.FIVE, 1));
        Machine machine = new Machine(initialCoins);
        Change change = machine.pay(0.03d, payment);
        assertEquals(0.02d, machine.round(change.getTotal(), 2));
        Coin coin = change.getChangeCoins().get(0);
        assertEquals(coin.getCount(), 1);
        assertEquals(coin.getValue(), CoinsEnum.ONE);
    }

    @Test
    void pay_OnePenceOwed_PoundPayment_NintyNinePenceChange() throws InsufficientFundsExcetion {
        List<Coin> initialCoins = new ArrayList<>();
        initialCoins.add(new Coin(CoinsEnum.ONE, 100));
        List<Coin> payment = new ArrayList<>();
        payment.add(new Coin(CoinsEnum.HUNDERED, 1));
        Machine machine = new Machine(initialCoins);
        Change change = machine.pay(0.01d, payment);
        assertEquals(0.99d, machine.round(change.getTotal(), 2));
        Coin coin = change.getChangeCoins().get(0);
        assertEquals(coin.getCount(), 99);
        assertEquals(coin.getValue(), CoinsEnum.ONE);
        assertEquals(1, machine.getCoinReserve().size());
        Coin reserveCoin = machine.getCoinReserve().get(0);
        assertEquals(CoinsEnum.ONE, reserveCoin.getValue());
        assertEquals(1, reserveCoin.getCount());
    }

    @Test
    void pay_PoundOwed_OneHundredPencePayment_NoChange_HoppedPence() throws InsufficientFundsExcetion {
        List<Coin> initialCoins = new ArrayList<>();
        List<Coin> payment = new ArrayList<>();
        payment.add(new Coin(CoinsEnum.ONE, 100));
        Machine machine = new Machine(initialCoins);
        Change change = machine.pay(1.0d, payment);
        assertEquals(0d, machine.round(change.getTotal(), 2));
        Coin hopperCoin = machine.getCoinHopper().get(0);
        assertEquals(100, hopperCoin.getCount());
        assertEquals(CoinsEnum.ONE, hopperCoin.getValue());
    }

    @Test
    void addPaymentToBucketTwice() throws InsufficientFundsExcetion {
        List<Coin> initialCoins = new ArrayList<>();
        List<Coin> payment = new ArrayList<>();
        payment.add(new Coin(CoinsEnum.ONE, 1));
        Machine machine = new Machine(initialCoins);
        machine.addPaymentToHopper(payment);
        Coin hopperCoin = machine.getCoinHopper().get(0);
        assertEquals(1, hopperCoin.getCount());
        machine.addPaymentToHopper(payment);
        assertEquals(2, hopperCoin.getCount());
    }

    @Test
    void addPaymentToBucketExistingChange() throws InsufficientFundsExcetion {
        List<Coin> initialCoins = new ArrayList<>();
        List<Coin> payment = new ArrayList<>();
        payment.add(new Coin(CoinsEnum.ONE, 1));
        Machine machine = new Machine(initialCoins);
        machine.getCoinHopper().add(new Coin(CoinsEnum.TWO_HUNDRED, 0));
        machine.addPaymentToHopper(payment);
        assertEquals(2, machine.getCoinHopper().size());

        Coin hopperCoin = machine.getCoinHopper().get(0);
        assertEquals(0, hopperCoin.getCount());
        assertEquals(CoinsEnum.TWO_HUNDRED, hopperCoin.getValue());

        hopperCoin = machine.getCoinHopper().get(1);
        assertEquals(1, hopperCoin.getCount());
        assertEquals(CoinsEnum.ONE, hopperCoin.getValue());
    }

    @Test
    void round_verify() {
        Machine machine = new Machine(new ArrayList<>());
        // Putting all tests together for the round method to check extremes.
        // All maths done in application is on 2 decimal places, rounding is done due to
        // remove floating point issues.
        assertEquals(0.00, machine.round(0.001, 2));
        assertEquals(0.00, machine.round(-0.001, 2));
        assertEquals(0.01, machine.round(0.009, 2));
        assertEquals(0.01, machine.round(0.014, 2));
        assertEquals(0.02, machine.round(0.015, 2));
    }
}
