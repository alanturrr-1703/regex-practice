package com.example;

import java.util.List;
import java.util.ArrayList;
import java.util.regex.Pattern;
import java.util.regex.Matcher;

/**
 * Extracts the content of block comments (/* ... *{@literal /}) from source code.
 * Comments may span multiple lines.
 *
 * <p>Key concept: {@code .} does NOT match newline by default.
 * To match multiline comment content, you MUST use {@link Pattern#DOTALL}.
 *
 * <p>Without DOTALL: {@code /\*.*?\* /} — fails on multiline comments
 * because {@code .} won't match {@code \n}.
 *
 * <p>With DOTALL: {@code /\*.*?\* /} with {@code Pattern.DOTALL} — {@code .} matches
 * any character including newline, and lazy {@code .*?} stops at the first {@code *&#47;}.
 *
 * <p>TODO — implement {@link #extractBlockComments(String)}:
 * <ul>
 *   <li>TODO: Escape the forward slash and asterisk appropriately (/ needs no escape, * does in some positions)</li>
 *   <li>TODO: Use LAZY quantifier {@code .*?} to stop at the FIRST closing {@code *&#47;}</li>
 *   <li>TODO: Pass {@link Pattern#DOTALL} as the second argument to {@link Pattern#compile(String, int)}</li>
 *   <li>TODO: Capture group 1 is the content (without the surrounding comment markers)</li>
 *   <li>TODO: Trim the captured content or return it as-is — be consistent and document your choice</li>
 * </ul>
 */
public class Solution {

    // TODO: Compile a Pattern that matches /* content */ including across multiple lines.
    // The pattern for the comment markers: /\* and \*/
    // In Java string: "/\\*" for /* and "\\*/" for */
    // Content: (.*?) with Pattern.DOTALL flag
    // Full: Pattern.compile("/\\*(.*?)\\*/", Pattern.DOTALL)
    // private static final Pattern COMMENT_PATTERN = Pattern.compile(/* your pattern */, Pattern.DOTALL);

    /**
     * Extracts the raw content between {@code /*} and {@code *&#47;} comment markers.
     * Handles multiline comments. Multiple comments in the input are all extracted.
     *
     * <p>The returned content includes leading/trailing whitespace as-is (no trimming).
     *
     * @param code the source code string; never null
     * @return list of comment contents (without the surrounding markers); empty if none found
     */
    public List<String> extractBlockComments(String code) {
        throw new UnsupportedOperationException("TODO: implement this method");
    }
}
