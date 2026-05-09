package com.example;

import java.util.List;
import java.util.ArrayList;
import java.util.regex.Pattern;
import java.util.regex.Matcher;

/**
 * Log Severity Extractor
 *
 * <p>Your task: implement {@link #extractWarningsAndErrors(String)} that returns the
 * message text from log lines prefixed with [WARN] or [ERROR].
 *
 * <p>Key teaching point: a naive lookbehind {@code (?<=\[WARN\] |\[ERROR\] )} is
 * variable-length (7 vs 8 chars) and throws PatternSyntaxException in Java 8-13.
 *
 * <p>TODO: Use a capturing group instead of lookbehind for Java 8 compatibility:
 * <pre>
 *   \[(?:WARN|ERROR)\] (.+)
 * </pre>
 * Then extract group(1) for the message text.
 *
 * <p>TODO: The dot '.' does not match newlines by default — leverage this to
 * naturally stop each match at the end of a line.
 *
 * <p>TODO: Return an empty list (never null) for null/empty/no-matching input.
 */
public class Solution {

    // TODO: Add static Pattern field here.
    // private static final Pattern LOG_PATTERN = Pattern.compile("...");

    /**
     * Extracts message text from log lines with [WARN] or [ERROR] severity.
     *
     * <p>Format: "[SEVERITY] message text"
     * <p>Lines with INFO, DEBUG, TRACE, etc. are ignored.
     *
     * <p>Examples:
     * <ul>
     *   <li>"[ERROR] disk full\n[INFO] ok\n[WARN] low memory" → ["disk full", "low memory"]</li>
     *   <li>"[DEBUG] nothing" → []</li>
     * </ul>
     *
     * @param logText multi-line log string; may be null
     * @return list of message texts in order of appearance; never null
     */
    public List<String> extractWarningsAndErrors(String logText) {
        throw new UnsupportedOperationException("TODO");
    }
}
