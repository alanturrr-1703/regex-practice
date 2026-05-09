# Hints

## Hint 1 — Why This Fails Without DOTALL
Pattern `"/\\*(.*?)\\*/"` on a multiline comment like `"/* line1\nline2 */"` will return NO match. The reason: `.` by default does NOT match newline characters (`\n`). The lazy `.*?` cannot cross the line boundary, so it never reaches the closing `*/`.

## Hint 2 — The DOTALL Flag
Java's `Pattern.DOTALL` flag makes `.` match ANY character including newline. Pass it as the second argument: `Pattern.compile("/\\*(.*?)\\*/", Pattern.DOTALL)`. Now `.*?` can cross line boundaries and match multiline comments correctly.

## Hint 3 — The Lazy Part Is Still Important
Even with DOTALL, you need lazy `.*?`. Without it, greedy `.*` would span from the first `/*` to the LAST `*/` in the input, treating everything as one match. Lazy stops at the FIRST `*/` — correct.

## Hint 4 — Escaping the Comment Markers
- `/*` in regex: `/\*` — the `/` needs no escaping, but `*` is a quantifier and needs escaping. In Java: `"/\\*"`
- `*/` in regex: `\*/` — same reason. In Java: `"\\*/"`
- Content capture: `(.*?)` — lazy, captures into group 1
- Full pattern Java string: `"/\\*(.*?)\\*/"` with `Pattern.DOTALL`

## Hint 5 — Putting It Together
```java
private static final Pattern COMMENT_PATTERN =
    Pattern.compile("/\\*(.*?)\\*/", Pattern.DOTALL);

public List<String> extractBlockComments(String code) {
    // Create matcher, loop with find(), collect group(1)
    // ...your implementation...
}
```
Group 1 will contain the raw comment content including any leading/trailing spaces or newlines.
