package com.example;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests for the unicode-identifier problem.
 * All tests FAIL until Solution.extractIdentifiers() is implemented.
 */
class SolutionTest {

    private Solution solution;

    @BeforeEach
    void setUp() {
        solution = new Solution();
    }

    @Test
    void testBasicLatinAndAccentedLetters() {
        // wörld: ö is \p{L} (Latin small letter o with diaeresis, code point U+00F6)
        // café: é is \p{L}
        List<String> result = solution.extractIdentifiers("hello wörld café");
        assertEquals(List.of("hello", "wörld", "café"), result);
    }

    @Test
    void testDigitPrefixedTokenNotMatched() {
        // "123bad": "bad" is preceded by '3' (a \p{N}) — should NOT be returned
        // x1 and y2 are valid (start with letter)
        List<String> result = solution.extractIdentifiers("x1 y2 123bad");
        assertEquals(List.of("x1", "y2"), result);
    }

    @Test
    void testUnderscoreStartIdentifiers() {
        List<String> result = solution.extractIdentifiers("_private _1");
        assertEquals(List.of("_private", "_1"), result);
    }

    @Test
    void testCjkCharacters() {
        // CJK characters are \p{L} (Unicode Letter category Lo)
        List<String> result = solution.extractIdentifiers("日本語 english");
        assertEquals(List.of("日本語", "english"), result);
    }

    @Test
    void testEmptyInputReturnsEmpty() {
        List<String> result = solution.extractIdentifiers("");
        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    void testOnlyDigitsReturnsEmpty() {
        List<String> result = solution.extractIdentifiers("123 456");
        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    void testSingleUnderscore() {
        List<String> result = solution.extractIdentifiers("_");
        assertEquals(List.of("_"), result);
    }

    @Test
    void testIdentifierWithDigitContinuation() {
        // x1bad should be ONE identifier: x(letter) then 1(digit) then bad(letters) — all continuation
        List<String> result = solution.extractIdentifiers("x1bad");
        assertEquals(List.of("x1bad"), result);
    }

    @Test
    void testMixedSeparatorsAndIdentifiers() {
        // Spaces and punctuation act as separators
        List<String> result = solution.extractIdentifiers("foo, bar; baz!");
        assertEquals(List.of("foo", "bar", "baz"), result);
    }

    @Test
    void testImmediatelyPrecededByLetterNotMatched() {
        // In "helloworld", the whole thing should be ONE identifier, not two
        List<String> result = solution.extractIdentifiers("helloworld");
        assertEquals(List.of("helloworld"), result);
    }

    @Test
    void testImmediatelyPrecededByUnderscoreNotSplit() {
        // In "foo_bar", the whole thing is ONE identifier (underscore is valid continuation)
        List<String> result = solution.extractIdentifiers("foo_bar");
        assertEquals(List.of("foo_bar"), result);
    }

    @Test
    void testOrderPreserved() {
        List<String> result = solution.extractIdentifiers("alpha beta gamma");
        assertEquals(List.of("alpha", "beta", "gamma"), result);
    }
}
