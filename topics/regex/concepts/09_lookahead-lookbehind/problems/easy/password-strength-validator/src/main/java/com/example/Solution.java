package com.example;

import java.util.regex.Pattern;
import java.util.regex.Matcher;

/**
 * Password Strength Validator
 *
 * <p>Your task: implement {@link #isStrongPassword(String)} using a single regex pattern
 * with multiple chained positive lookaheads. The pattern must enforce all five password
 * strength rules simultaneously.
 *
 * <p>TODO: Design a regex of the form:
 * <pre>
 *   ^(?=.*[RULE1])(?=.*[RULE2])...CONSUME_PART$
 * </pre>
 *
 * <p>TODO: Compile the pattern as a static field (not inside the method).
 *
 * <p>TODO: Handle null input without throwing NullPointerException.
 */
public class Solution {

    // TODO: Add static Pattern field here — compile once, use many times.
    // private static final Pattern STRONG_PASSWORD_PATTERN = Pattern.compile("...");

    /**
     * Returns true if the password meets all strength requirements:
     * <ul>
     *   <li>At least 8 characters long</li>
     *   <li>Contains at least one uppercase letter (A-Z)</li>
     *   <li>Contains at least one lowercase letter (a-z)</li>
     *   <li>Contains at least one digit (0-9)</li>
     *   <li>Contains at least one special character from: !@#$%^&amp;*</li>
     * </ul>
     *
     * @param password the password string to validate; may be null
     * @return true if the password is strong, false otherwise (including null)
     */
    public boolean isStrongPassword(String password) {
        throw new UnsupportedOperationException("TODO");
    }
}
