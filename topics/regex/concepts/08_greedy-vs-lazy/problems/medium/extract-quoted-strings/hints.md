# Hints

## Hint 1 — Why Lazy Fails for Escaped Quotes
Simple lazy `'.*?'` stops at the FIRST `'` it sees. If the string content contains `\'`, the lazy pattern incorrectly terminates at the `'` inside the escape. For `'it\'s fine'`, lazy gives you `"it\"` as content — wrong.

## Hint 2 — The Two-Case Pattern
Think about what can legally appear inside a single-quoted string:
1. Any character that is NOT a single-quote and NOT a backslash
2. A backslash followed by exactly one character (an escape sequence)

These two cases together cover all valid string content. Use alternation: `[^'\\]|\\.`

## Hint 3 — Building the Robust Pattern
Wrap the alternation in a non-capturing group with `*`:
- Regex: `'((?:[^'\\]|\\.)*)'`
- The `(?:...)` is non-capturing so the `*` quantifier applies to the whole alternation
- The outer `(...)` is a capturing group for the content

## Hint 4 — Java String Escaping
In the Java string for the robust pattern:
- `[^'\\]` needs `\\\\` in Java for the `\\` part: `"[^'\\\\]"`
- `\\.` needs `\\\\.` in Java: `"\\\\."`
- Full Java string: `"'((?:[^'\\\\]|\\\\.)*)'"`
- Print `pattern.pattern()` to verify it shows: `'((?:[^'\\]|\\.)*)'`

## Hint 5 — The Simple Method
For extractSimpleQuoted, just use: `Pattern.compile("'(.*?)'")`. The `?` makes it lazy. Use `matcher.group(1)` for the content. This method does NOT handle `\'` — that's fine, it's the simpler version.
