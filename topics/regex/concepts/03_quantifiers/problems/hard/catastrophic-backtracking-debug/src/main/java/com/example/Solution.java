package com.example;

import java.util.regex.Pattern;
import java.util.regex.Matcher;

/**
 * Solution for the "Catastrophic Backtracking Debug" problem.
 *
 * <p><strong>READ THIS CAREFULLY BEFORE IMPLEMENTING.</strong>
 *
 * <p>The pattern below is INTENTIONALLY BROKEN and left here for educational
 * purposes. DO NOT use it in your implementation. Understand WHY it's broken,
 * then write a safe replacement.
 *
 * <pre>
 * BROKEN pattern: (a+)+b
 *
 * Why it's broken — the engine explores exponential paths:
 *   For input "aaac" (3 a's + c), the outer (+) can create groups as:
 *     (a)(a)(a)c  — 3 groups of 1
 *     (aa)(a)c    — 1 group of 2, 1 group of 1
 *     (a)(aa)c    — 1 group of 1, 1 group of 2
 *     (aaa)c      — 1 group of 3
 *   → 4 = 2^(3-1) paths for 3 a's
 *   For "aaaaaaaaaaaaaaaaaaaaac" (20 a's), this is 2^19 = 524,288 paths!
 *   The engine tries them ALL before reporting no match.
 *
 * How to fix:
 *   Option A: Simplify — (a+)+ is semantically equivalent to a+
 *             Use pattern: a+b
 *   Option B: Possessive — a++b (never backtracks through the a's)
 *   Option C: Atomic group — (?>a+)b
 * </pre>
 *
 * <p>TODO: Implement {@link #matchesPattern(String)}.
 *       - Declare a static final Pattern using a SAFE pattern (NOT the broken one above)
 *       - Return false for null input
 *       - Return true only if input is exactly: one or more 'a's followed by 'b'
 *       - Use matcher.matches() (full-string match, no anchors needed)
 *       - Verify your solution passes the timeout test in SolutionTest
 */
public class Solution {

    // -------------------------------------------------------------------------
    // BROKEN pattern (DO NOT USE — here for study only):
    // private static final Pattern BROKEN = Pattern.compile("(a+)+b");
    // -------------------------------------------------------------------------

    // TODO: Replace this with a safe pattern.
    //       Hint: "a+b" used with matches() is both correct and safe.
    //       "a++b" (possessive) is even safer — zero backtracking.

    /**
     * Returns {@code true} if {@code input} consists of exactly one or more
     * {@code 'a'} characters followed by exactly one {@code 'b'} character,
     * with no other characters before or after.
     *
     * <p>This method MUST complete in O(n) time even for adversarial inputs
     * such as 20+ 'a' characters followed by 'c'. Do NOT use nested quantifiers.
     *
     * @param input the string to test; may be {@code null}
     * @return {@code true} if input matches the pattern {@code a+b} exactly
     */
    public boolean matchesPattern(String input) {
        throw new UnsupportedOperationException("TODO");
    }
}
