package com.example;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests for {@link Solution#extractProtocols(List)}.
 *
 * <p>All tests will fail with {@link UnsupportedOperationException} until the
 * method is implemented. That is intentional — make the tests pass.</p>
 */
class SolutionTest {

    private final Solution solution = new Solution();

    // -----------------------------------------------------------------------
    // Core matching
    // -----------------------------------------------------------------------

    @Test
    @DisplayName("http and https protocols both extracted")
    void testHttpAndHttps() {
        List<Solution.UrlParts> result = solution.extractProtocols(
                List.of("http://example.com", "https://secure.com"));

        assertEquals(2, result.size());
        assertEquals("http",        result.get(0).protocol());
        assertEquals("example.com", result.get(0).host());
        assertEquals("https",       result.get(1).protocol());
        assertEquals("secure.com",  result.get(1).host());
    }

    @Test
    @DisplayName("ftp protocol extracted with correct host")
    void testFtp() {
        List<Solution.UrlParts> result = solution.extractProtocols(
                List.of("ftp://files.example.net"));

        assertEquals(1, result.size());
        assertEquals("ftp",               result.get(0).protocol());
        assertEquals("files.example.net", result.get(0).host());
    }

    @Test
    @DisplayName("sftp protocol extracted")
    void testSftp() {
        List<Solution.UrlParts> result = solution.extractProtocols(
                List.of("sftp://deploy.server.io"));

        assertEquals(1, result.size());
        assertEquals("sftp",             result.get(0).protocol());
        assertEquals("deploy.server.io", result.get(0).host());
    }

    @Test
    @DisplayName("Mixed protocols — 3 recognized, 1 unrecognized (mailto)")
    void testMixedWithUnrecognized() {
        List<Solution.UrlParts> result = solution.extractProtocols(
                List.of("http://example.com",
                        "https://secure.com",
                        "ftp://files.net",
                        "mailto:user@host"));

        assertEquals(3, result.size(), "mailto should be skipped; 3 recognized protocols");
        assertEquals("http",    result.get(0).protocol());
        assertEquals("https",   result.get(1).protocol());
        assertEquals("ftp",     result.get(2).protocol());
    }

    // -----------------------------------------------------------------------
    // Alternation-inside-group correctness
    // -----------------------------------------------------------------------

    @Test
    @DisplayName("group(1) captures the matched protocol branch correctly")
    void testGroupCapturesProtocol() {
        List<Solution.UrlParts> result = solution.extractProtocols(
                List.of("ssh://bastion.internal"));

        assertEquals(1, result.size());
        assertEquals("ssh",              result.get(0).protocol());
        assertEquals("bastion.internal", result.get(0).host());
    }

    // -----------------------------------------------------------------------
    // Edge cases
    // -----------------------------------------------------------------------

    @Test
    @DisplayName("Empty list returns empty list")
    void testEmptyList() {
        List<Solution.UrlParts> result = solution.extractProtocols(List.of());
        assertTrue(result.isEmpty());
    }

    @Test
    @DisplayName("All unrecognized protocols returns empty list")
    void testAllUnrecognized() {
        List<Solution.UrlParts> result = solution.extractProtocols(
                List.of("mailto:a@b.com", "file:///local/path", "jdbc:mysql://db:3306"));
        assertTrue(result.isEmpty(), "No recognized protocols should yield empty list");
    }

    @Test
    @DisplayName("URL with path — host does not include the path")
    void testHostDoesNotIncludePath() {
        List<Solution.UrlParts> result = solution.extractProtocols(
                List.of("https://example.com/some/path?q=1"));

        assertEquals(1, result.size());
        assertEquals("example.com", result.get(0).host(),
            "Host should stop at the first '/' after the hostname");
    }
}
