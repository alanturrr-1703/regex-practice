package com.example;

import java.util.regex.Pattern;
import java.util.regex.Matcher;

/**
 * Solution for the "Validate Starts With HTTP" problem.
 *
 * <p>Problem: Return {@code true} if the input string begins with
 * {@code "http://"} or {@code "https://"}. Use the {@code ^} anchor
 * to ensure the match is at the very start of the string.
 *
 * <p>Key concept: Without {@code ^}, {@code find()} would match
 * {@code "http://"} anywhere in the string, including after leading
 * whitespace or other characters. The {@code ^} anchor prevents this.
 *
 * <p>TODO: Implement this method.
 *       Pattern: {@code ^https?://}
 *       In Java string: {@code "^https?://"}
 *       Use {@code Matcher.find()} — the {@code ^} anchor ensures position 0.
 *       Handle {@code null} input by returning {@code false}.
 */
public class Solution {

    // TODO: Declare a static final Pattern here.
    //       Pattern: ^https?://
    //       Java string: "^https?://"

    /**
     * Returns {@code true} if {@code input} starts with {@code "http://"}
     * or {@code "https://"}.
     *
     * @param input the string to check; may be {@code null}
     * @return {@code true} if input begins with a valid HTTP(S) scheme
     */
    public boolean startsWithHttp(String input) {
        throw new UnsupportedOperationException("TODO");
    }
}
