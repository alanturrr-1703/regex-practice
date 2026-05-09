package com.example;

import java.util.List;
import java.util.ArrayList;
import java.util.regex.Pattern;
import java.util.regex.Matcher;

/**
 * Problem: Split on Delimiter Keeping Delimiter
 * Difficulty: Medium
 * Concept: Java Pattern & Matcher API — start(), end(), manual split
 *
 * <p>Split a string on semicolons but keep each semicolon attached to
 * the text that precedes it. Cannot be done with Pattern.split().
 * Requires Matcher.find() with position tracking via start() and end().
 *
 * <p>Behavior:
 * <ul>
 *   <li>"a;b;c"     → ["a;", "b;", "c"]</li>
 *   <li>";leading"  → [";", "leading"]</li>
 *   <li>"trailing;" → ["trailing;"]</li>
 *   <li>"a;;b"      → ["a;", ";", "b"]</li>
 *   <li>""          → []</li>
 * </ul>
 *
 * <p>TODO: Implement {@link #splitKeepingDelimiter(String)}.
 */
public class Solution {

    /**
     * Splits {@code input} on semicolons, attaching each semicolon to the
     * preceding token. Trailing empty strings are NOT added to the result.
     *
     * <p>Algorithm hint: Use pattern {@code [^;]*;} to match each token
     * (zero or more non-semicolons followed by one semicolon). After the
     * loop, capture the remaining substring from {@code matcher.end()} to
     * the end of input (only if non-empty).
     *
     * @param input the string to split (never null)
     * @return list of tokens with delimiters attached; empty list for empty input
     * @throws UnsupportedOperationException until implemented
     */
    public List<String> splitKeepingDelimiter(String input) {
        throw new UnsupportedOperationException("TODO");
    }
}
