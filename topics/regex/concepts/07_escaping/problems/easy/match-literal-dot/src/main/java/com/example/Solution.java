package com.example;

import java.util.List;
import java.util.ArrayList;
import java.util.regex.Pattern;
import java.util.regex.Matcher;

/**
 * Filters a list of strings, returning only those that contain
 * at least one literal dot character ('.').
 *
 * <p>Key challenge: in regex, the dot (.) is a metacharacter that matches
 * ANY character. To match a literal dot, you must escape it.
 *
 * <p>Two-layer escaping reminder:
 * <ul>
 *   <li>Regex engine needs: {@code \.} (backslash + dot)</li>
 *   <li>Java string literal needs: {@code "\\."} (two chars: two backslashes + dot)</li>
 * </ul>
 *
 * <p>TODO — implement {@link #filterContainingDot(List)}:
 * <ul>
 *   <li>TODO: Declare a static final Pattern field — compile it ONCE, not per call</li>
 *   <li>TODO: Use the correct escaped pattern — "\\" + "." in Java string</li>
 *   <li>TODO: Use Matcher.find() (NOT String.matches()) to search within each string</li>
 *   <li>TODO: Skip null elements gracefully</li>
 *   <li>TODO: Return all strings that contain at least one literal dot</li>
 * </ul>
 */
public class Solution {

    // TODO: Declare your compiled Pattern here as a static final field.
    // Remember: to match a literal dot, what does the regex engine need?
    // And how do you write that in a Java string?
    // private static final Pattern DOT_PATTERN = Pattern.compile(/* your pattern here */);

    /**
     * Returns a filtered list containing only strings that have at least one
     * literal '.' character.
     *
     * @param inputs a list of strings to filter; never null, but may contain null elements
     * @return a new list with only the strings containing a literal dot; never null
     */
    public List<String> filterContainingDot(List<String> inputs) {
        throw new UnsupportedOperationException("TODO: implement this method");
    }
}
