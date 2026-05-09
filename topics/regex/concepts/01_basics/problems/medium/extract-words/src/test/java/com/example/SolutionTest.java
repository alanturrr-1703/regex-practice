package com.example;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests for the extract-words problem.
 * All tests FAIL until Solution.extractWords() is implemented.
 */
class SolutionTest {

    private Solution solution;

    @BeforeEach
    void setUp() {
        solution = new Solution();
    }

    @Test
    void testBasicWordsWithPunctuation() {
        List<String> result = solution.extractWords("Hello, world! 123");
        assertEquals(List.of("Hello", "world"), result);
    }

    @Test
    void testUnderscoreActsAsSeparator() {
        // \w includes underscore; [a-zA-Z] does not — "foo" and "bar" are separate words
        List<String> result = solution.extractWords("foo_bar");
        assertEquals(List.of("foo", "bar"), result);
    }

    @Test
    void testOnlyWhitespaceReturnsEmpty() {
        List<String> result = solution.extractWords("  ");
        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    void testDigitAsSeparator() {
        // "Java" and "Rocks" are separated by "9"
        List<String> result = solution.extractWords("Java9Rocks");
        assertEquals(List.of("Java", "Rocks"), result);
    }

    @Test
    void testSingleWord() {
        assertEquals(List.of("abc"), solution.extractWords("abc"));
    }

    @Test
    void testOnlyDigitsReturnsEmpty() {
        List<String> result = solution.extractWords("123");
        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    void testEmptyStringReturnsEmpty() {
        List<String> result = solution.extractWords("");
        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    void testSingleLetterWords() {
        List<String> result = solution.extractWords("a1b2c3");
        assertEquals(List.of("a", "b", "c"), result);
    }

    @Test
    void testMixedPunctuationSeparators() {
        // All non-letter chars are separators
        List<String> result = solution.extractWords("one,two;three.four");
        assertEquals(List.of("one", "two", "three", "four"), result);
    }

    @Test
    void testPreservesCase() {
        // Words should be returned with original case, not lowercased
        List<String> result = solution.extractWords("ABCdef");
        assertEquals(List.of("ABCdef"), result);
    }

    @Test
    void testOrderPreserved() {
        List<String> result = solution.extractWords("zebra apple mango");
        assertEquals(List.of("zebra", "apple", "mango"), result);
    }
}
