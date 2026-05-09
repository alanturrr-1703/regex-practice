package com.example;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Solution for the <b>backreference-duplicate-word</b> problem.
 *
 * <p>Your task: implement {@link #findDuplicateWords(String)} to find consecutive
 * duplicate words using a regex backreference.</p>
 *
 * <h2>Concept: backreferences</h2>
 * <p>A backreference {@code \1} in a pattern matches the <em>same text</em> that
 * was captured by group 1 earlier in the same match attempt. Unlike a pattern
 * reference, it does not re-evaluate the group's pattern — it matches the literal
 * string that was stored in the capture buffer.</p>
 *
 * <h2>Pattern to build</h2>
 * <pre>
 *   \b(\w+)\s+\1\b
 *   ^^  ^1  ^^  ^^
 *   |   |   |   word boundary after the second occurrence
 *   |   |   one-or-more whitespace
 *   |   capturing group 1: one or more word chars
 *   word boundary before the first occurrence
 * </pre>
 *
 * <h2>Java string form</h2>
 * {@code "\\b(\\w+)\\s+\\1\\b"}
 * Every {@code \} in regex becomes {@code \\} in a Java string literal.
 *
 * <h2>Case-insensitive flag</h2>
 * Pass {@code Pattern.CASE_INSENSITIVE} so that "Hello" and "hello" are treated
 * as the same word. The backreference also becomes case-insensitive.
 *
 * <h2>TODOs</h2>
 * <ol>
 *   <li>Declare a {@code private static final Pattern} constant with the backreference
 *       pattern and the {@code CASE_INSENSITIVE} flag.</li>
 *   <li>In {@code findDuplicateWords}, loop with {@code while (matcher.find())}.</li>
 *   <li>Add {@code matcher.group(1)} (the first word, not the pair) to the result list.</li>
 * </ol>
 */
public class Solution {

    /**
     * Finds all consecutive duplicate words in {@code input}.
     *
     * <p>A duplicate is a word followed immediately (modulo whitespace) by the same word
     * again. Matching is case-insensitive. The returned word is the first occurrence
     * as captured from the input (preserves original casing).</p>
     *
     * @param input the string to scan; never {@code null}
     * @return list of duplicated words in the order they appear; empty if none found
     * @throws UnsupportedOperationException until the method is implemented
     */
    public List<String> findDuplicateWords(String input) {
        throw new UnsupportedOperationException("TODO");
    }
}
