package com.example;

import java.util.List;
import java.util.regex.Pattern;
import java.util.regex.Matcher;

/**
 * Problem: Filter Log Lines Containing "ERROR"
 *
 * <p>Given a list of strings (log lines), return a new list containing only those
 * strings that contain the exact, case-sensitive literal substring {@code "ERROR"}.
 *
 * <p>Key concepts to understand before implementing:
 * <ul>
 *   <li>{@link Pattern#compile(String)} — compiles a regex pattern (do this once, not per line)</li>
 *   <li>{@link Matcher#find()} — searches for the pattern <em>anywhere</em> in the string</li>
 *   <li>{@link Matcher#matches()} — requires the pattern to match the <em>entire</em> string</li>
 *   <li>{@link String#matches(String)} — full-string match (same trap as Matcher.matches)</li>
 * </ul>
 *
 * <p>The distinction between {@code find()} and {@code matches()} is the core lesson here.
 *
 * <p><b>TODO:</b>
 * <ol>
 *   <li>Compile the literal pattern {@code "ERROR"} as a {@code static final Pattern}.</li>
 *   <li>Iterate over {@code lines}.</li>
 *   <li>For each line, use {@code Matcher.find()} to check for the substring.</li>
 *   <li>Collect and return matching lines, preserving order.</li>
 * </ol>
 *
 * <p>Example:
 * <pre>
 *   filterErrors(["ERROR: disk full", "error: low mem", "CRITICAL ERROR"])
 *       → ["ERROR: disk full", "CRITICAL ERROR"]
 * </pre>
 */
public class Solution {

    /**
     * Returns a filtered list containing only the strings from {@code lines}
     * that contain the case-sensitive literal substring {@code "ERROR"}.
     *
     * @param lines a non-null list of non-null strings (elements may be empty)
     * @return a new list (never null) with matching lines in their original order
     * @throws UnsupportedOperationException until this method is implemented
     */
    public List<String> filterErrors(List<String> lines) {
        throw new UnsupportedOperationException("TODO: implement filterErrors");
    }
}
