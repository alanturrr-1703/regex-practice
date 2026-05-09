package com.example;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests for Solution.hasOnlyValidEscapes().
 *
 * IMPORTANT: In these tests, Java string literals contain the actual characters
 * passed to the method. So "\\n" in test source = backslash + 'n' (two chars).
 * This represents the Java escape sequence \n as it would appear in source code.
 *
 * All tests FAIL until the method is implemented.
 */
class SolutionTest {

    private final Solution solution = new Solution();

    @Test
    void testNoBackslashes() {
        // No backslashes at all → valid
        assertTrue(solution.hasOnlyValidEscapes("hello world"));
    }

    @Test
    void testValidNewlineEscape() {
        // "\\n" in Java source = backslash + 'n' character → valid \n escape
        assertTrue(solution.hasOnlyValidEscapes("line1\\nline2"));
    }

    @Test
    void testValidTabEscape() {
        assertTrue(solution.hasOnlyValidEscapes("col1\\tcol2"));
    }

    @Test
    void testValidBackslashEscape() {
        // "\\\\" in Java source = two backslash characters → valid \\ escape
        assertTrue(solution.hasOnlyValidEscapes("a\\\\b"));
    }

    @Test
    void testValidDoubleQuoteEscape() {
        // "\\\"" in Java source = backslash + double-quote → valid \" escape
        assertTrue(solution.hasOnlyValidEscapes("say \\\"hello\\\""));
    }

    @Test
    void testInvalidXEscape() {
        // "\\x" in Java source = backslash + 'x' → invalid escape
        assertFalse(solution.hasOnlyValidEscapes("bad\\xescape"));
    }

    @Test
    void testInvalidQEscape() {
        assertFalse(solution.hasOnlyValidEscapes("\\q"));
    }

    @Test
    void testLoneBackslashAtEnd() {
        // A single backslash at the end of the string — incomplete escape
        // "\\" in Java source = one backslash character
        assertFalse(solution.hasOnlyValidEscapes("trailing\\"));
    }

    @Test
    void testEmptyString() {
        assertTrue(solution.hasOnlyValidEscapes(""));
    }

    @Test
    void testMixOfValidAndInvalid() {
        // Valid \n followed by invalid \x
        assertFalse(solution.hasOnlyValidEscapes("\\n\\x"));
    }

    @Test
    void testValidCarriageReturnEscape() {
        assertTrue(solution.hasOnlyValidEscapes("\\r\\n"));
    }

    @Test
    void testValidNullEscape() {
        assertTrue(solution.hasOnlyValidEscapes("prefix\\0suffix"));
    }
}
