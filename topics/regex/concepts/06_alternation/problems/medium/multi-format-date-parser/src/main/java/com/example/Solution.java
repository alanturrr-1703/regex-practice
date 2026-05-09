package com.example;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Solution for the <b>multi-format-date-parser</b> problem.
 *
 * <p>Your task: implement {@link #extractDates(String)} to find all dates in any
 * of three formats and return each matched string as-found in the input.</p>
 *
 * <h2>Supported formats</h2>
 * <ol>
 *   <li>{@code YYYY-MM-DD} — e.g. {@code 2024-01-15}</li>
 *   <li>{@code MM/DD/YYYY} — e.g. {@code 01/15/2024}</li>
 *   <li>{@code DD Mon YYYY} — e.g. {@code 15 Jan 2024} (3-letter month abbreviation)</li>
 * </ol>
 *
 * <h2>Pattern structure</h2>
 * <pre>
 *   (?:
 *     \d{4}-\d{2}-\d{2}                          ← Format 1: YYYY-MM-DD
 *     |
 *     \d{2}/\d{2}/\d{4}                          ← Format 2: MM/DD/YYYY
 *     |
 *     \d{1,2}\s+(?:Jan|Feb|Mar|Apr|May|Jun|      ← Format 3: DD Mon YYYY
 *               Jul|Aug|Sep|Oct|Nov|Dec)\s+\d{4}
 *   )
 * </pre>
 *
 * <h2>Ordering note</h2>
 * <p>Alternation ordering matters when alternatives can match at the same position.
 * In this case, Format 1 (hyphens) and Format 2 (slashes) and Format 3 (spaces + month name)
 * are unambiguous at each position, so ordering among them does not affect correctness.
 * However, it is good practice to put more-specific patterns first.</p>
 *
 * <h2>TODOs</h2>
 * <ol>
 *   <li>Build a non-capturing alternation pattern with all three date formats.</li>
 *   <li>Use {@code find()} in a loop and collect {@code matcher.group()} for each match.</li>
 *   <li>Return the raw matched strings — no parsing or normalization.</li>
 * </ol>
 */
public class Solution {

    /**
     * Extracts all date strings from {@code input} that match any of the three supported formats.
     *
     * <p>Returns the matched text as it appears in the input, with no reformatting.</p>
     *
     * @param input arbitrary text; never {@code null}
     * @return list of matched date strings in order of appearance; empty if none found
     * @throws UnsupportedOperationException until the method is implemented
     */
    public List<String> extractDates(String input) {
        throw new UnsupportedOperationException("TODO");
    }
}
