package com.example;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests for fix-matches-vs-find.
 *
 * ALL tests will fail until Solution.containsZipCode() is implemented correctly.
 *
 * Key tests:
 * - testZipEmbeddedInText: verifies matches() vs find() bug is fixed
 * - testSixDigitsReturnsFalse: verifies boundary lookaheads are used
 */
class SolutionTest {

    private Solution solution;

    @BeforeEach
    void setUp() {
        solution = new Solution();
    }

    @Test
    @DisplayName("ZIP embedded in sentence returns true (catches matches() vs find() bug)")
    void testZipEmbeddedInText() {
        assertTrue(solution.containsZipCode("ZIP: 90210 is in CA"),
            "find() should locate 90210 inside the sentence; matches() would return false");
    }

    @Test
    @DisplayName("Standalone 5-digit ZIP returns true")
    void testStandaloneZip() {
        assertTrue(solution.containsZipCode("90210"));
    }

    @Test
    @DisplayName("4-digit number returns false — too short for ZIP")
    void testFourDigitsFalse() {
        assertFalse(solution.containsZipCode("9021"));
    }

    @Test
    @DisplayName("6-digit number returns false — not an isolated 5-digit ZIP")
    void testSixDigitsReturnsFalse() {
        assertFalse(solution.containsZipCode("902109"),
            "902109 has 6 consecutive digits; no standalone 5-digit ZIP exists");
    }

    @Test
    @DisplayName("Empty string returns false")
    void testEmptyInput() {
        assertFalse(solution.containsZipCode(""));
    }

    @Test
    @DisplayName("String with only letters returns false")
    void testNoDigits() {
        assertFalse(solution.containsZipCode("no digits here"));
    }

    @Test
    @DisplayName("ZIP followed immediately by letter returns true (letter is not a digit)")
    void testZipFollowedByLetter() {
        assertTrue(solution.containsZipCode("90210CA"),
            "90210 is followed by 'C' (not a digit), so it's an isolated 5-digit group");
    }

    @Test
    @DisplayName("ZIP preceded by another digit returns false")
    void testZipPrecededByDigit() {
        assertFalse(solution.containsZipCode("190210"),
            "190210 has 6 consecutive digits — no isolated 5-digit group");
    }

    @Test
    @DisplayName("ZIP surrounded by spaces in text returns true")
    void testZipSurroundedBySpaces() {
        assertTrue(solution.containsZipCode("The zip code is 12345 for this area."));
    }

    @Test
    @DisplayName("String with 5-digit number that is part of a hyphenated ZIP+4 returns true for the 5-digit portion")
    void testZipPlusFour() {
        // "90210-1234" — the 90210 part is followed by '-' (not a digit), so it matches
        assertTrue(solution.containsZipCode("90210-1234"),
            "90210 in 90210-1234 is followed by '-', not a digit");
    }
}
