package com.example;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests for {@link Solution#hasExactlyFiveDigits(String)}.
 *
 * These tests WILL FAIL until the method is implemented.
 * All tests use JUnit 5 assertions.
 */
@DisplayName("Validate Digit Count")
class SolutionTest {

    private Solution solution;

    @BeforeEach
    void setUp() {
        solution = new Solution();
    }

    // -----------------------------------------------------------------------
    // Positive cases — should return true
    // -----------------------------------------------------------------------

    @Test
    @DisplayName("Exactly 5 digits surrounded by letters")
    void testFiveDigitsSurroundedByLetters() {
        assertTrue(solution.hasExactlyFiveDigits("abc12345def"),
                "A run of exactly 5 digits between letters should match");
    }

    @Test
    @DisplayName("Exactly 5 digits as entire string")
    void testFiveDigitsOnlyString() {
        assertTrue(solution.hasExactlyFiveDigits("12345"),
                "A string that IS exactly 5 digits should match");
    }

    @Test
    @DisplayName("5 digits with code prefix")
    void testFiveDigitsWithCodePrefix() {
        assertTrue(solution.hasExactlyFiveDigits("code12345zip"),
                "5 digits embedded in alphanumeric text should match");
    }

    @Test
    @DisplayName("5 digits with non-digit surrounding characters (dash)")
    void testFiveDigitsSurroundedByDashes() {
        assertTrue(solution.hasExactlyFiveDigits("-12345-"),
                "Dashes are non-digits — 5-digit run should match");
    }

    @Test
    @DisplayName("Two digit groups — second is exactly 5")
    void testSecondGroupIsExactlyFive() {
        assertTrue(solution.hasExactlyFiveDigits("1234 12345"),
                "Second group of exactly 5 digits should cause a match");
    }

    @Test
    @DisplayName("5 digits at end of string")
    void testFiveDigitsAtEnd() {
        assertTrue(solution.hasExactlyFiveDigits("prefix12345"),
                "5 digits at end of string should match");
    }

    // -----------------------------------------------------------------------
    // Negative cases — should return false
    // -----------------------------------------------------------------------

    @Test
    @DisplayName("6 consecutive digits should NOT match")
    void testSixDigitsDoNotMatch() {
        assertFalse(solution.hasExactlyFiveDigits("abc123456def"),
                "6 consecutive digits should NOT produce a match");
    }

    @Test
    @DisplayName("Only 4 digits should NOT match")
    void testFourDigitsDoNotMatch() {
        assertFalse(solution.hasExactlyFiveDigits("1234"),
                "Only 4 digits — too short, should NOT match");
    }

    @Test
    @DisplayName("7 consecutive digits should NOT match")
    void testSevenDigitsDoNotMatch() {
        assertFalse(solution.hasExactlyFiveDigits("x1234567y"),
                "7 consecutive digits should NOT match");
    }

    @Test
    @DisplayName("10 consecutive digits should NOT match")
    void testTenDigitsDoNotMatch() {
        assertFalse(solution.hasExactlyFiveDigits("1234512345"),
                "10 consecutive digits — no isolated 5-digit run, should NOT match");
    }

    @Test
    @DisplayName("Empty string should NOT match")
    void testEmptyString() {
        assertFalse(solution.hasExactlyFiveDigits(""),
                "Empty string should return false");
    }

    @Test
    @DisplayName("Null input should NOT match")
    void testNullInput() {
        assertFalse(solution.hasExactlyFiveDigits(null),
                "Null input should return false without throwing an exception");
    }

    @Test
    @DisplayName("No digits at all should NOT match")
    void testNoDigits() {
        assertFalse(solution.hasExactlyFiveDigits("abcdefgh"),
                "String with no digits should not match");
    }

    @Test
    @DisplayName("6 digits starting with 0 should NOT match")
    void testSixDigitsStartingWithZero() {
        assertFalse(solution.hasExactlyFiveDigits("012345"),
                "6 digits (0-prefixed) should NOT match — still 6 consecutive digits");
    }
}
