# Concept: Escaping

> *"Every backslash you write in a Java regex passes through two interpreters before it does anything. Most bugs live in the gap between them."*

---

## Overview

Escaping is the mechanism by which you tell a regex engine to treat a character **literally** rather than as a metacharacter. In Java, escaping has an extra layer of complexity: your regex pattern is a **Java string**, so you must escape for the **Java string literal parser** first, and then for the **regex engine** second.

This double-layer escaping is the single most common source of confusion for Java regex beginners — and even experienced developers who come from Python or JavaScript, where raw strings eliminate the problem.

---

## Why This Concept Matters

- `.` in a regex matches **any character**. To match a literal dot (e.g., in an IP address or file extension), you must write `\.` in the regex — which means `"\\."` in a Java string.
- `\d` in a regex matches a digit. In Java, you write `"\\d"`. If you write `"\d"`, Java's string parser throws an error because `\d` is not a valid Java string escape sequence.
- User-supplied strings used as regex patterns without escaping are a **security vulnerability** — they can match unintended input or crash with `PatternSyntaxException`.

Getting escaping wrong produces some of the most confusing bugs in regex: patterns that compile fine but match the wrong things silently.

---

## Roadmap

After completing this concept, you will be able to:

1. Explain the two-layer escaping model (Java string → regex engine)
2. Write any literal character match correctly in Java
3. Escape all 14 regex metacharacters correctly
4. Use `Pattern.quote()` to safely handle user-provided input
5. Use `Matcher.quoteReplacement()` for safe replacements
6. Debug `PatternSyntaxException` caused by incorrect escaping
7. Understand what needs escaping inside `[...]` character classes vs outside

**Problem progression:**

| Problem | Difficulty | Core Skill |
|---|---|---|
| match-literal-dot | Easy | `\\.` vs `.` |
| count-special-regex-chars | Easy | Escaping all metacharacters in a class |
| extract-ip-addresses | Medium | `\\.` in a multi-part pattern |
| java-escape-sequence-validator | Medium | `\\\\` — matching literal backslashes |
| string-literal-extractor | Hard | `[^"\\]|\\.` — the escaped-content pattern |

---

## Prerequisites

- `basics/` — Pattern, Matcher, find() loop
- `character-classes/` — how `[...]` works

---

## Common Mistakes

| Mistake | Example | Fix |
|---|---|---|
| Using `.` to match a literal dot | `"3.14".matches("3.14")` → true (but wrong — `.` matches any char) | Use `"3\\.14"` |
| Single backslash in Java string | `"\d"` → compile error | Use `"\\d"` |
| Forgetting to escape `$` in patterns | `Pattern.compile("$5")` matches end-of-string followed by "5" | Use `"\\$5"` |
| Not escaping `(` or `)` | `"(hello)"` → opens a capture group | Use `"\\(hello\\)"` |
| Not escaping `-` in a character class | `"[a-z-A-Z]"` → `-` creates a range `z-A` (which is empty or invalid) | Put `-` first, last, or escape it: `"[a-zA-Z\\-]"` or `"[-a-zA-Z]"` |
| Escaping when inside `\Q...\E` | `"\\Qhello.world\\E"` — no need to escape the dot | The entire span is literal |
| Passing raw user input as regex | `Pattern.compile(userInput)` — user types `"[invalid"` → crash | Use `Pattern.compile(Pattern.quote(userInput))` |
| `$` in replacement string | `matcher.replaceAll("$10")` — `$1` is interpreted as group 1 reference | Use `Matcher.quoteReplacement("$10")` |

---

## Debugging Mindset

When an escaping bug hits, ask these questions in order:

1. **Did the pattern throw `PatternSyntaxException`?**
   - If yes: you have too few backslashes. The regex engine received an incomplete escape sequence.
   - Count: Java string `"\\"` → regex `\` → valid. Java string `"\"` → compile error (Java sees an escaped quote, not a string end).

2. **Did the pattern compile but match the wrong things?**
   - If yes: you probably have too many — or the wrong — metacharacters unescaped. A `.` matching `X` instead of a literal dot is the classic case.

3. **Is the character inside a `[...]` class?**
   - Inside a class: `]`, `\`, `^` (at start), `-` (in the middle) need escaping.
   - Outside a class: `.`, `*`, `+`, `?`, `^`, `$`, `{`, `}`, `[`, `]`, `|`, `(`, `)`, `\` need escaping.

4. **Are you in a replacement string?**
   - `$` and `\` have special meaning in `Matcher.replaceAll()` replacement strings.
   - Use `Matcher.quoteReplacement()` if the replacement contains these characters.

**Quick diagnostic**: print the pattern string before compiling it:
```
String pat = "\\d{3}\\.\\d{4}";
System.out.println("Pattern: " + pat);  // prints: \d{3}\.\d{4}
Pattern.compile(pat);
```

---

## Real-World Usage

- **IP address extraction**: dots between octets must be literal `\\.`
- **File extension matching**: `\\.java$` not `.java$`
- **URL parsing**: escaping `?`, `&`, `=` in query string patterns
- **Config file parsing**: escaping `=`, `.`, `[` in property key patterns
- **CSV parsing**: escaping `.` in decimal numbers, `$` in currency
- **Security input validation**: using `Pattern.quote()` for user-supplied search terms
- **Code analysis tools**: extracting string literals requires escaping `"` and `\` correctly
