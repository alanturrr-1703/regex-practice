package com.example;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests for {@link Solution#endsWithSemicolon(String)}.
 * These tests WILL FAIL until the method is implemented.
 */
@DisplayName("Validate Ends With Semicolon")
class SolutionTest {

    private Solution solution;

    @BeforeEach
    void setUp() {
        solution = new Solution();
    }

    @Test
    @DisplayName("Statement ending with semicolon returns true")
    void testStatementWithSemicolon() {
        assertTrue(solution.endsWithSemicolon("int x = 5;"));
    }

    @Test
    @DisplayName("Semicolon followed by spaces returns true")
    void testSemicolonWithTrailingSpaces() {
        assertTrue(solution.endsWithSemicolon("int x = 5;  "),
                "Trailing whitespace after ; is acceptable");
    }

    @Test
    @DisplayName("Semicolon followed by tab returns true")
    void testSemicolonWithTrailingTab() {
        assertTrue(solution.endsWithSemicolon("int x = 5;\t"));
    }

    @Test
    @DisplayName("Just a semicolon returns true")
    void testSemicolonOnly() {
        assertTrue(solution.endsWithSemicolon(";"));
    }

    @Test
    @DisplayName("Semicolon with leading spaces but no trailing returns true")
    void testLeadingSpaceSemicolon() {
        assertTrue(solution.endsWithSemicolon(" ;"),
                "Leading space is fine — semicolon is still at the effective end");
    }

    @Test
    @DisplayName("Statement without semicolon returns false")
    void testStatementWithoutSemicolon() {
        assertFalse(solution.endsWithSemicolon("int x = 5"),
                "No semicolon — should return false");
    }

    @Test
    @DisplayName("Empty string returns false")
    void testEmptyString() {
        assertFalse(solution.endsWithSemicolon(""));
    }

    @Test
    @DisplayName("Null returns false")
    void testNull() {
        assertFalse(solution.endsWithSemicolon(null));
    }

    @Test
    @DisplayName("Semicolon in middle (not at end) returns false")
    void testSemicolonInMiddle() {
        assertFalse(solution.endsWithSemicolon("a;b"),
                "Semicolon is not at the end of the string");
    }

    @Test
    @DisplayName("Semicolon followed by non-whitespace returns false")
    void testSemicolonFollowedByNonWhitespace() {
        assertFalse(solution.endsWithSemicolon("a; b"),
                "Non-whitespace after semicolon — $ anchor fails");
    }

    @Test
    @DisplayName("Semicolon then whitespace then non-whitespace returns false")
    void testSemicolonWhitespaceThenChar() {
        assertFalse(solution.endsWithSemicolon("a;  x"),
                "Content after whitespace — should fail");
    }
}
