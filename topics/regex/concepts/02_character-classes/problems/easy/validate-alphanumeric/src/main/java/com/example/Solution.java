package com.example;

import java.util.regex.Pattern;
import java.util.regex.Matcher;

/**
 * Problem: Validate that a string is entirely alphanumeric, with length >= 1.
 *
 * <p>Allowed characters: ASCII letters (a-z, A-Z) and digits (0-9) only.
 * No spaces, underscores, or any other characters are permitted.
 *
 * <p>Key concepts:
 * <ul>
 *   <li>{@code [a-zA-Z0-9]} — character class with explicit ranges (NOT {@code \w}
 *       which would include underscore)</li>
 *   <li>{@code +} quantifier — requires at least 1 character (handles empty string case)</li>
 *   <li>{@link Matcher#matches()} or {@link String#matches(String)} — full-string validation</li>
 * </ul>
 *
 * <p><b>TODO:</b>
 * <ol>
 *   <li>Compile the pattern {@code [a-zA-Z0-9]+}.</li>
 *   <li>Use {@code matches()} (full-string) — NOT {@code find()} (substring).</li>
 *   <li>Return the boolean result.</li>
 * </ol>
 *
 * <p>Example:
 * <pre>
 *   isAlphanumeric("Hello123")   → true
 *   isAlphanumeric("Hello 123")  → false  (space not allowed)
 *   isAlphanumeric("")           → false  (minimum length 1)
 *   isAlphanumeric("abc!")       → false  (! not allowed)
 *   isAlphanumeric("Z9")         → true
 * </pre>
 */
public class Solution {

    /**
     * Returns {@code true} if {@code input} consists entirely of alphanumeric
     * ASCII characters and has at least 1 character.
     *
     * @param input a non-null string
     * @return {@code true} if valid alphanumeric, {@code false} otherwise
     * @throws UnsupportedOperationException until this method is implemented
     */
    public boolean isAlphanumeric(String input) {
        throw new UnsupportedOperationException("TODO: implement isAlphanumeric");
    }
}
