package com.example;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Solution for the <b>match-log-levels</b> problem.
 *
 * <p>Your task: implement {@link #containsLogLevel(String)} to return {@code true}
 * if the input contains any of the log level keywords as a standalone word.</p>
 *
 * <h2>Keywords</h2>
 * <p>{@code DEBUG}, {@code INFO}, {@code WARN}, {@code ERROR}, {@code FATAL} — case-sensitive.</p>
 *
 * <h2>Key concepts</h2>
 * <ul>
 *   <li><b>Alternation:</b> {@code DEBUG|INFO|WARN|ERROR|FATAL} — not a character class!</li>
 *   <li><b>Word boundaries:</b> {@code \b...\b} — ensures "INFORMATION" does not match "INFO"</li>
 *   <li><b>Grouping:</b> {@code (?:DEBUG|INFO|WARN|ERROR|FATAL)} — scope the alternation so
 *       word boundaries apply to the whole keyword, not just the last alternative</li>
 *   <li><b>{@code find()} not {@code matches()}:</b> the keyword can appear anywhere in the string</li>
 * </ul>
 *
 * <h2>Pattern</h2>
 * <pre>
 *   \b(?:DEBUG|INFO|WARN|ERROR|FATAL)\b
 * </pre>
 * Java string: {@code "\\b(?:DEBUG|INFO|WARN|ERROR|FATAL)\\b"}
 *
 * <h2>The character class trap</h2>
 * <p>{@code [DEBUG|INFO|WARN|ERROR|FATAL]} is a character class matching ONE character
 * from the set D, E, B, U, G, |, I, N, F, O, W, A, R, T, L. That is NOT alternation.
 * The correct syntax is the pipe outside of brackets.</p>
 *
 * <h2>TODOs</h2>
 * <ol>
 *   <li>Declare a {@code private static final Pattern} with the alternation pattern
 *       wrapped in word boundaries.</li>
 *   <li>Return {@code matcher.find()} — do NOT use {@code matches()}.</li>
 * </ol>
 */
public class Solution {

    /**
     * Returns {@code true} if {@code input} contains one of the log level keywords
     * {@code DEBUG}, {@code INFO}, {@code WARN}, {@code ERROR}, or {@code FATAL}
     * as a standalone word.
     *
     * <p>Matching is case-sensitive. "debug", "INFORMATION", and "WARNING" do not match.</p>
     *
     * @param input the string to search; never {@code null}
     * @return {@code true} if a log level keyword is found as a complete word
     * @throws UnsupportedOperationException until the method is implemented
     */
    public boolean containsLogLevel(String input) {
        throw new UnsupportedOperationException("TODO");
    }
}
