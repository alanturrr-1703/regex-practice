package com.example;

import java.util.List;
import java.util.ArrayList;
import java.util.Collections;
import java.util.regex.Pattern;
import java.util.regex.Matcher;

/**
 * Pattern Cache Refactor
 *
 * <p>Your task: implement {@link #filterPhoneNumbers(List)} using a static Pattern field
 * compiled exactly once — NOT inside the method body or loop.
 *
 * <p>The BROKEN approach (shown for reference — DO NOT copy this):
 * <pre>
 * // BROKEN: recompiles "\\d{3}-\\d{4}" on every iteration
 * // for (String line : lines) {
 * //     if (line.matches("\\d{3}-\\d{4}")) { result.add(line); }
 * // }
 * </pre>
 *
 * <p>TODO: Add a {@code private static final Pattern PHONE_PATTERN} field here.
 *
 * <p>TODO: In the method, use {@code PHONE_PATTERN.matcher(s).matches()} to test each string.
 *
 * <p>TODO: Return an empty list for null input. Handle null elements in the list gracefully.
 */
public class Solution {

    // TODO: Add static Pattern field here — compile once at class load time.
    // private static final Pattern PHONE_PATTERN = Pattern.compile("...");

    /**
     * Returns all strings from the input list that match the phone number pattern
     * {@code \d{3}-\d{4}} (exactly 3 digits, hyphen, 4 digits).
     *
     * <p>Uses a pre-compiled static Pattern for efficiency.
     *
     * <p>Examples:
     * <ul>
     *   <li>["123-4567", "abc-defg", "999-0000", "12-345"] → ["123-4567", "999-0000"]</li>
     *   <li>[] → []</li>
     *   <li>null → []</li>
     * </ul>
     *
     * @param inputs list of strings to filter; may be null
     * @return new list containing only phone-number-matching strings; never null
     */
    public List<String> filterPhoneNumbers(List<String> inputs) {
        throw new UnsupportedOperationException("TODO");
    }
}
