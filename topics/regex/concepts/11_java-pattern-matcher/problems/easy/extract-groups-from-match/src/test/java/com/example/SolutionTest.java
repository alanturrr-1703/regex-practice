package com.example;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests for the extract-groups-from-match problem.
 *
 * ALL tests will fail until Solution.extractKeyValuePairs() is implemented.
 */
class SolutionTest {

    private Solution solution;

    @BeforeEach
    void setUp() {
        solution = new Solution();
    }

    @Test
    @DisplayName("Extracts two key=value pairs from a simple string")
    void testTwoPairs() {
        Map<String, String> result = solution.extractKeyValuePairs("name=Alice age=30");
        assertEquals("Alice", result.get("name"));
        assertEquals("30", result.get("age"));
        assertEquals(2, result.size());
    }

    @Test
    @DisplayName("Empty input returns empty map")
    void testEmptyInput() {
        Map<String, String> result = solution.extractKeyValuePairs("");
        assertTrue(result.isEmpty(), "Empty input should produce an empty map");
    }

    @Test
    @DisplayName("String with no key=value pairs returns empty map")
    void testNoPairs() {
        Map<String, String> result = solution.extractKeyValuePairs("no pairs here");
        assertTrue(result.isEmpty(), "No pairs should produce an empty map");
    }

    @Test
    @DisplayName("Extracts three pairs x=1 y=2 z=3")
    void testThreePairs() {
        Map<String, String> result = solution.extractKeyValuePairs("x=1 y=2 z=3");
        assertEquals("1", result.get("x"));
        assertEquals("2", result.get("y"));
        assertEquals("3", result.get("z"));
        assertEquals(3, result.size());
    }

    @Test
    @DisplayName("Non-pair tokens after a valid pair are ignored")
    void testExtraTokenIgnored() {
        Map<String, String> result = solution.extractKeyValuePairs("key=value extra");
        assertEquals("value", result.get("key"));
        assertEquals(1, result.size(), "Token 'extra' has no '=' so should be ignored");
    }

    @Test
    @DisplayName("Keys and values can contain underscores (word chars)")
    void testUnderscoresInKeyAndValue() {
        Map<String, String> result = solution.extractKeyValuePairs("my_key=my_value");
        assertEquals("my_value", result.get("my_key"));
    }

    @Test
    @DisplayName("Numeric values are returned as strings")
    void testNumericValue() {
        Map<String, String> result = solution.extractKeyValuePairs("count=42");
        assertEquals("42", result.get("count"));
    }

    @Test
    @DisplayName("a=b=c matches only a=b; the remaining =c is not a valid pair")
    void testChainedEqualsMatchesOnlyFirst() {
        Map<String, String> result = solution.extractKeyValuePairs("a=b=c");
        // a=b is matched; =c is not a valid key=value pair (no key before =)
        assertEquals("b", result.get("a"));
        assertFalse(result.containsKey("b"), "b should not be a key in the result");
    }

    @Test
    @DisplayName("Multiple spaces between pairs are handled transparently")
    void testMultipleSpaces() {
        Map<String, String> result = solution.extractKeyValuePairs("k1=v1   k2=v2");
        assertEquals("v1", result.get("k1"));
        assertEquals("v2", result.get("k2"));
    }
}
