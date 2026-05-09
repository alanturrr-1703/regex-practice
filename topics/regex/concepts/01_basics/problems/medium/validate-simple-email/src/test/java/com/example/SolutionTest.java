package com.example;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests for the validate-simple-email problem.
 * All tests FAIL until Solution.isValidEmail() is implemented.
 */
class SolutionTest {

    private Solution solution;

    @BeforeEach
    void setUp() {
        solution = new Solution();
    }

    @Test
    void testStandardValidEmail() {
        assertTrue(solution.isValidEmail("user@example.com"));
    }

    @Test
    void testEmptyDomainAfterAtIsInvalid() {
        assertFalse(solution.isValidEmail("bad@"));
    }

    @Test
    void testEmptyLocalPartIsInvalid() {
        assertFalse(solution.isValidEmail("@domain.com"));
    }

    @Test
    void testTldWithOnlyOneLetterIsInvalid() {
        // TLD minimum is 2 letters
        assertFalse(solution.isValidEmail("user@domain.c"));
    }

    @Test
    void testTwoLetterTldIsValid() {
        assertTrue(solution.isValidEmail("user@domain.io"));
    }

    @Test
    void testSixLetterTldIsValid() {
        assertTrue(solution.isValidEmail("user@domain.museum"));
    }

    @Test
    void testSevenLetterTldIsInvalid() {
        // "toolong" has 7 letters — exceeds maximum of 6
        assertFalse(solution.isValidEmail("user@domain.toolong"));
    }

    @Test
    void testPlainAddressWithoutAtIsInvalid() {
        assertFalse(solution.isValidEmail("plainaddress"));
    }

    @Test
    void testEmptyStringIsInvalid() {
        assertFalse(solution.isValidEmail(""));
    }

    @Test
    void testTldWithDigitIsInvalid() {
        // TLD must be letters only — digit makes it invalid
        assertFalse(solution.isValidEmail("user@domain.c0m"));
    }

    @Test
    void testNoDotInDomainIsInvalid() {
        assertFalse(solution.isValidEmail("user@domaincom"));
    }

    @Test
    void testSubdomainIsValid() {
        // Domain part can contain dots (subdomains) — [^@\s]+ allows dots
        assertTrue(solution.isValidEmail("user@sub.domain.com"));
    }

    @Test
    void testWhitespaceInEmailIsInvalid() {
        assertFalse(solution.isValidEmail("user @domain.com"));
    }
}
