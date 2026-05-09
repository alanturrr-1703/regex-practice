package com.example;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests for the detect-digit problem.
 * All tests FAIL until Solution.containsDigit() is implemented.
 */
class SolutionTest {

    private Solution solution;

    @BeforeEach
    void setUp() {
        solution = new Solution();
    }

    @Test
    void testStringWithDigitReturnsTrue() {
        assertTrue(solution.containsDigit("hello123"));
    }

    @Test
    void testStringWithoutDigitReturnsFalse() {
        assertFalse(solution.containsDigit("hello"));
    }

    @Test
    void testEmptyStringReturnsFalse() {
        assertFalse(solution.containsDigit(""));
    }

    @Test
    void testDigitSurroundedBySpaces() {
        // Spaces are not digits, but the digit is still there
        assertTrue(solution.containsDigit("   9   "));
    }

    @Test
    void testSpecialCharsNoDigitReturnsFalse() {
        assertFalse(solution.containsDigit("abc!@#"));
    }

    @Test
    void testSingleDigitReturnsTrue() {
        assertTrue(solution.containsDigit("5"));
    }

    @Test
    void testDigitAtStartReturnsTrue() {
        assertTrue(solution.containsDigit("1abc"));
    }

    @Test
    void testDigitAtEndReturnsTrue() {
        assertTrue(solution.containsDigit("abc9"));
    }

    @Test
    void testOnlySpacesReturnsFalse() {
        assertFalse(solution.containsDigit("   "));
    }

    @Test
    void testAllDigitsReturnsTrue() {
        assertTrue(solution.containsDigit("12345"));
    }

    @Test
    void testZeroReturnsTrue() {
        // '0' is a digit
        assertTrue(solution.containsDigit("abc0def"));
    }

    @Test
    void testMixedAlphanumericReturnsTrue() {
        assertTrue(solution.containsDigit("a1b2c3"));
    }
}
