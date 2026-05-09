package com.example;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests for {@link Solution#parseLine(String)}.
 *
 * <p>All tests will fail with {@link UnsupportedOperationException} until the
 * method is implemented. That is intentional — make the tests pass.</p>
 */
class SolutionTest {

    private final Solution solution = new Solution();

    private static final String VALID_LINE =
            "127.0.0.1 - - [10/Oct/2024:13:55:36 +0000] \"GET /index.html HTTP/1.1\" 200 1234";

    private static final String VALID_LINE_404 =
            "192.168.1.1 - - [10/Oct/2024:14:00:00 +0000] \"POST /login HTTP/1.1\" 404 -";

    private static final String VALID_LINE_QUERY =
            "10.0.0.1 - - [01/Jan/2024:00:00:01 +0000] \"GET /search?q=hello&lang=en HTTP/1.1\" 200 512";

    // -----------------------------------------------------------------------
    // Core parsing — all 6 named groups
    // -----------------------------------------------------------------------

    @Test
    @DisplayName("Valid 200 line — all six named groups extracted correctly")
    void testValidLine200() {
        Optional<Solution.LogEntry> result = solution.parseLine(VALID_LINE);

        assertTrue(result.isPresent(), "Valid log line should parse successfully");
        Solution.LogEntry entry = result.get();
        assertEquals("127.0.0.1",                  entry.ip());
        assertEquals("10/Oct/2024:13:55:36 +0000",  entry.date());
        assertEquals("GET",                          entry.method());
        assertEquals("/index.html",                  entry.path());
        assertEquals("200",                          entry.status());
        assertEquals("1234",                         entry.bytes());
    }

    @Test
    @DisplayName("Valid 404 line with dash bytes — bytes field accepts '-'")
    void testValidLine404WithDashBytes() {
        Optional<Solution.LogEntry> result = solution.parseLine(VALID_LINE_404);

        assertTrue(result.isPresent());
        Solution.LogEntry entry = result.get();
        assertEquals("192.168.1.1", entry.ip());
        assertEquals("POST",        entry.method());
        assertEquals("/login",      entry.path());
        assertEquals("404",         entry.status());
        assertEquals("-",           entry.bytes(), "Bytes can be '-' for no-content responses");
    }

    @Test
    @DisplayName("Valid line with query-string path")
    void testValidLineWithQueryString() {
        Optional<Solution.LogEntry> result = solution.parseLine(VALID_LINE_QUERY);

        assertTrue(result.isPresent());
        assertEquals("/search?q=hello&lang=en", result.get().path(),
            "Path group should capture the full path including query string");
    }

    @Test
    @DisplayName("Date field is extracted WITHOUT the surrounding brackets")
    void testDateWithoutBrackets() {
        Optional<Solution.LogEntry> result = solution.parseLine(VALID_LINE);

        assertTrue(result.isPresent());
        String date = result.get().date();
        assertFalse(date.startsWith("["), "Date should not include the opening bracket");
        assertFalse(date.endsWith("]"),   "Date should not include the closing bracket");
        assertEquals("10/Oct/2024:13:55:36 +0000", date);
    }

    // -----------------------------------------------------------------------
    // Failure cases
    // -----------------------------------------------------------------------

    @Test
    @DisplayName("Non-log-format string returns Optional.empty()")
    void testInvalidLine() {
        Optional<Solution.LogEntry> result = solution.parseLine("not a log line at all");
        assertFalse(result.isPresent(), "Non-matching line should return Optional.empty()");
    }

    @Test
    @DisplayName("Empty string returns Optional.empty()")
    void testEmptyLine() {
        Optional<Solution.LogEntry> result = solution.parseLine("");
        assertFalse(result.isPresent());
    }

    @Test
    @DisplayName("Partial line (missing bytes) returns Optional.empty()")
    void testPartialLine() {
        String partial = "127.0.0.1 - - [10/Oct/2024:13:55:36 +0000] \"GET /index.html HTTP/1.1\" 200";
        Optional<Solution.LogEntry> result = solution.parseLine(partial);
        assertFalse(result.isPresent(), "Incomplete line should not parse");
    }
}
