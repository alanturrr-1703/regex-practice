package com.example;

import java.util.List;
import java.util.ArrayList;
import java.util.regex.Pattern;
import java.util.regex.Matcher;

/**
 * Extracts the values inside square brackets from a string.
 * Each [...] pair produces one entry in the result list.
 *
 * <p>Greedy bug: {@code \[.*\]} matches from the FIRST {@code [} to the LAST {@code ]},
 * consuming everything in between as ONE match.
 *
 * <p>Lazy fix: {@code \[.*?\]} matches each pair individually,
 * stopping at the FIRST {@code ]} it encounters.
 *
 * <p>TODO — implement {@link #extractBracketedValues(String)}:
 * <ul>
 *   <li>TODO: Use lazy pattern: {@code "\\[(.*?)\\]"} — escape the brackets</li>
 *   <li>TODO: Use Matcher.find() in a loop</li>
 *   <li>TODO: Return matcher.group(1) for the content (without the brackets)</li>
 *   <li>TODO: Empty brackets {@code []} should return an empty string entry</li>
 * </ul>
 */
public class Solution {

    // BROKEN (greedy): matches from first [ to last ]
    // private static final Pattern BROKEN = Pattern.compile("\\[.*\\]");

    // TODO: Declare the correct LAZY pattern here
    // private static final Pattern BRACKET_PATTERN = Pattern.compile(/* your lazy pattern */);

    /**
     * Returns a list of values found inside square brackets.
     * Each [content] pair contributes one entry.
     *
     * @param input the string to search; never null
     * @return list of bracket contents; empty if no brackets found
     */
    public List<String> extractBracketedValues(String input) {
        throw new UnsupportedOperationException("TODO: implement this method");
    }
}
