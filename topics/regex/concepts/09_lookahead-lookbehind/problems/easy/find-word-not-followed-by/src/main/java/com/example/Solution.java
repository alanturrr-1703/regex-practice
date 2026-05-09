package com.example;

import java.util.regex.Pattern;
import java.util.regex.Matcher;

/**
 * Find Word Not Followed By
 *
 * <p>Your task: implement {@link #countStandaloneFile(String)} using a negative lookahead
 * to count occurrences of "file" that are NOT followed by "name" or "path".
 *
 * <p>TODO: Define a static Pattern field using negative lookahead: {@code (?!name|path)}.
 *
 * <p>TODO: Use {@code Matcher.find()} in a loop to count all occurrences.
 *
 * <p>TODO: Return 0 for null input.
 */
public class Solution {

    // TODO: Add static Pattern field here.
    // private static final Pattern FILE_PATTERN = Pattern.compile("...");

    /**
     * Counts occurrences of "file" in the input that are NOT immediately followed
     * by "name" or "path".
     *
     * <p>Examples:
     * <ul>
     *   <li>"read file from filepath" → 1 (first "file" is standalone)</li>
     *   <li>"file file file" → 3</li>
     *   <li>"filename and file" → 1</li>
     * </ul>
     *
     * @param input the string to search; may be null
     * @return count of standalone "file" occurrences; 0 for null or no-match input
     */
    public int countStandaloneFile(String input) {
        throw new UnsupportedOperationException("TODO");
    }
}
