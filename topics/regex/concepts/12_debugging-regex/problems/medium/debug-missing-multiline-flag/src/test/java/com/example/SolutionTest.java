package com.example;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests for debug-missing-multiline-flag.
 *
 * ALL tests will fail until Solution.extractCommentLines() is implemented correctly.
 *
 * Key test: testCommentNotAtStart verifies that MULTILINE is used —
 * the broken pattern would return [] for input not starting with "#".
 */
class SolutionTest {

    private Solution solution;

    @BeforeEach
    void setUp() {
        solution = new Solution();
    }

    @Test
    @DisplayName("Finds two comment lines in a three-line string")
    void testTwoCommentLines() {
        String text = "# comment\ncode line\n# another comment";
        List<String> result = solution.extractCommentLines(text);
        assertEquals(2, result.size());
        assertEquals("# comment", result.get(0));
        assertEquals("# another comment", result.get(1));
    }

    @Test
    @DisplayName("String with no comment lines returns empty list")
    void testNoCommentLines() {
        List<String> result = solution.extractCommentLines("no comments\nhere");
        assertTrue(result.isEmpty());
    }

    @Test
    @DisplayName("Single comment-only string returns that line")
    void testSingleComment() {
        List<String> result = solution.extractCommentLines("# only one");
        assertEquals(List.of("# only one"), result);
    }

    @Test
    @DisplayName("Empty input returns empty list")
    void testEmptyInput() {
        List<String> result = solution.extractCommentLines("");
        assertTrue(result.isEmpty());
    }

    @Test
    @DisplayName("Comment line NOT at start of string is found (requires MULTILINE)")
    void testCommentNotAtStart() {
        // The broken pattern (no MULTILINE) would return [] here
        // because "code" doesn't start with "#" and ^ only matches position 0
        String text = "code line\n# this is a comment\nmore code";
        List<String> result = solution.extractCommentLines(text);
        assertEquals(1, result.size(),
            "Should find the comment on line 2; broken pattern without MULTILINE finds 0");
        assertEquals("# this is a comment", result.get(0));
    }

    @Test
    @DisplayName("Comment lines with content are returned with full text")
    void testCommentContentPreserved() {
        String text = "# key = value\n# another line";
        List<String> result = solution.extractCommentLines(text);
        assertEquals(List.of("# key = value", "# another line"), result);
    }

    @Test
    @DisplayName("Line with # not at start is NOT treated as comment line")
    void testHashNotAtLineStart() {
        String text = "code # inline comment\n# real comment";
        List<String> result = solution.extractCommentLines(text);
        assertEquals(1, result.size(), "Only lines STARTING with # are comments");
        assertEquals("# real comment", result.get(0));
    }

    @Test
    @DisplayName("All lines are comments")
    void testAllLinesAreComments() {
        String text = "# line1\n# line2\n# line3";
        List<String> result = solution.extractCommentLines(text);
        assertEquals(3, result.size());
    }
}
