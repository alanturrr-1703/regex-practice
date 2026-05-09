package com.example;

import java.util.List;
import java.util.ArrayList;
import java.util.Collections;
import java.util.regex.Pattern;
import java.util.regex.Matcher;

/**
 * Solution for the "Parse Repeated Tokens" problem.
 *
 * <p>Problem: Given a pipe-delimited string of fields where each valid field
 * consists of 1 to 8 uppercase letters (A-Z), extract and return only the
 * valid fields. Invalid fields (wrong case, wrong length, empty) are ignored.
 *
 * <p>Key insight: Use a character class {@code [A-Z]} combined with a range
 * quantifier {@code {1,8}} to define exactly what constitutes a valid field.
 * The pattern should match 1-8 uppercase letters as a complete, isolated token.
 *
 * <p>TODO: Implement this method.
 *       Define the pattern: {@code [A-Z]{1,8}}
 *       You must ensure the match is a COMPLETE field (bounded on both sides
 *       by a pipe, start-of-string, or end-of-string), not a substring of a
 *       longer invalid field like "TOOLONGFIELD".
 *       Consider using word boundaries or lookahead/lookbehind.
 *       Use a while-find loop to collect all valid fields.
 *       Return an empty list for null or empty input.
 */
public class Solution {

    // TODO: Declare a static final Pattern field here.
    //       Think carefully: if the field "TOOLONGFIELD" (12 chars) appears,
    //       does [A-Z]{1,8} match "TOOLONG" as a prefix? Yes — you must prevent this.
    //       Strategy: assert that neither side of the match is an uppercase letter.
    //       Use: (?<![A-Z])[A-Z]{1,8}(?![A-Z])
    //       Java string: "(?<![A-Z])[A-Z]{1,8}(?![A-Z])"

    /**
     * Extracts all valid pipe-delimited fields from the input string.
     *
     * <p>A valid field contains exactly 1 to 8 uppercase letters (A–Z).
     * Fields with lowercase letters, digits, or more than 8 uppercase letters
     * are ignored. Pipe characters and other delimiters are not part of a field.
     *
     * @param input the pipe-delimited string; may be {@code null}
     * @return list of valid uppercase fields; never {@code null}
     */
    public List<String> parseFields(String input) {
        throw new UnsupportedOperationException("TODO");
    }
}
