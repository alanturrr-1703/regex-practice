package com.example;

import java.util.List;
import java.util.ArrayList;
import java.util.regex.Pattern;
import java.util.regex.Matcher;

/**
 * Problem: Extract all CSS hex color codes from a string.
 *
 * <p>A valid hex color is: {@code #} followed by exactly 6 hexadecimal digits.
 * Hexadecimal digits are {@code 0-9}, {@code a-f}, {@code A-F}.
 * 3-digit shorthand ({@code #fff}) is NOT matched.
 *
 * <p>Key concepts:
 * <ul>
 *   <li>{@code [0-9a-fA-F]} — multi-range character class for hex digits</li>
 *   <li>{@code {6}} — exact repetition: exactly 6 occurrences</li>
 *   <li>Literal {@code #} in the pattern</li>
 *   <li>{@link Matcher#find()} loop to extract all matches</li>
 * </ul>
 *
 * <p><b>TODO:</b>
 * <ol>
 *   <li>Build the pattern: {@code #} followed by {@code [0-9a-fA-F]{6}}.</li>
 *   <li>Use the {@code find()} loop to collect all matches.</li>
 *   <li>Return the list including the {@code #} in each result.</li>
 * </ol>
 *
 * <p>Example:
 * <pre>
 *   extractHexColors("color: #ff0000 and #ABCDEF") → ["#ff0000", "#ABCDEF"]
 *   extractHexColors("no colors here")             → []
 *   extractHexColors("#xyz123")                    → []
 *   extractHexColors("#fff")                       → []
 *   extractHexColors("background:#001122;")        → ["#001122"]
 * </pre>
 */
public class Solution {

    /**
     * Extracts all 6-digit CSS hex color codes (including the {@code #} prefix) from {@code input}.
     *
     * @param input a non-null string (may be empty)
     * @return list of hex color strings in order of appearance; never null; may be empty
     * @throws UnsupportedOperationException until this method is implemented
     */
    public List<String> extractHexColors(String input) {
        throw new UnsupportedOperationException("TODO: implement extractHexColors");
    }
}
