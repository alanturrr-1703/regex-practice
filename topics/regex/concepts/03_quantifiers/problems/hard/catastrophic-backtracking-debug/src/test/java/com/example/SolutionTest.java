package com.example;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Timeout;

import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests for {@link Solution#matchesPattern(String)}.
 *
 * These tests WILL FAIL until the method is implemented.
 *
 * The critical test is {@link #testAdversarialInputCompletesQuickly()} —
 * it verifies that the implementation does NOT exhibit catastrophic backtracking
 * by asserting a 100ms timeout on a malicious input.
 */
@DisplayName("Catastrophic Backtracking Debug")
class SolutionTest {

    private Solution solution;

    @BeforeEach
    void setUp() {
        solution = new Solution();
    }

    // -----------------------------------------------------------------------
    // Positive cases — should return true
    // -----------------------------------------------------------------------

    @Test
    @DisplayName("Single 'a' followed by 'b' matches")
    void testSingleAFollowedByB() {
        assertTrue(solution.matchesPattern("ab"),
                "'ab' should match: one 'a' followed by 'b'");
    }

    @Test
    @DisplayName("Three 'a's followed by 'b' matches")
    void testThreeAsFollowedByB() {
        assertTrue(solution.matchesPattern("aaab"),
                "'aaab' should match: three 'a's followed by 'b'");
    }

    @Test
    @DisplayName("Two 'a's followed by 'b' matches")
    void testTwoAsFollowedByB() {
        assertTrue(solution.matchesPattern("aab"),
                "'aab' should match");
    }

    @Test
    @DisplayName("20 'a's followed by 'b' matches")
    void testTwentyAsFollowedByB() {
        assertTrue(solution.matchesPattern("aaaaaaaaaaaaaaaaaaab"),
                "20 'a's followed by 'b' should match");
    }

    // -----------------------------------------------------------------------
    // Negative cases — should return false
    // -----------------------------------------------------------------------

    @Test
    @DisplayName("'b' alone (no 'a') does not match")
    void testBAloneDoesNotMatch() {
        assertFalse(solution.matchesPattern("b"),
                "No 'a' before 'b' — should not match");
    }

    @Test
    @DisplayName("'aaa' (no trailing 'b') does not match")
    void testAsWithoutBDoesNotMatch() {
        assertFalse(solution.matchesPattern("aaa"),
                "No 'b' at end — should not match");
    }

    @Test
    @DisplayName("Empty string does not match")
    void testEmptyStringDoesNotMatch() {
        assertFalse(solution.matchesPattern(""),
                "Empty string should not match");
    }

    @Test
    @DisplayName("Null input does not match")
    void testNullDoesNotMatch() {
        assertFalse(solution.matchesPattern(null),
                "Null input should return false, not throw");
    }

    @Test
    @DisplayName("'aabb' (two b's) does not match")
    void testTwoBsDoesNotMatch() {
        assertFalse(solution.matchesPattern("aabb"),
                "Two 'b's at end — should not match");
    }

    @Test
    @DisplayName("'cab' (starts with 'c') does not match")
    void testStartsWithCDoesNotMatch() {
        assertFalse(solution.matchesPattern("cab"),
                "Starts with 'c' — not purely 'a's before 'b'");
    }

    @Test
    @DisplayName("'ba' (wrong order) does not match")
    void testWrongOrderDoesNotMatch() {
        assertFalse(solution.matchesPattern("ba"),
                "'b' before 'a' — wrong order, should not match");
    }

    // -----------------------------------------------------------------------
    // Performance test — verifies NO catastrophic backtracking
    // -----------------------------------------------------------------------

    @Test
    @Timeout(value = 100, unit = TimeUnit.MILLISECONDS)
    @DisplayName("Adversarial input (20 a's + c) must complete within 100ms — tests for ReDoS safety")
    void testAdversarialInputCompletesQuickly() {
        // "aaaaaaaaaaaaaaaaaaaac" = 20 a's followed by 'c'
        // The BROKEN pattern (a+)+b would explore 2^19 = 524,288 paths
        // A safe pattern returns immediately (O(n) at most)
        String adversarial = "aaaaaaaaaaaaaaaaaaaac";  // 20 a's + c
        boolean result = solution.matchesPattern(adversarial);
        assertFalse(result,
                "20 a's followed by 'c' should return false AND return quickly");
    }

    @Test
    @Timeout(value = 100, unit = TimeUnit.MILLISECONDS)
    @DisplayName("Longer adversarial input (30 a's + c) also completes quickly")
    void testLongerAdversarialInputCompletesQuickly() {
        String adversarial = "aaaaaaaaaaaaaaaaaaaaaaaaaaaaac";  // 29 a's + c
        boolean result = solution.matchesPattern(adversarial);
        assertFalse(result,
                "Many a's followed by 'c' must return false quickly — O(n), not O(2^n)");
    }
}
