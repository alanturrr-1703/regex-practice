package com.example;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Timeout;
import java.time.Duration;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests for Optimize Email Validator.
 *
 * All tests MUST fail until Solution.isValidEmail() is implemented.
 *
 * The performance test verifies that the adversarial input "aaaaaaaaaaaaaaaaaaaaa@"
 * does NOT cause catastrophic backtracking (completes within 500ms).
 */
class SolutionTest {

    private final Solution solution = new Solution();

    // ------------------------------------------------------------------ valid emails

    @Test
    @DisplayName("Standard email → true")
    void testStandardEmail() {
        assertTrue(solution.isValidEmail("user@example.com"));
    }

    @Test
    @DisplayName("Email with subdomains → true")
    void testSubdomainEmail() {
        assertTrue(solution.isValidEmail("user.name+tag@example.co.uk"));
    }

    @Test
    @DisplayName("Email with hyphen in domain → true")
    void testHyphenDomain() {
        assertTrue(solution.isValidEmail("user@my-domain.org"));
    }

    @Test
    @DisplayName("Email with numeric local part → true")
    void testNumericLocal() {
        assertTrue(solution.isValidEmail("123@example.com"));
    }

    // ------------------------------------------------------------------ invalid emails

    @Test
    @DisplayName("No @ symbol → false")
    void testNoAtSymbol() {
        assertFalse(solution.isValidEmail("bad-email"));
    }

    @Test
    @DisplayName("Starts with @ → false")
    void testStartsWithAt() {
        assertFalse(solution.isValidEmail("@nodomain"));
    }

    @Test
    @DisplayName("Dot immediately after @ → false")
    void testDotAfterAt() {
        assertFalse(solution.isValidEmail("user@.com"));
    }

    @Test
    @DisplayName("Empty string → false")
    void testEmpty() {
        assertFalse(solution.isValidEmail(""));
    }

    @Test
    @DisplayName("Null → false")
    void testNull() {
        assertFalse(solution.isValidEmail(null));
    }

    @Test
    @DisplayName("No TLD → false")
    void testNoTld() {
        assertFalse(solution.isValidEmail("user@example"));
    }

    @Test
    @DisplayName("Input over 254 chars → false")
    void testTooLong() {
        String longEmail = "a".repeat(250) + "@b.com";
        assertFalse(solution.isValidEmail(longEmail),
                "Emails over 254 chars should be rejected before matching");
    }

    // ------------------------------------------------------------------ performance test

    @Test
    @DisplayName("Adversarial input completes within 500ms (no ReDoS)")
    void testAdversarialInputIsNonCatastrophic() {
        // Adversarial: many 'a's followed by '@' with no domain
        // This would hang for seconds on the SLOW_PATTERN
        String adversarial = "a".repeat(50) + "@";
        assertTimeout(Duration.ofMillis(500), () -> {
            boolean result = solution.isValidEmail(adversarial);
            assertFalse(result, "Adversarial input is not a valid email");
        }, "Email validation must complete within 500ms — possible ReDoS in your pattern!");
    }

    @Test
    @DisplayName("Performance: 1000 valid emails validated quickly")
    @Timeout(2)  // 2 seconds for 1000 validations
    void testPerformanceBulk() {
        for (int i = 0; i < 1000; i++) {
            solution.isValidEmail("user" + i + "@example.com");
        }
    }
}
