package com.example;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * Tests for Solution.extractBlockComments().
 * All tests FAIL until the method is implemented.
 */
class SolutionTest {

    private final Solution solution = new Solution();

    @Test
    void testSingleLineComment() {
        List<String> result = solution.extractBlockComments("code /* comment */ more");
        assertEquals(Collections.singletonList(" comment "), result);
    }

    @Test
    void testTwoComments() {
        List<String> result = solution.extractBlockComments("/* first */ code /* second */");
        assertEquals(Arrays.asList(" first ", " second "), result);
    }

    @Test
    void testMultilineComment() {
        String code = "/* line1\nline2 */";
        List<String> result = solution.extractBlockComments(code);
        assertEquals(Collections.singletonList(" line1\nline2 "), result);
    }

    @Test
    void testNoComments() {
        List<String> result = solution.extractBlockComments("no comments here");
        assertEquals(Collections.emptyList(), result);
    }

    @Test
    void testEmptyComment() {
        List<String> result = solution.extractBlockComments("/**/");
        assertEquals(Collections.singletonList(""), result);
    }

    @Test
    void testEmptyInput() {
        List<String> result = solution.extractBlockComments("");
        assertEquals(Collections.emptyList(), result);
    }

    @Test
    void testMultilineWithTwoComments() {
        String code = "/* first\ncomment */\ncode\n/* second */";
        List<String> result = solution.extractBlockComments(code);
        assertEquals(Arrays.asList(" first\ncomment ", " second "), result);
    }

    @Test
    void testLazyStopsAtFirstClosing() {
        // Lazy should stop at first */ and not span to a later one
        // If greedy were used: /* a */ b /* c */ would return " a */ b /* c "
        String code = "/* a */ b /* c */";
        List<String> result = solution.extractBlockComments(code);
        assertEquals(Arrays.asList(" a ", " c "), result);
    }
}
