package com.example;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests for the match-literal-string problem.
 *
 * All tests will FAIL until Solution.filterErrors() is implemented.
 * The method currently throws UnsupportedOperationException.
 */
class SolutionTest {

    private Solution solution;

    @BeforeEach
    void setUp() {
        solution = new Solution();
    }

    /**
     * Core test: mixed case input. Only lines with uppercase "ERROR" should match.
     * Validates case sensitivity and substring matching simultaneously.
     */
    @Test
    void testMixedCaseOnlyUppercaseMatches() {
        List<String> input = Arrays.asList(
            "ERROR: disk full",
            "error: low mem",
            "CRITICAL ERROR",
            "no problem",
            ""
        );
        List<String> expected = List.of("ERROR: disk full", "CRITICAL ERROR");
        assertEquals(expected, solution.filterErrors(input));
    }

    /**
     * Empty list input should produce an empty result, not null or exception.
     */
    @Test
    void testEmptyListReturnsEmptyList() {
        List<String> result = solution.filterErrors(List.of());
        assertNotNull(result, "Result must not be null");
        assertTrue(result.isEmpty(), "Result must be empty for empty input");
    }

    /**
     * None of the lines contain "ERROR" — result must be empty.
     */
    @Test
    void testNoMatchesReturnsEmptyList() {
        List<String> input = Arrays.asList(
            "info: all systems nominal",
            "warning: low disk space at 80%",
            "debug: initializing subsystem"
        );
        List<String> result = solution.filterErrors(input);
        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    /**
     * All lines contain "ERROR" — all should be returned, order preserved.
     */
    @Test
    void testAllLinesMatch() {
        List<String> input = Arrays.asList(
            "ERROR",
            "ERROR: something went wrong",
            "Something ERROR happened here"
        );
        assertEquals(input, solution.filterErrors(input));
    }

    /**
     * "ERRORS" contains "ERROR" as a substring — must be included.
     * Tests that the implementation does substring matching, not word matching.
     */
    @Test
    void testErrorEmbeddedInLongerWord() {
        List<String> input = Arrays.asList("ERRORS found in log", "my_ERROR_code");
        List<String> result = solution.filterErrors(input);
        assertEquals(2, result.size());
        assertTrue(result.contains("ERRORS found in log"));
        assertTrue(result.contains("my_ERROR_code"));
    }

    /**
     * Empty string does not contain "ERROR" — must not be returned.
     */
    @Test
    void testEmptyStringDoesNotMatch() {
        List<String> input = Arrays.asList("", "   ", "ERROR");
        List<String> expected = List.of("ERROR");
        assertEquals(expected, solution.filterErrors(input));
    }

    /**
     * Order of results must match the order of matching elements in the input list.
     */
    @Test
    void testOrderPreserved() {
        List<String> input = Arrays.asList(
            "ok message",
            "ERROR 1: first",
            "another ok message",
            "ERROR 2: second",
            "final ok"
        );
        List<String> expected = List.of("ERROR 1: first", "ERROR 2: second");
        assertEquals(expected, solution.filterErrors(input));
    }

    /**
     * Lowercase "error" must NOT match — the search is case-sensitive.
     */
    @Test
    void testCaseSensitivityLowercase() {
        List<String> input = Arrays.asList("error: file not found", "Error: permission denied");
        List<String> result = solution.filterErrors(input);
        assertTrue(result.isEmpty(),
            "Lowercase 'error' and mixed-case 'Error' must not match");
    }

    /**
     * A line with "ERROR" appearing multiple times is still one list element.
     * It should appear exactly once in the result.
     */
    @Test
    void testLineWithMultipleErrorOccurrences() {
        List<String> input = List.of("ERROR: ERROR in sub-ERROR-system");
        List<String> expected = List.of("ERROR: ERROR in sub-ERROR-system");
        assertEquals(expected, solution.filterErrors(input));
    }
}
