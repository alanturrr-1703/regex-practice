package com.example;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests for {@link Solution#parseFields(String)}.
 *
 * These tests WILL FAIL until the method is implemented.
 */
@DisplayName("Parse Repeated Tokens")
class SolutionTest {

    private Solution solution;

    @BeforeEach
    void setUp() {
        solution = new Solution();
    }

    @Test
    @DisplayName("Three valid fields")
    void testThreeValidFields() {
        assertEquals(List.of("AAA", "BBB", "CCC"), solution.parseFields("AAA|BBB|CCC"));
    }

    @Test
    @DisplayName("Leading and trailing pipes are ignored")
    void testLeadingTrailingPipes() {
        assertEquals(List.of("HELLO"), solution.parseFields("|HELLO|"));
    }

    @Test
    @DisplayName("TOOLONGFIELD (12 chars) is invalid — only OK is extracted")
    void testTooLongFieldSkipped() {
        assertEquals(List.of("OK"), solution.parseFields("TOOLONGFIELD|OK"));
    }

    @Test
    @DisplayName("Lowercase field is invalid — only uppercase extracted")
    void testLowercaseFieldSkipped() {
        assertEquals(List.of("BBB"), solution.parseFields("aaa|BBB"));
    }

    @Test
    @DisplayName("Empty string returns empty list")
    void testEmptyString() {
        assertTrue(solution.parseFields("").isEmpty());
    }

    @Test
    @DisplayName("Null input returns empty list")
    void testNullInput() {
        List<String> result = solution.parseFields(null);
        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    @DisplayName("Short valid fields (1 char) are accepted")
    void testSingleLetterFields() {
        assertEquals(List.of("A", "BC", "DEF"), solution.parseFields("A|BC|DEF"));
    }

    @Test
    @DisplayName("Exactly 8 uppercase letters is valid")
    void testExactlyEightLetters() {
        assertEquals(List.of("ABCDEFGH"), solution.parseFields("ABCDEFGH"));
    }

    @Test
    @DisplayName("9 uppercase letters is invalid — no prefix match allowed")
    void testNineLettersInvalid() {
        assertTrue(solution.parseFields("ABCDEFGHI").isEmpty(),
                "A 9-character token must not match as an 8-character prefix");
    }

    @Test
    @DisplayName("8 valid and 9 invalid — only valid returned")
    void testEightValidNineInvalid() {
        assertEquals(List.of("ABCDEFGH"), solution.parseFields("ABCDEFGH|ABCDEFGHI"));
    }

    @Test
    @DisplayName("Only pipes — empty list")
    void testOnlyPipes() {
        assertTrue(solution.parseFields("|||").isEmpty());
    }

    @Test
    @DisplayName("Mixed case field is invalid")
    void testMixedCaseFieldSkipped() {
        assertEquals(List.of("UPPER"), solution.parseFields("mix3d|UPPER"));
    }

    @Test
    @DisplayName("Order of valid fields is preserved")
    void testOrderPreserved() {
        assertEquals(List.of("FIRST", "SECOND", "THIRD"),
                solution.parseFields("FIRST|invalid|SECOND|TOOLONGF123|THIRD"));
    }
}
