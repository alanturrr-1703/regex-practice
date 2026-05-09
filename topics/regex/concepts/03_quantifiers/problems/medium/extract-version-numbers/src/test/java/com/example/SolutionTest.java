package com.example;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests for {@link Solution#extractVersions(String)}.
 *
 * These tests WILL FAIL until the method is implemented.
 */
@DisplayName("Extract Version Numbers")
class SolutionTest {

    private Solution solution;

    @BeforeEach
    void setUp() {
        solution = new Solution();
    }

    @Test
    @DisplayName("Two valid versions in a sentence")
    void testTwoVersionsInSentence() {
        List<String> result = solution.extractVersions("version 1.2.3 and 10.0.5");
        assertEquals(List.of("1.2.3", "10.0.5"), result);
    }

    @Test
    @DisplayName("Version with suffix (dash) still matches")
    void testVersionWithDashSuffix() {
        List<String> result = solution.extractVersions("v2.0.0-beta");
        assertEquals(List.of("2.0.0"), result);
    }

    @Test
    @DisplayName("Four-component version (1.2.3.4) should return empty list")
    void testFourComponentVersionNotMatched() {
        List<String> result = solution.extractVersions("1.2.3.4");
        assertTrue(result.isEmpty(),
                "A four-component version like 1.2.3.4 should NOT match any version");
    }

    @Test
    @DisplayName("No version in string returns empty list")
    void testNoVersion() {
        List<String> result = solution.extractVersions("no version here");
        assertTrue(result.isEmpty());
    }

    @Test
    @DisplayName("Version at end of sentence")
    void testVersionAtEndOfSentence() {
        List<String> result = solution.extractVersions("release 3.14.1 done");
        assertEquals(List.of("3.14.1"), result);
    }

    @Test
    @DisplayName("Two versions at start and end of string")
    void testVersionsAtBothEnds() {
        List<String> result = solution.extractVersions("from 0.1.0 to 1.0.0");
        assertEquals(List.of("0.1.0", "1.0.0"), result);
    }

    @Test
    @DisplayName("Version preceded by 'v' prefix (not a digit)")
    void testVersionWithVPrefix() {
        List<String> result = solution.extractVersions("v1.0.0 and v2.3.4");
        assertEquals(List.of("1.0.0", "2.3.4"), result);
    }

    @Test
    @DisplayName("Component with 4+ digits should not match")
    void testFourDigitComponentNotMatched() {
        List<String> result = solution.extractVersions("1234.5.6");
        assertTrue(result.isEmpty(),
                "1234.5.6 should not match — major has 4 digits");
    }

    @Test
    @DisplayName("Empty string returns empty list")
    void testEmptyString() {
        List<String> result = solution.extractVersions("");
        assertTrue(result.isEmpty());
    }

    @Test
    @DisplayName("Null input returns empty list")
    void testNullInput() {
        List<String> result = solution.extractVersions(null);
        assertNotNull(result, "Should return empty list, not null");
        assertTrue(result.isEmpty());
    }

    @Test
    @DisplayName("Three-digit components are valid")
    void testThreeDigitComponents() {
        List<String> result = solution.extractVersions("app 123.456.789 released");
        // 456 and 789 are 3 digits — these exceed what's common but fit {1,3}
        // Wait — {1,3} allows max 3. 456 = 3 digits. Valid.
        assertEquals(List.of("123.456.789"), result);
    }

    @Test
    @DisplayName("Two-component version (1.2) does NOT match")
    void testTwoComponentVersionNotMatched() {
        List<String> result = solution.extractVersions("version 1.2 only");
        assertTrue(result.isEmpty(),
                "A two-component version (major.minor only) should not match");
    }
}
