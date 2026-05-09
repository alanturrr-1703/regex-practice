package com.example;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests for {@link Solution#containsLogLevel(String)}.
 *
 * <p>All tests will fail with {@link UnsupportedOperationException} until the
 * method is implemented. That is intentional — make the tests pass.</p>
 */
class SolutionTest {

    private final Solution solution = new Solution();

    // -----------------------------------------------------------------------
    // Matching cases
    // -----------------------------------------------------------------------

    @Test
    @DisplayName("DEBUG at start of log line matches")
    void testDebug() {
        assertTrue(solution.containsLogLevel("DEBUG: starting server"));
    }

    @Test
    @DisplayName("ERROR in middle of sentence matches")
    void testError() {
        assertTrue(solution.containsLogLevel("An ERROR occurred in module X"));
    }

    @Test
    @DisplayName("FATAL at start of string matches")
    void testFatal() {
        assertTrue(solution.containsLogLevel("FATAL system crash"));
    }

    @Test
    @DisplayName("WARN standalone matches")
    void testWarn() {
        assertTrue(solution.containsLogLevel("WARN: disk usage above 90%"));
    }

    @Test
    @DisplayName("INFO as the entire string matches")
    void testInfoAlone() {
        assertTrue(solution.containsLogLevel("INFO"));
    }

    // -----------------------------------------------------------------------
    // Non-matching cases
    // -----------------------------------------------------------------------

    @Test
    @DisplayName("'INFORMATION' does not match INFO (word boundary)")
    void testInformationNotInfo() {
        assertFalse(solution.containsLogLevel("INFORMATION: something happened"),
            "INFORMATION contains INFO but INFORMATION is not a standalone word match");
    }

    @Test
    @DisplayName("Lowercase 'debugging' does not match (case-sensitive)")
    void testLowercaseDebug() {
        assertFalse(solution.containsLogLevel("debugging is fun"));
    }

    @Test
    @DisplayName("'WARNING' does not match WARN (word boundary)")
    void testWarningNotWarn() {
        assertFalse(solution.containsLogLevel("WARNING: disk low"),
            "WARNING contains WARN but WARNING is not a standalone WARN match");
    }

    @Test
    @DisplayName("Empty string returns false")
    void testEmptyString() {
        assertFalse(solution.containsLogLevel(""));
    }

    @Test
    @DisplayName("Lowercase 'error' does not match (case-sensitive)")
    void testLowercaseError() {
        assertFalse(solution.containsLogLevel("there was an error in processing"));
    }
}
