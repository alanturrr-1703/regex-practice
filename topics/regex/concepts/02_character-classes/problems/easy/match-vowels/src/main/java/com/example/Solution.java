package com.example;

import java.util.regex.Pattern;
import java.util.regex.Matcher;

/**
 * Problem: Count the number of vowels in a string.
 *
 * <p>Vowels are: a, e, i, o, u — case-insensitive. Only the five traditional
 * English vowels count.
 *
 * <p>Key concepts:
 * <ul>
 *   <li>Enumerated character class {@code [aeiou]} — matches one of these chars</li>
 *   <li>{@link Pattern#CASE_INSENSITIVE} flag — makes {@code [aeiou]} match uppercase too</li>
 *   <li>{@link Matcher#find()} counting loop — call {@code find()} in a while-loop,
 *       incrementing a counter each time</li>
 * </ul>
 *
 * <p><b>TODO:</b>
 * <ol>
 *   <li>Compile a pattern that matches a single vowel, case-insensitively.</li>
 *   <li>Loop with {@code Matcher.find()}, incrementing a counter for each match.</li>
 *   <li>Return the count.</li>
 * </ol>
 *
 * <p>Example:
 * <pre>
 *   countVowels("Hello World") → 3   (e, o, o)
 *   countVowels("AEIOU")       → 5
 *   countVowels("rhythm")      → 0
 *   countVowels("")            → 0
 *   countVowels("Beautiful")   → 5   (e, a, u, i, u)
 * </pre>
 */
public class Solution {

    /**
     * Counts the number of vowel characters (a, e, i, o, u — case-insensitive) in {@code input}.
     *
     * @param input a non-null string (may be empty)
     * @return the count of vowel characters; 0 if none found
     * @throws UnsupportedOperationException until this method is implemented
     */
    public int countVowels(String input) {
        throw new UnsupportedOperationException("TODO: implement countVowels");
    }
}
