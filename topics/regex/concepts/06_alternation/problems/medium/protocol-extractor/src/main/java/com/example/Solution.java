package com.example;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Solution for the <b>protocol-extractor</b> problem.
 *
 * <p>Your task: implement {@link #extractProtocols(List)} to parse the protocol
 * and host from each URL in the list using alternation inside a capturing group.</p>
 *
 * <h2>Recognized protocols</h2>
 * <p>{@code http}, {@code https}, {@code ftp}, {@code sftp}, {@code ssh}</p>
 *
 * <h2>Pattern structure</h2>
 * <pre>
 *   (https?|ftp|sftp|ssh)   ← group 1: protocol (alternation inside capture)
 *   ://
 *   ([^/\s]+)               ← group 2: host (everything up to / or whitespace)
 * </pre>
 *
 * <p>Java string: {@code "(https?|ftp|sftp|ssh)://([^/\\s]+)"}</p>
 *
 * <h2>Group index when alternation is inside a group</h2>
 * <p>The alternation {@code https?|ftp|sftp|ssh} is inside group 1.
 * {@code matcher.group(1)} returns whichever branch matched — "http", "https",
 * "ftp", "sftp", or "ssh".</p>
 *
 * <h2>Ordering note</h2>
 * <p>{@code https?} covers both "http" and "https". Ordering between {@code ftp}
 * and {@code sftp} doesn't matter here because they start with different characters
 * ('f' vs 's') — they cannot both match at the same position.</p>
 *
 * <h2>TODOs</h2>
 * <ol>
 *   <li>Declare the pattern with alternation inside group 1 for the protocol.</li>
 *   <li>For each URL in the list, apply {@code find()}.</li>
 *   <li>If matched, create a {@link UrlParts} with {@code group(1)} and {@code group(2)}.</li>
 *   <li>Skip URLs that don't match any recognized protocol.</li>
 * </ol>
 */
public class Solution {

    /**
     * Immutable value object for a parsed URL's protocol and host.
     *
     * @param protocol the URL scheme, e.g. {@code "http"}, {@code "ftp"}
     * @param host     the host name, e.g. {@code "example.com"}
     */
    public record UrlParts(String protocol, String host) {}

    /**
     * Extracts the protocol and host from each URL in {@code urls}.
     *
     * <p>URLs whose protocol is not in {http, https, ftp, sftp, ssh} are silently skipped.</p>
     *
     * @param urls list of URL strings; never {@code null}
     * @return list of {@link UrlParts} for URLs with recognized protocols; order matches input
     * @throws UnsupportedOperationException until the method is implemented
     */
    public List<UrlParts> extractProtocols(List<String> urls) {
        throw new UnsupportedOperationException("TODO");
    }
}
