package com.example;

import java.util.List;
import java.util.ArrayList;
import java.util.regex.Pattern;
import java.util.regex.Matcher;

/**
 * Extract Prices using Positive Lookbehind
 *
 * <p>Your task: implement {@link #extractAmounts(String)} using a positive lookbehind
 * {@code (?<=\$|€)} to extract numeric amounts preceded by a currency symbol.
 *
 * <p>TODO: Design the pattern: lookbehind for $ or euro, then number pattern.
 *
 * <p>TODO: Remember that Java 8 lookbehind requires fixed-length alternatives.
 *          Both $ and € are single Java chars — this is allowed.
 *
 * <p>TODO: Return an empty list (not null) for null/no-match input.
 */
public class Solution {

    // TODO: Add static Pattern field here.
    // private static final Pattern PRICE_PATTERN = Pattern.compile("...");

    /**
     * Extracts numeric amounts that are immediately preceded by '$' or '€'.
     * The currency symbol itself is NOT included in the results.
     *
     * <p>A numeric amount is: one or more digits, optionally followed by '.' and 2 digits.
     *
     * <p>Examples:
     * <ul>
     *   <li>"total: $19.99 and €25.00" → ["19.99", "25.00"]</li>
     *   <li>"no currency" → []</li>
     *   <li>"$100 off" → ["100"]</li>
     *   <li>"€ 50" → [] (space between symbol and digits)</li>
     * </ul>
     *
     * @param input the string to search; may be null
     * @return list of matched numeric strings in order of appearance; never null
     */
    public List<String> extractAmounts(String input) {
        throw new UnsupportedOperationException("TODO");
    }
}
