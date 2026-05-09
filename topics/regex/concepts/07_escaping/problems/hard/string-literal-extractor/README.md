# String Literal Extractor

**Difficulty:** Hard
**Concepts Tested:** `[^"\\]|\\.` — the escaped-content pattern, composing escape-aware extraction
**Concept:** escaping

---

## Problem Statement

Given a snippet of Java source code as a string, extract the **content** of every double-quoted string literal in it. Handle escaped double-quotes (`\"`) inside strings — they should NOT terminate the string.

Return a list of content strings (without the surrounding double-quote characters). Do NOT de-escape the content — return it exactly as it appears between the quotes.

---

## The Core Pattern

The pattern to match the content of a string literal that may contain escaped characters is:

```
"((?:[^"\\]|\\.)*)"
```

Breaking it down:
- `"` — opening double quote (literal)
- `(...)` — capture group for the content
- `(?:...)` — non-capturing group, one alternative
- `[^"\\]` — any char that is NOT a double-quote or backslash (the "normal" chars)
- `|` — OR
- `\\.` — a backslash followed by ANY character (an escape sequence)
- `*` — zero or more of the above alternatives
- `"` — closing double quote (literal)

This is THE canonical pattern for extracting content from delimited strings with escape sequences. Understanding why it works is the deepest escaping insight in this curriculum.

---

## Why This Pattern Works

The key insight: you can only have TWO types of characters inside a string literal:
1. **Normal characters** — anything that is NOT `"` and NOT `\`
2. **Escape sequences** — a `\` followed by exactly one more character

The `[^"\\]|\\.` alternation covers exactly these two cases. When the engine sees a `\`, it takes the `\\.` branch and consumes TWO characters (the backslash and the following char). This prevents the following `\"` from being seen as a closing quote.

---

## Constraints

- Input is never `null`, may be empty
- String literals may span within a line (no true multiline strings in this simplified version)
- The input is assumed to be syntactically valid (no unclosed string literals)
- Return extracted contents WITHOUT surrounding double quotes
- Return content WITHOUT de-escaping (e.g., `\"` stays as `\"` in the result)
- Multiple string literals on one line should all be extracted

---

## Examples

### Example 1
**Input:** `String s = "hello";`
**Output:** `["hello"]`

### Example 2
**Input:** `String s = "say \"hi\"";`
**Output:** `["say \"hi\""]` (the `\"` sequences are returned as-is)

### Example 3
**Input:** `String a = "one"; String b = "two";`
**Output:** `["one", "two"]`

### Example 4
**Input:** `String s = "";`
**Output:** `[""]` (empty string literal → empty content)

---

## Edge Cases

- Empty string `""` → one result: `[""]`
- String with only escaped content: `"\\\""` → one result: `["\\\""]`
- Multiple adjacent strings: `"a""b"` → `["a", "b"]`
- No string literals → `[]`
- String with backslash at end: `"end\\"` — the `\\` is an escaped backslash, so the closing `"` after it IS the closing quote. This is why the pattern works correctly.

---

## Expected Time Complexity

O(n) — linear scan through the source code string

---

## Real-World Relevance

- Java/Python/JavaScript source code analyzers
- IDE features: "find string literals", "extract hardcoded strings"
- Internationalization tools: extracting strings for translation
- Security scanners: finding hardcoded credentials in source code
- Code generators: modifying string literals programmatically

---

## Regex Thinking Process

**Step 1:** You need to match an opening `"`, some content, and a closing `"`.

**Step 2:** The naive content pattern `.*` would be wrong — it crosses string boundaries greedily.

**Step 3:** Even `.*?` (lazy) is not right — it would stop at the first `"` even if it's an escaped `\"`.

**Step 4:** The correct content pattern: `(?:[^"\\]|\\.)*`
- Match any non-quote, non-backslash character, OR
- Match a backslash followed by anything (consuming the escape sequence as a unit)
- Zero or more times

**Step 5:** The full pattern: `"((?:[^"\\]|\\.)*)"`

**Step 6:** Java string: `"\"((?:[^\"\\\\]|\\\\.)*)\""` — count those backslashes carefully.

---

## Common Mistakes

1. Using `".*?"` — lazy, but doesn't handle `\"` inside: stops at the first quote even if escaped
2. Forgetting to escape `\` in the character class: `[^"\\]` needs `\\` in regex (four chars in Java)
3. Forgetting that `\\.` consumes TWO characters — the backslash AND the following char
4. Off-by-one backslash count in the Java string — print the pattern to verify

---

## Debugging Advice

Test your pattern with exactly these inputs and verify each output:
1. `"hello"` → content: `hello`
2. `"say \"hi\""` → content: `say \"hi\"`  
3. `"back\\\\"` — this Java string is: `"back\\"` with a backslash-backslash at end inside quotes. Content: `back\\`

If `"say \"hi\""` fails (extracting only `say ` instead of the full content), your pattern is not handling `\"` correctly — revisit the `\\.` branch.

Print the raw pattern string to verify:
```
System.out.println(pattern.pattern());
// Should print: "((?:[^"\\]|\\.)*)"
```
