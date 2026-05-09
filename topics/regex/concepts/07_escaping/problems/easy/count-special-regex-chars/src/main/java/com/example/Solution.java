package com.example;

import java.util.regex.Pattern;
import java.util.regex.Matcher;

/**
 * Counts the number of regex metacharacters in the input string.
 *
 * <p>Regex metacharacters (14 total): . * + ? ^ $ { } [ ] | ( ) \
 *
 * <p>This problem forces you to build a character class that includes
 * all 14 metacharacters. Inside a character class [...], most metacharacters
 * lose their special meaning — but not all. You must handle:
 * <ul>
 *   <li>{@code \} — still an escape character inside a class</li>
 *   <li>{@code ^} — negates the class if it is the first character</li>
 *   <li>{@code -} — creates a range if between two characters</li>
 *   <li>{@code ]} — closes the class</li>
 * </ul>
 *
 * <p>TODO — implement {@link #countSpecialChars(String)}:
 * <ul>
 *   <li>TODO: Build a character class containing all 14 metacharacters</li>
 *   <li>TODO: Position '-' at start or end to avoid creating a range</li>
 *   <li>TODO: Escape '\' as '\\\\' in the Java string (= '\\' in regex = one backslash)</li>
 *   <li>TODO: Use Matcher.find() in a loop and count each match</li>
 *   <li>TODO: Print pattern.pattern() to verify the engine sees what you intend</li>
 * </ul>
 */
public class Solution {

    // TODO: Declare a static final Pattern that matches any one regex metacharacter.
    // The 14 metacharacters are: . * + ? ^ $ { } [ ] | ( ) \
    // Think carefully about the escaping required inside a character class.
    // private static final Pattern META_PATTERN = Pattern.compile(/* your pattern here */);

    /**
     * Counts occurrences of regex metacharacters in the input string.
     *
     * @param input the string to inspect; never null
     * @return the count of metacharacter occurrences; 0 if none or if input is empty
     */
    public int countSpecialChars(String input) {
        throw new UnsupportedOperationException("TODO: implement this method");
    }
}
