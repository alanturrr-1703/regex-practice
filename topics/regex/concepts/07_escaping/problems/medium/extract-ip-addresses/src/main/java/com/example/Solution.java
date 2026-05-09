package com.example;

import java.util.List;
import java.util.ArrayList;
import java.util.regex.Pattern;
import java.util.regex.Matcher;

/**
 * Extracts all IPv4 addresses from a string.
 *
 * <p>IPv4 format: four groups of 1-3 digits, separated by literal dots.
 * Example: "192.168.1.1", "10.0.0.1", "255.255.255.0"
 *
 * <p>Key escaping challenge: the dot between octets MUST be a literal dot.
 * In regex: {@code \.} — in Java string: {@code "\\."}.
 * An unescaped {@code .} matches ANY character, which would incorrectly
 * match strings like "1X2Y3Z4".
 *
 * <p>Boundary challenge: "1.2.3.4.5" must NOT match. Use lookahead/lookbehind
 * to ensure the match is not surrounded by more digit-dot sequences.
 *
 * <p>Note: numeric range (0-255) is NOT validated by the regex.
 * "999.999.999.999" will match — that is acceptable.
 *
 * <p>TODO — implement {@link #extractIpAddresses(String)}:
 * <ul>
 *   <li>TODO: Build the four-octet pattern with escaped dots: {@code \\d{1,3}\\.\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}}</li>
 *   <li>TODO: Add left boundary: not preceded by a digit {@code (?<!\\d)}</li>
 *   <li>TODO: Add right boundary: not followed by dot+digit {@code (?!\\.\\d)}</li>
 *   <li>TODO: Compile as a static final Pattern field</li>
 *   <li>TODO: Use Matcher.find() loop to collect all matches</li>
 * </ul>
 */
public class Solution {

    // TODO: Declare your compiled Pattern here.
    // The dots between octets must be escaped as literal dots.
    // Add boundaries to prevent matching "1.2.3.4" inside "1.2.3.4.5".
    // private static final Pattern IP_PATTERN = Pattern.compile(/* your pattern */);

    /**
     * Extracts all structurally valid IPv4 addresses from the input string.
     *
     * @param input the text to search; never null
     * @return list of matched IPv4 address strings; empty if none found
     */
    public List<String> extractIpAddresses(String input) {
        throw new UnsupportedOperationException("TODO: implement this method");
    }
}
