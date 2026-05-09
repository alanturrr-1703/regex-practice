package com.example;

import java.util.regex.Pattern;
import java.util.regex.Matcher;

/**
 * Problem: Detect if a string contains at least one digit character.
 *
 * <p>This problem teaches Java's double-escaping requirement for regex patterns.
 * The regex shorthand {@code \d} matches any ASCII digit (0-9). In a Java string
 * literal, you must write {@code "\\d"} — two characters — so that the Java string
 * layer passes a single {@code \} to the regex engine, which then interprets {@code \d}.
 *
 * <p>Key concepts:
 * <ul>
 *   <li>Java string layer: {@code "\\"} → single backslash {@code \}</li>
 *   <li>Regex engine receives: {@code \d} → digit shorthand = {@code [0-9]}</li>
 *   <li>{@link Matcher#find()} — returns true if pattern found anywhere in string</li>
 * </ul>
 *
 * <p><b>TODO:</b>
 * <ol>
 *   <li>Create a {@code Pattern} using the digit shorthand {@code \d}.
 *       Remember Java double-escaping: the Java string must be {@code "\\d"}.</li>
 *   <li>Use {@code Matcher.find()} — not {@code matches()} — to search for a digit
 *       anywhere in the input string.</li>
 *   <li>Return the boolean result of {@code find()}.</li>
 * </ol>
 *
 * <p>Example:
 * <pre>
 *   containsDigit("hello123") → true
 *   containsDigit("hello")    → false
 *   containsDigit("")         → false
 *   containsDigit("   9   ")  → true
 * </pre>
 */
public class Solution {

    /**
     * Returns {@code true} if {@code input} contains at least one ASCII digit (0-9).
     *
     * @param input a non-null string (may be empty)
     * @return {@code true} if at least one digit character is found, {@code false} otherwise
     * @throws UnsupportedOperationException until this method is implemented
     */
    public boolean containsDigit(String input) {
        Pattern p = Pattern.compile(".*\\d.*");
        Matcher m = p.matcher(input);
        return m.find();
    }
}
