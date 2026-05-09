package com.example;

import java.util.regex.Pattern;
import java.util.regex.Matcher;

/**
 * Overlapping Pattern Finder
 *
 * <p>Your task: implement {@link #countOverlapping(String, String)} that counts ALL
 * overlapping occurrences of {@code pattern} as a literal string within {@code input}.
 *
 * <p>The technique: wrap the pattern in a zero-width lookahead inside a capturing group:
 * <pre>
 *   (?=(PATTERN))
 * </pre>
 * Because the outer {@code (?=...)} is zero-width, {@code Matcher.find()} advances only
 * 1 character after each match, enabling detection of overlapping occurrences.
 *
 * <p>TODO: Use {@code Pattern.quote(pattern)} to treat {@code pattern} as a literal string
 *         (not a regex), so metacharacters like {@code .}, {@code +}, {@code *} are safe.
 *
 * <p>TODO: Guard against null or empty inputs — return 0 in those cases.
 *
 * <p>TODO: Count via {@code while (m.find()) count++}.
 */
public class Solution {

    /**
     * Counts overlapping occurrences of {@code pattern} (treated as a literal string)
     * within {@code input}.
     *
     * <p>Examples:
     * <ul>
     *   <li>countOverlapping("abababa", "aba") → 3  (positions 0, 2, 4)</li>
     *   <li>countOverlapping("aaaa", "aa") → 3      (positions 0, 1, 2)</li>
     *   <li>countOverlapping("hello", "ll") → 1</li>
     *   <li>countOverlapping("abcdef", "xyz") → 0</li>
     * </ul>
     *
     * @param input   the string to search; may be null or empty
     * @param pattern the literal pattern to search for; may be null or empty
     * @return number of overlapping occurrences; 0 if input or pattern is null/empty
     */
    public int countOverlapping(String input, String pattern) {
        throw new UnsupportedOperationException("TODO");
    }
}
