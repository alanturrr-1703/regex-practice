package com.example;

import java.util.regex.Pattern;
import java.util.regex.Matcher;

/**
 * Solution for the "Validate Digit Count" problem.
 *
 * <p>Problem: Return {@code true} if the input string contains a run of EXACTLY
 * 5 consecutive digits, where the run is not adjacent to any other digit
 * (neither before nor after).
 *
 * <p>Key insight: {@code \d{5}} alone will match 5 digits even inside a longer
 * run like "123456". You must use negative lookahead and lookbehind to enforce
 * the "exactly 5, no more" constraint.
 *
 * <p>TODO: Implement this method using the pattern {@code (?<!\d)\d{5}(?!\d)}.
 *       Compile the pattern as a {@code static final} field.
 *       Handle {@code null} input by returning {@code false}.
 *       Use {@code Matcher.find()} — NOT {@code Matcher.matches()}.
 */
public class Solution {

    // TODO: Declare a static final Pattern field here.
    //       Pattern: (?<!\d)\d{5}(?!\d)
    //       In Java string: "(?<!\\d)\\d{5}(?!\\d)"

    /**
     * Returns {@code true} if {@code input} contains a run of exactly 5
     * consecutive digits that is not immediately preceded or followed by
     * another digit.
     *
     * @param input the string to search; may be {@code null}
     * @return {@code true} if a valid 5-digit run is found, {@code false} otherwise
     */
    public boolean hasExactlyFiveDigits(String input) {
        throw new UnsupportedOperationException("TODO");
    }
}
