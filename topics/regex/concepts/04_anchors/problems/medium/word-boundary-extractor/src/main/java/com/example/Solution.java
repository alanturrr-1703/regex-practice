package com.example;

import java.util.regex.Pattern;
import java.util.regex.Matcher;

/**
 * Solution for the "Word Boundary Extractor" problem.
 *
 * <p>Problem: Count the number of occurrences of the word "log" as a
 * standalone word in the input string. "log" must not be part of a larger
 * word like "logger", "catalog", or "dialog".
 *
 * <p>Key concept: {@code \b} (word boundary) is a zero-width assertion that
 * matches at the transition between a word character ({@code \w}) and a
 * non-word character ({@code \W}). Using {@code \blog\b} ensures "log"
 * only matches when surrounded by non-word characters (or string boundaries).
 *
 * <p>TODO: Implement this method.
 *       Pattern: {@code \blog\b}
 *       In Java string: {@code "\\blog\\b"}
 *       Use a {@code while (matcher.find())} loop to count matches.
 *       The match is CASE-SENSITIVE: "LOG" does not count.
 *       Handle {@code null} input by returning 0.
 */
public class Solution {

    // TODO: Declare a static final Pattern field here.
    //       Pattern: \blog\b
    //       Java string: "\\blog\\b"
    //       Do NOT add Pattern.CASE_INSENSITIVE — the test expects case-sensitivity.

    /**
     * Counts occurrences of the standalone word {@code "log"} in the input.
     *
     * <p>A standalone "log" means it is not part of a larger word. For example:
     * "logger" does not count, "catalog" does not count, "log." counts.
     *
     * @param input the string to search; may be {@code null}
     * @return the number of standalone "log" occurrences; 0 for null input
     */
    public int countWordLog(String input) {
        throw new UnsupportedOperationException("TODO");
    }
}
