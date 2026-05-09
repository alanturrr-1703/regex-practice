package com.example;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests for {@link Solution#collapseWhitespace(String)}.
 *
 * These tests WILL FAIL until the method is implemented.
 */
@DisplayName("Collapse Whitespace")
class SolutionTest {

    private Solution solution;

    @BeforeEach
    void setUp() {
        solution = new Solution();
    }

    @Test
    @DisplayName("Multiple spaces between words collapse to one")
    void testMultipleSpacesCollapse() {
        assertEquals("hello world", solution.collapseWhitespace("hello   world"));
    }

    @Test
    @DisplayName("Leading and trailing spaces are trimmed")
    void testLeadingTrailingTrimmed() {
        assertEquals("spaces", solution.collapseWhitespace("  spaces  "));
    }

    @Test
    @DisplayName("Tabs are treated as whitespace and collapsed")
    void testTabsCollapsed() {
        assertEquals("a b", solution.collapseWhitespace("a\t\tb"));
    }

    @Test
    @DisplayName("Empty string returns empty string")
    void testEmptyString() {
        assertEquals("", solution.collapseWhitespace(""));
    }

    @Test
    @DisplayName("Already normalized string is unchanged")
    void testAlreadyNormalized() {
        assertEquals("no change", solution.collapseWhitespace("no change"));
    }

    @Test
    @DisplayName("Newlines are treated as whitespace and collapsed")
    void testNewlinesCollapsed() {
        assertEquals("a b", solution.collapseWhitespace("a\n\nb"));
    }

    @Test
    @DisplayName("All whitespace string returns empty string")
    void testAllWhitespace() {
        assertEquals("", solution.collapseWhitespace("   "));
    }

    @Test
    @DisplayName("Mixed whitespace types are collapsed")
    void testMixedWhitespaceTypes() {
        assertEquals("hello world", solution.collapseWhitespace("  hello\t\tworld\n"));
    }

    @Test
    @DisplayName("Multiple word runs all normalized")
    void testMultipleWordRuns() {
        assertEquals("one two three four", solution.collapseWhitespace("one  two   three    four"));
    }

    @Test
    @DisplayName("Single tab only returns empty string")
    void testSingleTabOnly() {
        assertEquals("", solution.collapseWhitespace("\t"));
    }

    @Test
    @DisplayName("Single character string is unchanged")
    void testSingleCharacter() {
        assertEquals("a", solution.collapseWhitespace("a"));
    }

    @Test
    @DisplayName("Null input returns null")
    void testNullInput() {
        assertNull(solution.collapseWhitespace(null),
                "Null input should return null (not throw an exception)");
    }
}
