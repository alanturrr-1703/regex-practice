package com.example;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests for {@link Solution#extractSections(String)}.
 * These tests WILL FAIL until the method is implemented.
 */
@DisplayName("Multiline Section Parser")
class SolutionTest {

    private Solution solution;

    @BeforeEach
    void setUp() {
        solution = new Solution();
    }

    @Test
    @DisplayName("Two section headers interspersed with content")
    void testTwoSectionsWithContent() {
        String input = ">> title\ncontent\n>> another";
        assertEquals(List.of("title", "another"), solution.extractSections(input));
    }

    @Test
    @DisplayName("No section headers returns empty list")
    void testNoSections() {
        assertTrue(solution.extractSections("no sections\nhere").isEmpty());
    }

    @Test
    @DisplayName("Single section header")
    void testOnlyOne() {
        assertEquals(List.of("only one"), solution.extractSections(">> only one"));
    }

    @Test
    @DisplayName("Three consecutive section headers")
    void testThreeConsecutiveSections() {
        String input = ">> First\n>> Second\n>> Third";
        assertEquals(List.of("First", "Second", "Third"), solution.extractSections(input));
    }

    @Test
    @DisplayName("Section headers interspersed with regular lines")
    void testMixedContent() {
        String input = "content\n>> Section A\nmore content\n>> Section B";
        assertEquals(List.of("Section A", "Section B"), solution.extractSections(input));
    }

    @Test
    @DisplayName(">> embedded in middle of line is NOT a section header")
    void testEmbeddedMarkersIgnored() {
        String input = "text >> embedded\n>> real section";
        assertEquals(List.of("real section"), solution.extractSections(input),
                "A >> not at line start should be ignored (needs ^ with MULTILINE)");
    }

    @Test
    @DisplayName("Line with spaces before >> is NOT a section header")
    void testIndentedNotASection() {
        String input = "  >> indented\n>> valid";
        assertEquals(List.of("valid"), solution.extractSections(input),
                "Leading spaces before >> mean it's not at line start — ignored");
    }

    @Test
    @DisplayName("Empty string returns empty list")
    void testEmptyString() {
        assertTrue(solution.extractSections("").isEmpty());
    }

    @Test
    @DisplayName("Null returns empty list")
    void testNull() {
        List<String> result = solution.extractSections(null);
        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    @DisplayName("Section header content is trimmed of surrounding whitespace")
    void testContentTrimmed() {
        String input = ">>   padded content   ";
        List<String> result = solution.extractSections(input);
        assertFalse(result.isEmpty(), "Should extract content from '>>   padded content   '");
        assertEquals("padded content", result.get(0),
                "Content should be trimmed of leading/trailing whitespace");
    }

    @Test
    @DisplayName("Section header with no space after >> still works")
    void testNoSpaceAfterMarker() {
        String input = ">>noSpace";
        List<String> result = solution.extractSections(input);
        assertFalse(result.isEmpty());
        assertEquals("noSpace", result.get(0));
    }
}
