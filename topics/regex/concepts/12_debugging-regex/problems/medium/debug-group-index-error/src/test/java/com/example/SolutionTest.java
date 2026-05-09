package com.example;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests for debug-group-index-error.
 *
 * ALL tests will fail until Solution.extractYear() is implemented correctly.
 *
 * Key test: testReturnsOnlyYear verifies group(1) is used, not group(0).
 * If group(0) is used, the result would be "2024-01-15", not "2024".
 */
class SolutionTest {

    private Solution solution;

    @BeforeEach
    void setUp() {
        solution = new Solution();
    }

    @Test
    @DisplayName("Returns only the 4-digit year, not the full date (verifies group(1) not group(0))")
    void testReturnsOnlyYear() {
        Optional<String> result = solution.extractYear("2024-01-15");
        assertTrue(result.isPresent());
        assertEquals("2024", result.get(),
            "Should return only '2024'; if returns '2024-01-15', group(0) was used instead of group(1)");
    }

    @Test
    @DisplayName("Returns empty Optional when input has no date")
    void testNoDatReturnsEmpty() {
        Optional<String> result = solution.extractYear("no date");
        assertTrue(result.isEmpty(), "Should return Optional.empty() when no date found");
    }

    @Test
    @DisplayName("Finds date embedded in a sentence and returns year")
    void testDateEmbeddedInSentence() {
        Optional<String> result = solution.extractYear("2023-12-31 event");
        assertTrue(result.isPresent());
        assertEquals("2023", result.get());
    }

    @Test
    @DisplayName("Empty input returns empty Optional")
    void testEmptyInput() {
        Optional<String> result = solution.extractYear("");
        assertTrue(result.isEmpty());
    }

    @Test
    @DisplayName("Different year values are correctly extracted")
    void testDifferentYears() {
        assertEquals(Optional.of("1999"), solution.extractYear("1999-06-15 midnight"));
        assertEquals(Optional.of("2000"), solution.extractYear("2000-01-01"));
        assertEquals(Optional.of("2024"), solution.extractYear("event on 2024-11-30"));
    }

    @Test
    @DisplayName("Result is exactly 4 characters (year only)")
    void testResultLengthIsFour() {
        Optional<String> result = solution.extractYear("2024-03-22");
        assertTrue(result.isPresent());
        assertEquals(4, result.get().length(),
            "Year should be exactly 4 characters; group(0) would give 10 characters");
    }

    @Test
    @DisplayName("Returns first date found when multiple dates present")
    void testFirstDateWhenMultiple() {
        Optional<String> result = solution.extractYear("from 2020-01-01 to 2021-12-31");
        assertTrue(result.isPresent());
        assertEquals("2020", result.get(), "Should return year of the first date found");
    }
}
