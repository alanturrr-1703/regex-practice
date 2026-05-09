package com.example;

import java.util.List;
import java.util.ArrayList;
import java.util.regex.Pattern;
import java.util.regex.Matcher;

/**
 * Problem: Debug Missing Multiline Flag
 * Difficulty: Medium — Debugging
 * Concept: Pattern.MULTILINE changes ^ and $ to match line boundaries
 *
 * <p>A broken method tries to find all lines in a multiline string that
 * START with "#" (comment lines). The broken pattern uses {@code ^#.*} without
 * the {@code Pattern.MULTILINE} flag, so {@code ^} only matches the absolute
 * start of the ENTIRE input — not the start of each line.
 *
 * <p>BROKEN CODE:
 * <pre>
 *   Pattern broken = Pattern.compile("^#.*"); // no MULTILINE flag
 * </pre>
 *
 * <p>With this broken pattern:
 * <ul>
 *   <li>On {@code "# comment\ncode\n# another"}: finds 0 matches if the string
 *       doesn't start with "#", or only 1 match if it does (just the first line)</li>
 *   <li>{@code ^} only anchors to position 0 of the entire input string</li>
 * </ul>
 *
 * <p>Fix: add {@code Pattern.MULTILINE} flag so {@code ^} matches at the start
 * of every line (after every {@code \n}).
 *
 * <p>TODO: Implement {@link #extractCommentLines(String)}.
 */
public class Solution {

    /**
     * Returns all lines in {@code text} that begin with the {@code #} character.
     *
     * <p>A "line" is delimited by {@code \n}. The returned strings include the
     * leading {@code #} and any text on that line.
     *
     * <p>BROKEN pattern (do NOT use):
     * <pre>
     *   Pattern.compile("^#.*")  // missing Pattern.MULTILINE
     * </pre>
     *
     * <p>Fix: compile with {@code Pattern.MULTILINE}:
     * <pre>
     *   Pattern.compile("^#.*", Pattern.MULTILINE)
     * </pre>
     *
     * @param text the multiline text to search (never null)
     * @return list of comment lines (including the {@code #}); empty list if none
     * @throws UnsupportedOperationException until implemented
     */
    public List<String> extractCommentLines(String text) {
        // BROKEN: ^#.* without MULTILINE only matches if text starts with "#"
        // Pattern broken = Pattern.compile("^#.*");

        throw new UnsupportedOperationException("TODO");
    }
}
