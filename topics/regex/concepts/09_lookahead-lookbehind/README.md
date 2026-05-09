# Lookahead & Lookbehind Assertions

## Concept Overview

Lookahead and lookbehind are **zero-width assertions** — they match a position in the string
rather than consuming characters. They let you say "match X only if Y follows/precedes it"
without including Y in the match result.

| Syntax       | Name               | Meaning                                      |
|--------------|--------------------|----------------------------------------------|
| `(?=X)`      | Positive lookahead | Succeeds if X matches at current position    |
| `(?!X)`      | Negative lookahead | Succeeds if X does NOT match at position     |
| `(?<=X)`     | Positive lookbehind| Succeeds if X matches immediately before     |
| `(?<!X)`     | Negative lookbehind| Succeeds if X does NOT match immediately before |

## Java-Specific Rules

- **Java 8–13**: lookbehind must be **fixed-length** (no `*`, `+`, `?` inside `(?<=...)`)
- **Java 14+**: bounded variable-length lookbehind is supported (`(?<=\w{1,10})`)
- Lookahead has **no length restriction** in any Java version
- All four forms are available in `java.util.regex`

## Prerequisites

- Basic regex quantifiers (`*`, `+`, `?`, `{n,m}`)
- Character classes and anchors
- Java `Pattern` / `Matcher` API

## Problems in This Section

| Problem | Difficulty | Key Technique |
|---------|-----------|---------------|
| [password-strength-validator](problems/easy/password-strength-validator/) | Easy | Multiple chained positive lookaheads |
| [find-word-not-followed-by](problems/easy/find-word-not-followed-by/) | Easy | Negative lookahead `(?!...)` |
| [extract-prices](problems/medium/extract-prices/) | Medium | Positive lookbehind `(?<=...)` |
| [log-severity-extractor](problems/medium/log-severity-extractor/) | Medium | Lookbehind alternation, capturing group workaround |
| [overlapping-pattern-finder](problems/hard/overlapping-pattern-finder/) | Hard | Lookahead as zero-width scanner for overlapping matches |

## How to Study This Topic

1. Read `notes.md` end-to-end before touching any problem.
2. Solve **Easy** first — they isolate a single assertion type.
3. **Medium** problems combine assertions with real parsing tasks.
4. **Hard** uses the lookahead-as-scanner trick — a technique that appears in parser engineering.

## Quick Reference

```regex-practice/topics/regex/concepts/lookahead-lookbehind/README.md#L1-1
# Chained lookaheads for password validation
^(?=.*[A-Z])(?=.*[a-z])(?=.*\d)(?=.*[!@#]).{8,}$

# Negative lookahead: "file" NOT followed by "name"
file(?!name|path)

# Positive lookbehind: digits preceded by $ or euro
(?<=[$€])\d+(?:\.\d{2})?

# Overlapping match trick
(?=(aba))   <- zero-width; inner group captures without consuming
```
