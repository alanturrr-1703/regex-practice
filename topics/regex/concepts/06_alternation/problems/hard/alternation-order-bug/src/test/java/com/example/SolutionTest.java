package com.example;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests for {@link Solution#extractAmounts(String)}.
 *
 * <p>All tests verify that the FIXED pattern correctly captures full decimal amounts
 * (e.g., {@code "$10.50"}) rather than truncated integer amounts (e.g., {@code "$10"}).</p>
 *
 * <p>All tests will fail with {@link UnsupportedOperationException} until the
 * method is implemented. That is intentional — make the tests pass.</p>
 */
class SolutionTest {

    private final Solution solution = new Solution();

    // -----------------------------------------------------------------------
    // Core: decimal amounts must be captured in full
    // -----------------------------------------------------------------------

    @Test
    @DisplayName("Decimal followed by integer — both captured correctly")
    void testDecimalThenInteger() {
        List<String> result = solution.extractAmounts("$10.50 and $10");

        assertEquals(2, result.size());
        assertEquals("$10.50", result.get(0),
            "Decimal amount must be captured fully; broken order gives '$10' instead");
        assertEquals("$10", result.get(1));
    }

    @Test
    @DisplayName("Euro decimal amount captured in full")
    void testEuroDecimal() {
        List<String> result = solution.extractAmounts("\u20ac20.99 total");

        assertEquals(1, result.size());
        assertEquals("\u20ac20.99", result.get(0),
            "Euro decimal amount must be fully captured");
    }

    @Test
    @DisplayName("Dollar decimal $0.01 — minimum decimal value")
    void testDollarCentAmount() {
        List<String> result = solution.extractAmounts("$0.01");

        assertEquals(1, result.size());
        assertEquals("$0.01", result.get(0));
    }

    @Test
    @DisplayName("Mixed: euro integer and dollar decimal")
    void testMixedCurrencies() {
        List<String> result = solution.extractAmounts("\u20ac5 and $100.00");

        assertEquals(2, result.size());
        assertEquals("\u20ac5",    result.get(0));
        assertEquals("$100.00", result.get(1));
    }

    // -----------------------------------------------------------------------
    // The ordering bug demonstration
    // -----------------------------------------------------------------------

    @Test
    @DisplayName("'$10.50' must return '$10.50', not '$10' (ordering bug check)")
    void testOrderingBugFixed() {
        List<String> result = solution.extractAmounts("price is $10.50");

        assertEquals(1, result.size());
        // If BROKEN_PATTERN is used, this would be "$10" — the bug
        assertEquals("$10.50", result.get(0),
            "The FIXED pattern must return '$10.50'. " +
            "If '$10' is returned, the BROKEN pattern is being used (integer alternative comes first).");
    }

    // -----------------------------------------------------------------------
    // No-match cases
    // -----------------------------------------------------------------------

    @Test
    @DisplayName("No currency amounts returns empty list")
    void testNoAmounts() {
        List<String> result = solution.extractAmounts("no money here");
        assertTrue(result.isEmpty());
    }

    @Test
    @DisplayName("Empty string returns empty list")
    void testEmptyString() {
        List<String> result = solution.extractAmounts("");
        assertTrue(result.isEmpty());
    }

    // -----------------------------------------------------------------------
    // Integer-only amounts (fallback branch)
    // -----------------------------------------------------------------------

    @Test
    @DisplayName("Integer-only amounts are still captured (fallback branch)")
    void testIntegerOnlyAmounts() {
        List<String> result = solution.extractAmounts("$5 and \u20ac10");

        assertEquals(2, result.size());
        assertEquals("$5",          result.get(0));
        assertEquals("\u20ac10", result.get(1));
    }
}
