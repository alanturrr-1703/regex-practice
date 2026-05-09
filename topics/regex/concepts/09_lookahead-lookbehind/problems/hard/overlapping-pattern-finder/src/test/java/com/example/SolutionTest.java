package com.example;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests for Overlapping Pattern Finder.
 *
 * All tests MUST fail until Solution.countOverlapping() is implemented.
 */
class SolutionTest {

    private final Solution solution = new Solution();

    @Test
    @DisplayName("abababa / aba → 3 overlapping matches")
    void testAbaInAbabaaba() {
        assertEquals(3, solution.countOverlapping("abababa", "aba"),
                "Positions 0, 2, 4 — three overlapping 'aba'");
    }

    @Test
    @DisplayName("aaaa / aa → 3 overlapping matches")
    void testDoubleAInAaaa() {
        assertEquals(3, solution.countOverlapping("aaaa", "aa"),
                "Positions 0, 1, 2 — three overlapping 'aa'");
    }

    @Test
    @DisplayName("hello / ll → 1 (no overlap possible)")
    void testLlInHello() {
        assertEquals(1, solution.countOverlapping("hello", "ll"),
                "'ll' appears once at position 2");
    }

    @Test
    @DisplayName("abcdef / xyz → 0 (no match)")
    void testNoMatch() {
        assertEquals(0, solution.countOverlapping("abcdef", "xyz"),
                "Pattern not present in input");
    }

    @Test
    @DisplayName("Empty input → 0")
    void testEmptyInput() {
        assertEquals(0, solution.countOverlapping("", "a"),
                "Empty input has no matches");
    }

    @Test
    @DisplayName("Null input → 0")
    void testNullInput() {
        assertEquals(0, solution.countOverlapping(null, "a"),
                "Null input should return 0");
    }

    @Test
    @DisplayName("Null pattern → 0")
    void testNullPattern() {
        assertEquals(0, solution.countOverlapping("hello", null),
                "Null pattern should return 0");
    }

    @Test
    @DisplayName("Empty pattern → 0")
    void testEmptyPattern() {
        assertEquals(0, solution.countOverlapping("hello", ""),
                "Empty pattern should return 0");
    }

    @Test
    @DisplayName("Single char pattern in single char input")
    void testSingleCharMatch() {
        assertEquals(1, solution.countOverlapping("a", "a"),
                "Single 'a' in 'a' is 1 match");
    }

    @Test
    @DisplayName("aaa / a → 3 (single char, all match)")
    void testSingleCharInAllSame() {
        assertEquals(3, solution.countOverlapping("aaa", "a"),
                "Each 'a' is its own match");
    }

    @Test
    @DisplayName("Pattern longer than input → 0")
    void testPatternLongerThanInput() {
        assertEquals(0, solution.countOverlapping("ab", "abc"),
                "Pattern longer than input cannot match");
    }

    @Test
    @DisplayName("Pattern equals input → 1")
    void testPatternEqualsInput() {
        assertEquals(1, solution.countOverlapping("aba", "aba"),
                "Pattern equal to input matches exactly once");
    }

    @Test
    @DisplayName("Input with regex metacharacters in pattern — treated as literals")
    void testMetacharsInPattern() {
        // "a+b" as literal: the '+' is a literal character, not a quantifier
        assertEquals(1, solution.countOverlapping("a+b", "+"),
                "'+' in pattern should be treated as a literal, not a quantifier");
    }

    @Test
    @DisplayName("Bioinformatics-style DNA sequence")
    void testDnaSequence() {
        // ATATA contains ATA at positions 0 and 2 (overlapping)
        assertEquals(2, solution.countOverlapping("ATATA", "ATA"),
                "DNA motif 'ATA' appears at positions 0 and 2 in 'ATATA'");
    }
}
