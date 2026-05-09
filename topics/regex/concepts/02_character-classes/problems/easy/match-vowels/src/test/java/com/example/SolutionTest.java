package com.example;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests for the match-vowels problem.
 * All tests FAIL until Solution.countVowels() is implemented.
 */
class SolutionTest {

    private Solution solution;

    @BeforeEach
    void setUp() {
        solution = new Solution();
    }

    @Test
    void testMixedCaseString() {
        // "Hello World" → H-e-l-l-o W-o-r-l-d → e, o, o → 3
        assertEquals(3, solution.countVowels("Hello World"));
    }

    @Test
    void testEmptyStringReturnsZero() {
        assertEquals(0, solution.countVowels(""));
    }

    @Test
    void testAllUppercaseVowels() {
        assertEquals(5, solution.countVowels("AEIOU"));
    }

    @Test
    void testNoVowels() {
        // "rhythm" has no vowels
        assertEquals(0, solution.countVowels("rhythm"));
    }

    @Test
    void testBeautiful() {
        // B-e-a-u-t-i-f-u-l → e, a, u, i, u → 5
        assertEquals(5, solution.countVowels("Beautiful"));
    }

    @Test
    void testLowercaseVowelsOnly() {
        assertEquals(5, solution.countVowels("aeiou"));
    }

    @Test
    void testDigitsAndPunctuationNotCounted() {
        // "h3ll0!" → no vowels
        assertEquals(0, solution.countVowels("h3ll0!"));
    }

    @Test
    void testSingleVowel() {
        assertEquals(1, solution.countVowels("a"));
    }

    @Test
    void testSingleConsonant() {
        assertEquals(0, solution.countVowels("b"));
    }

    @Test
    void testMixedCaseVowels() {
        // "aAeEiIoOuU" → 10 vowels (all 10 should be counted case-insensitively)
        assertEquals(10, solution.countVowels("aAeEiIoOuU"));
    }

    @Test
    void testOnlyConsonants() {
        assertEquals(0, solution.countVowels("bcdfg"));
    }

    @Test
    void testYIsNotAVowel() {
        // 'y' is not counted as a vowel in this problem
        assertEquals(0, solution.countVowels("why"));
    }
}
