package com.example;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * Tests for Solution.filterContainingDot().
 * All tests FAIL until the method is properly implemented.
 */
class SolutionTest {

    private final Solution solution = new Solution();

    @Test
    void testStringsWithDots() {
        List<String> input = Arrays.asList("hello.world", "helloXworld", "3.14", "nodot");
        List<String> result = solution.filterContainingDot(input);
        assertEquals(Arrays.asList("hello.world", "3.14"), result);
    }

    @Test
    void testSingleDotString() {
        List<String> input = Collections.singletonList(".");
        List<String> result = solution.filterContainingDot(input);
        assertEquals(Collections.singletonList("."), result);
    }

    @Test
    void testEmptyStringsNotMatched() {
        List<String> input = Arrays.asList("", "abc", "   ");
        List<String> result = solution.filterContainingDot(input);
        assertEquals(Collections.emptyList(), result);
    }

    @Test
    void testMultipleDotsInOneString() {
        List<String> input = Arrays.asList("a.b.c", "version.2.0", "plain");
        List<String> result = solution.filterContainingDot(input);
        assertEquals(Arrays.asList("a.b.c", "version.2.0"), result);
    }

    @Test
    void testEmptyList() {
        List<String> result = solution.filterContainingDot(Collections.emptyList());
        assertEquals(Collections.emptyList(), result);
    }

    @Test
    void testDotAtStart() {
        List<String> input = Collections.singletonList(".gitignore");
        List<String> result = solution.filterContainingDot(input);
        assertEquals(Collections.singletonList(".gitignore"), result);
    }

    @Test
    void testDotAtEnd() {
        List<String> input = Collections.singletonList("file.");
        List<String> result = solution.filterContainingDot(input);
        assertEquals(Collections.singletonList("file."), result);
    }

    @Test
    void testNullElementsSkipped() {
        List<String> input = Arrays.asList("has.dot", null, "nodot");
        List<String> result = solution.filterContainingDot(input);
        assertEquals(Collections.singletonList("has.dot"), result);
    }

    @Test
    void testNoDotInAnyString() {
        List<String> input = Arrays.asList("hello", "world", "java");
        List<String> result = solution.filterContainingDot(input);
        assertEquals(Collections.emptyList(), result);
    }

    @Test
    void testOnlyDots() {
        List<String> input = Arrays.asList("...", "..", ".");
        List<String> result = solution.filterContainingDot(input);
        assertEquals(Arrays.asList("...", "..", "."), result);
    }
}
