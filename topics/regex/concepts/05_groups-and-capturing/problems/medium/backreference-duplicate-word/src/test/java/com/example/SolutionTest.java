package com.example;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests for {@link Solution#findDuplicateWords(String)}.
 *
 * <p>All tests will fail with {@link UnsupportedOperationException} until the
 * method is implemented. That is intentional — make the tests pass.</p>
 */
class SolutionTest {

    private final Solution solution = new Solution();

    // -----------------------------------------------------------------------
    // Core functionality
    // -----------------------------------------------------------------------

    @Test
    @DisplayName("Duplicate at start of sentence")
    void testDuplicateAtStart() {
        List<String> result = solution.findDuplicateWords("the the cat sat");
        assertEquals(List.of("the"), result);
    }

    @Test
    @DisplayName("Duplicate in the middle of a sentence")
    void testDuplicateInMiddle() {
        List<String> result = solution.findDuplicateWords("it is is done");
        assertEquals(List.of("is"), result);
    }

    @Test
    @DisplayName("Two separate duplicate pairs in one string")
    void testTwoDuplicatePairs() {
        List<String> result = solution.findDuplicateWords("a a b b");
        assertEquals(2, result.size(), "Should find two duplicate pairs");
        assertEquals("a", result.get(0));
        assertEquals("b", result.get(1));
    }

    @Test
    @DisplayName("Case-insensitive: 'Hello hello' is a duplicate pair")
    void testCaseInsensitive() {
        List<String> result = solution.findDuplicateWords("Hello hello world");
        assertEquals(1, result.size());
        // Returns the FIRST occurrence casing (group(1)), which is "Hello"
        assertEquals("Hello", result.get(0),
            "Should return the first occurrence's casing via group(1)");
    }

    // -----------------------------------------------------------------------
    // No-match edge cases
    // -----------------------------------------------------------------------

    @Test
    @DisplayName("No consecutive duplicates returns empty list")
    void testNoDuplicates() {
        List<String> result = solution.findDuplicateWords("no duplicates here");
        assertTrue(result.isEmpty());
    }

    @Test
    @DisplayName("Empty string returns empty list")
    void testEmptyString() {
        List<String> result = solution.findDuplicateWords("");
        assertTrue(result.isEmpty());
    }

    @Test
    @DisplayName("'aaa' is a single token — no duplicate pair")
    void testContinuousTokenIsNotDuplicate() {
        List<String> result = solution.findDuplicateWords("aaa");
        assertTrue(result.isEmpty(),
            "'aaa' is one word token; no whitespace between instances");
    }

    @Test
    @DisplayName("Non-adjacent same word is NOT a duplicate")
    void testNonAdjacentNotDuplicate() {
        // "the" appears twice but not consecutively
        List<String> result = solution.findDuplicateWords("the cat the");
        assertTrue(result.isEmpty(),
            "Non-adjacent same word should not count as duplicate");
    }

    // -----------------------------------------------------------------------
    // group(1) vs group(0) correctness
    // -----------------------------------------------------------------------

    @Test
    @DisplayName("Result contains the single word, not the 'word word' pair")
    void testReturnsSingleWordNotPair() {
        List<String> result = solution.findDuplicateWords("hello hello");
        assertEquals(1, result.size());
        assertEquals("hello", result.get(0),
            "Should return 'hello', not 'hello hello'");
        assertNotEquals("hello hello", result.get(0),
            "group(0) would be 'hello hello'; group(1) should be 'hello'");
    }
}
