package oracle.exercise.harness;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.junitpioneer.jupiter.StdIo;
import org.junitpioneer.jupiter.StdOut;

import oracle.exercise.api.*;

public class CliTest {

    @Test
    @StdIo({ "1", "2", "3", "4", "5", "6", "7", "8" }) // One up for each position
    void checkProductInput() {
        Cli cli = new Cli();
        assertNotNull(cli.getMachine().getCoinReserve());
        int count = 1;
        final List<CoinsEnum> coinsEnum = new ArrayList<>();
        coinsEnum.addAll(Arrays.asList(CoinsEnum.values()));
        for (Coin coin : cli.getMachine().getCoinReserve()) {
            assertEquals(count++, coin.getCount());
            // Remove them from the list of enum
            coinsEnum.remove(coin.getValue());
        }
        // If we have them all then this list should be empty
        assertEquals(0, coinsEnum.size());
    }

    @Test
    @StdIo({ "exit" })
    void checkRunForExit(StdOut out) {
        final List<Coin> setupCoins = new ArrayList<>();
        Cli cli = new Cli(setupCoins);
        cli.run();
        String[] outputLines = out.capturedLines();
        assertEquals(2, outputLines.length);
        assertEquals("Enter the cost of product:", outputLines[0]);
        assertEquals("Goodbye", outputLines[1]);
    }

    @Test
    @StdIo({
            "1.01", // Product cost
            "1", // 1p
            "0", // 2p
            "0", // 5p
            "0", // 10p
            "0", // 20p
            "0", // 50p
            "1", // £1
            "0", // £2
            "hopper", // target statement
            "exit"
    })
    void checkRunForHopper(StdOut out) {
        final List<Coin> setupCoins = new ArrayList<>();
        Cli cli = new Cli(setupCoins);
        cli.run();
        String[] outputLines = out.capturedLines();
        assertEquals(23, outputLines.length);
        assertEquals("0.01: 1",
                outputLines[13]);
        assertEquals("0.02: 0",
                outputLines[14]);
        assertEquals("1.00: 1",
                outputLines[19]);
        assertEquals("Goodbye", outputLines[22]);
    }

    @Test
    @StdIo({
            "1.01", // Product cost
            "1", // 1p
            "0", // 2p
            "0", // 5p
            "0", // 10p
            "0", // 20p
            "0", // 50p
            "1", // £1
            "0", // £2
            "float", // target statement
            "exit"
    })
    void checkRunForReserve(StdOut out) {
        final List<Coin> setupCoins = new ArrayList<>();
        Cli cli = new Cli(setupCoins);
        cli.run();
        String[] outputLines = out.capturedLines();
        assertEquals(16, outputLines.length);
        assertEquals("No coins in the float",
                outputLines[13]);
        assertEquals("Goodbye", outputLines[15]);
    }

    @Test
    @StdIo({
            "0.01", // Product cost
            "0", // 1p
            "1", // 2p
            "1", // 5p
            "0", // 10p
            "0", // 20p
            "0", // 50p
            "0", // £1
            "0", // £2
            "exit"
    })
    void checkSimpleChange(StdOut out) {
        final List<Coin> setupCoins = new ArrayList<>();
        setupCoins.add(new Coin(CoinsEnum.ONE, 1));
        Cli cli = new Cli(setupCoins);
        cli.run();
        String[] outputLines = out.capturedLines();
        assertEquals(16, outputLines.length);
        assertEquals("Change returned: ", outputLines[11]);
        assertEquals("0.01: 1", outputLines[12]);
        assertEquals("Total: 0.01", outputLines[13]);
        assertEquals("Goodbye", outputLines[15]);
    }

    @Test
    @StdIo({
            "1.0", // Product cost
            "0", // 1p
            "0", // 2p
            "0", // 5p
            "0", // 10p
            "0", // 20p
            "0", // 50p
            "0", // £1
            "1", // £2
            "exit"
    })
    void checkSimpleChangeWholeNumber(StdOut out) {
        final List<Coin> setupCoins = new ArrayList<>();
        setupCoins.add(new Coin(CoinsEnum.ONE, 10));
        setupCoins.add(new Coin(CoinsEnum.TWO, 10));
        setupCoins.add(new Coin(CoinsEnum.FIVE, 10));
        setupCoins.add(new Coin(CoinsEnum.TEN, 10));
        setupCoins.add(new Coin(CoinsEnum.TWENTY, 10));
        setupCoins.add(new Coin(CoinsEnum.FIFTY, 10));
        setupCoins.add(new Coin(CoinsEnum.HUNDERED, 10));
        setupCoins.add(new Coin(CoinsEnum.TWO_HUNDRED, 10));
        Cli cli = new Cli(setupCoins);
        cli.run();
        String[] outputLines = out.capturedLines();
        assertEquals(16, outputLines.length);
        assertEquals("Change to pay: 1.00", outputLines[10]);
        assertEquals("Change returned: ", outputLines[11]);
        assertEquals("1.00: 1", outputLines[12]);
        assertEquals("Total: 1.00", outputLines[13]);
        assertEquals("Goodbye", outputLines[15]);
    }

    @Test
    @StdIo({
            "0.01", // Product cost
            "a", // Invalid 1p
            "1", // 1p
            "0", // 2p
            "0", // 5p
            "0", // 10p
            "0", // 20p
            "0", // 50p
            "0", // £1
            "0", // £2
            "exit"
    })
    void check_InvalidInt(StdOut out) {
        final List<Coin> setupCoins = new ArrayList<>();
        setupCoins.add(new Coin(CoinsEnum.ONE, 1));
        Cli cli = new Cli(setupCoins);
        cli.run();
        String[] outputLines = out.capturedLines();
        assertEquals(15, outputLines.length);
        assertEquals("Invalid input, please only enter whole numbers from 0", outputLines[3]);
        assertEquals("Goodbye", outputLines[14]);
    }

    @Test
    @StdIo({
            "1.0", // Product cost
            "1", // 1p
            "0", // 2p
            "0", // 5p
            "0", // 10p
            "0", // 20p
            "0", // 50p
            "0", // £1
            "0", // £2
            "exit"
    })
    void check_InsufficientFunds(StdOut out) {
        final List<Coin> setupCoins = new ArrayList<>();
        setupCoins.add(new Coin(CoinsEnum.ONE, 1));
        Cli cli = new Cli(setupCoins);
        cli.run();
        String[] outputLines = out.capturedLines();
        assertEquals(13, outputLines.length);
        assertEquals("Value was 1.00 but only paid 0.01",
                outputLines[10]);
    }

    @Test
    @StdIo({
            "10", // 1p
            "20", // 2p
            "30", // 5p
            "40", // 10p
            "50", // 20p
            "60", // 50p
            "70", // £1
            "80" // £2
    })
    void check_setupInitialCoins(StdOut out) {
        final List<Coin> setupCoins = new ArrayList<>();
        final List<CoinsEnum> coinsEnum = new ArrayList<>();
        coinsEnum.addAll(Arrays.asList(CoinsEnum.values()));
        Cli cli = new Cli(setupCoins);
        List<Coin> coins = cli.setupInitialCoins();
        assertEquals(8, coins.size());
        int coinCount = 10;
        for (Coin coin : cli.getMachine().getCoinReserve()) {
            assertEquals(coinCount += 10, coin.getCount());
            // Remove them from the list of enum
            coinsEnum.remove(coin.getValue());
        }
    }
}
