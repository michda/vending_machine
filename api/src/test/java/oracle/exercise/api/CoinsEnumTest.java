package oracle.exercise.api;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import org.junit.jupiter.api.Test;

public class CoinsEnumTest {
    @Test
    void test_getDenomination_null() {
        assertNull(CoinsEnum.getCoinFromDenomination(0.3d));
    }

    @Test
    void test_getDenomination_Fifty() {
        assertEquals(CoinsEnum.FIFTY, CoinsEnum.getCoinFromDenomination(0.5d));
    }
}
