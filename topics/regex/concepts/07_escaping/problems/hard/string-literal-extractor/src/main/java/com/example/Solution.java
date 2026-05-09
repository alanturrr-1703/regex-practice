package com.example;

import java.util.List;
import java.util.ArrayList;
import java.util.regex.Pattern;
import java.util.regex.Matcher;

/**
 * Extracts the content of all double-quoted string literals from Java source code,
 * correctly handling escaped double-quotes inside strings.
 *
 * <p>The canonical pattern for matching content of a delimited string with escaping:
 * <pre>
 *   "((?:[^"\\]|\\.)*)"
 * </pre>
 * Where:
 * <ul>
 *   <li>{@code "} — opening literal double-quote</li>
 *   <li>{@code (?:[^"\\]|\\.)*} — zero or more of: (non-quote non-backslash) OR (backslash + any)</li>
 *   <li>{@code "} — closing literal double-quote</li>
 * </ul>
 *
 * <p>Why this works: when the engine encounters a {@code \}, it takes the {@code \\.} branch
 * and consumes TWO characters (backslash + following char). This means {@code \"} is consumed
 * as an escape sequence and does NOT terminate the string.
 *
 * <p>Java escaping note: the regex {@code "} (literal double-quote) must be written as
 * {@code \"} inside a Java string. The regex {@code \\} (for the backslash in the
 * character class) becomes {@code \\\\} in Java source. Count carefully.
 *
 * <p>TODO — implement {@link #extractStringLiterals(String)}:
 * <ul>
 *   <li>TODO: Build pattern: {@code "\"((?:[^\"\\\\]|\\\\.)*)\""} — and verify by printing it</li>
 *   <li>TODO: The content is in group(1) — NOT group(0) which includes the surrounding quotes</li>
 *   <li>TODO: Use Matcher.find() loop and collect group(1) for each match</li>
 *   <li>TODO: An empty string literal {@code ""} should produce an empty-string entry in the list</li>
 * </ul>
 */
public class Solution {

    // TODO: Declare your compiled Pattern here as a static final field.
    // The pattern must match a double-quoted string that may contain \" inside.
    // Hint: the core content pattern is: (?:[^"\\]|\\.)*
    // Full pattern: "((?:[^"\\]|\\.)*)"
    // In Java string: "\"((?:[^\"\\\\]|\\\\.)*)\""
    // VERIFY by printing: pattern.pattern() — should show:  "((?:[^"\\]|\\.)*)"
    // private static final Pattern STRING_LITERAL = Pattern.compile(/* your pattern */);

    /**
     * Extracts the content of all double-quoted string literals from the given
     * Java source code snippet. Returns the content without the surrounding quotes
     * and without de-escaping (backslash sequences are returned as-is).
     *
     * @param javaCode a string of Java source code; never null
     * @return list of string literal contents; empty if no string literals found
     */
    public List<String> extractStringLiterals(String javaCode) {
        throw new UnsupportedOperationException("TODO: implement this method");
    }
}
