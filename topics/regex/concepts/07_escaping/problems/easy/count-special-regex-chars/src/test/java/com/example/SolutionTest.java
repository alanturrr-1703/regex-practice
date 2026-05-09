package com.example;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests for Solution.countSpecialChars().
 * All tests FAIL until the method is implemented.
 */
class SolutionTest {

    private final Solution solution = new Solution();

    @Test
    void testDotAndStar() {
        assertEquals(2, solution.countSpecialChars("a.b*c"));
    }

    @Test
    void testNoSpecialChars() {
        assertEquals(0, solution.countSpecialChars("hello"));
    }

    @Test
    void testDollarAndParens() {
        // $ ( ) are all metacharacters
        assertEquals(3, solution.countSpecialChars("$(test)"));
    }

    @Test
    void testEmptyString() {
        assertEquals(0, solution.countSpecialChars(""));
    }

    @Test
    void testSingleBackslash() {
        // "\\" in Java = one backslash character
        assertEquals(1, solution.countSpecialChars("\\"));
    }

    @Test
    void testAllMetacharacters() {
        // ".*+?^${}[]|()\\" in Java = 14 metacharacters (1 each)
        // The \\ at the end represents one literal backslash
        String allMeta = ".*+?^${}[]|()\\";
        assertEquals(14, solution.countSpecialChars(allMeta));
    }

    @Test
    void testMixedContent() {
        // "abc.def" has 1 metachar (the dot)
        assertEquals(1, solution.countSpecialChars("abc.def"));
    }

    @Test
    void testMultipleOfSameChar() {
        // "..." has 3 dots, each counts
        assertEquals(3, solution.countSpecialChars("..."));
    }

    @Test
    void testPipeAndBrackets() {
        // | [ ] are metacharacters
        assertEquals(3, solution.countSpecialChars("[a|b]"));
    }

    @Test
    void testOnlyLettersAndDigits() {
        assertEquals(0, solution.countSpecialChars("abc123XYZ"));
    }
}
