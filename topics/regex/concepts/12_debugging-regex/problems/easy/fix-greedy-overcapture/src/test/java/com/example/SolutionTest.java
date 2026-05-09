package com.example;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests for fix-greedy-overcapture.
 *
 * ALL tests will fail until Solution.extractTitles() is implemented correctly.
 *
 * Key test: testTwoConsecutiveTitles verifies the greedy overcapture bug is fixed —
 * a correct implementation must return TWO elements for two title tags.
 */
class SolutionTest {

    private Solution solution;

    @BeforeEach
    void setUp() {
        solution = new Solution();
    }

    @Test
    @DisplayName("Single title tag extracts content correctly")
    void testSingleTitle() {
        List<String> result = solution.extractTitles("<title>Hello</title>");
        assertEquals(List.of("Hello"), result);
    }

    @Test
    @DisplayName("Two consecutive title tags return TWO separate results (greedy fix required)")
    void testTwoConsecutiveTitles() {
        List<String> result = solution.extractTitles(
            "<title>A</title><title>B</title>");
        assertEquals(2, result.size(),
            "Should find 2 titles; if only 1 found, the greedy overcapture bug is NOT fixed");
        assertEquals("A", result.get(0));
        assertEquals("B", result.get(1));
    }

    @Test
    @DisplayName("No title tags returns empty list")
    void testNoTitles() {
        List<String> result = solution.extractTitles("no titles here");
        assertTrue(result.isEmpty());
    }

    @Test
    @DisplayName("Empty title content is captured as empty string")
    void testEmptyTitleContent() {
        List<String> result = solution.extractTitles("<title></title>");
        assertEquals(List.of(""), result);
    }

    @Test
    @DisplayName("Title with spaces in content extracted correctly")
    void testTitleWithSpaces() {
        List<String> result = solution.extractTitles(
            "<title>Page One</title> text <title>Page Two</title>");
        assertEquals(List.of("Page One", "Page Two"), result);
    }

    @Test
    @DisplayName("Empty input returns empty list")
    void testEmptyInput() {
        List<String> result = solution.extractTitles("");
        assertTrue(result.isEmpty());
    }

    @Test
    @DisplayName("Title surrounded by other HTML passes through correctly")
    void testTitleInFullHtml() {
        String html = "<html><head><title>My Page</title></head><body></body></html>";
        List<String> result = solution.extractTitles(html);
        assertEquals(List.of("My Page"), result);
    }

    @Test
    @DisplayName("Three titles are all extracted (regression for multiple-match greedy bug)")
    void testThreeTitles() {
        List<String> result = solution.extractTitles(
            "<title>One</title><title>Two</title><title>Three</title>");
        assertEquals(3, result.size());
        assertEquals("One",   result.get(0));
        assertEquals("Two",   result.get(1));
        assertEquals("Three", result.get(2));
    }
}
