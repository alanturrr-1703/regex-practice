# Hints

## Hint 1 — The Lazy Approach Fails
Your first instinct might be `".*?"` — match a quote, then lazily match as little as possible, then a closing quote. But this fails on `"say \"hi\""` — the lazy `.*?` stops at the `\"` and thinks the string ended there. You need a smarter pattern.

## Hint 2 — Think About What Can Be Inside a String
A double-quoted string can contain exactly two types of content:
1. Any character that is NOT a double-quote and NOT a backslash
2. A backslash followed by exactly one more character (an escape sequence)

These two cases are mutually exclusive and together cover everything that can appear in a valid string literal. This leads directly to the pattern.

## Hint 3 — The Character-Level Pattern
For the content inside the quotes, use:
```
(?:[^"\\]|\\.)*
```
- `[^"\\]` — non-quote, non-backslash char
- `\\.` — backslash + ANY one char (the escape sequence)
- `(?:...)` — non-capturing group
- `*` — zero or more

When the engine sees `\`, it takes the `\\.` branch and eats TWO chars (the `\` and the next one). So `\"` is consumed as a unit and does NOT close the string.

## Hint 4 — Full Pattern and Java Escaping
The full regex: `"((?:[^"\\]|\\.)*)"` where the outer `"` chars are literal quote characters.

In Java source, every `\` becomes `\\`, and literal `"` inside the string needs `\"`:
```java
Pattern.compile("\"((?:[^\"\\\\]|\\\\.)*)\""  )
```
Print `pattern.pattern()` — it should show: `"((?:[^"\\]|\\.)*)"` — two quotes, the group, etc.

## Hint 5 — Extracting the Content
The full match includes the surrounding quotes. The content you want is **group 1** — `matcher.group(1)`. Use a `while (matcher.find())` loop and collect `matcher.group(1)` for each match.
