package com.example;

import java.util.List;
import java.util.ArrayList;
import java.util.regex.Pattern;
import java.util.regex.Matcher;

/**
 * Problem: Fix Greedy Overcapture
 * Difficulty: Easy — Debugging
 * Concept: Greedy quantifier overcapture; lazy vs negated-class fix
 *
 * <p>A broken regex tries to extract title contents from HTML title tags using
 * a greedy {@code .*} — which spans across multiple tags, capturing far too much.
 *
 * <p>BROKEN pattern (do NOT use):
 * <pre>
 *   Pattern.compile("&lt;title&gt;.*&lt;/title&gt;")
 * </pre>
 * On {@code "<title>First</title><title>Second</title>"}, the greedy {@code .*}
 * matches {@code "First</title><title>Second"} — the entire content between the
 * FIRST opening tag and the LAST closing tag.
 *
 * <p>Your task: fix the pattern so it correctly extracts each title separately.
 * Use either a lazy quantifier ({@code .*?}) or a negated character class ({@code [^<]*}).
 *
 * <p>TODO: Implement {@link #extractTitles(String)}.
 */
public class Solution {

    /**
     * Extracts the text content of every {@code <title>...</title>} tag in
     * {@code html} and returns them in a list.
     *
     * <p>The broken pattern {@code <title>.*</title>} uses greedy {@code .*}
     * which spans across tags. Fix it by changing {@code .*} to:
     * <ul>
     *   <li>{@code .*?} (lazy: match minimum), OR</li>
     *   <li>{@code [^<]*} (negated class: stop at any {@code <})</li>
     * </ul>
     *
     * <p>Use a capturing group to extract the title content (not the tags).
     *
     * @param html the HTML string to parse (never null)
     * @return list of title contents in document order; empty list if none found
     * @throws UnsupportedOperationException until implemented
     */
    public List<String> extractTitles(String html) {
        // BROKEN (greedy overcapture — do NOT use this):
        // Pattern broken = Pattern.compile("<title>.*</title>");

        throw new UnsupportedOperationException("TODO");
    }
}
