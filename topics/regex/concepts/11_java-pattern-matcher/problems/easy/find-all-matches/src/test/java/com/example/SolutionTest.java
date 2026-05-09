package com.example;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests for the find-all-matches problem.
 *
 * ALL tests will fail until Solution.findAll() is implemented.
 * The UnsupportedOperationException thrown by the stub causes each test to error.
 */
class SolutionTest {

    private Solution solution;

    @BeforeEach
    void setUp() {
        solution = new Solution();
    }

    @Test
    @DisplayName("Finds all digit sequences in a sentence")
    void testFindAllDigits() {
        List<String> result = solution.findAll("one 1 two 2 three 3", "\\d+");
        assertEquals(List.of("1", "2", "3"), result);
    }

    @Test
    @DisplayName("Returns empty list when pattern has no matches")
    void testNoMatchesReturnsEmptyList() {
        List<String> result = solution.findAll("hello", "xyz");
        assertEquals(List.of(), result);
    }

    @Test
    @DisplayName("Single large match when entire string matches the pattern")
    void testEntireStringIsOneMatch() {
        List<String> result = solution.findAll("aabbcc", "[a-c]+");
        assertEquals(List.of("aabbcc"), result);
    }

    @Test
    @DisplayName("Empty input returns empty list regardless of pattern")
    void testEmptyInput() {
        List<String> result = solution.findAll("", "\\w+");
        assertEquals(List.of(), result);
    }

    @Test
    @DisplayName("Finds all lowercase word segments interleaved with digits")
    void testAlphaSegmentsOnly() {
        List<String> result = solution.findAll("abc123def456", "[a-z]+");
        assertEquals(List.of("abc", "def"), result);
    }

    @Test
    @DisplayName("Finds all digit segments interleaved with alpha")
    void testDigitSegmentsInterleaved() {
        List<String> result = solution.findAll("abc123def456", "\\d+");
        assertEquals(List.of("123", "456"), result);
    }

    @Test
    @DisplayName("Single-character matches are returned individually")
    void testSingleCharacterMatches() {
        List<String> result = solution.findAll("a1b2c3", "\\d");
        assertEquals(List.of("1", "2", "3"), result);
    }

    @Test
    @DisplayName("Pattern matching repeated chars returns correct segments")
    void testRepeatedCharPattern() {
        List<String> result = solution.findAll("aaabbbccc", "a+|b+|c+");
        assertEquals(List.of("aaa", "bbb", "ccc"), result);
    }

    @Test
    @DisplayName("Empty list returned for empty input with digit pattern")
    void testEmptyInputWithDigitPattern() {
        List<String> result = solution.findAll("", "\\d+");
        assertTrue(result.isEmpty(), "Empty input should yield empty list");
    }

    @Test
    @DisplayName("Word boundary matches return each word")
    void testWordMatches() {
        List<String> result = solution.findAll("hello world foo", "\\w+");
        assertEquals(List.of("hello", "world", "foo"), result);
    }
}
