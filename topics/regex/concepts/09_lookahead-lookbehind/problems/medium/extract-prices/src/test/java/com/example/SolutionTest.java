package com.example;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests for Extract Prices.
 *
 * All tests MUST fail until Solution.extractAmounts() is implemented.
 */
class SolutionTest {

    private final Solution solution = new Solution();

    @Test
    @DisplayName("Mixed dollar and euro amounts")
    void testMixedCurrencies() {
        List<String> result = solution.extractAmounts("total: $19.99 and €25.00");
        assertEquals(List.of("19.99", "25.00"), result);
    }

    @Test
    @DisplayName("No currency → empty list")
    void testNoCurrency() {
        List<String> result = solution.extractAmounts("no currency here");
        assertTrue(result.isEmpty(), "Should return empty list when no currency found");
    }

    @Test
    @DisplayName("Dollar only, integer amount")
    void testDollarInteger() {
        List<String> result = solution.extractAmounts("$100 off");
        assertEquals(List.of("100"), result);
    }

    @Test
    @DisplayName("Euro with space — no match")
    void testEuroWithSpace() {
        List<String> result = solution.extractAmounts("€ 50 off");
        assertTrue(result.isEmpty(), "Space between € and 50 should prevent match");
    }

    @Test
    @DisplayName("Multiple dollar amounts")
    void testMultipleDollars() {
        List<String> result = solution.extractAmounts("$10 and $20 and $30");
        assertEquals(List.of("10", "20", "30"), result);
    }

    @Test
    @DisplayName("Null input → empty list")
    void testNull() {
        List<String> result = solution.extractAmounts(null);
        assertNotNull(result, "Result should never be null");
        assertTrue(result.isEmpty(), "Null input should return empty list");
    }

    @Test
    @DisplayName("Zero amount")
    void testZeroAmount() {
        List<String> result = solution.extractAmounts("$0");
        assertEquals(List.of("0"), result);
    }

    @Test
    @DisplayName("Large number without decimal")
    void testLargeNumber() {
        List<String> result = solution.extractAmounts("€1000000");
        assertEquals(List.of("1000000"), result);
    }

    @Test
    @DisplayName("Euro decimal and dollar decimal together")
    void testEuroAndDollarDecimals() {
        List<String> result = solution.extractAmounts("€0.99 and $1234.56");
        assertEquals(List.of("0.99", "1234.56"), result);
    }

    @Test
    @DisplayName("Currency symbol at end with no digits → no match")
    void testSymbolAtEnd() {
        List<String> result = solution.extractAmounts("price is $");
        assertTrue(result.isEmpty(), "Symbol with no following digits should not match");
    }

    @Test
    @DisplayName("Currency symbol NOT included in result")
    void testSymbolNotInResult() {
        List<String> result = solution.extractAmounts("$42.99");
        assertEquals(1, result.size());
        assertFalse(result.get(0).contains("$"), "Result must not contain currency symbol");
        assertEquals("42.99", result.get(0));
    }
}
