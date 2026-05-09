package com.example;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests for Possessive Quantifier Usage.
 *
 * All tests MUST fail until Solution.tokenize() is implemented.
 */
class SolutionTest {

    private final Solution solution = new Solution();

    @Test
    @DisplayName("Standard ERROR line parsed correctly")
    void testErrorLine() {
        Optional<Solution.TokenizedLine> result = solution.tokenize("14:23:01 ERROR some message here");
        assertTrue(result.isPresent(), "Should parse valid log line");
        assertEquals("14:23:01", result.get().getTimestamp());
        assertEquals("ERROR", result.get().getSeverity());
        assertEquals("some message here", result.get().getMessage());
    }

    @Test
    @DisplayName("INFO line parsed correctly")
    void testInfoLine() {
        Optional<Solution.TokenizedLine> result = solution.tokenize("09:00:00 INFO server started");
        assertTrue(result.isPresent());
        assertEquals("09:00:00", result.get().getTimestamp());
        assertEquals("INFO", result.get().getSeverity());
        assertEquals("server started", result.get().getMessage());
    }

    @Test
    @DisplayName("WARN line with special chars in message")
    void testWarnLineSpecialMessage() {
        Optional<Solution.TokenizedLine> result = solution.tokenize("23:59:59 WARN disk usage > 80%");
        assertTrue(result.isPresent());
        assertEquals("WARN", result.get().getSeverity());
        assertEquals("disk usage > 80%", result.get().getMessage());
    }

    @Test
    @DisplayName("Non-matching line → empty Optional")
    void testNonMatchingLine() {
        Optional<Solution.TokenizedLine> result = solution.tokenize("not a log line");
        assertFalse(result.isPresent(), "Non-matching line should return empty Optional");
    }

    @Test
    @DisplayName("Null input → empty Optional")
    void testNull() {
        assertFalse(solution.tokenize(null).isPresent(), "Null should return empty Optional");
    }

    @Test
    @DisplayName("Empty string → empty Optional")
    void testEmpty() {
        assertFalse(solution.tokenize("").isPresent());
    }

    @Test
    @DisplayName("DEBUG line")
    void testDebugLine() {
        Optional<Solution.TokenizedLine> result = solution.tokenize("00:00:01 DEBUG initializing");
        assertTrue(result.isPresent());
        assertEquals("DEBUG", result.get().getSeverity());
        assertEquals("initializing", result.get().getMessage());
    }

    @Test
    @DisplayName("Line without message — severity only")
    void testNoMessage() {
        Optional<Solution.TokenizedLine> result = solution.tokenize("12:00:00 TRACE");
        // TRACE with no space/message after: matches if pattern allows empty message
        // (depends on learner's pattern — this tests the edge case)
        // The pattern "^([\d:]++) ([A-Z]++) (.*)$" — TRACE at end, group(3)=""
        assertTrue(result.isPresent(), "Line with only timestamp and severity should match");
        assertEquals("TRACE", result.get().getSeverity());
        assertEquals("", result.get().getMessage());
    }

    @Test
    @DisplayName("Message with multiple spaces preserved")
    void testMessageWithSpaces() {
        Optional<Solution.TokenizedLine> result = solution.tokenize("12:34:56 ERROR   triple space message");
        assertTrue(result.isPresent());
        // The message part includes whatever follows the severity and single space
        assertTrue(result.get().getMessage().contains("triple"), "Message should be preserved");
    }

    @Test
    @DisplayName("Timestamp field is exactly the time portion")
    void testTimestampExtraction() {
        Optional<Solution.TokenizedLine> result = solution.tokenize("08:30:00 INFO heartbeat");
        assertTrue(result.isPresent());
        assertEquals("08:30:00", result.get().getTimestamp(),
                "Timestamp should be exactly '08:30:00'");
    }
}
