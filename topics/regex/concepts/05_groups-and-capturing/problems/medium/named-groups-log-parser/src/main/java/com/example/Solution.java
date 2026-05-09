package com.example;

import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Solution for the <b>named-groups-log-parser</b> problem.
 *
 * <p>Your task: implement {@link #parseLine(String)} to parse a single Apache
 * Combined Log Format line using <em>named</em> capturing groups.</p>
 *
 * <h2>Log format</h2>
 * <pre>
 *   IP - - [DATE] "METHOD /path HTTP/1.1" STATUS BYTES
 *   127.0.0.1 - - [10/Oct/2024:13:55:36 +0000] "GET /index.html HTTP/1.1" 200 1234
 * </pre>
 *
 * <h2>Named groups to define</h2>
 * <ul>
 *   <li>{@code (?<ip>...)}     — client IP address</li>
 *   <li>{@code (?<date>...)}   — timestamp inside {@code [...]}, without the brackets</li>
 *   <li>{@code (?<method>...)} — HTTP method (GET, POST, etc.)</li>
 *   <li>{@code (?<path>...)}   — request path (may include query string)</li>
 *   <li>{@code (?<status>...)} — 3-digit HTTP status code</li>
 *   <li>{@code (?<bytes>...)}  — bytes sent; can be digits or {@code -}</li>
 * </ul>
 *
 * <h2>Escaping reminders</h2>
 * <ul>
 *   <li>{@code [} and {@code ]} must be escaped as {@code \[} and {@code \]}</li>
 *   <li>In Java strings: {@code "\\["} and {@code "\\]"}</li>
 *   <li>To match "anything except {@code ]}" in a class: {@code [^\]]} in regex,
 *       {@code "[^\\]]"} in Java</li>
 * </ul>
 *
 * <h2>TODOs</h2>
 * <ol>
 *   <li>Build the full log pattern with all 6 named groups as a
 *       {@code private static final Pattern}.</li>
 *   <li>Use {@code matcher.matches()} (not {@code find()}) to require a complete-line match.</li>
 *   <li>On success, construct and return {@code Optional.of(new LogEntry(...))}
 *       using {@code matcher.group("name")} for each field.</li>
 *   <li>On failure, return {@code Optional.empty()}.</li>
 * </ol>
 */
public class Solution {

    /**
     * Immutable value object representing one parsed Apache log line.
     *
     * @param ip     client IP address
     * @param date   timestamp string (content inside brackets, without brackets)
     * @param method HTTP method, e.g. {@code "GET"}
     * @param path   request path, e.g. {@code "/index.html"}
     * @param status HTTP status code string, e.g. {@code "200"}
     * @param bytes  bytes transferred string; digits or {@code "-"}
     */
    public record LogEntry(
            String ip,
            String date,
            String method,
            String path,
            String status,
            String bytes) {}

    /**
     * Parses a single Apache Combined Log Format line into a {@link LogEntry}.
     *
     * @param line the log line to parse; never {@code null}
     * @return {@link Optional} containing the parsed entry, or empty if the line
     *         does not match the expected format
     * @throws UnsupportedOperationException until the method is implemented
     */
    public Optional<LogEntry> parseLine(String line) {
        throw new UnsupportedOperationException("TODO");
    }
}
