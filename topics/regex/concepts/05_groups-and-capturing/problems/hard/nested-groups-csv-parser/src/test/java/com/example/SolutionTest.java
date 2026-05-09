package com.example;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests for {@link Solution#parseCsvLine(String)}.
 *
 * <p>All tests will fail with {@link UnsupportedOperationException} until the
 * method is implemented. That is intentional — make the tests pass.</p>
 */
class SolutionTest {

    private final Solution solution = new Solution();

    // -----------------------------------------------------------------------
    // Core: mixed quoted and unquoted fields
    // -----------------------------------------------------------------------

    @Test
    @DisplayName("Mixed: unquoted, quoted-with-comma, quoted, unquoted")
    void testMixedFields() {
        List<String> result = solution.parseCsvLine("one,\"two, with comma\",\"three\",four");

        assertEquals(4, result.size());
        assertEquals("one",            result.get(0));
        assertEquals("two, with comma", result.get(1));
        assertEquals("three",          result.get(2));
        assertEquals("four",           result.get(3));
    }

    @Test
    @DisplayName("All unquoted fields")
    void testAllUnquoted() {
        List<String> result = solution.parseCsvLine("a,b,c");

        assertEquals(List.of("a", "b", "c"), result);
    }

    @Test
    @DisplayName("All quoted fields")
    void testAllQuoted() {
        List<String> result = solution.parseCsvLine("\"alpha\",\"beta\",\"gamma\"");

        assertEquals(List.of("alpha", "beta", "gamma"), result);
    }

    @Test
    @DisplayName("Quoted fields: quoted, empty-quoted, unquoted")
    void testQuotedAndEmptyQuoted() {
        List<String> result = solution.parseCsvLine("\"quoted\",\"\",plain");

        assertEquals(3, result.size());
        assertEquals("quoted", result.get(0));
        assertEquals("",       result.get(1), "Empty quoted field should be empty string");
        assertEquals("plain",  result.get(2));
    }

    // -----------------------------------------------------------------------
    // Edge cases: empty fields
    // -----------------------------------------------------------------------

    @Test
    @DisplayName("Empty unquoted field between commas")
    void testEmptyUnquotedField() {
        List<String> result = solution.parseCsvLine("a,,b");

        assertEquals(3, result.size());
        assertEquals("a", result.get(0));
        assertEquals("",  result.get(1), "Empty unquoted field should be empty string");
        assertEquals("b", result.get(2));
    }

    @Test
    @DisplayName("Single quoted field — no commas")
    void testSingleQuotedField() {
        List<String> result = solution.parseCsvLine("\"only one\"");

        assertEquals(List.of("only one"), result);
    }

    @Test
    @DisplayName("Single unquoted field — no commas")
    void testSingleUnquotedField() {
        List<String> result = solution.parseCsvLine("solo");

        assertEquals(List.of("solo"), result);
    }

    @Test
    @DisplayName("Quoted field with comma must not split on the comma")
    void testQuotedCommaNotSplit() {
        List<String> result = solution.parseCsvLine("\"has, comma\",\"normal\"");

        assertEquals(2, result.size(), "Comma inside quotes must not create extra fields");
        assertEquals("has, comma", result.get(0));
        assertEquals("normal",     result.get(1));
    }

    // -----------------------------------------------------------------------
    // Null-group correctness
    // -----------------------------------------------------------------------

    @Test
    @DisplayName("Quoted and unquoted in same line — groups resolve correctly")
    void testGroupResolution() {
        // Quoted field fires group(1); unquoted fires group(2)
        // Both must be extracted correctly
        List<String> result = solution.parseCsvLine("\"q\",u");

        assertEquals(2, result.size());
        assertEquals("q", result.get(0), "Quoted field stripped of quotes");
        assertEquals("u", result.get(1), "Unquoted field returned as-is");
    }
}
