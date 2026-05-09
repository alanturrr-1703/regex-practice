package com.example;

import java.util.Optional;
import java.util.regex.Pattern;
import java.util.regex.Matcher;

/**
 * Problem: Debug Group Index Error
 * Difficulty: Medium — Debugging
 * Concept: group(0) vs group(1) off-by-one; the whole match vs first capturing group
 *
 * <p>A broken method tries to parse a "YYYY-MM-DD" date string and return the year.
 * The broken code uses {@code matcher.group(0)} thinking it's the first capturing
 * group — but {@code group(0)} is the ENTIRE match, not the first group.
 *
 * <p>BROKEN CODE:
 * <pre>
 *   Matcher m = Pattern.compile("(\\d{4})-(\\d{2})-(\\d{2})").matcher(input);
 *   if (m.find()) return Optional.of(m.group(0)); // BUG: group(0) = entire match
 * </pre>
 *
 * <p>For input "2024-01-15":
 * <ul>
 *   <li>{@code group(0)} = "2024-01-15" (WRONG — entire match)</li>
 *   <li>{@code group(1)} = "2024" (CORRECT — first capturing group = year)</li>
 * </ul>
 *
 * <p>TODO: Fix the group index to correctly return only the year.
 * Implement {@link #extractYear(String)}.
 */
public class Solution {

    /**
     * Parses a string that may contain a date in "YYYY-MM-DD" format and returns
     * the year portion as an {@link Optional} String.
     *
     * <p>Returns {@link Optional#empty()} if no date is found.
     *
     * <p>BROKEN implementation (do NOT use):
     * <pre>
     *   Matcher m = Pattern.compile("(\\d{4})-(\\d{2})-(\\d{2})").matcher(input);
     *   if (m.find()) return Optional.of(m.group(0)); // group(0) is entire "2024-01-15"
     * </pre>
     *
     * <p>Fix: change {@code group(0)} to {@code group(1)} — the first capturing group
     * is {@code (\\d{4})} which captures just the year.
     *
     * @param input the string to search (never null)
     * @return Optional containing the 4-digit year string, or empty if no date found
     * @throws UnsupportedOperationException until implemented
     */
    public Optional<String> extractYear(String input) {
        // BROKEN: group(0) returns the entire match "2024-01-15", not just the year
        // Matcher m = Pattern.compile("(\\d{4})-(\\d{2})-(\\d{2})").matcher(input);
        // if (m.find()) return Optional.of(m.group(0)); // BUG: should be group(1)

        throw new UnsupportedOperationException("TODO");
    }
}
