package com.example;

import java.util.List;
import java.util.ArrayList;
import java.util.regex.Pattern;
import java.util.regex.Matcher;

/**
 * Problem: Extract restricted tokens from encoded data.
 *
 * <p>A restricted token is a maximal contiguous sequence of characters from:
 * <ul>
 *   <li>Lowercase letters {@code a} through {@code m} (code points 97–109)</li>
 *   <li>Digits {@code 0} through {@code 4} (code points 48–52)</li>
 * </ul>
 *
 * <p>All other characters act as separators and are not included in tokens.
 *
 * <p>Key concepts:
 * <ul>
 *   <li>{@code [a-m0-4]} — combined character class with two custom ranges</li>
 *   <li>{@code +} quantifier — maximal (greedy) run of matching characters</li>
 *   <li>Range boundaries: {@code m} (code point 109) is the last included letter;
 *       {@code n} (110) is excluded. {@code 4} (52) is the last included digit;
 *       {@code 5} (53) is excluded.</li>
 * </ul>
 *
 * <p><b>TODO:</b>
 * <ol>
 *   <li>Compile the pattern {@code [a-m0-4]+} as a Java string {@code "[a-m0-4]+"}.</li>
 *   <li>Use the {@code find()} loop to collect all matches.</li>
 *   <li>Return the list of matched tokens.</li>
 * </ol>
 *
 * <p>Example:
 * <pre>
 *   extractRestrictedTokens("abc123xyz567") → ["abc123"]  (x,5,6,7 out of range)
 *   extractRestrictedTokens("hello")        → ["hell"]    (o out of range a-m)
 *   extractRestrictedTokens("aaa444")       → ["aaa444"]  (4 is in range 0-4)
 *   extractRestrictedTokens("xyz999")       → []          (all chars out of range)
 * </pre>
 */
public class Solution {

    /**
     * Extracts all maximal tokens consisting only of characters in {@code [a-m0-4]}.
     *
     * @param input a non-null string (may be empty)
     * @return list of restricted tokens in order of appearance; never null; may be empty
     * @throws UnsupportedOperationException until this method is implemented
     */
    public List<String> extractRestrictedTokens(String input) {
        throw new UnsupportedOperationException("TODO: implement extractRestrictedTokens");
    }
}
