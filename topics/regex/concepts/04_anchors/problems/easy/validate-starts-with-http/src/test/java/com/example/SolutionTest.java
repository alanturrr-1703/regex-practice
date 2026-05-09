package com.example;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests for {@link Solution#startsWithHttp(String)}.
 * These tests WILL FAIL until the method is implemented.
 */
@DisplayName("Validate Starts With HTTP")
class SolutionTest {

    private Solution solution;

    @BeforeEach
    void setUp() {
        solution = new Solution();
    }

    @Test
    @DisplayName("http:// URL returns true")
    void testHttpUrl() {
        assertTrue(solution.startsWithHttp("http://example.com"));
    }

    @Test
    @DisplayName("https:// URL returns true")
    void testHttpsUrl() {
        assertTrue(solution.startsWithHttp("https://example.com"));
    }

    @Test
    @DisplayName("https:// with path returns true")
    void testHttpsWithPath() {
        assertTrue(solution.startsWithHttp("https://example.com/path?q=1"));
    }

    @Test
    @DisplayName("ftp:// URL returns false")
    void testFtpUrl() {
        assertFalse(solution.startsWithHttp("ftp://example.com"),
                "ftp:// does not start with http:// or https://");
    }

    @Test
    @DisplayName("Leading space before http:// returns false")
    void testLeadingSpaceReturnsFalse() {
        assertFalse(solution.startsWithHttp(" http://example.com"),
                "Leading space means http:// is not at position 0");
    }

    @Test
    @DisplayName("Empty string returns false")
    void testEmptyString() {
        assertFalse(solution.startsWithHttp(""));
    }

    @Test
    @DisplayName("Null returns false")
    void testNull() {
        assertFalse(solution.startsWithHttp(null));
    }

    @Test
    @DisplayName("Bare 'http' without :// returns false")
    void testBareHttp() {
        assertFalse(solution.startsWithHttp("http"),
                "'http' without :// is not a valid URL scheme start");
    }

    @Test
    @DisplayName("String containing http:// but not at start returns false")
    void testHttpNotAtStart() {
        assertFalse(solution.startsWithHttp("see http://example.com"),
                "http:// in the middle should not match — ^ requires start of string");
    }

    @Test
    @DisplayName("HTTPS uppercase is case-sensitive — returns false")
    void testUppercaseReturnsFalse() {
        assertFalse(solution.startsWithHttp("HTTP://example.com"),
                "Pattern is case-sensitive; uppercase HTTP should not match");
    }
}
