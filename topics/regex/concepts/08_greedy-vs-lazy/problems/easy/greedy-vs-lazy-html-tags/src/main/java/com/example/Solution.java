package com.example;

import java.util.List;
import java.util.ArrayList;
import java.util.regex.Pattern;
import java.util.regex.Matcher;

/**
 * Extracts the content of all {@code <b>...</b>} bold tag pairs from an HTML string.
 *
 * <p>The key lesson: greedy vs lazy quantifier choice.
 *
 * <p>BROKEN pattern (do not use): {@code <b>(.*)</b>}
 * <br>Greedy {@code .*} spans from the FIRST {@code <b>} to the LAST {@code </b>},
 * producing one incorrect match that includes everything between them.
 *
 * <p>CORRECT pattern: {@code <b>(.*?)</b>}
 * <br>Lazy {@code .*?} stops at the FIRST {@code </b>}, correctly separating each bold region.
 *
 * <p>TODO — implement {@link #extractBoldContents(String)}:
 * <ul>
 *   <li>TODO: Use the lazy pattern: {@code "<b>(.*?)</b>"}</li>
 *   <li>TODO: Use Matcher.find() in a loop</li>
 *   <li>TODO: Return matcher.group(1) — NOT group(0) which includes the tags</li>
 *   <li>TODO: Handle empty content: {@code <b></b>} should return an empty string entry</li>
 * </ul>
 */
public class Solution {

    // BROKEN pattern (for reference — do NOT use this):
    // private static final Pattern BROKEN = Pattern.compile("<b>(.*)</b>");
    // The above is GREEDY — eats from first <b> to last </b>

    // TODO: Declare the correct LAZY pattern here as a static final field
    // private static final Pattern BOLD_PATTERN = Pattern.compile(/* your lazy pattern */);

    /**
     * Returns a list of the text content found between {@code <b>} and {@code </b>} tags.
     * Uses lazy matching to correctly handle multiple pairs.
     *
     * @param html the HTML string to search; never null
     * @return list of bold content strings; empty list if no bold tags found
     */
    public List<String> extractBoldContents(String html) {
        throw new UnsupportedOperationException("TODO: implement this method");
    }
}
