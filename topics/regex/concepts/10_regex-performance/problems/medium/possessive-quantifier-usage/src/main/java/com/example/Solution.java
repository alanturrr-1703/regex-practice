package com.example;

import java.util.Optional;
import java.util.regex.Pattern;
import java.util.regex.Matcher;

/**
 * Possessive Quantifier Usage — Log Line Tokenizer
 *
 * <p>Your task: implement {@link #tokenize(String)} using a Pattern with possessive
 * quantifiers ({@code ++}, {@code *+}) to parse a log line into timestamp, severity,
 * and message fields without any backtracking.
 *
 * <p>Log format: {@code HH:MM:SS SEVERITY message text}
 * <p>Example: {@code "14:23:01 ERROR disk full"}
 *
 * <p>TODO: Define a static Pattern using possessive quantifiers:
 * <pre>
 *   ^([\d:]++) ([A-Z]++) (.*)$
 * </pre>
 * Possessive {@code ++} is safe because the character sets are disjoint from the
 * space separators.
 *
 * <p>TODO: Return Optional.empty() for null input or lines not matching the format.
 *
 * <p>TODO: Extract group(1)=timestamp, group(2)=severity, group(3)=message.
 */
public class Solution {

    /**
     * Immutable container for a parsed log line.
     */
    public static class TokenizedLine {
        private final String timestamp;
        private final String severity;
        private final String message;

        public TokenizedLine(String timestamp, String severity, String message) {
            this.timestamp = timestamp;
            this.severity = severity;
            this.message = message;
        }

        public String getTimestamp() { return timestamp; }
        public String getSeverity()  { return severity;  }
        public String getMessage()   { return message;   }

        @Override
        public String toString() {
            return "TokenizedLine{timestamp='" + timestamp + "', severity='" +
                   severity + "', message='" + message + "'}";
        }
    }

    // TODO: Add static Pattern field here using possessive quantifiers.
    // private static final Pattern LOG_PATTERN = Pattern.compile("...");

    /**
     * Parses a log line of format "HH:MM:SS SEVERITY message" into its three components.
     *
     * <p>Uses possessive quantifiers for efficient, non-backtracking tokenization.
     *
     * <p>Examples:
     * <ul>
     *   <li>"14:23:01 ERROR some message" → TokenizedLine("14:23:01", "ERROR", "some message")</li>
     *   <li>"not a log line" → Optional.empty()</li>
     *   <li>null → Optional.empty()</li>
     * </ul>
     *
     * @param line the log line to parse; may be null
     * @return Optional containing the parsed tokens, or empty if format doesn't match
     */
    public Optional<TokenizedLine> tokenize(String line) {
        throw new UnsupportedOperationException("TODO");
    }
}
