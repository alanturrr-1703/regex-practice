package com.example;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests for the replace-with-custom-logic problem.
 *
 * ALL tests will fail until Solution.doubleNumbers() is implemented.
 */
class SolutionTest {

    private Solution solution;

    @BeforeEach
    void setUp() {
        solution = new Solution();
    }

    @Test
    @DisplayName("Doubles a single digit: 3 -> 6")
    void testSingleDigit() {
        assertEquals("I have 6 cats", solution.doubleNumbers("I have 3 cats"));
    }

    @Test
    @DisplayName("Zero doubled is still zero")
    void testZeroDoubledIsZero() {
        assertEquals("0 items", solution.doubleNumbers("0 items"));
    }

    @Test
    @DisplayName("Doubles multiple numbers in a string")
    void testMultipleNumbers() {
        assertEquals("200 and 400", solution.doubleNumbers("100 and 200"));
    }

    @Test
    @DisplayName("String with no numbers is returned unchanged")
    void testNoNumbers() {
        assertEquals("no numbers", solution.doubleNumbers("no numbers"));
    }

    @Test
    @DisplayName("Doubles two numbers: 5 -> 10, 50 -> 100")
    void testMixedSmallAndLarge() {
        assertEquals("mixed 10 and 100", solution.doubleNumbers("mixed 5 and 50"));
    }

    @Test
    @DisplayName("Empty string returns empty string")
    void testEmptyInput() {
        assertEquals("", solution.doubleNumbers(""));
    }

    @Test
    @DisplayName("Large number: 1000000000 -> 2000000000")
    void testLargeNumber() {
        assertEquals("2000000000", solution.doubleNumbers("1000000000"));
    }

    @Test
    @DisplayName("Number adjacent to letters is still doubled")
    void testNumberAdjacentToLetters() {
        assertEquals("6cats", solution.doubleNumbers("3cats"));
    }

    @Test
    @DisplayName("Text before and after numbers is preserved exactly")
    void testTextPreservedAroundNumbers() {
        String result = solution.doubleNumbers("start 7 middle 9 end");
        assertEquals("start 14 middle 18 end", result);
    }

    @Test
    @DisplayName("Single character that is a digit")
    void testSingleCharacterDigit() {
        assertEquals("4", solution.doubleNumbers("2"));
    }
}
