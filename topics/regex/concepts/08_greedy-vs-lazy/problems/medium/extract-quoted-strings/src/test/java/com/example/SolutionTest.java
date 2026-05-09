package com.example;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * Tests for Solution.extractSimpleQuoted() and Solution.extractRobustQuoted().
 * All tests FAIL until the methods are implemented.
 */
class SolutionTest {

    private final Solution solution = new Solution();

    // --- Tests for extractSimpleQuoted ---

    @Test
    void testSimpleQuotedBasic() {
        assertEquals(Arrays.asList("hello", "bye"),
            solution.extractSimpleQuoted("say 'hello' then 'bye'"));
    }

    @Test
    void testSimpleQuotedNoQuotes() {
        assertEquals(Collections.emptyList(),
            solution.extractSimpleQuoted("no quotes here"));
    }

    @Test
    void testSimpleQuotedEmptyString() {
        assertEquals(Collections.singletonList(""),
            solution.extractSimpleQuoted("''"));
    }

    @Test
    void testSimpleQuotedSingleQuote() {
        assertEquals(Collections.singletonList("one"),
            solution.extractSimpleQuoted("'one'"));
    }

    @Test
    void testSimpleQuotedEmptyInput() {
        assertEquals(Collections.emptyList(),
            solution.extractSimpleQuoted(""));
    }

    // --- Tests for extractRobustQuoted ---

    @Test
    void testRobustQuotedWithEscapedQuote() {
        // Input contains: 'it\'s fine'
        // In Java string: "'it\\'s fine'"
        // Actual chars: 'it\'s fine'
        // Content (without outer quotes): it\'s fine
        String input = "'it\\'s fine'";
        List<String> result = solution.extractRobustQuoted(input);
        assertEquals(Collections.singletonList("it\\'s fine"), result);
    }

    @Test
    void testRobustQuotedNormal() {
        assertEquals(Collections.singletonList("hello"),
            solution.extractRobustQuoted("'hello'"));
    }

    @Test
    void testRobustQuotedMultiple() {
        assertEquals(Arrays.asList("one", "two"),
            solution.extractRobustQuoted("'one' and 'two'"));
    }

    @Test
    void testRobustQuotedEmpty() {
        assertEquals(Collections.singletonList(""),
            solution.extractRobustQuoted("''"));
    }

    @Test
    void testRobustQuotedNoQuotes() {
        assertEquals(Collections.emptyList(),
            solution.extractRobustQuoted("no quotes"));
    }
}
