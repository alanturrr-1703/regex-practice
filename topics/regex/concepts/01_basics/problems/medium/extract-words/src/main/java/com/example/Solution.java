package com.example;

import java.util.List;
import java.util.ArrayList;
import java.util.regex.Pattern;
import java.util.regex.Matcher;

/**
 * Problem: Extract all alphabetic words from a string.
 *
 * <p>A "word" is defined as a maximal contiguous sequence of ASCII letters (a-z, A-Z) only.
 * Digits, underscores, punctuation, and whitespace are NOT part of a word and act as separators.
 *
 * <p>Key concepts:
 * <ul>
 *   <li>{@code [a-zA-Z]} — character class matching only ASCII letters (NOT {@code \w} which
 *       also includes digits and underscore)</li>
 *   <li>{@code +} quantifier — one or more occurrences</li>
 *   <li>{@link Matcher#find()} loop — the standard pattern for extracting all matches</li>
 *   <li>{@link Matcher#group()} — returns the text matched by the most recent {@code find()}</li>
 * </ul>
 *
 * <p><b>Critical distinction:</b> {@code \w+} would return "foo_bar" as ONE token.
 * {@code [a-zA-Z]+} returns "foo" and "bar" as TWO tokens, because underscore is excluded.
 *
 * <p><b>TODO:</b>
 * <ol>
 *   <li>Compile the pattern {@code [a-zA-Z]+} (Java string: {@code "[a-zA-Z]+"}).</li>
 *   <li>Use the {@code while (m.find())} loop pattern.</li>
 *   <li>Collect {@code m.group()} for each match.</li>
 *   <li>Return the collected list (empty list if no matches).</li>
 * </ol>
 *
 * <p>Example:
 * <pre>
 *   extractWords("Hello, world! 123") → ["Hello", "world"]
 *   extractWords("foo_bar")           → ["foo", "bar"]
 *   extractWords("Java9Rocks")        → ["Java", "Rocks"]
 *   extractWords("  ")               → []
 * </pre>
 */
public class Solution {

    /**
     * Extracts all maximal sequences of ASCII alphabetic characters from {@code input}.
     *
     * @param input a non-null string (may be empty)
     * @return a new list of words in the order they appear; never null; may be empty
     * @throws UnsupportedOperationException until this method is implemented
     */
    public List<String> extractWords(String input) {
        throw new UnsupportedOperationException("TODO: implement extractWords");
    }
}
