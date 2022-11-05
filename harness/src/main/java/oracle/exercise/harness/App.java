package oracle.exercise.harness;

/**
 * Test harness for the Vending Machine Change API
 */
public class App {

    static final String VENDING_MACHINE_TEST_SIMULATOR = "Vending Machine Test Simulator";

    private App() {
        System.out.println(VENDING_MACHINE_TEST_SIMULATOR);
        new Cli().run();
    }

    public static void main(String[] args) {
        new App();
    }

}
