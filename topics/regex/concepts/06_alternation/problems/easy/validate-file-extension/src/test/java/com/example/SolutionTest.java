package com.example;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests for {@link Solution#isSourceFile(String)}.
 *
 * <p>All tests will fail with {@link UnsupportedOperationException} until the
 * method is implemented. That is intentional — make the tests pass.</p>
 */
class SolutionTest {

    private final Solution solution = new Solution();

    // -----------------------------------------------------------------------
    // Matching cases
    // -----------------------------------------------------------------------

    @Test
    @DisplayName(".java extension matches")
    void testJava() {
        assertTrue(solution.isSourceFile("Main.java"));
    }

    @Test
    @DisplayName(".py extension matches")
    void testPy() {
        assertTrue(solution.isSourceFile("script.py"));
    }

    @Test
    @DisplayName(".py uppercase matches (case-insensitive)")
    void testPyUppercase() {
        assertTrue(solution.isSourceFile("SCRIPT.PY"),
            "Extension matching should be case-insensitive");
    }

    @Test
    @DisplayName(".ts extension matches")
    void testTs() {
        assertTrue(solution.isSourceFile("app.ts"));
    }

    @Test
    @DisplayName(".go extension matches")
    void testGo() {
        assertTrue(solution.isSourceFile("mod.go"));
    }

    @Test
    @DisplayName(".js uppercase matches")
    void testJsUppercase() {
        assertTrue(solution.isSourceFile("bundle.JS"));
    }

    @Test
    @DisplayName(".JAVA uppercase matches")
    void testJavaUppercase() {
        assertTrue(solution.isSourceFile("Main.JAVA"));
    }

    // -----------------------------------------------------------------------
    // Non-matching cases
    // -----------------------------------------------------------------------

    @Test
    @DisplayName(".png extension does not match")
    void testPng() {
        assertFalse(solution.isSourceFile("image.png"));
    }

    @Test
    @DisplayName("'.javascript' is not '.js' (anchored to end of string)")
    void testJavascriptNotJs() {
        assertFalse(solution.isSourceFile("file.javascript"),
            ".javascript is longer than .js; the $ anchor must prevent this match");
    }

    @Test
    @DisplayName("No extension returns false")
    void testNoExtension() {
        assertFalse(solution.isSourceFile("noextension"));
    }

    @Test
    @DisplayName("Empty string returns false")
    void testEmptyString() {
        assertFalse(solution.isSourceFile(""));
    }

    @Test
    @DisplayName("Just a dot returns false")
    void testJustDot() {
        assertFalse(solution.isSourceFile("."));
    }
}
