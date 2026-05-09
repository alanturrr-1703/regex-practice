package com.example;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests for the validate-alphanumeric problem.
 * All tests FAIL until Solution.isAlphanumeric() is implemented.
 */
class SolutionTest {

    private Solution solution;

    @BeforeEach
    void setUp() {
        solution = new Solution();
    }

    @Test
    void testMixedAlphanumericIsValid() {
        assertTrue(solution.isAlphanumeric("Hello123"));
    }

    @Test
    void testSpaceIsInvalid() {
        assertFalse(solution.isAlphanumeric("Hello 123"));
    }

    @Test
    void testEmptyStringIsInvalid() {
        assertFalse(solution.isAlphanumeric(""));
    }

    @Test
    void testSpecialCharIsInvalid() {
        assertFalse(solution.isAlphanumeric("abc!"));
    }

    @Test
    void testLetterAndDigitIsValid() {
        assertTrue(solution.isAlphanumeric("Z9"));
    }

    @Test
    void testDigitsOnlyIsValid() {
        assertTrue(solution.isAlphanumeric("1234"));
    }

    @Test
    void testLettersOnlyIsValid() {
        assertTrue(solution.isAlphanumeric("abc"));
    }

    @Test
    void testUnderscoreIsInvalid() {
        // \w includes underscore, but [a-zA-Z0-9] does not
        assertFalse(solution.isAlphanumeric("_hello"));
    }

    @Test
    void testHyphenIsInvalid() {
        assertFalse(solution.isAlphanumeric("hello-world"));
    }

    @Test
    void testSingleLetterIsValid() {
        assertTrue(solution.isAlphanumeric("a"));
    }

    @Test
    void testSingleDigitIsValid() {
        assertTrue(solution.isAlphanumeric("0"));
    }

    @Test
    void testTrailingSpaceIsInvalid() {
        // "Hello123 " — trailing space makes it invalid
        assertFalse(solution.isAlphanumeric("Hello123 "));
    }

    @Test
    void testLeadingSpaceIsInvalid() {
        assertFalse(solution.isAlphanumeric(" abc"));
    }
}
