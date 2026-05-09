package com.example;

import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Solution for the <b>capture-first-word</b> problem.
 *
 * <p>Your task: implement {@link #captureFirstWord(String)} so that it returns
 * the first word (sequence of {@code \w} characters) found anywhere in {@code input},
 * wrapped in an {@link Optional}. Return {@link Optional#empty()} if no word exists.</p>
 *
 * <h2>Key concepts to apply</h2>
 * <ul>
 *   <li>{@code group(0)} is the entire matched text; {@code group(1)} is capturing
 *       group 1. For the pattern {@code (\w+)} they are the same. For the pattern
 *       {@code \s*(\w+)} they differ (group 0 includes leading spaces).</li>
 *   <li>Use {@code matcher.find()}, not {@code matcher.matches()}.
 *       {@code matches()} requires the ENTIRE input to match the pattern.</li>
 *   <li>Always check the boolean return of {@code find()} before calling {@code group()};
 *       calling {@code group()} on a non-matching Matcher throws
 *       {@link IllegalStateException}.</li>
 * </ul>
 *
 * <h2>Pattern</h2>
 * <pre>
 *   (\w+)   — one or more word characters, captured in group 1
 * </pre>
 * Java string: {@code "(\\w+)"}
 *
 * <h2>TODOs</h2>
 * <ol>
 *   <li>Declare a {@code private static final Pattern} constant.</li>
 *   <li>Create a {@code Matcher} against {@code input}.</li>
 *   <li>If {@code find()} returns {@code true}, return {@code Optional.of(group(1))}.</li>
 *   <li>Otherwise return {@code Optional.empty()}.</li>
 * </ol>
 */
public class Solution {

    /**
     * Returns the first word (sequence of {@code \w+} characters) found in {@code input}.
     *
     * <p>Leading non-word characters are skipped automatically by {@code find()}.
     * Digits and underscores are word characters.</p>
     *
     * @param input the string to search; never {@code null}
     * @return {@link Optional} containing the first word, or empty if none found
     * @throws UnsupportedOperationException until the method is implemented
     */
    public Optional<String> captureFirstWord(String input) {
        throw new UnsupportedOperationException("TODO");
    }
}
