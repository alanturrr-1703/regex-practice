package com.example;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static com.example.Solution.TokenType.*;

/**
 * Tests for the simple-lexer problem.
 * All tests FAIL until Solution.tokenize() is implemented.
 */
class SolutionTest {

    private Solution solution;

    /** Shorthand factory to reduce test verbosity. */
    private static Solution.Token tok(Solution.TokenType type, String value) {
        return new Solution.Token(type, value);
    }

    @BeforeEach
    void setUp() {
        solution = new Solution();
    }

    @Test
    void testEmptyExpressionReturnsEmptyList() {
        List<Solution.Token> result = solution.tokenize("");
        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    void testSingleNumber() {
        List<Solution.Token> result = solution.tokenize("42");
        assertEquals(List.of(tok(NUMBER, "42")), result);
    }

    @Test
    void testSimpleAddition() {
        List<Solution.Token> result = solution.tokenize("1 + 2");
        assertEquals(List.of(
            tok(NUMBER, "1"),
            tok(OPERATOR, "+"),
            tok(NUMBER, "2")
        ), result);
    }

    @Test
    void testParenthesizedIdentifier() {
        List<Solution.Token> result = solution.tokenize("(x)");
        assertEquals(List.of(
            tok(LPAREN, "("),
            tok(IDENTIFIER, "x"),
            tok(RPAREN, ")")
        ), result);
    }

    @Test
    void testMultiplicationWithIdentifier() {
        List<Solution.Token> result = solution.tokenize("100 * y_1");
        assertEquals(List.of(
            tok(NUMBER, "100"),
            tok(OPERATOR, "*"),
            tok(IDENTIFIER, "y_1")
        ), result);
    }

    @Test
    void testComplexExpression() {
        List<Solution.Token> result = solution.tokenize("12 + 300 * (x - y_1)");
        assertEquals(List.of(
            tok(NUMBER, "12"),
            tok(OPERATOR, "+"),
            tok(NUMBER, "300"),
            tok(OPERATOR, "*"),
            tok(LPAREN, "("),
            tok(IDENTIFIER, "x"),
            tok(OPERATOR, "-"),
            tok(IDENTIFIER, "y_1"),
            tok(RPAREN, ")")
        ), result);
    }

    @Test
    void testWhitespaceOnlyReturnsEmptyList() {
        List<Solution.Token> result = solution.tokenize("   ");
        assertNotNull(result);
        assertTrue(result.isEmpty(), "Whitespace-only input should produce no tokens");
    }

    @Test
    void testAdjacentTokensNoWhitespace() {
        // No spaces between tokens — lexer must still work correctly
        List<Solution.Token> result = solution.tokenize("x+y");
        assertEquals(List.of(
            tok(IDENTIFIER, "x"),
            tok(OPERATOR, "+"),
            tok(IDENTIFIER, "y")
        ), result);
    }

    @Test
    void testMultiCharIdentifier() {
        List<Solution.Token> result = solution.tokenize("myVar");
        assertEquals(List.of(tok(IDENTIFIER, "myVar")), result);
    }

    @Test
    void testAllOperatorTypes() {
        List<Solution.Token> result = solution.tokenize("a+b-c*d/e");
        assertEquals(List.of(
            tok(IDENTIFIER, "a"), tok(OPERATOR, "+"),
            tok(IDENTIFIER, "b"), tok(OPERATOR, "-"),
            tok(IDENTIFIER, "c"), tok(OPERATOR, "*"),
            tok(IDENTIFIER, "d"), tok(OPERATOR, "/"),
            tok(IDENTIFIER, "e")
        ), result);
    }

    @Test
    void testInvalidCharacterThrowsException() {
        assertThrows(IllegalArgumentException.class,
            () -> solution.tokenize("x!"));
    }

    @Test
    void testInvalidCharacterAtStartThrowsException() {
        assertThrows(IllegalArgumentException.class,
            () -> solution.tokenize("@x"));
    }
}
