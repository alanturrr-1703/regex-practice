package com.example;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests for Identify Catastrophic Pattern.
 *
 * All tests MUST fail until Solution.isSafePattern() is implemented.
 *
 * Note: catastrophic-pattern tests may take up to ~100ms each (the timeout budget).
 * Safe-pattern tests should complete almost instantly.
 */
class SolutionTest {

    private final Solution solution = new Solution();

    // ------------------------------------------------------------------ catastrophic patterns

    @Test
    @DisplayName("(a+)+ — classic catastrophic nesting → false")
    void testClassicNestedQuantifier() {
        assertFalse(solution.isSafePattern("(a+)+"),
                "(a+)+ is the canonical catastrophic backtracking pattern");
    }

    @Test
    @DisplayName("(.+)+ — wildcard nesting → false")
    void testWildcardNesting() {
        assertFalse(solution.isSafePattern("(.+)+"),
                "(.+)+ causes catastrophic backtracking on 'aaa...a!'");
    }

    @Test
    @DisplayName("([a-z]+)+ — character class nesting → false")
    void testCharClassNesting() {
        assertFalse(solution.isSafePattern("([a-z]+)+"),
                "([a-z]+)+ is catastrophic on lowercase inputs");
    }

    // ------------------------------------------------------------------ safe patterns

    @Test
    @DisplayName("\\d+ — safe single quantifier → true")
    void testSafeDigitPlus() {
        assertTrue(solution.isSafePattern("\\d+"),
                "\\d+ has no nested quantifiers — safe");
    }

    @Test
    @DisplayName("[a-z]+ — safe character class → true")
    void testSafeCharClass() {
        assertTrue(solution.isSafePattern("[a-z]+"),
                "[a-z]+ is a simple quantifier — safe");
    }

    @Test
    @DisplayName("(?:a+)b — safe: linear backtracking → true")
    void testNonCapturingGroupWithTerminator() {
        assertTrue(solution.isSafePattern("(?:a+)b"),
                "(?:a+)b has a terminator 'b' — only ONE level of quantifier, linear");
    }

    @Test
    @DisplayName("Simple literal pattern → true")
    void testLiteralPattern() {
        assertTrue(solution.isSafePattern("hello"),
                "A literal pattern has no quantifiers — trivially safe");
    }

    // ------------------------------------------------------------------ edge cases

    @Test
    @DisplayName("Null regex → false")
    void testNull() {
        assertFalse(solution.isSafePattern(null),
                "Null input should return false without throwing");
    }

    @Test
    @DisplayName("Invalid regex → false")
    void testInvalidRegex() {
        assertFalse(solution.isSafePattern("[invalid"),
                "Invalid regex should return false (PatternSyntaxException)");
    }

    @Test
    @DisplayName("a++ — possessive quantifier → true")
    void testPossessiveQuantifier() {
        assertTrue(solution.isSafePattern("a++"),
                "Possessive quantifier a++ never backtracks — safe");
    }

    @Test
    @DisplayName("\\w+ — word chars safe → true")
    void testWordCharsSafe() {
        assertTrue(solution.isSafePattern("\\w+"),
                "\\w+ is a simple quantifier — safe");
    }
}
