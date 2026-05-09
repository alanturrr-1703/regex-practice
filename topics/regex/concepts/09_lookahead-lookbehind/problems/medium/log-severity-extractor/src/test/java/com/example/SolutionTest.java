package com.example;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests for Log Severity Extractor.
 *
 * All tests MUST fail until Solution.extractWarningsAndErrors() is implemented.
 */
class SolutionTest {

    private final Solution solution = new Solution();

    @Test
    @DisplayName("Mixed severities — only WARN and ERROR extracted")
    void testMixedSeverities() {
        String log = "[ERROR] disk full\n[INFO] system ok\n[WARN] low memory";
        List<String> result = solution.extractWarningsAndErrors(log);
        assertEquals(List.of("disk full", "low memory"), result);
    }

    @Test
    @DisplayName("Only DEBUG — empty result")
    void testDebugOnly() {
        List<String> result = solution.extractWarningsAndErrors("[DEBUG] nothing");
        assertTrue(result.isEmpty(), "DEBUG lines should not be extracted");
    }

    @Test
    @DisplayName("Empty string → empty list")
    void testEmpty() {
        assertTrue(solution.extractWarningsAndErrors("").isEmpty());
    }

    @Test
    @DisplayName("Null input → empty list")
    void testNull() {
        List<String> result = solution.extractWarningsAndErrors(null);
        assertNotNull(result, "Result must never be null");
        assertTrue(result.isEmpty());
    }

    @Test
    @DisplayName("Single WARN line")
    void testSingleWarn() {
        List<String> result = solution.extractWarningsAndErrors("[WARN] single");
        assertEquals(List.of("single"), result);
    }

    @Test
    @DisplayName("Multiple ERROR lines")
    void testMultipleErrors() {
        String log = "[ERROR] a\n[ERROR] b\n[ERROR] c";
        List<String> result = solution.extractWarningsAndErrors(log);
        assertEquals(List.of("a", "b", "c"), result);
    }

    @Test
    @DisplayName("INFO lines are ignored")
    void testInfoIgnored() {
        String log = "[INFO] startup\n[INFO] ready";
        assertTrue(solution.extractWarningsAndErrors(log).isEmpty());
    }

    @Test
    @DisplayName("Order preserved: ERROR before WARN")
    void testOrderPreserved() {
        String log = "[ERROR] first\n[WARN] second\n[ERROR] third";
        List<String> result = solution.extractWarningsAndErrors(log);
        assertEquals(List.of("first", "second", "third"), result);
    }

    @Test
    @DisplayName("Non-bracketed severity does NOT match")
    void testNoBrackets() {
        List<String> result = solution.extractWarningsAndErrors("WARN: not matched");
        assertTrue(result.isEmpty(), "Only [WARN] format should match, not WARN:");
    }

    @Test
    @DisplayName("Message with spaces and punctuation")
    void testMessageWithSpecialChars() {
        String log = "[ERROR] connection refused: timeout after 30s";
        List<String> result = solution.extractWarningsAndErrors(log);
        assertEquals(List.of("connection refused: timeout after 30s"), result);
    }

    @Test
    @DisplayName("All four severity types — only WARN and ERROR returned")
    void testAllSeverities() {
        String log = "[DEBUG] d\n[INFO] i\n[WARN] w\n[ERROR] e";
        List<String> result = solution.extractWarningsAndErrors(log);
        assertEquals(List.of("w", "e"), result);
    }
}
