package com.praktikum.rest.runners;
import org.testng.TestListenerAdapter;
import org.testng.TestNG;
import com.praktikum.rest.tests.UserAPITests;
import com.praktikum.rest.tests.AuthenticationTests;
import com.praktikum.rest.tests.AdvancedAPITests;
import com.praktikum.rest.tests.FakerDataTests;

/**
 * Optional test runner class untuk execute tests via main method
 * Berguna untuk custom test execution atau integration dengan CI/CD
 */
public class TestRunner {
    /**
     * Main method untuk execute tests programmatically
     */
    public static void main(String[] args) {
        // Create TestNG instance
        TestNG testng = new TestNG();
        // Create test listener untuk capture results
        TestListenerAdapter tla = new TestListenerAdapter();
        // Set test classes to run
        testng.setTestClasses(new Class[] {
                UserAPITests.class,
                AuthenticationTests.class,
                AdvancedAPITests.class,
                FakerDataTests.class
        });

        // Add listener untuk result capture
        testng.addListener(tla);
        // Run tests
        testng.run();
        // Print test results summary
        System.out.println("=== TEST EXECUTION SUMMARY ===");
        System.out.println("Passed tests: " + tla.getPassedTests().size());
        System.out.println("Failed tests: " + tla.getFailedTests().size());
        System.out.println("Skipped tests: " + tla.getSkippedTests().size());
        System.out.println("Total tests: " +
                (tla.getPassedTests().size() +
                        tla.getFailedTests().size() +
                        tla.getSkippedTests().size()));
    }
}