package com.example;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

class SolutionTest {

    private final Solution solution = new Solution();

    @Test
    void testTwoBracketedValues() {
        assertEquals(Arrays.asList("123", "456"), solution.extractBracketedValues("[123][456]"));
    }

    @Test
    void testSingleBracket() {
        assertEquals(Collections.singletonList("hello"), solution.extractBracketedValues("[hello]"));
    }

    @Test
    void testNoBrackets() {
        assertEquals(Collections.emptyList(), solution.extractBracketedValues("no brackets"));
    }

    @Test
    void testThreeBrackets() {
        assertEquals(Arrays.asList("a", "b", "c"), solution.extractBracketedValues("[a][b][c]"));
    }

    @Test
    void testEmptyBracket() {
        assertEquals(Collections.singletonList(""), solution.extractBracketedValues("[]"));
    }

    @Test
    void testEmptyInput() {
        assertEquals(Collections.emptyList(), solution.extractBracketedValues(""));
    }

    @Test
    void testBracketInText() {
        assertEquals(Collections.singletonList("value"), solution.extractBracketedValues("text [value] more"));
    }

    @Test
    void testMultipleBracketsWithText() {
        assertEquals(Arrays.asList("one", "two"), solution.extractBracketedValues("[one] and [two]"));
    }
}
