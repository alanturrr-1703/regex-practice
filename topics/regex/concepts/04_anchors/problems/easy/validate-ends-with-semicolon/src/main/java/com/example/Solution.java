package com.example;

import java.util.regex.Pattern;
import java.util.regex.Matcher;

/**
 * Solution for the "Validate Ends With Semicolon" problem.
 *
 * <p>Problem: Return {@code true} if the input string ends with a semicolon
 * ({@code ;}), optionally followed by trailing whitespace.
 *
 * <p>Key concept: The {@code $} anchor matches the end of the string (or just
 * before a trailing {@code \n}). Combined with {@code \s*}, it allows the
 * semicolon to be followed by optional whitespace.
 *
 * <p>TODO: Implement this method.
 *       Pattern: {@code ;\s*$}
 *       In Java string: {@code ";\\s*$"}
 *       Use {@code Matcher.find()} — the {@code $} restricts the end position.
 *       Handle {@code null} input by returning {@code false}.
 */
public class Solution {

    // TODO: Declare a static final Pattern here.
    //       Pattern: ;\s*$
    //       Java string: ";\\s*$"

    /**
     * Returns {@code true} if {@code input} ends with a semicolon, optionally
     * followed by whitespace (spaces, tabs, etc.).
     *
     * @param input the string to check; may be {@code null}
     * @return {@code true} if the string ends with {@code ;} (plus optional whitespace)
     */
    public boolean endsWithSemicolon(String input) {
        throw new UnsupportedOperationException("TODO");
    }
}
