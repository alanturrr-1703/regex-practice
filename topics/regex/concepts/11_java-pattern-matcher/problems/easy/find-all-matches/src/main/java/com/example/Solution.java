package com.example;

import java.util.List;
import java.util.ArrayList;
import java.util.regex.Pattern;
import java.util.regex.Matcher;

/**
 * Problem: Find All Matches
 * Difficulty: Easy
 * Concept: Java Pattern & Matcher API
 *
 * <p>Given a string and a regex pattern string, return a List<String> of ALL
 * non-overlapping matches found by scanning the input left-to-right.
 *
 * <p>This is the foundational {@code while (matcher.find())} loop that every
 * Java regex user must know.
 *
 * <p>TODO: Implement {@link #findAll(String, String)}.
 */
public class Solution {

    /**
     * Returns all non-overlapping substrings of {@code input} that match {@code regex},
     * in left-to-right order.
     *
     * <p>Algorithm sketch:
     * <ol>
     *   <li>Compile {@code regex} into a {@link Pattern}</li>
     *   <li>Create a {@link Matcher} from {@code input}</li>
     *   <li>Loop: {@code while (matcher.find())} — collect {@code matcher.group()}</li>
     *   <li>Return the collected list (empty if no matches)</li>
     * </ol>
     *
     * @param input the string to search in (never null, may be empty)
     * @param regex a valid Java regex pattern string
     * @return list of matched substrings in order; empty list if none found
     * @throws UnsupportedOperationException until implemented
     */
    public List<String> findAll(String input, String regex) {
        throw new UnsupportedOperationException("TODO");
    }
}
