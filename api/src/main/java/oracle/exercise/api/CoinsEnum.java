package oracle.exercise.api;

/**
 * Used to enforce types of coins, if a new coin is entered into circulation
 * then add it to this Enum
 */
public enum CoinsEnum {
    ONE(0.01d), TWO(0.02d), FIVE(0.05d), TEN(0.1d), TWENTY(0.2d), FIFTY(0.5d), HUNDERED(1.0d), TWO_HUNDRED(2.0d);

    private final double denomination;

    private CoinsEnum(final double denomination) {
        this.denomination = denomination;
    }

    public double getDenomination() {
        return denomination;
    }

    public static CoinsEnum getCoinFromDenomination(double denomination) {
        for (CoinsEnum e : values()) {
            if (e.denomination == denomination) {
                return e;
            }
        }
        return null;
    }
}
