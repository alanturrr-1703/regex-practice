package com.example;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests for the extract-hex-colors problem.
 * All tests FAIL until Solution.extractHexColors() is implemented.
 */
class SolutionTest {

    private Solution solution;

    @BeforeEach
    void setUp() {
        solution = new Solution();
    }

    @Test
    void testTwoHexColors() {
        List<String> result = solution.extractHexColors("color: #ff0000 and #ABCDEF");
        assertEquals(List.of("#ff0000", "#ABCDEF"), result);
    }

    @Test
    void testNoColorsReturnsEmptyList() {
        List<String> result = solution.extractHexColors("no colors here");
        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    void testNonHexCharsAfterHashNotMatched() {
        // x, y, z are not hex digits
        List<String> result = solution.extractHexColors("#xyz123");
        assertTrue(result.isEmpty());
    }

    @Test
    void testThreeDigitHexNotMatched() {
        // #fff has only 3 hex chars — not matched
        List<String> result = solution.extractHexColors("#fff");
        assertTrue(result.isEmpty());
    }

    @Test
    void testHexColorEmbeddedInCSS() {
        List<String> result = solution.extractHexColors("background:#001122;");
        assertEquals(List.of("#001122"), result);
    }

    @Test
    void testEmptyInputReturnsEmptyList() {
        List<String> result = solution.extractHexColors("");
        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    void testAllDigitsColor() {
        List<String> result = solution.extractHexColors("#123456");
        assertEquals(List.of("#123456"), result);
    }

    @Test
    void testAllLettersColor() {
        // All lowercase hex letters
        List<String> result = solution.extractHexColors("#abcdef");
        assertEquals(List.of("#abcdef"), result);
    }

    @Test
    void testMixedCaseColorPreserved() {
        // Case of the original text should be preserved in the result
        List<String> result = solution.extractHexColors("#AbCdEf");
        assertEquals(List.of("#AbCdEf"), result);
    }

    @Test
    void testMultipleColorsOnSameLine() {
        List<String> result = solution.extractHexColors("#ff0000 #00ff00 #0000ff");
        assertEquals(List.of("#ff0000", "#00ff00", "#0000ff"), result);
    }

    @Test
    void testHashAloneNotMatched() {
        List<String> result = solution.extractHexColors("# and #12 and ##");
        assertTrue(result.isEmpty());
    }

    @Test
    void testOrderPreserved() {
        List<String> result = solution.extractHexColors("first:#aabbcc; second:#112233;");
        assertEquals(List.of("#aabbcc", "#112233"), result);
    }
}
