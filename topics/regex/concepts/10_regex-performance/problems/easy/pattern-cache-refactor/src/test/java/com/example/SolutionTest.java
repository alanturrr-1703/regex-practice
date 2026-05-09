package com.example;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests for Pattern Cache Refactor.
 *
 * All tests MUST fail until Solution.filterPhoneNumbers() is implemented.
 */
class SolutionTest {

    private final Solution solution = new Solution();

    @Test
    @DisplayName("Mixed valid and invalid strings — returns only matches")
    void testMixedInput() {
        List<String> input = Arrays.asList("123-4567", "abc-defg", "999-0000", "12-345");
        assertEquals(Arrays.asList("123-4567", "999-0000"), solution.filterPhoneNumbers(input));
    }

    @Test
    @DisplayName("Empty list → empty result")
    void testEmptyList() {
        assertTrue(solution.filterPhoneNumbers(Collections.emptyList()).isEmpty());
    }

    @Test
    @DisplayName("Single non-matching string → empty result")
    void testSingleNonMatch() {
        assertEquals(Collections.emptyList(),
                solution.filterPhoneNumbers(Collections.singletonList("bad")));
    }

    @Test
    @DisplayName("Null input → empty result")
    void testNullInput() {
        List<String> result = solution.filterPhoneNumbers(null);
        assertNotNull(result, "Result must never be null");
        assertTrue(result.isEmpty());
    }

    @Test
    @DisplayName("All zeros — valid phone format")
    void testAllZeros() {
        assertEquals(Collections.singletonList("000-0000"),
                solution.filterPhoneNumbers(Collections.singletonList("000-0000")));
    }

    @Test
    @DisplayName("Wrong digit counts — no matches")
    void testWrongDigitCounts() {
        List<String> input = Arrays.asList("123-456", "1234-567", "12-4567", "1234-45678");
        assertTrue(solution.filterPhoneNumbers(input).isEmpty(),
                "None of these match \\d{3}-\\d{4}");
    }

    @Test
    @DisplayName("Extra chars appended — no match (matches() requires full string)")
    void testExtraChars() {
        List<String> input = Arrays.asList("123-45678", "x123-4567", "123-4567x");
        assertTrue(solution.filterPhoneNumbers(input).isEmpty(),
                "Extra characters should prevent matching");
    }

    @Test
    @DisplayName("Order preserved")
    void testOrderPreserved() {
        List<String> input = Arrays.asList("999-9999", "111-1111", "555-5555");
        assertEquals(Arrays.asList("999-9999", "111-1111", "555-5555"),
                solution.filterPhoneNumbers(input),
                "Order must match the input list order");
    }

    @Test
    @DisplayName("All valid phone numbers")
    void testAllValid() {
        List<String> input = Arrays.asList("123-4567", "987-6543", "000-1111");
        assertEquals(input, solution.filterPhoneNumbers(input));
    }

    @Test
    @DisplayName("No dash → no match")
    void testNoDash() {
        assertFalse(solution.filterPhoneNumbers(Collections.singletonList("1234567")).contains("1234567"),
                "Number without hyphen should not match");
    }
}
