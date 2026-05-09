package com.example;

import java.util.regex.Pattern;
import java.util.regex.Matcher;

/**
 * Problem: Fix matches() vs find()
 * Difficulty: Easy — Debugging
 * Concept: String.matches() vs Pattern + Matcher.find(); boundary lookaheads
 *
 * <p>A broken method tries to check if a string CONTAINS a 5-digit ZIP code
 * anywhere in it. The broken code uses {@code input.matches("\\d{5}")} which
 * requires the ENTIRE string to be exactly 5 digits.
 *
 * <p>BROKEN CODE (do NOT use):
 * <pre>
 *   // BROKEN: matches() requires the FULL string to match "\\d{5}"
 *   return input.matches("\\d{5}");
 * </pre>
 *
 * <p>Your task: fix the method to correctly detect whether the input CONTAINS
 * a 5-digit ZIP code (not surrounded by other digits).
 * Use {@link Pattern} and {@link Matcher#find()} with boundary assertions.
 *
 * <p>The correct pattern is {@code (?<!\d)\d{5}(?!\d)} which uses:
 * <ul>
 *   <li>{@code (?<!\d)} — negative lookbehind: no digit before the 5 digits</li>
 *   <li>{@code \d{5}} — exactly 5 digits</li>
 *   <li>{@code (?!\d)} — negative lookahead: no digit after the 5 digits</li>
 * </ul>
 *
 * <p>TODO: Implement {@link #containsZipCode(String)}.
 */
public class Solution {

    /**
     * Returns {@code true} if {@code input} contains a 5-digit ZIP code that is
     * not part of a longer digit sequence.
     *
     * <p>Examples:
     * <ul>
     *   <li>"ZIP: 90210 is in CA" → true</li>
     *   <li>"90210"               → true</li>
     *   <li>"9021"                → false (only 4 digits)</li>
     *   <li>"902109"              → false (6 digits — no isolated 5-digit group)</li>
     *   <li>""                    → false</li>
     * </ul>
     *
     * <p>Do NOT use {@code input.matches()} — it requires the entire string to match.
     * Use {@code Pattern.compile(...).matcher(input).find()} instead.
     *
     * @param input the string to search (never null)
     * @return true if a standalone 5-digit ZIP code is found anywhere in input
     * @throws UnsupportedOperationException until implemented
     */
    public boolean containsZipCode(String input) {
        // BROKEN: requires entire string to be exactly 5 digits
        // return input.matches("\\d{5}");

        throw new UnsupportedOperationException("TODO");
    }
}
