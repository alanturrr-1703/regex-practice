package com.example;

import java.util.List;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Objects;
import java.util.regex.Pattern;
import java.util.regex.Matcher;

/**
 * Solution for the "Log Line Anchor Parser" problem.
 *
 * <p>Problem: Parse structured log lines from a multiline string. Each valid
 * log line has the strict format:
 * <pre>
 *   [LEVEL] YYYY-MM-DD HH:MM:SS - Message text
 * </pre>
 * Where LEVEL is one of: DEBUG, INFO, WARN, ERROR.
 * Lines not matching this exact format must be silently ignored.
 *
 * <p>Return a {@code List<LogEntry>} with one entry per valid line.
 *
 * <p>Key concepts:
 * <ul>
 *   <li>{@code ^} with {@code Pattern.MULTILINE} — anchors to each line start</li>
 *   <li>{@code $} with {@code Pattern.MULTILINE} — anchors to each line end</li>
 *   <li>Named capturing groups for structured extraction</li>
 *   <li>Strict format enforcement — partial matches are rejected by anchors</li>
 * </ul>
 *
 * <p>TODO: Implement the {@link #parse(String)} method.
 *       Compile the pattern with {@code Pattern.MULTILINE}.
 *       Use named groups: {@code (?<level>...)}, {@code (?<date>...)},
 *       {@code (?<time>...)}, {@code (?<message>...)}
 *       Pattern (with MULTILINE):
 *       {@code ^\[(DEBUG|INFO|WARN|ERROR)\] (\d{4}-\d{2}-\d{2}) (\d{2}:\d{2}:\d{2}) - (.+)$}
 *       Return empty list for null or no-match input.
 */
public class Solution {

    /**
     * Immutable value object representing a parsed log entry.
     */
    public static final class LogEntry {
        /** The log level: DEBUG, INFO, WARN, or ERROR. */
        public final String level;
        /** The date in YYYY-MM-DD format. */
        public final String date;
        /** The time in HH:MM:SS format. */
        public final String time;
        /** The log message text. */
        public final String message;

        /**
         * Constructs a LogEntry with the given fields.
         *
         * @param level   log level (DEBUG/INFO/WARN/ERROR)
         * @param date    date string (YYYY-MM-DD)
         * @param time    time string (HH:MM:SS)
         * @param message log message text
         */
        public LogEntry(String level, String date, String time, String message) {
            this.level = level;
            this.date = date;
            this.time = time;
            this.message = message;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (!(o instanceof LogEntry)) return false;
            LogEntry other = (LogEntry) o;
            return Objects.equals(level, other.level)
                    && Objects.equals(date, other.date)
                    && Objects.equals(time, other.time)
                    && Objects.equals(message, other.message);
        }

        @Override
        public int hashCode() {
            return Objects.hash(level, date, time, message);
        }

        @Override
        public String toString() {
            return "LogEntry{level='" + level + "', date='" + date
                    + "', time='" + time + "', message='" + message + "'}";
        }
    }

    // TODO: Declare a static final Pattern field here.
    //       Use Pattern.MULTILINE so ^ matches each line start.
    //       Pattern:
    //         ^\[(DEBUG|INFO|WARN|ERROR)\] (\d{4}-\d{2}-\d{2}) (\d{2}:\d{2}:\d{2}) - (.+)$
    //       Java string with MULTILINE:
    //         Pattern.compile(
    //           "^\\[(DEBUG|INFO|WARN|ERROR)\\] (\\d{4}-\\d{2}-\\d{2}) " +
    //           "(\\d{2}:\\d{2}:\\d{2}) - (.+)$",
    //           Pattern.MULTILINE)
    //       Group numbers: 1=level, 2=date, 3=time, 4=message

    /**
     * Parses structured log lines from a multiline string.
     *
     * <p>Each valid log line must match the format:
     * {@code [LEVEL] YYYY-MM-DD HH:MM:SS - Message text}
     * Lines not matching this format are silently skipped.
     *
     * @param logText the multiline log string; may be {@code null}
     * @return list of {@link LogEntry} objects for each valid line; never {@code null}
     */
    public List<LogEntry> parse(String logText) {
        throw new UnsupportedOperationException("TODO");
    }
}
