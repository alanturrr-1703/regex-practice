package com.example;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Timeout;

import java.time.Duration;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests for debug-catastrophic-log-parser.
 *
 * ALL tests will fail until Solution.parseLogs() is implemented correctly.
 *
 * CRITICAL TESTS:
 * - testAdversarialInputCompletesQuickly: verifies ReDoS safety (200ms timeout)
 *   The broken pattern would hang or time out on this test.
 * - testValidLineParsed: verifies correct label/message extraction
 */
class SolutionTest {

    private Solution solution;

    @BeforeEach
    void setUp() {
        solution = new Solution();
    }

    @Test
    @DisplayName("Parses a simple label: message line correctly")
    void testValidLineParsed() {
        List<Solution.LogEntry> result = solution.parseLogs(List.of("ERROR: disk full"));
        assertEquals(1, result.size());
        assertEquals("ERROR", result.get(0).label);
        assertEquals("disk full", result.get(0).message);
    }

    @Test
    @DisplayName("Parses multiple valid lines and returns them in order")
    void testMultipleValidLines() {
        List<Solution.LogEntry> result = solution.parseLogs(List.of(
            "INFO: user logged in",
            "WARN: low memory"
        ));
        assertEquals(2, result.size());
        assertEquals("INFO", result.get(0).label);
        assertEquals("user logged in", result.get(0).message);
        assertEquals("WARN", result.get(1).label);
        assertEquals("low memory", result.get(1).message);
    }

    @Test
    @DisplayName("Empty list returns empty list")
    void testEmptyList() {
        List<Solution.LogEntry> result = solution.parseLogs(List.of());
        assertTrue(result.isEmpty());
    }

    @Test
    @DisplayName("Malformed line with no colon is silently skipped")
    void testMalformedLineSkipped() {
        List<Solution.LogEntry> result = solution.parseLogs(List.of(
            "VALID: has colon",
            "no colon here",
            "ALSO VALID: another"
        ));
        assertEquals(2, result.size(), "Malformed line should be silently skipped");
        assertEquals("VALID", result.get(0).label);
        assertEquals("ALSO VALID", result.get(1).label);
    }

    @Test
    @DisplayName("Empty lines are silently skipped")
    void testEmptyLineSkipped() {
        List<Solution.LogEntry> result = solution.parseLogs(List.of(
            "",
            "LEVEL: message"
        ));
        assertEquals(1, result.size());
        assertEquals("LEVEL", result.get(0).label);
    }

    @Test
    @DisplayName("Message can be empty (label: with nothing after)")
    void testEmptyMessage() {
        List<Solution.LogEntry> result = solution.parseLogs(List.of("LABEL: "));
        assertEquals(1, result.size());
        assertEquals("LABEL", result.get(0).label);
        // message after trimming the colon and space is empty or whitespace
        assertNotNull(result.get(0).message);
    }

    @Test
    @DisplayName("Multi-word label is parsed correctly")
    void testMultiWordLabel() {
        List<Solution.LogEntry> result = solution.parseLogs(
            List.of("ERROR LEVEL HIGH: critical failure"));
        assertEquals(1, result.size());
        // label is trimmed text before the colon
        assertTrue(result.get(0).label.contains("ERROR"),
            "Label should contain the full text before the colon");
        assertEquals("critical failure", result.get(0).message);
    }

    @Test
    @DisplayName("CRITICAL: adversarial input (many words, no colon) completes in under 200ms")
    void testAdversarialInputCompletesQuickly() {
        // This input would cause catastrophic backtracking with the broken pattern:
        // "^((\w+\s*)+):\s*(.*?)$" needs O(2^n) time to fail on n words with no ":"
        String adversarial =
            "word word word word word word word word word word word word!";
        assertTimeout(Duration.ofMillis(200), () -> {
            List<Solution.LogEntry> result = solution.parseLogs(List.of(adversarial));
            assertTrue(result.isEmpty(),
                "Adversarial line has no colon, so it must be skipped");
        }, "Pattern is catastrophically backtracking — use [^:]+ or \\w+(?:\\s+\\w+)* instead");
    }

    @Test
    @DisplayName("CRITICAL: longer adversarial input also completes quickly")
    void testLongerAdversarialInputCompletesQuickly() {
        // Even more words — broken pattern would take minutes; safe pattern is instant
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < 20; i++) {
            if (i > 0) sb.append(' ');
            sb.append("word");
        }
        sb.append("!"); // ends with "!" — no colon
        String adversarial = sb.toString();

        assertTimeout(Duration.ofMillis(200), () -> {
            List<Solution.LogEntry> result = solution.parseLogs(List.of(adversarial));
            assertTrue(result.isEmpty());
        }, "Safe pattern must handle 20-word adversarial input in well under 200ms");
    }
}
