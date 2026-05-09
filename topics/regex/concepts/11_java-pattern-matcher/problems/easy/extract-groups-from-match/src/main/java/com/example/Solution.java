package com.example;

import java.util.Map;
import java.util.LinkedHashMap;
import java.util.regex.Pattern;
import java.util.regex.Matcher;

/**
 * Problem: Extract Groups From Match
 * Difficulty: Easy
 * Concept: Java Pattern & Matcher API — Capturing Groups
 *
 * <p>Given a string with {@code key=value} pairs, extract all pairs into a Map.
 * Use the pattern {@code (\w+)=(\w+)} and {@code matcher.group(1)} for the key,
 * {@code matcher.group(2)} for the value.
 *
 * <p>Key teaching: group numbering starts at 1. group(0) is the entire match.
 *
 * <p>TODO: Implement {@link #extractKeyValuePairs(String)}.
 */
public class Solution {

    /**
     * Extracts all {@code key=value} pairs from {@code input} and returns them as a map.
     *
     * <p>Pattern to use: {@code (\w+)=(\w+)}
     * <ul>
     *   <li>group(1) — the key (word characters before {@code =})</li>
     *   <li>group(2) — the value (word characters after {@code =})</li>
     * </ul>
     *
     * <p>Tokens that do not match the pattern are silently ignored.
     * If the same key appears multiple times, the last occurrence wins.
     *
     * @param input the input string (never null, may be empty)
     * @return map of key-value pairs; empty map if no pairs found
     * @throws UnsupportedOperationException until implemented
     */
    public Map<String, String> extractKeyValuePairs(String input) {
        throw new UnsupportedOperationException("TODO");
    }
}
