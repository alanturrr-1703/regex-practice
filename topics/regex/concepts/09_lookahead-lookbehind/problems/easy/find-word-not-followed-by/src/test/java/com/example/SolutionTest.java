package com.example;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests for Find Word Not Followed By.
 *
 * All tests MUST fail until Solution.countStandaloneFile() is implemented.
 */
class SolutionTest {

    private final Solution solution = new Solution();

    @Test
    @DisplayName("'read file from filepath' → 1")
    void testMixedFileAndFilepath() {
        assertEquals(1, solution.countStandaloneFile("read file from filepath"),
                "Only the standalone 'file' should be counted");
    }

    @Test
    @DisplayName("'no match here' → 0")
    void testNoMatch() {
        assertEquals(0, solution.countStandaloneFile("no match here"),
                "No 'file' token at all");
    }

    @Test
    @DisplayName("'file file file' → 3")
    void testThreeStandaloneFiles() {
        assertEquals(3, solution.countStandaloneFile("file file file"),
                "All three are standalone");
    }

    @Test
    @DisplayName("'filename and file' → 1")
    void testFilenameExcluded() {
        assertEquals(1, solution.countStandaloneFile("filename and file"),
                "'filename' is excluded; second 'file' is counted");
    }

    @Test
    @DisplayName("Empty string → 0")
    void testEmpty() {
        assertEquals(0, solution.countStandaloneFile(""),
                "Empty string has no matches");
    }

    @Test
    @DisplayName("Null input → 0")
    void testNull() {
        assertEquals(0, solution.countStandaloneFile(null),
                "Null should return 0 without throwing");
    }

    @Test
    @DisplayName("'filepath' only → 0")
    void testFilepathOnly() {
        assertEquals(0, solution.countStandaloneFile("filepath"),
                "'file' in 'filepath' is followed by 'path' — excluded");
    }

    @Test
    @DisplayName("'filename' only → 0")
    void testFilenameOnly() {
        assertEquals(0, solution.countStandaloneFile("filename"),
                "'file' in 'filename' is followed by 'name' — excluded");
    }

    @Test
    @DisplayName("'file' only → 1")
    void testSingleFile() {
        assertEquals(1, solution.countStandaloneFile("file"),
                "Standalone 'file' should count");
    }

    @Test
    @DisplayName("'file_backup' → 1 (followed by underscore, not name/path)")
    void testFileUnderscoreBackup() {
        assertEquals(1, solution.countStandaloneFile("file_backup"),
                "'file' followed by '_backup' is not excluded");
    }

    @Test
    @DisplayName("'filetype and filename and file' → 2")
    void testMixedThreeTypes() {
        // 'filetype': file followed by 'type' → counts
        // 'filename': file followed by 'name' → excluded
        // 'file': standalone → counts
        assertEquals(2, solution.countStandaloneFile("filetype and filename and file"),
                "'filetype' and standalone 'file' count; 'filename' does not");
    }

    @Test
    @DisplayName("'profile' → 1 (contains 'file' not followed by name/path)")
    void testSubstringInLargerWord() {
        assertEquals(1, solution.countStandaloneFile("profile"),
                "'file' inside 'profile' is not followed by 'name' or 'path'");
    }
}
