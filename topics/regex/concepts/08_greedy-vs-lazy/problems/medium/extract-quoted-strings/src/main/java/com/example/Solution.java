package com.example;

import java.util.List;
import java.util.ArrayList;
import java.util.regex.Pattern;
import java.util.regex.Matcher;

/**
 * Extracts the content of single-quoted strings from input text.
 *
 * <p>This problem has TWO methods with increasing sophistication:
 *
 * <h3>Method 1: extractSimpleQuoted (lazy)</h3>
 * <p>Assumes no escaped single quotes inside strings. Uses lazy {@code '.*?'}.
 * Fast and simple, but fails on inputs like {@code 'it\'s fine'}.
 *
 * <h3>Method 2: extractRobustQuoted (escape-aware)</h3>
 * <p>Handles escaped single quotes {@code \'} inside strings. Uses the
 * canonical escape-aware pattern: {@code '(?:[^'\\]|\\.)*'}
 * <ul>
 *   <li>{@code [^'\\]} — any char that is NOT a quote or backslash</li>
 *   <li>{@code \\.} — a backslash followed by any char (escape sequence)</li>
 * </ul>
 *
 * <p>TODO — implement both methods:
 * <ul>
 *   <li>TODO: {@code extractSimpleQuoted} — use lazy pattern {@code '(.*?)'}</li>
 *   <li>TODO: {@code extractRobustQuoted} — use escape-aware pattern {@code '((?:[^'\\]|\\.)*)'}</li>
 *   <li>TODO: In Java strings, ' does not need escaping, but \ does: use {@code "\\\\"}  for \\ in regex</li>
 *   <li>TODO: Return content only (group(1)), not the surrounding quotes</li>
 * </ul>
 */
public class Solution {

    // TODO: Pattern for simple quoted strings (lazy, no escape handling)
    // private static final Pattern SIMPLE_QUOTED = Pattern.compile(/* your lazy pattern */);

    // TODO: Pattern for robust quoted strings (handles \' inside)
    // private static final Pattern ROBUST_QUOTED = Pattern.compile(/* your escape-aware pattern */);

    /**
     * Extracts content of single-quoted strings. Does NOT handle escaped quotes inside.
     * Uses lazy matching: stops at the first closing quote.
     *
     * @param input the text to search; never null
     * @return list of quoted string contents; empty if none found
     */
    public List<String> extractSimpleQuoted(String input) {
        throw new UnsupportedOperationException("TODO: implement this method");
    }

    /**
     * Extracts content of single-quoted strings, correctly handling escaped
     * single quotes ({@code \'}) inside the string.
     *
     * @param input the text to search; never null
     * @return list of quoted string contents; empty if none found
     */
    public List<String> extractRobustQuoted(String input) {
        throw new UnsupportedOperationException("TODO: implement this method");
    }
}
