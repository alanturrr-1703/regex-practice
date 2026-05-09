package com.example;

import java.util.regex.Pattern;
import java.util.regex.Matcher;

/**
 * Solution for the "Collapse Whitespace" problem.
 *
 * <p>Problem: Normalize a string by collapsing runs of whitespace into a single
 * space and trimming leading/trailing whitespace.
 *
 * <p>Key tool: {@code \s+} matches one or more whitespace characters (space, tab,
 * newline, carriage return, etc.). Combined with {@code replaceAll}, this collapses
 * every whitespace run to a single space in one pass.
 *
 * <p>TODO: Implement this method.
 *       Use {@code replaceAll("\\s+", " ")} followed by {@code .trim()}.
 *       Handle {@code null} input — return {@code null} for null, or document
 *       your choice if you return {@code ""} instead.
 *       Do NOT use split/join — use regex replacement directly.
 */
public class Solution {

    // TODO (optional): Declare a static final Pattern if you prefer pre-compilation.
    //   Pattern.compile("\\s+") can be stored here and used with
    //   Matcher.replaceAll(" ") for slightly better performance.

    /**
     * Collapses all runs of whitespace in {@code input} into a single space,
     * then trims leading and trailing whitespace.
     *
     * @param input the string to normalize; may be {@code null}
     * @return the normalized string, or {@code null} if input is {@code null}
     */
    public String collapseWhitespace(String input) {
        throw new UnsupportedOperationException("TODO");
    }
}
