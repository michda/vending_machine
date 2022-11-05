package oracle.exercise.harness;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;
import org.junitpioneer.jupiter.StdIo;
import org.junitpioneer.jupiter.StdOut;

public class AppTest {

    @Test
    @StdIo({ "1", "2", "3", "4", "5", "6", "7", "8", "exit" })
    void test_main(StdOut out) {
        String[] mainStringArgs = { "" };
        App.main(mainStringArgs);

        String[] outputLines = out.capturedLines();

        assertEquals(13, outputLines.length);
        assertEquals("Goodbye", outputLines[12]);
    }

}
