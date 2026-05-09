package com.example;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Solution for the <b>nested-groups-csv-parser</b> problem.
 *
 * <p>Your task: implement {@link #parseCsvLine(String)} to parse a single CSV line
 * where fields may optionally be quoted, and quoted fields may contain commas.</p>
 *
 * <h2>The challenge</h2>
 * <p>A field is either:</p>
 * <ul>
 *   <li><b>Quoted:</b> {@code "content"} — the surrounding quotes are stripped;
 *       the content may contain commas.</li>
 *   <li><b>Unquoted:</b> {@code content} — anything except comma and quote.</li>
 * </ul>
 *
 * <h2>Pattern structure</h2>
 * <pre>
 *   (?:^|,)                 ← non-capturing: start-of-line or comma separator
 *   (?:
 *     "([^"]*)"             ← QUOTED:   group 1 captures content inside quotes
 *     |
 *     ([^,"]*)              ← UNQUOTED: group 2 captures raw content (may be empty)
 *   )
 * </pre>
 *
 * <p>Java string: {@code "(?:^|,)(?:\"([^\"]*)\"|([^\",]*))"}</p>
 *
 * <h2>Key complication: null groups</h2>
 * <p>Since only one branch of the alternation fires per match:</p>
 * <ul>
 *   <li>If the quoted branch fires: {@code group(1)} has the content, {@code group(2)} is {@code null}</li>
 *   <li>If the unquoted branch fires: {@code group(2)} has the content, {@code group(1)} is {@code null}</li>
 * </ul>
 * <p>Check: {@code matcher.group(1) != null ? matcher.group(1) : matcher.group(2)}</p>
 *
 * <h2>TODOs</h2>
 * <ol>
 *   <li>Declare the {@code static final Pattern} with the pattern above.</li>
 *   <li>In a {@code while (matcher.find())} loop, extract each field by checking
 *       which group is non-null.</li>
 *   <li>Handle the empty unquoted field case: {@code group(2)} returns {@code ""} (not null)
 *       for an empty unquoted field — that is correct.</li>
 * </ol>
 */
public class Solution {

    /**
     * Parses a single CSV line and returns the field values.
     *
     * <p>Quoted fields have their surrounding quotes stripped. Quoted fields may
     * contain commas. Unquoted empty fields (consecutive commas) return empty strings.</p>
     *
     * @param line the CSV line to parse; never {@code null}
     * @return list of field strings in order; never {@code null}
     * @throws UnsupportedOperationException until the method is implemented
     */
    public List<String> parseCsvLine(String line) {
        throw new UnsupportedOperationException("TODO");
    }
}
