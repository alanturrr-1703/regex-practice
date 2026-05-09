package com.example;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests for the custom-character-range problem.
 * All tests FAIL until Solution.extractRestrictedTokens() is implemented.
 */
class SolutionTest {

    private Solution solution;

    @BeforeEach
    void setUp() {
        solution = new Solution();
    }

    @Test
    void testStopsAtOutOfRangeLetterAndDigit() {
        // "abc123" is in range; "xyz" not in a-m; "567" not in 0-4
        List<String> result = solution.extractRestrictedTokens("abc123xyz567");
        assertEquals(List.of("abc123"), result);
    }

    @Test
    void testLetterJustOutOfRange() {
        // h,e,l,l ∈ a-m; o ∉ a-m (o=111 > m=109)
        List<String> result = solution.extractRestrictedTokens("hello");
        assertEquals(List.of("hell"), result);
    }

    @Test
    void testAllInRange() {
        // a,a,a ∈ a-m; 4,4,4 ∈ 0-4 (max digit allowed)
        List<String> result = solution.extractRestrictedTokens("aaa444");
        assertEquals(List.of("aaa444"), result);
    }

    @Test
    void testAllOutOfRangeReturnsEmpty() {
        // x,y,z ∉ a-m; 9,9,9 ∉ 0-4
        List<String> result = solution.extractRestrictedTokens("xyz999");
        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    void testEmptyInputReturnsEmpty() {
        List<String> result = solution.extractRestrictedTokens("");
        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    void testBoundaryLetterMIncluded() {
        List<String> result = solution.extractRestrictedTokens("m");
        assertEquals(List.of("m"), result);
    }

    @Test
    void testBoundaryLetterNExcluded() {
        List<String> result = solution.extractRestrictedTokens("n");
        assertTrue(result.isEmpty());
    }

    @Test
    void testBoundaryDigit4Included() {
        List<String> result = solution.extractRestrictedTokens("4");
        assertEquals(List.of("4"), result);
    }

    @Test
    void testBoundaryDigit5Excluded() {
        List<String> result = solution.extractRestrictedTokens("5");
        assertTrue(result.isEmpty());
    }

    @Test
    void testMultipleTokensSeparatedByOutOfRangeChars() {
        // "abc" then 'n' (out) then "012" then '9' (out) then "de"
        List<String> result = solution.extractRestrictedTokens("abcn012z9de");
        assertEquals(List.of("abc", "012", "de"), result);
    }

    @Test
    void testDigit0Included() {
        List<String> result = solution.extractRestrictedTokens("0");
        assertEquals(List.of("0"), result);
    }

    @Test
    void testMixedAllowedAndDisallowedInSequence() {
        // "am04" all in range; "Z" not in range; "bm31" all in range
        List<String> result = solution.extractRestrictedTokens("am04Zbm31");
        assertEquals(List.of("am04", "bm31"), result);
    }
}
