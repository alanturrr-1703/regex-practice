package com.example;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests for {@link Solution#captureFirstWord(String)}.
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
    @DisplayName("Simple two-word string returns first word")
    void testHelloWorld() {
        assertEquals(Optional.of("hello"), solution.captureFirstWord("hello world"));
    }

    @Test
    @DisplayName("Leading spaces are skipped; second token becomes first word found")
    void testLeadingSpaces() {
        assertEquals(Optional.of("spaces"), solution.captureFirstWord("  spaces first"));
    }

    @Test
    @DisplayName("Digits are word characters — returned as the first word")
    void testDigitsFirst() {
        assertEquals(Optional.of("123"), solution.captureFirstWord("123 num first"));
    }

    @Test
    @DisplayName("Underscore is a word character — included in the word")
    void testUnderscore() {
        assertEquals(Optional.of("_private"), solution.captureFirstWord("_private field"));
    }

    @Test
    @DisplayName("Single character input returns that character")
    void testSingleChar() {
        assertEquals(Optional.of("a"), solution.captureFirstWord("a"));
    }

    // -----------------------------------------------------------------------
    // Edge cases — no match
    // -----------------------------------------------------------------------

    @Test
    @DisplayName("All punctuation returns empty Optional")
    void testAllPunctuation() {
        assertEquals(Optional.empty(), solution.captureFirstWord("!@#$%"));
    }

    @Test
    @DisplayName("Empty string returns empty Optional")
    void testEmptyString() {
        assertEquals(Optional.empty(), solution.captureFirstWord(""));
    }

    @Test
    @DisplayName("Only spaces returns empty Optional")
    void testOnlySpaces() {
        assertEquals(Optional.empty(), solution.captureFirstWord("   "));
    }

    // -----------------------------------------------------------------------
    // group(0) vs group(1) correctness
    // -----------------------------------------------------------------------

    @Test
    @DisplayName("Returned word should not include leading punctuation or spaces")
    void testNoLeadingPunctInResult() {
        Optional<String> result = solution.captureFirstWord("... hello");
        assertTrue(result.isPresent());
        // group(0) of a \s*(\w+) pattern would include "..." if the pattern has \W* prefix
        // group(1) should be just "hello"
        assertEquals("hello", result.get(),
            "Result should be just the word, not any surrounding characters");
    }
}
