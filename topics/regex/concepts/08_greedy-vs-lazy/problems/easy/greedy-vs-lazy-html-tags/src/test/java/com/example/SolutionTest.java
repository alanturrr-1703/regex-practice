package com.example;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * Tests for Solution.extractBoldContents().
 * All tests FAIL until the method is implemented.
 *
 * If you implement with greedy .*  instead of lazy .*? :
 * - testTwoPairs() will fail (returns 1 item instead of 2)
 * - testThreePairs() will fail (returns 1 item instead of 3)
 */
class SolutionTest {

    private final Solution solution = new Solution();

    @Test
    void testSinglePair() {
        assertEquals(
            Collections.singletonList("hello"),
            solution.extractBoldContents("<b>hello</b>")
        );
    }

    @Test
    void testTwoPairs() {
        // This test SPECIFICALLY catches the greedy bug
        assertEquals(
            Arrays.asList("one", "two"),
            solution.extractBoldContents("<b>one</b> and <b>two</b>")
        );
    }

    @Test
    void testNoBoldTags() {
        assertEquals(
            Collections.emptyList(),
            solution.extractBoldContents("no bold text here")
        );
    }

    @Test
    void testEmptyBoldContent() {
        assertEquals(
            Collections.singletonList(""),
            solution.extractBoldContents("<b></b>")
        );
    }

    @Test
    void testBoldWithInnerTag() {
        assertEquals(
            Collections.singletonList("with <i>inner</i> tag"),
            solution.extractBoldContents("<b>with <i>inner</i> tag</b>")
        );
    }

    @Test
    void testThreePairs() {
        assertEquals(
            Arrays.asList("a", "b", "c"),
            solution.extractBoldContents("<b>a</b><b>b</b><b>c</b>")
        );
    }

    @Test
    void testEmptyInput() {
        assertEquals(
            Collections.emptyList(),
            solution.extractBoldContents("")
        );
    }

    @Test
    void testBoldWithSpaces() {
        assertEquals(
            Collections.singletonList("  spaces  "),
            solution.extractBoldContents("<b>  spaces  </b>")
        );
    }
}
