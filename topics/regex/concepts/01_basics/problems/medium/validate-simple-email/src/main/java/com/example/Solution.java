package com.example;

import java.util.regex.Pattern;
import java.util.regex.Matcher;

/**
 * Problem: Validate a string as a basic email address.
 *
 * <p>An email is considered valid if it matches ALL of the following:
 * <ol>
 *   <li>One or more characters that are NOT {@code @} or whitespace (local part)</li>
 *   <li>Exactly one {@code @} sign</li>
 *   <li>One or more characters that are NOT {@code @} or whitespace (domain)</li>
 *   <li>A literal dot {@code .}</li>
 *   <li>A TLD of 2–6 ASCII letters only</li>
 * </ol>
 *
 * <p>Key concepts:
 * <ul>
 *   <li>{@code String.matches()} / {@code Matcher.matches()} — full-string match (anchored)</li>
 *   <li>Negated character class {@code [^@\\s]} — any char except {@code @} and whitespace</li>
 *   <li>Literal dot escape {@code \\.} — matches {@code .} not any character</li>
 *   <li>Range quantifier {@code {2,6}} — between 2 and 6 occurrences</li>
 * </ul>
 *
 * <p>This is NOT a full RFC email validator — it is a teaching exercise for pattern composition.
 *
 * <p><b>TODO:</b>
 * <ol>
 *   <li>Build the pattern piece by piece: local part + {@code @} + domain + {@code \.} + TLD.</li>
 *   <li>Use {@code Matcher.matches()} or {@code String.matches()} for full-string validation.</li>
 *   <li>Remember: in Java strings, {@code \.} must be written as {@code "\\."} and
 *       {@code \s} as {@code "\\s"}.</li>
 * </ol>
 *
 * <p>Example:
 * <pre>
 *   isValidEmail("user@example.com")  → true
 *   isValidEmail("bad@")              → false
 *   isValidEmail("@domain.com")       → false
 *   isValidEmail("user@domain.toolong") → false (TLD > 6 letters)
 * </pre>
 */
public class Solution {

    /**
     * Returns {@code true} if {@code email} matches the simplified email format.
     *
     * @param email a non-null string to validate
     * @return {@code true} if the string is a valid simplified email, {@code false} otherwise
     * @throws UnsupportedOperationException until this method is implemented
     */
    public boolean isValidEmail(String email) {
        throw new UnsupportedOperationException("TODO: implement isValidEmail");
    }
}
