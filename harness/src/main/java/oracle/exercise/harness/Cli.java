package oracle.exercise.harness;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;
import java.util.regex.Pattern;

import oracle.exercise.api.Change;
import oracle.exercise.api.Coin;
import oracle.exercise.api.CoinsEnum;
import oracle.exercise.api.Machine;
import oracle.exercise.api.exceptions.InsufficientFundsExcetion;

/**
 * Work horse of the test harness.
 * 
 */
public class Cli {
    private static final String EXIT = "exit";
    private static final String HOPPER = "hopper";
    private static final String FLOAT = "float";
    // The hot words to watch for
    private static final List<String> HOTWORDS = Arrays.asList(EXIT, HOPPER, FLOAT);
    private static final String INITIALISING_COINS = "INITIALISING COINS";
    private final Pattern coinsNumberPattern = Pattern.compile("^(\\d{1,})$");
    private final Pattern productNumberPattern = Pattern.compile("^(\\d{1,2}|\\d{1,2}.\\d{1,2}|.\\d{1,2})$");

    private final Machine machine;

    protected Machine getMachine() {
        return machine;
    }

    private final Scanner command;

    /**
     * Default, assumed using System.in and calls setup
     */
    public Cli() {
        this(System.in, null);
        machine.getCoinReserve().addAll(setupInitialCoins());
    }

    /**
     * Pass the coins to used for further testing
     * 
     * @param initalCoins
     */
    public Cli(final List<Coin> initalCoins) {
        this(System.in, initalCoins);
    }

    public Cli(final InputStream inputStream, final List<Coin> initalCoins) {
        command = new Scanner(inputStream);
        if (initalCoins == null) {
            machine = new Machine(new ArrayList<>());
        } else {
            machine = new Machine(initalCoins);
        }
    }

    /**
     * Parse the values in {@link oracle.exercise.api.CoinsEnum} to generate the
     * starting value of each coin.
     * 
     * @return The List of the {@link oracle.exercise.api.Coin} to be used for the
     *         Float
     */
    protected List<Coin> setupInitialCoins() {
        final List<Coin> initialCoins = new ArrayList<>();

        System.out.println(INITIALISING_COINS);
        System.out.println("Please enter the number of each of the coins for the float");
        int loopCounter = 0;
        final CoinsEnum coinsEnumValues[] = CoinsEnum.values();
        while (loopCounter < coinsEnumValues.length) {
            System.out.println(String.format("%.2f: ", coinsEnumValues[loopCounter].getDenomination()));
            final String inputLine = command.nextLine().trim();

            if (validIntInput(inputLine)) {
                final int count = Integer.parseInt(inputLine);
                initialCoins.add(new Coin(coinsEnumValues[loopCounter++], count));
            } else {
                System.out.println("Invalid input, please only enter whole numbers from 0");
            }

        }
        return initialCoins;
    }

    /**
     * Parse the input for a product. If the value is a number then parse the coins
     * used to pay. If the value is exit,
     * 
     * @return
     */
    protected String productInput() {
        final List<Coin> paymentCoins = new ArrayList<>();

        System.out.println("Enter the cost of product:");
        String inputLine = command.nextLine().trim().toLowerCase();
        if (!validProductInput(inputLine) || HOTWORDS.contains(inputLine))
            return inputLine;
        final double cost = Double.parseDouble(inputLine);

        System.out.println("Enter the number of each coin you paid with:");
        int loopCounter = 0;
        final CoinsEnum coinsEnumValues[] = CoinsEnum.values();
        while (loopCounter < coinsEnumValues.length) {
            System.out.println(String.format("%.2f: ", coinsEnumValues[loopCounter].getDenomination()));
            inputLine = command.nextLine().trim();

            if (validIntInput(inputLine)) {
                final int count = Integer.parseInt(inputLine);
                paymentCoins.add(new Coin(coinsEnumValues[loopCounter++], count));
            } else {
                System.out.println("Invalid input, please only enter whole numbers from 0");
            }
        }
        Change change;
        try {
            change = machine.pay(cost, paymentCoins);
        } catch (final InsufficientFundsExcetion ex) {
            System.out.println(ex.getMessage());
            return "";
        }
        if (change.getTotal() > 0) {
            printReturnedChange(change);
        } else {
            System.out.println("No change due");
        }
        return "";
    }

    /**
     * This method loops until the exit condition is met. "exit" returned on the
     * command line
     */
    protected void run() {
        boolean running = true;
        while (running) {
            final String input = productInput();
            if (input.equalsIgnoreCase(EXIT)) {
                running = false;
                System.out.println("Goodbye");
            } else if (input.equalsIgnoreCase(HOPPER)) {
                printChangeInHopper();
            } else if (input.equalsIgnoreCase(FLOAT)) {
                printChangeInFloat();
            }
        }
        command.close();
    }

    private void printChangeInHopper() {
        System.out.println("These are the coins in the hopper:");
        if (machine.getCoinHopper().size() == 0) {
            System.out.println("No coins in the hopper");
        }
        for (final Coin coin : machine.getCoinHopper()) {
            System.out.println(String.format("%.2f: %d", coin.getValue().getDenomination(), coin.getCount()));
        }
    }

    private void printChangeInFloat() {
        System.out.println("These are the coins in the float:");
        if (machine.getCoinReserve().size() == 0) {
            System.out.println("No coins in the float");
        }
        for (final Coin coin : machine.getCoinReserve()) {
            System.out.println(String.format("%.2f: %d", coin.getValue().getDenomination(), coin.getCount()));
        }
    }

    private void printReturnedChange(final Change change) {
        System.out.println(String.format("Change to pay: %.2f", change.getTotal()));
        System.out.println(String.format("Change returned: "));
        double total = 0.0f;
        if (change.getChangeCoins().size() == 0) {
            System.out.println("No change available");
        } else {
            for (final Coin coin : change.getChangeCoins()) {
                System.out.println(String.format("%.2f: %d", coin.getValue().getDenomination(), coin.getCount()));
                total += (coin.getValue().getDenomination() * coin.getCount());
            }
            System.out.println(String.format("Total: %.2f", total));
        }
    }

    /**
     * Validated against the REGEX for an integer input
     * 
     * @param input The text to text
     * @return TRUE is the input matches the validation
     */
    protected boolean validIntInput(final String input) {
        return coinsNumberPattern.matcher(input).matches();
    }

    /**
     * Validated against the REGEX for a floating point input
     * 
     * @param input The text to text
     * @return TRUE is the input matches the validation
     */
    protected boolean validProductInput(final String input) {
        return productNumberPattern.matcher(input).matches();
    }

}
