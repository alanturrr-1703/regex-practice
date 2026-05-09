package com.example;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests for {@link Solution#countWordLog(String)}.
 * These tests WILL FAIL until the method is implemented.
 */
@DisplayName("Word Boundary Extractor")
class SolutionTest {

    private Solution solution;

    @BeforeEach
    void setUp() {
        solution = new Solution();
    }

    @Test
    @DisplayName("Single standalone 'log' returns 1")
    void testSingleLog() {
        assertEquals(1, solution.countWordLog("log the event"));
    }

    @Test
    @DisplayName("'logger' does NOT count — 'log' is part of a longer word")
    void testLoggerDoesNotCount() {
        assertEquals(0, solution.countWordLog("logger"));
    }

    @Test
    @DisplayName("'logs' does NOT count — 'log' with 's' is a different word")
    void testLogsDoesNotCount() {
        assertEquals(0, solution.countWordLog("logs events"));
    }

    @Test
    @DisplayName("Two standalone 'log' occurrences returns 2")
    void testTwoLogs() {
        assertEquals(2, solution.countWordLog("log and log again"));
    }

    @Test
    @DisplayName("'catalog logger' — no standalone 'log'")
    void testCatalogLogger() {
        assertEquals(0, solution.countWordLog("catalog logger"),
                "Both 'catalog' and 'logger' contain 'log' but not as standalone word");
    }

    @Test
    @DisplayName("'LOG' uppercase does NOT match — case-sensitive")
    void testUppercaseDoesNotMatch() {
        assertEquals(0, solution.countWordLog("LOG"),
                "Pattern is case-sensitive; uppercase LOG should not match");
    }

    @Test
    @DisplayName("'log.' counts — dot is a non-word boundary character")
    void testLogWithDot() {
        assertEquals(1, solution.countWordLog("log."),
                "Punctuation is a non-word character, creating a \\b boundary");
    }

    @Test
    @DisplayName("'log_file' does NOT count — underscore is a word character")
    void testLogUnderscoreNoMatch() {
        assertEquals(0, solution.countWordLog("log_file"),
                "Underscore is \\w — no word boundary between 'log' and '_'");
    }

    @Test
    @DisplayName("Three standalone 'log' in a row")
    void testThreeLogs() {
        assertEquals(3, solution.countWordLog("log log log"));
    }

    @Test
    @DisplayName("'(log)' counts — parentheses are non-word characters")
    void testLogInParentheses() {
        assertEquals(1, solution.countWordLog("(log)"));
    }

    @Test
    @DisplayName("'log1' does NOT count — digit is a word character")
    void testLogFollowedByDigit() {
        assertEquals(0, solution.countWordLog("log1"),
                "Digits are \\w — no word boundary between 'log' and '1'");
    }

    @Test
    @DisplayName("'blog' does NOT count — 'b' before 'log' is word character")
    void testBlog() {
        assertEquals(0, solution.countWordLog("blog"),
                "'b' is a word character — no boundary before 'log' in 'blog'");
    }

    @Test
    @DisplayName("Empty string returns 0")
    void testEmptyString() {
        assertEquals(0, solution.countWordLog(""));
    }

    @Test
    @DisplayName("Null returns 0")
    void testNull() {
        assertEquals(0, solution.countWordLog(null));
    }
}
