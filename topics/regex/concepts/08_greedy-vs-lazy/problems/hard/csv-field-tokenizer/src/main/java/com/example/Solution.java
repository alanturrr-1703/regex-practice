package com.example;

import java.util.List;
import java.util.ArrayList;
import java.util.regex.Pattern;
import java.util.regex.Matcher;

/**
 * Parses a multi-line CSV string into a list of rows, each row a list of field strings.
 *
 * <p>This problem teaches that regex quantifier mode (greedy, lazy, possessive)
 * is NOT always the answer. Sometimes, a PRECISE character class is the right tool:
 * <ul>
 *   <li>Quoted field: {@code "([^"]*)"} — content cannot contain {@code "}, so use negated class</li>
 *   <li>Unquoted field: {@code ([^,\n]*)} — content cannot contain {@code ,} or newline</li>
 * </ul>
 *
 * <p>The combined token pattern uses alternation:
 * {@code "([^"]*)"} OR {@code ([^,\n]*)}
 *
 * <p>Naive splitting on {@code ,} BREAKS on quoted fields that contain commas.
 * The token-scanning approach avoids this by matching quoted fields as atomic units.
 *
 * <p>Constraints:
 * <ul>
 *   <li>Quoted fields: no escaped quotes inside (simplified)</li>
 *   <li>Empty fields: allowed (consecutive commas)</li>
 *   <li>Rows separated by {@code \n}</li>
 * </ul>
 *
 * <p>TODO — implement {@link #parseCsv(String)}:
 * <ul>
 *   <li>TODO: Split input into lines on {@code \n}</li>
 *   <li>TODO: For each non-empty line, scan for field tokens using the combined pattern</li>
 *   <li>TODO: The pattern: {@code "\"([^\"]*)\"|([^,\\n]*)"}</li>
 *   <li>TODO: Check group(1) for quoted content, group(2) for unquoted content</li>
 *   <li>TODO: After each match, check if next char is a comma and advance past it</li>
 *   <li>TODO: Handle empty fields: two consecutive commas produce an empty-string field</li>
 * </ul>
 */
public class Solution {

    // TODO: Declare a compiled Pattern for a single CSV field token.
    // Pattern matches EITHER a quoted field OR an unquoted field.
    // Quoted: "([^"]*)" — content in group 1
    // Unquoted: ([^,\n]*) — content in group 2
    // In Java string: "\"([^\"]*)\"|([^,\\n]*)"
    // private static final Pattern FIELD_PATTERN = Pattern.compile(/* your pattern */);

    /**
     * Parses a CSV-formatted string into a list of rows.
     * Each row is a list of field values. Quoted fields may contain commas.
     *
     * @param text the CSV text; never null; rows separated by newline
     * @return list of rows, each row a list of field strings; empty if input is empty
     */
    public List<List<String>> parseCsv(String text) {
        throw new UnsupportedOperationException("TODO: implement this method");
    }
}
