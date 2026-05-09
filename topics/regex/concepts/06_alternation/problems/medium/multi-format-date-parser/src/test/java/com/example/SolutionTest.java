package com.example;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests for {@link Solution#extractDates(String)}.
 *
 * <p>All tests will fail with {@link UnsupportedOperationException} until the
 * method is implemented. That is intentional — make the tests pass.</p>
 */
class SolutionTest {

    private final Solution solution = new Solution();

    // -----------------------------------------------------------------------
    // Format 1: YYYY-MM-DD
    // -----------------------------------------------------------------------

    @Test
    @DisplayName("Format 1 (YYYY-MM-DD) extracted correctly")
    void testFormat1() {
        List<String> result = solution.extractDates("Scheduled: 2024-01-15");
        assertEquals(List.of("2024-01-15"), result);
    }

    // -----------------------------------------------------------------------
    // Format 2: MM/DD/YYYY
    // -----------------------------------------------------------------------

    @Test
    @DisplayName("Format 2 (MM/DD/YYYY) extracted correctly")
    void testFormat2() {
        List<String> result = solution.extractDates("Due: 01/15/2024");
        assertEquals(List.of("01/15/2024"), result);
    }

    // -----------------------------------------------------------------------
    // Format 3: DD Mon YYYY
    // -----------------------------------------------------------------------

    @Test
    @DisplayName("Format 3 (DD Mon YYYY) extracted correctly")
    void testFormat3() {
        List<String> result = solution.extractDates("Meeting on 15 Jan 2024");
        assertEquals(List.of("15 Jan 2024"), result);
    }

    @Test
    @DisplayName("Format 3 with two-digit day")
    void testFormat3TwoDigitDay() {
        List<String> result = solution.extractDates("Born 04 Dec 1990");
        assertEquals(List.of("04 Dec 1990"), result);
    }

    // -----------------------------------------------------------------------
    // Mixed formats in one string
    // -----------------------------------------------------------------------

    @Test
    @DisplayName("All three formats in one string — all extracted")
    void testAllThreeFormats() {
        String input = "Formats: 2024-01-15 and 01/15/2024 and 15 Jan 2024";
        List<String> result = solution.extractDates(input);

        assertEquals(3, result.size(), "All three date formats should be found");
        assertTrue(result.contains("2024-01-15"), "Format 1 not found");
        assertTrue(result.contains("01/15/2024"), "Format 2 not found");
        assertTrue(result.contains("15 Jan 2024"), "Format 3 not found");
    }

    @Test
    @DisplayName("Two format-1 dates and one format-2 date")
    void testMixedFormat1And2() {
        List<String> result = solution.extractDates("From 2024-01-01 to 2024-03-31, filed 03/31/2024");
        assertEquals(3, result.size());
    }

    // -----------------------------------------------------------------------
    // No-match cases
    // -----------------------------------------------------------------------

    @Test
    @DisplayName("No dates in input returns empty list")
    void testNoDates() {
        List<String> result = solution.extractDates("no dates here");
        assertTrue(result.isEmpty());
    }

    @Test
    @DisplayName("Empty string returns empty list")
    void testEmptyString() {
        List<String> result = solution.extractDates("");
        assertTrue(result.isEmpty());
    }

    // -----------------------------------------------------------------------
    // Return raw matched text
    // -----------------------------------------------------------------------

    @Test
    @DisplayName("Returned strings are the raw matched text, not reformatted")
    void testRawMatchedText() {
        List<String> result = solution.extractDates("See 15 Mar 2023 for details");
        assertEquals(1, result.size());
        // The result should be the exact substring from the input
        assertEquals("15 Mar 2023", result.get(0));
    }
}
