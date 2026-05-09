package com.example;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Solution for the <b>extract-date-parts</b> problem.
 *
 * <p>Your task: implement {@link #extractDates(String)} so that it finds every
 * {@code YYYY-MM-DD} date embedded anywhere in {@code input} and returns the
 * three components (year, month, day) as a {@link DateParts} record.</p>
 *
 * <h2>Key concepts to apply</h2>
 * <ul>
 *   <li>Compile a {@link Pattern} with three capturing groups —
 *       one each for year, month, and day.</li>
 *   <li>Use {@code matcher.find()} in a {@code while} loop to iterate all matches.</li>
 *   <li>Read captured text with {@code matcher.group(1)}, {@code matcher.group(2)},
 *       {@code matcher.group(3)}.</li>
 *   <li>{@code matcher.group(0)} (or {@code matcher.group()}) is the entire match —
 *       <em>not</em> the year.</li>
 * </ul>
 *
 * <h2>Pattern hint</h2>
 * <pre>
 *   (\d{4})-(\d{2})-(\d{2})
 *     ^1      ^2      ^3
 * </pre>
 * In a Java string literal every {@code \} must be doubled:
 * {@code "(\\d{4})-(\\d{2})-(\\d{2})"}
 *
 * <h2>TODOs</h2>
 * <ol>
 *   <li>Declare a {@code private static final Pattern} constant with the pattern above.</li>
 *   <li>Inside {@code extractDates}, create a {@code Matcher} against {@code input}.</li>
 *   <li>Loop with {@code while (matcher.find())} and build one {@link DateParts}
 *       per iteration.</li>
 *   <li>Return the list; return an empty list for no-match cases.</li>
 * </ol>
 */
public class Solution {

    /**
     * Immutable value object for one parsed date.
     *
     * @param year  four-digit year string, e.g. {@code "2024"}
     * @param month two-digit month string, e.g. {@code "01"}
     * @param day   two-digit day string, e.g. {@code "15"}
     */
    public record DateParts(String year, String month, String day) {}

    /**
     * Finds all {@code YYYY-MM-DD} dates in {@code input} and returns their components.
     *
     * <p>Dates are not validated for calendar correctness — {@code "2024-13-99"} is a
     * valid structural match. The regex matches shape, not semantics.</p>
     *
     * @param input arbitrary text that may contain zero or more dates; never {@code null}
     * @return ordered list of {@link DateParts}; empty list if no dates are found
     * @throws UnsupportedOperationException until the method is implemented
     */
    public List<DateParts> extractDates(String input) {
        throw new UnsupportedOperationException("TODO");
    }
}
