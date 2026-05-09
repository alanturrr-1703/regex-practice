package com.example;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * Tests for Solution.parseCsv().
 * All tests FAIL until the method is implemented.
 */
class SolutionTest {

    private final Solution solution = new Solution();

    @Test
    void testSimpleRow() {
        List<List<String>> result = solution.parseCsv("a,b,c");
        assertEquals(List.of(List.of("a", "b", "c")), result);
    }

    @Test
    void testQuotedFieldWithComma() {
        // Input: "quoted,field",plain
        // Java string: "\"quoted,field\",plain"
        String input = "\"quoted,field\",plain";
        List<List<String>> result = solution.parseCsv(input);
        assertEquals(List.of(List.of("quoted,field", "plain")), result);
    }

    @Test
    void testEmptyField() {
        // a,,b has an empty middle field
        List<List<String>> result = solution.parseCsv("a,,b");
        assertEquals(List.of(List.of("a", "", "b")), result);
    }

    @Test
    void testMultipleRows() {
        List<List<String>> result = solution.parseCsv("row1a,row1b\nrow2a,row2b");
        assertEquals(List.of(
            List.of("row1a", "row1b"),
            List.of("row2a", "row2b")
        ), result);
    }

    @Test
    void testEmptyInput() {
        List<List<String>> result = solution.parseCsv("");
        assertEquals(Collections.emptyList(), result);
    }

    @Test
    void testSingleValue() {
        List<List<String>> result = solution.parseCsv("hello");
        assertEquals(List.of(List.of("hello")), result);
    }

    @Test
    void testAllQuotedFields() {
        // "a","b","c"
        String input = "\"a\",\"b\",\"c\"";
        List<List<String>> result = solution.parseCsv(input);
        assertEquals(List.of(List.of("a", "b", "c")), result);
    }

    @Test
    void testLeadingComma() {
        // ,a has an empty first field
        List<List<String>> result = solution.parseCsv(",a");
        assertEquals(List.of(List.of("", "a")), result);
    }

    @Test
    void testQuotedEmptyField() {
        // "" is an empty quoted field
        String input = "\"\"";
        List<List<String>> result = solution.parseCsv(input);
        assertEquals(List.of(List.of("")), result);
    }
}
