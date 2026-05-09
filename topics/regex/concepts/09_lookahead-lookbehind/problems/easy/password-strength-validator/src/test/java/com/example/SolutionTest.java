package com.example;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests for Password Strength Validator.
 *
 * All tests MUST fail until Solution.isStrongPassword() is implemented.
 */
class SolutionTest {

    private final Solution solution = new Solution();

    // ------------------------------------------------------------------ valid passwords

    @Test
    @DisplayName("Secure1! — meets all 5 criteria")
    void testValidPassword_Secure1() {
        assertTrue(solution.isStrongPassword("Secure1!"),
                "Secure1! should be a strong password");
    }

    @Test
    @DisplayName("ValidPass1@ — meets all 5 criteria")
    void testValidPassword_ValidPass1() {
        assertTrue(solution.isStrongPassword("ValidPass1@"),
                "ValidPass1@ should be a strong password");
    }

    @Test
    @DisplayName("Exactly 8 chars with all required types")
    void testValidPassword_ExactlyEightChars() {
        assertTrue(solution.isStrongPassword("Secure1!"),
                "Exactly 8 characters with all types should pass");
    }

    // ------------------------------------------------------------------ missing uppercase

    @Test
    @DisplayName("secure1! — no uppercase letter → false")
    void testInvalid_NoUppercase() {
        assertFalse(solution.isStrongPassword("secure1!"),
                "Password without uppercase should fail");
    }

    // ------------------------------------------------------------------ missing lowercase

    @Test
    @DisplayName("SECURE1! — no lowercase letter → false")
    void testInvalid_NoLowercase() {
        assertFalse(solution.isStrongPassword("SECURE1!"),
                "Password without lowercase should fail");
    }

    // ------------------------------------------------------------------ missing digit

    @Test
    @DisplayName("SecurePass! — no digit → false")
    void testInvalid_NoDigit() {
        assertFalse(solution.isStrongPassword("SecurePass!"),
                "Password without digit should fail");
    }

    // ------------------------------------------------------------------ missing special char

    @Test
    @DisplayName("Secure123 — no special character → false")
    void testInvalid_NoSpecialChar() {
        assertFalse(solution.isStrongPassword("Secure123"),
                "Password without special character should fail");
    }

    // ------------------------------------------------------------------ too short

    @Test
    @DisplayName("Sec1! — only 5 chars → false")
    void testInvalid_TooShort() {
        assertFalse(solution.isStrongPassword("Sec1!"),
                "Password with fewer than 8 characters should fail");
    }

    @Test
    @DisplayName("7-char password with all types → false")
    void testInvalid_SevenChars() {
        assertFalse(solution.isStrongPassword("Secur1!"),
                "7-character password should fail (need at least 8)");
    }

    // ------------------------------------------------------------------ edge cases

    @Test
    @DisplayName("Null input → false (no NPE)")
    void testNull() {
        assertFalse(solution.isStrongPassword(null),
                "Null input should return false without throwing");
    }

    @Test
    @DisplayName("Empty string → false")
    void testEmpty() {
        assertFalse(solution.isStrongPassword(""),
                "Empty string should return false");
    }

    @Test
    @DisplayName("All digits, long → false")
    void testAllDigits() {
        assertFalse(solution.isStrongPassword("12345678"),
                "All digits (no letters or special) should fail");
    }

    @Test
    @DisplayName("Special char only: !@#$%^&* at various positions")
    void testSpecialCharAllowed() {
        assertTrue(solution.isStrongPassword("Passw0r*"),
                "* is a valid special character");
        assertTrue(solution.isStrongPassword("!Passw0r"),
                "! at start is valid");
        assertTrue(solution.isStrongPassword("Passw0r@"),
                "@ at end is valid");
    }

    @Test
    @DisplayName("Long valid password with many specials")
    void testLongValidPassword() {
        assertTrue(solution.isStrongPassword("MyS3cur3P@ssw0rd!"),
                "Long complex password should pass");
    }
}
