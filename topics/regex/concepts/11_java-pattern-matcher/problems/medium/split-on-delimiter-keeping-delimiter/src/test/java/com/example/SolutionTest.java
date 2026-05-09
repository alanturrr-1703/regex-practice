package com.example;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests for the split-on-delimiter-keeping-delimiter problem.
 *
 * ALL tests will fail until Solution.splitKeepingDelimiter() is implemented.
 */
class SolutionTest {

    private Solution solution;

    @BeforeEach
    void setUp() {
        solution = new Solution();
    }

    @Test
    @DisplayName("Basic split: a;b;c -> [a;, b;, c]")
    void testBasicSplit() {
        List<String> result = solution.splitKeepingDelimiter("a;b;c");
        assertEquals(List.of("a;", "b;", "c"), result);
    }

    @Test
    @DisplayName("No delimiter: entire string is one token")
    void testNoDelimiter() {
        List<String> result = solution.splitKeepingDelimiter("abc");
        assertEquals(List.of("abc"), result);
    }

    @Test
    @DisplayName("Leading semicolon: ;leading -> [;, leading]")
    void testLeadingSemicolon() {
        List<String> result = solution.splitKeepingDelimiter(";leading");
        assertEquals(List.of(";", "leading"), result);
    }

    @Test
    @DisplayName("Trailing semicolon: trailing; -> [trailing;] (no empty string appended)")
    void testTrailingSemicolon() {
        List<String> result = solution.splitKeepingDelimiter("trailing;");
        assertEquals(List.of("trailing;"), result);
    }

    @Test
    @DisplayName("Consecutive semicolons: a;;b -> [a;, ;, b]")
    void testConsecutiveSemicolons() {
        List<String> result = solution.splitKeepingDelimiter("a;;b");
        assertEquals(List.of("a;", ";", "b"), result);
    }

    @Test
    @DisplayName("Empty input returns empty list")
    void testEmptyInput() {
        List<String> result = solution.splitKeepingDelimiter("");
        assertTrue(result.isEmpty(), "Empty input should return empty list");
    }

    @Test
    @DisplayName("Single semicolon only: ; -> [;]")
    void testOnlySemicolon() {
        List<String> result = solution.splitKeepingDelimiter(";");
        assertEquals(List.of(";"), result);
    }

    @Test
    @DisplayName("Multiple consecutive semicolons: ;;; -> [;, ;, ;]")
    void testMultipleConsecutiveSemicolons() {
        List<String> result = solution.splitKeepingDelimiter(";;;");
        assertEquals(List.of(";", ";", ";"), result);
    }

    @Test
    @DisplayName("Longer tokens between semicolons")
    void testLongerTokens() {
        List<String> result = solution.splitKeepingDelimiter("hello;world;foo");
        assertEquals(List.of("hello;", "world;", "foo"), result);
    }

    @Test
    @DisplayName("Token after last semicolon is included without semicolon")
    void testRemainingTokenAfterLastSemicolon() {
        List<String> result = solution.splitKeepingDelimiter("x;y;z");
        assertEquals(3, result.size());
        assertEquals("z", result.get(2), "Last element should not have semicolon");
    }
}
