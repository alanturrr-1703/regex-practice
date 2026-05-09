package com.example;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * Tests for Solution.extractStringLiterals().
 *
 * NOTE on Java string escaping in these tests:
 * - A literal backslash in test source is written as \\
 * - A literal double-quote in test source is written as \"
 * So when we write "say \\\"hi\\\"" as a Java string, the actual characters are:
 * s a y   space  \  "  h  i  \  "
 * which represents the Java source code fragment: say \"hi\"
 *
 * All tests FAIL until the method is implemented.
 */
class SolutionTest {

    private final Solution solution = new Solution();

    @Test
    void testSimpleStringLiteral() {
        // Source: String s = "hello";
        List<String> result = solution.extractStringLiterals("String s = \"hello\";");
        assertEquals(Collections.singletonList("hello"), result);
    }

    @Test
    void testStringWithEscapedQuote() {
        // Source contains: "say \"hi\""
        // The \" inside is an escape sequence — should NOT end the string
        // Input Java string: "\"say \\\"hi\\\"\""
        // Actual characters: "say \"hi\""
        String code = "String s = \"say \\\"hi\\\"\";";
        List<String> result = solution.extractStringLiterals(code);
        // Content (without outer quotes): say \"hi\"
        assertEquals(Collections.singletonList("say \\\"hi\\\""), result);
    }

    @Test
    void testMultipleStringLiterals() {
        String code = "String a = \"one\"; String b = \"two\";";
        List<String> result = solution.extractStringLiterals(code);
        assertEquals(Arrays.asList("one", "two"), result);
    }

    @Test
    void testEmptyStringLiteral() {
        String code = "String s = \"\";";
        List<String> result = solution.extractStringLiterals(code);
        assertEquals(Collections.singletonList(""), result);
    }

    @Test
    void testNoStringLiterals() {
        List<String> result = solution.extractStringLiterals("int x = 42;");
        assertEquals(Collections.emptyList(), result);
    }

    @Test
    void testEmptyInput() {
        List<String> result = solution.extractStringLiterals("");
        assertEquals(Collections.emptyList(), result);
    }

    @Test
    void testStringWithEscapedBackslash() {
        // Source: "back\\"  (backslash-backslash inside the string literal)
        // In Java test string: "\"back\\\\\""
        // Chars in code: "back\\"
        // The \\ inside is an escaped backslash, closing " is the real end
        String code = "String s = \"back\\\\\";";
        List<String> result = solution.extractStringLiterals(code);
        assertEquals(Collections.singletonList("back\\\\"), result);
    }

    @Test
    void testAdjacentStringLiterals() {
        String code = "\"a\"\"b\"";
        List<String> result = solution.extractStringLiterals(code);
        assertEquals(Arrays.asList("a", "b"), result);
    }

    @Test
    void testStringWithNewlineEscape() {
        // Content: "line1\nline2" — backslash + n (not actual newline)
        String code = "\"line1\\nline2\"";
        List<String> result = solution.extractStringLiterals(code);
        assertEquals(Collections.singletonList("line1\\nline2"), result);
    }
}
