package com.example;

import java.util.regex.Pattern;
import java.util.regex.Matcher;

/**
 * Optimize Email Validator
 *
 * <p>Your task: implement {@link #isValidEmail(String)} using a SAFE, non-catastrophic
 * regex pattern. The slow pattern below is provided for reference — do NOT use it.
 *
 * <p>SLOW PATTERN (catastrophic on adversarial input — DO NOT USE):
 * <pre>
 * // ^([a-z]+[a-z0-9]*)*@([a-z0-9]+\.)+[a-z]{2,6}$
 * // The local part ([a-z]+[a-z0-9]*)* creates 2^N backtracking paths on "aaaa...a@"
 * </pre>
 *
 * <p>TODO: Design a safe pattern with a flat, unambiguous local part:
 * <pre>
 *   [a-zA-Z0-9._%+\-]+   (one quantifier, no nesting)
 * </pre>
 *
 * <p>TODO: Bound input length to 254 chars (RFC 5321) before matching.
 *
 * <p>TODO: Compile the safe pattern as a static final field.
 *
 * <p>TODO: The method must return false for null, empty, too-long, or non-matching input.
 */
public class Solution {

    // TODO: Add static Pattern field here — safe, flat, unambiguous.
    // private static final Pattern EMAIL_PATTERN = Pattern.compile("...");

    // Reference (DO NOT USE — catastrophic!):
    // private static final String SLOW_PATTERN = "^([a-z]+[a-z0-9]*)*@([a-z0-9]+\\.)+[a-z]{2,6}$";

    /**
     * Validates an email address using a safe, non-catastrophic regex pattern.
     *
     * <p>Valid format: {@code local@domain.tld}
     * <ul>
     *   <li>Local part: [a-zA-Z0-9._%+\-]+</li>
     *   <li>@ symbol</li>
     *   <li>Domain labels: [a-zA-Z0-9\-]+ separated by dots</li>
     *   <li>TLD: [a-zA-Z]{2,}</li>
     * </ul>
     *
     * <p>Examples:
     * <ul>
     *   <li>"user@example.com" → true</li>
     *   <li>"bad-email" → false</li>
     *   <li>"@nodomain" → false</li>
     *   <li>null → false</li>
     * </ul>
     *
     * @param email the email address to validate; may be null
     * @return true if the email is valid, false otherwise
     */
    public boolean isValidEmail(String email) {
        throw new UnsupportedOperationException("TODO");
    }
}
