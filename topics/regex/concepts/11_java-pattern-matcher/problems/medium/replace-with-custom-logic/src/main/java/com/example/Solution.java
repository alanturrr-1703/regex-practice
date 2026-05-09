package com.example;

import java.util.regex.Pattern;
import java.util.regex.Matcher;

/**
 * Problem: Replace With Custom Logic
 * Difficulty: Medium
 * Concept: Java Pattern & Matcher API — appendReplacement() + appendTail()
 *
 * <p>Replace every number in the input by doubling its value.
 * This requires per-match custom logic: the replacement depends on the
 * matched number, so a static {@code replaceAll(String)} cannot be used.
 *
 * <p>Required API:
 * <ul>
 *   <li>{@link Matcher#appendReplacement(StringBuffer, String)} — appends
 *       the gap text and the computed replacement for each match</li>
 *   <li>{@link Matcher#appendTail(StringBuffer)} — appends text after the
 *       last match (MUST be called, or trailing text is lost)</li>
 * </ul>
 *
 * <p>TODO: Implement {@link #doubleNumbers(String)}.
 */
public class Solution {

    /**
     * Returns a copy of {@code input} with every non-negative integer replaced
     * by double its value.
     *
     * <p>Examples:
     * <ul>
     *   <li>"I have 3 cats and 10 dogs" → "I have 6 cats and 20 dogs"</li>
     *   <li>"no numbers" → "no numbers"</li>
     *   <li>"0 items" → "0 items"</li>
     * </ul>
     *
     * <p>Algorithm sketch:
     * <ol>
     *   <li>Compile pattern {@code \\d+}</li>
     *   <li>Create a {@link StringBuffer} for output</li>
     *   <li>Loop with {@code matcher.find()}, parse number, double it,
     *       call {@code appendReplacement}</li>
     *   <li>Call {@code appendTail} after the loop</li>
     * </ol>
     *
     * @param input the input string (never null)
     * @return the transformed string with all numbers doubled
     * @throws UnsupportedOperationException until implemented
     */
    public String doubleNumbers(String input) {
        throw new UnsupportedOperationException("TODO");
    }
}
