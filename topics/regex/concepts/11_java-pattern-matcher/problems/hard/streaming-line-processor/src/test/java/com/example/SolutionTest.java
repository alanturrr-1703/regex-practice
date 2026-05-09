package com.example;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests for the streaming-line-processor problem.
 *
 * ALL tests will fail until Solution methods are implemented.
 * Note: Some tests call helper methods directly to allow incremental development.
 */
class SolutionTest {

    private Solution solution;

    @BeforeEach
    void setUp() {
        solution = new Solution();
    }

    // ---- processLines() tests ----

    @Test
    @DisplayName("Empty list returns empty list")
    void testEmptyListReturnsEmpty() {
        List<String> result = solution.processLines(List.of());
        assertTrue(result.isEmpty(), "processLines of empty list should return empty list");
    }

    @Test
    @DisplayName("Line with no special content passes through unchanged")
    void testNoSpecialContent() {
        List<String> result = solution.processLines(List.of("no special content here"));
        assertEquals(List.of("no special content here"), result);
    }

    @Test
    @DisplayName("Line with all three: timestamp preserved, email and IP masked")
    void testLineWithTimestampEmailAndIP() {
        List<String> input = List.of("09:30:00 user@example.com from 192.168.1.1");
        List<String> result = solution.processLines(input);
        assertEquals(1, result.size());
        String line = result.get(0);
        assertTrue(line.contains("09:30:00"),  "Timestamp must be preserved");
        assertTrue(line.contains("[EMAIL]"),   "Email must be masked");
        assertTrue(line.contains("[IP]"),      "IP must be masked");
        assertFalse(line.contains("user@example.com"), "Raw email must not appear");
        assertFalse(line.contains("192.168.1.1"),      "Raw IP must not appear");
    }

    @Test
    @DisplayName("Line with only email: email is masked, rest unchanged")
    void testLineWithOnlyEmail() {
        List<String> result = solution.processLines(
            List.of("contact admin@corp.org for help"));
        assertEquals(1, result.size());
        assertEquals("contact [EMAIL] for help", result.get(0));
    }

    @Test
    @DisplayName("Line with only IP: IP is masked, rest unchanged")
    void testLineWithOnlyIP() {
        List<String> result = solution.processLines(
            List.of("server at 10.0.0.1 is down"));
        assertEquals(1, result.size());
        assertEquals("server at [IP] is down", result.get(0));
    }

    @Test
    @DisplayName("Multiple lines processed independently and in order")
    void testMultipleLines() {
        List<String> input = List.of(
            "user@test.com connected",
            "no special content",
            "ip 172.16.0.1 detected"
        );
        List<String> result = solution.processLines(input);
        assertEquals(3, result.size());
        assertEquals("[EMAIL] connected", result.get(0));
        assertEquals("no special content", result.get(1));
        assertEquals("ip [IP] detected", result.get(2));
    }

    // ---- Helper method tests ----

    @Test
    @DisplayName("extractTimestamp: returns timestamp from line containing HH:MM:SS")
    void testExtractTimestampFound() {
        String ts = solution.extractTimestamp("09:30:00 some log message");
        assertEquals("09:30:00", ts);
    }

    @Test
    @DisplayName("extractTimestamp: returns null when no timestamp present")
    void testExtractTimestampNotFound() {
        String ts = solution.extractTimestamp("no timestamp here");
        assertNull(ts, "Should return null when no timestamp found");
    }

    @Test
    @DisplayName("maskEmails: replaces email with [EMAIL]")
    void testMaskEmails() {
        assertEquals("[EMAIL] is the sender",
            solution.maskEmails("user@example.com is the sender"));
    }

    @Test
    @DisplayName("maskEmails: replaces multiple emails")
    void testMaskEmailsMultiple() {
        String result = solution.maskEmails("from a@b.com to c@d.org");
        assertEquals("from [EMAIL] to [EMAIL]", result);
    }

    @Test
    @DisplayName("maskIPs: replaces IPv4 with [IP]")
    void testMaskIPs() {
        assertEquals("connected to [IP]",
            solution.maskIPs("connected to 192.168.1.1"));
    }

    @Test
    @DisplayName("maskIPs: line without IP is returned unchanged")
    void testMaskIPsNoMatch() {
        assertEquals("no ip here", solution.maskIPs("no ip here"));
    }
}
