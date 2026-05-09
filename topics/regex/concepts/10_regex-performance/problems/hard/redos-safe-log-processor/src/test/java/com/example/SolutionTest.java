package com.example;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import java.time.Duration;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests for ReDoS-Safe Log Processor.
 *
 * All tests MUST fail until Solution.processLogs() is implemented.
 *
 * The performance test verifies that adversarial inputs (unclosed quotes, long strings)
 * complete within 500ms for 1000 lines.
 */
class SolutionTest {

    private final Solution solution = new Solution();

    // ------------------------------------------------------------------ basic parsing

    @Test
    @DisplayName("ERROR line with context — all fields parsed")
    void testErrorWithContext() {
        List<Solution.LogRecord> result = solution.processLogs(
                Collections.singletonList("2024-01-15 ERROR [\"user-service\"] Connection timeout"));
        assertEquals(1, result.size());
        Solution.LogRecord r = result.get(0);
        assertEquals("2024-01-15", r.getTimestamp());
        assertEquals("ERROR", r.getLevel());
        assertEquals("user-service", r.getContext());
        assertEquals("Connection timeout", r.getMessage());
    }

    @Test
    @DisplayName("INFO line without context — context is null")
    void testInfoWithoutContext() {
        List<Solution.LogRecord> result = solution.processLogs(
                Collections.singletonList("2024-01-15 INFO startup complete"));
        assertEquals(1, result.size());
        Solution.LogRecord r = result.get(0);
        assertEquals("INFO", r.getLevel());
        assertNull(r.getContext(), "Context should be null when absent");
        assertEquals("startup complete", r.getMessage());
    }

    @Test
    @DisplayName("WARN line with context containing spaces")
    void testWarnWithSpacedContext() {
        List<Solution.LogRecord> result = solution.processLogs(
                Collections.singletonList("2024-01-15 WARN [\"db-pool\"] Pool exhausted: 100/100"));
        assertEquals(1, result.size());
        assertEquals("db-pool", result.get(0).getContext());
        assertEquals("Pool exhausted: 100/100", result.get(0).getMessage());
    }

    @Test
    @DisplayName("Empty context bracket — context is empty string")
    void testEmptyContext() {
        List<Solution.LogRecord> result = solution.processLogs(
                Collections.singletonList("2024-01-15 WARN [\"\"] empty context message"));
        assertEquals(1, result.size());
        assertEquals("", result.get(0).getContext());
        assertEquals("empty context message", result.get(0).getMessage());
    }

    // ------------------------------------------------------------------ malformed lines

    @Test
    @DisplayName("Malformed line skipped")
    void testMalformedLineSkipped() {
        List<Solution.LogRecord> result = solution.processLogs(
                Collections.singletonList("not a log line at all"));
        assertTrue(result.isEmpty(), "Malformed lines should be skipped");
    }

    @Test
    @DisplayName("Mixed valid and malformed lines")
    void testMixedLines() {
        List<String> lines = Arrays.asList(
                "2024-01-15 INFO good line",
                "bad line",
                "2024-01-15 ERROR another good line"
        );
        List<Solution.LogRecord> result = solution.processLogs(lines);
        assertEquals(2, result.size(), "Should return 2 valid records, skipping the bad line");
    }

    // ------------------------------------------------------------------ edge cases

    @Test
    @DisplayName("Null input → empty list")
    void testNull() {
        List<Solution.LogRecord> result = solution.processLogs(null);
        assertNotNull(result, "Result must never be null");
        assertTrue(result.isEmpty());
    }

    @Test
    @DisplayName("Empty list → empty result")
    void testEmptyList() {
        assertTrue(solution.processLogs(Collections.emptyList()).isEmpty());
    }

    @Test
    @DisplayName("Multiple valid lines")
    void testMultipleValidLines() {
        List<String> lines = Arrays.asList(
                "2024-01-15 DEBUG init",
                "2024-01-15 INFO ready",
                "2024-01-15 WARN [\"svc\"] slow",
                "2024-01-15 ERROR [\"db\"] failed"
        );
        List<Solution.LogRecord> result = solution.processLogs(lines);
        assertEquals(4, result.size());
        assertEquals("DEBUG", result.get(0).getLevel());
        assertEquals("ERROR", result.get(3).getLevel());
        assertEquals("db", result.get(3).getContext());
    }

    // ------------------------------------------------------------------ performance / ReDoS safety

    @Test
    @DisplayName("1000 lines (including adversarial) complete within 500ms")
    void testPerformanceSafety() {
        List<String> lines = new ArrayList<>(1000);
        for (int i = 0; i < 400; i++) {
            lines.add("2024-01-15 INFO [\"service-" + i + "\"] request processed");
        }
        for (int i = 0; i < 300; i++) {
            lines.add("2024-01-15 ERROR normal error " + i);
        }
        // Adversarial: lines with unclosed quotes — would hang with BROKEN_PATTERN
        for (int i = 0; i < 300; i++) {
            lines.add("2024-01-15 ERROR [\"aaaaaaaaaaaaaaaaaaaaaaaaaaaa");
        }

        assertTimeout(Duration.ofMillis(500), () -> {
            List<Solution.LogRecord> result = solution.processLogs(lines);
            // Adversarial lines are malformed — they should be skipped
            assertEquals(700, result.size(),
                    "Should parse 700 valid lines and skip 300 adversarial ones");
        }, "processLogs must complete within 500ms — check for ReDoS in your pattern!");
    }
}
