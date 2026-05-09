package com.example;

import java.util.List;
import java.util.ArrayList;
import java.util.Collections;
import java.util.regex.Pattern;
import java.util.regex.Matcher;

/**
 * Solution for the "Multiline Section Parser" problem.
 *
 * <p>Problem: Given a multiline string (lines separated by {@code \n}),
 * extract all lines that start with {@code ">>"}. Return the content of
 * those lines (without the {@code >>} prefix) as a {@code List<String>}.
 * Trim leading/trailing whitespace from each extracted value.
 *
 * <p>Key concept: By default, {@code ^} only matches position 0 (the very start
 * of the entire input string). With {@code Pattern.MULTILINE}, {@code ^}
 * additionally matches after every {@code \n} — i.e., at the start of each line.
 *
 * <p>TODO: Implement this method.
 *       Compile the pattern with {@code Pattern.MULTILINE}.
 *       Pattern: {@code ^>>(.+)$} (with MULTILINE flag)
 *       Use a capturing group to extract the content after {@code >>}.
 *       Trim each captured group.
 *       Return empty list for null or no-match input.
 */
public class Solution {

    // TODO: Declare a static final Pattern field here.
    //       Remember: Pattern.MULTILINE makes ^ match at line starts AND $ at line ends.
    //       Pattern: ^>>(.+)$ compiled with Pattern.MULTILINE
    //       Java: Pattern.compile("^>>(.+)$", Pattern.MULTILINE)

    /**
     * Extracts the content of all lines beginning with {@code ">>"} from a
     * multiline string. The {@code >>} prefix is stripped and the content is trimmed.
     *
     * @param text the multiline input string; may be {@code null}
     * @return list of section contents (trimmed, without {@code >>}); never {@code null}
     */
    public List<String> extractSections(String text) {
        throw new UnsupportedOperationException("TODO");
    }
}
