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
    // Core functionality
    // -----------------------------------------------------------------------

    @Test
    @DisplayName("Two dates embedded in sentence text")
    void testTwoDatesInText() {
        List<Solution.DateParts> result =
                solution.extractDates("Event on 2024-01-15 and 2023-12-31");

        assertEquals(2, result.size(), "Should find exactly two dates");
        assertEquals(new Solution.DateParts("2024", "01", "15"), result.get(0));
        assertEquals(new Solution.DateParts("2023", "12", "31"), result.get(1));
    }

    @Test
    @DisplayName("Single date with surrounding punctuation")
    void testSingleDate() {
        List<Solution.DateParts> result =
                solution.extractDates("Born: 1990-07-04.");

        assertEquals(1, result.size());
        Solution.DateParts dp = result.get(0);
        assertEquals("1990", dp.year());
        assertEquals("07",   dp.month());
        assertEquals("04",   dp.day());
    }

    @Test
    @DisplayName("Out-of-range date values are still extracted (regex is structural)")
    void testOutOfRangeDateStillExtracted() {
        List<Solution.DateParts> result =
                solution.extractDates("2024-13-99 is out of range");

        assertEquals(1, result.size(), "Regex matches shape, not calendar validity");
        assertEquals(new Solution.DateParts("2024", "13", "99"), result.get(0));
    }

    @Test
    @DisplayName("Two adjacent dates separated by a space")
    void testAdjacentDates() {
        List<Solution.DateParts> result =
                solution.extractDates("2024-01-01 2024-02-02");

        assertEquals(2, result.size(), "Both dates should be found");
        assertEquals("2024", result.get(0).year());
        assertEquals("01",   result.get(0).month());
        assertEquals("01",   result.get(0).day());
        assertEquals("2024", result.get(1).year());
        assertEquals("02",   result.get(1).month());
        assertEquals("02",   result.get(1).day());
    }

    // -----------------------------------------------------------------------
    // Edge cases
    // -----------------------------------------------------------------------

    @Test
    @DisplayName("No dates in input returns empty list")
    void testNoDates() {
        List<Solution.DateParts> result = solution.extractDates("no dates here");
        assertTrue(result.isEmpty(), "Expected empty list when no dates present");
    }

    @Test
    @DisplayName("Empty string returns empty list")
    void testEmptyString() {
        List<Solution.DateParts> result = solution.extractDates("");
        assertTrue(result.isEmpty(), "Empty input should yield empty list");
    }

    @Test
    @DisplayName("Date at very start of string")
    void testDateAtStart() {
        List<Solution.DateParts> result =
                solution.extractDates("2000-06-15 was a Monday");

        assertEquals(1, result.size());
        assertEquals("2000", result.get(0).year());
    }

    @Test
    @DisplayName("Date at very end of string")
    void testDateAtEnd() {
        List<Solution.DateParts> result =
                solution.extractDates("The release date is 2025-03-22");

        assertEquals(1, result.size());
        assertEquals("2025", result.get(0).year());
        assertEquals("03",   result.get(0).month());
        assertEquals("22",   result.get(0).day());
    }

    @Test
    @DisplayName("group(0) would be full date; group(1) is year — verify field mapping")
    void testGroupSemantics() {
        List<Solution.DateParts> result =
                solution.extractDates("2024-01-15");

        assertEquals(1, result.size());
        // year must NOT be the full date string "2024-01-15"
        assertNotEquals("2024-01-15", result.get(0).year(),
            "year() should be group(1) '2024', not group(0) '2024-01-15'");
        assertEquals("2024", result.get(0).year());
    }
}
