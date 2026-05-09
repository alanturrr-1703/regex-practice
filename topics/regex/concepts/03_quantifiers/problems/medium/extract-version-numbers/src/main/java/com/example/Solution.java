package com.example;

import java.util.List;
import java.util.ArrayList;
import java.util.Collections;
import java.util.regex.Pattern;
import java.util.regex.Matcher;

/**
 * Solution for the "Extract Version Numbers" problem.
 *
 * <p>Problem: Given a string, find all semantic version strings of the form
 * {@code major.minor.patch} where each component is 1-3 digits. The version
 * must NOT be immediately surrounded by other digits or by a dot-digit sequence.
 *
 * <p>Key insight: The base pattern {@code \d{1,3}\.\d{1,3}\.\d{1,3}} is not
 * sufficient — it will match {@code "1.2.3"} from inside {@code "1.2.3.4"}.
 * You must add boundary assertions to prevent over-matching.
 *
 * <p>TODO: Implement this method.
 *       Build the pattern step by step:
 *       1. Core: {@code \d{1,3}\.\d{1,3}\.\d{1,3}}
 *       2. Lookbehind: {@code (?<!\d)} — not preceded by a digit
 *       3. Lookahead (no extra dot+digit): {@code (?!\.\d)}
 *       4. Lookahead (no extra digit): {@code (?!\d)}
 *       Use a while-find loop to collect all matches.
 *       Return empty list for null or no-match input.
 */
public class Solution {

    // TODO: Declare a static final Pattern field here.
    //       Pattern: (?<!\d)\d{1,3}\.\d{1,3}\.\d{1,3}(?!\.\d)(?!\d)
    //       Java string: "(?<!\\d)\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}(?!\\.\\d)(?!\\d)"

    /**
     * Extracts all valid semantic version strings ({@code X.Y.Z}) from the input.
     *
     * <p>Each component (X, Y, Z) must be 1–3 digits. The version must not be
     * adjacent to other digits or followed by additional dot-separated components.
     *
     * @param input the string to search; may be {@code null}
     * @return a list of matched version strings; never {@code null}
     */
    public List<String> extractVersions(String input) {
        throw new UnsupportedOperationException("TODO");
    }
}
