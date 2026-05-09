package com.example;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests for {@link Solution#parse(String)}.
 * These tests WILL FAIL until the method is implemented.
 */
@DisplayName("Log Line Anchor Parser")
class SolutionTest {

    private Solution solution;

    @BeforeEach
    void setUp() {
        solution = new Solution();
    }

    // -----------------------------------------------------------------------
    // Helper to build expected LogEntry objects
    // -----------------------------------------------------------------------

    private static Solution.LogEntry entry(String level, String date, String time, String message) {
        return new Solution.LogEntry(level, date, time, message);
    }

    // -----------------------------------------------------------------------
    // Single-line tests
    // -----------------------------------------------------------------------

    @Test
    @DisplayName("INFO line is parsed correctly")
    void testSingleInfoLine() {
        String input = "[INFO] 2024-01-15 10:30:45 - Server started";
        List<Solution.LogEntry> result = solution.parse(input);
        assertEquals(1, result.size());
        assertEquals(entry("INFO", "2024-01-15", "10:30:45", "Server started"), result.get(0));
    }

    @Test
    @DisplayName("ERROR line is parsed correctly")
    void testSingleErrorLine() {
        String input = "[ERROR] 2024-06-01 23:59:59 - Database connection failed";
        List<Solution.LogEntry> result = solution.parse(input);
        assertEquals(1, result.size());
        assertEquals(entry("ERROR", "2024-06-01", "23:59:59", "Database connection failed"), result.get(0));
    }

    @Test
    @DisplayName("DEBUG line is parsed correctly")
    void testSingleDebugLine() {
        String input = "[DEBUG] 2024-03-10 08:00:00 - Cache miss for key=user:42";
        List<Solution.LogEntry> result = solution.parse(input);
        assertEquals(1, result.size());
        assertEquals(entry("DEBUG", "2024-03-10", "08:00:00", "Cache miss for key=user:42"), result.get(0));
    }

    @Test
    @DisplayName("WARN line is parsed correctly")
    void testSingleWarnLine() {
        String input = "[WARN] 2024-02-28 14:22:10 - Disk usage above 80%";
        List<Solution.LogEntry> result = solution.parse(input);
        assertEquals(1, result.size());
        assertEquals(entry("WARN", "2024-02-28", "14:22:10", "Disk usage above 80%"), result.get(0));
    }

    // -----------------------------------------------------------------------
    // Multi-line tests
    // -----------------------------------------------------------------------

    @Test
    @DisplayName("Multiple valid lines all parsed")
    void testMultipleValidLines() {
        String input = "[INFO] 2024-01-15 10:30:45 - Server started\n"
                + "[WARN] 2024-01-15 11:00:00 - High memory usage\n"
                + "[ERROR] 2024-01-15 11:05:33 - Out of memory";
        List<Solution.LogEntry> result = solution.parse(input);
        assertEquals(3, result.size());
        assertEquals("INFO",  result.get(0).level);
        assertEquals("WARN",  result.get(1).level);
        assertEquals("ERROR", result.get(2).level);
    }

    @Test
    @DisplayName("Malformed lines are silently skipped")
    void testMalformedLinesSkipped() {
        String input = "[INFO] 2024-01-15 10:30:45 - Server started\n"
                + "this is not a log line\n"
                + "[WARN] 2024-01-15 11:00:00 - High memory usage";
        List<Solution.LogEntry> result = solution.parse(input);
        assertEquals(2, result.size(),
                "The malformed middle line should be silently skipped");
        assertEquals("INFO", result.get(0).level);
        assertEquals("WARN", result.get(1).level);
    }

    @Test
    @DisplayName("Invalid level (TRACE) is skipped")
    void testInvalidLevelSkipped() {
        String input = "[TRACE] 2024-01-15 10:00:00 - Not a valid level";
        assertTrue(solution.parse(input).isEmpty(),
                "TRACE is not a recognized level — should be skipped");
    }

    @Test
    @DisplayName("Line with leading space is skipped — ^ requires line start")
    void testLeadingSpaceLineSkipped() {
        String input = " [INFO] 2024-01-15 10:00:00 - Leading space";
        assertTrue(solution.parse(input).isEmpty(),
                "Leading space prevents ^ from matching — line should be skipped");
    }

    // -----------------------------------------------------------------------
    // Edge cases
    // -----------------------------------------------------------------------

    @Test
    @DisplayName("Null input returns empty list")
    void testNullInput() {
        List<Solution.LogEntry> result = solution.parse(null);
        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    @DisplayName("Empty string returns empty list")
    void testEmptyString() {
        assertTrue(solution.parse("").isEmpty());
    }

    @Test
    @DisplayName("All malformed lines return empty list")
    void testAllMalformed() {
        String input = "not a log\njust text\n[BADLEVEL] 2024-01-01 00:00:00 - test";
        assertTrue(solution.parse(input).isEmpty());
    }

    @Test
    @DisplayName("Order of LogEntry list matches order of lines in input")
    void testOrderPreserved() {
        String input = "[DEBUG] 2024-01-01 00:00:01 - first\n"
                + "[INFO]  2024-01-01 00:00:02 - second with extra space\n"
                + "[ERROR] 2024-01-01 00:00:03 - third";
        // Note: line 2 has TWO spaces after [INFO] — this does NOT match the format
        List<Solution.LogEntry> result = solution.parse(input);
        assertEquals(2, result.size(),
                "Line with double space after level should fail — strict format");
        assertEquals("DEBUG", result.get(0).level);
        assertEquals("ERROR", result.get(1).level);
    }

    @Test
    @DisplayName("Message with special characters is captured correctly")
    void testMessageWithSpecialChars() {
        String input = "[ERROR] 2024-05-20 09:15:30 - Exception: java.lang.NullPointerException at Main.java:42";
        List<Solution.LogEntry> result = solution.parse(input);
        assertEquals(1, result.size());
        assertEquals("Exception: java.lang.NullPointerException at Main.java:42",
                result.get(0).message);
    }

    @Test
    @DisplayName("Message field is correctly extracted (not including ' - ' prefix)")
    void testMessageFieldExtraction() {
        String input = "[INFO] 2024-07-04 12:00:00 - Hello - World";
        List<Solution.LogEntry> result = solution.parse(input);
        assertEquals(1, result.size());
        // The message starts after the first " - " separator; the rest (including more " - ") is message
        assertEquals("Hello - World", result.get(0).message);
    }
}
