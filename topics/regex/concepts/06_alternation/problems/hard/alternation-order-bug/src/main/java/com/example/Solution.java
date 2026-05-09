package com.example;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Solution for the <b>alternation-order-bug</b> problem.
 *
 * <p>This class demonstrates a classic NFA alternation ordering bug and asks you
 * to understand it, then fix it.</p>
 *
 * <h2>The broken pattern (provided for study — do NOT use)</h2>
 * <pre>
 *   BROKEN: [$€]\d+|[$€]\d+\.\d{2}
 * </pre>
 *
 * <p>When applied to {@code "$10.50"}:</p>
 * <ol>
 *   <li>Engine at position 0 tries the LEFT alternative {@code [$€]\d+}.</li>
 *   <li>{@code [$€]} matches {@code '$'}; {@code \d+} greedily matches {@code "10"}
 *       and stops at the {@code '.'}.</li>
 *   <li>Left alternative SUCCEEDS → engine commits to this match → returns {@code "$10"}.</li>
 *   <li>The right alternative {@code [$€]\d+\.\d{2}} is NEVER tried at position 0.</li>
 *   <li>Result: {@code ["$10"]} instead of {@code ["$10.50"]} — <strong>wrong</strong>.</li>
 * </ol>
 *
 * <p>Root cause: {@code [$€]\d+} is a prefix of {@code [$€]\d+\.\d{2}}. Since it comes
 * first in the alternation and succeeds, the longer pattern is never reached.</p>
 *
 * <h2>Your tasks</h2>
 * <ol>
 *   <li>Define {@code FIXED_PATTERN} — move the decimal alternative BEFORE the integer alternative:
 *       <pre>[$€]\d+\.\d{2}|[$€]\d+</pre>
 *   </li>
 *   <li>Implement {@link #extractAmounts(String)} using your fixed pattern.</li>
 * </ol>
 *
 * <h2>Why the fix works</h2>
 * <pre>
 *   FIXED: [$€]\d+\.\d{2}|[$€]\d+
 *
 *   For "$10.50":  decimal alternative tried first → matches "$10.50" ✓
 *   For "$10":     decimal alternative fails (no '.') → integer alternative → "$10" ✓
 * </pre>
 */
public class Solution {

    /**
     * BROKEN pattern — provided for educational study only.
     *
     * <p>The integer alternative {@code [$€]\d+} comes before the decimal alternative
     * {@code [$€]\d+\.\d{2}}. Because the integer alternative succeeds for ANY amount
     * (including decimal ones), the decimal alternative is never tried. {@code "$10.50"}
     * is incorrectly matched as {@code "$10"}.</p>
     *
     * <p>Do NOT use this pattern in your implementation.</p>
     */
    public static final String BROKEN_PATTERN = "[$\u20ac]\\d+|[$\u20ac]\\d+\\.\\d{2}";

    // -------------------------------------------------------------------------
    // TODO: Define FIXED_PATTERN below.
    //
    // The fix is to put the DECIMAL alternative FIRST (more specific),
    // and the INTEGER alternative SECOND (less specific / fallback).
    //
    // Correct fixed pattern (regex):  [$€]\d+\.\d{2}|[$€]\d+
    // In Java string form:            "[$\u20ac]\\d+\\.\\d{2}|[$\u20ac]\\d+"
    //
    // Uncomment and complete the line:
    // public static final String FIXED_PATTERN = ...;
    // -------------------------------------------------------------------------

    /**
     * Extracts all currency amounts from {@code input}.
     *
     * <p>Amounts use {@code $} or {@code €} as the currency symbol, followed by
     * digits, optionally with a decimal point and exactly two decimal digits.</p>
     *
     * <p>You must use your {@code FIXED_PATTERN} (not {@code BROKEN_PATTERN}) to
     * implement this method correctly.</p>
     *
     * @param input text that may contain currency amounts; never {@code null}
     * @return list of matched amount strings in order of appearance
     * @throws UnsupportedOperationException until the method is implemented
     */
    public List<String> extractAmounts(String input) {
        throw new UnsupportedOperationException("TODO");
    }
}
