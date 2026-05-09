# Regex Basics

## Overview

Regular expressions are a formal language embedded inside your programming language. They describe **patterns** in character sequences. A regex engine reads a pattern and a subject string, then determines whether (and where) the pattern matches.

Before you write your first production regex, you need to internalize four foundational ideas:

1. **Literal characters** match exactly themselves.
2. **Metacharacters** carry special meaning and must be escaped to match literally.
3. **Shorthand classes** (`\d`, `\w`, `\s`) are compact aliases for common character sets.
4. **The API you choose** (`matches` vs `find` vs `lookingAt`) determines the semantics of "match."

---

## Why This Concept Matters

Every advanced regex technique — lookaheads, backreferences, atomic groups — is built on top of these basics. Developers who skip this foundation end up:

- Using `Pattern.matches()` when they need `Matcher.find()` (and wondering why nothing matches).
- Forgetting Java's double-escaping requirement and getting `PatternSyntaxException` at runtime.
- Writing patterns that accidentally match the empty string.
- Catastrophically regressing performance by composing naïve patterns at scale.

Regex is used everywhere: log parsing, input validation, tokenization, search-and-replace in editors, protocol parsing, compiler front-ends. Getting basics right is non-negotiable.

---

## Roadmap — What You'll Learn, In Order

| Step | Topic | Problem |
|------|-------|---------|
| 1 | Literal substring search; `matches()` vs `find()` | `match-literal-string` (Easy) |
| 2 | `\d` shorthand; Java double-escaping | `detect-digit` (Easy) |
| 3 | `[a-zA-Z]` class; `Matcher.find()` loop pattern | `extract-words` (Medium) |
| 4 | Composing a multi-part pattern; `String.matches()` semantics | `validate-simple-email` (Medium) |
| 5 | Multiple patterns; ordered matching; building a token stream | `simple-lexer` (Hard) |

---

## Prerequisites

- Java 11+ (uses `var`, `List.of()`, text blocks in tests)
- Understanding of `java.util.regex.Pattern` and `java.util.regex.Matcher` at an API level
- Familiarity with Java `String` methods (`matches`, `contains`, `split`)
- Basic understanding of what a character is (Unicode code point vs `char`)

---

## Common Mistakes

### 1. Using `String.matches()` for substring search

```regex-practice/topics/regex/concepts/basics/README.md#L1-1
// WRONG — matches() requires the ENTIRE string to match the pattern
"ERROR: disk full".matches("ERROR");   // → false!

// CORRECT — use find() for substring search
Pattern.compile("ERROR").matcher("ERROR: disk full").find();  // → true
```

`String.matches(regex)` is equivalent to `Pattern.matches(regex, input)`, which is equivalent to `input.matches("(?s)" + regex)` anchored at both ends. Most newcomers burn hours on this.

### 2. Forgetting Java's double-escaping

In a Java string literal, `\` is an escape character. To pass a literal backslash to the regex engine, you must write `\\`. To pass `\d`, you write `"\\d"`. This is a pure Java string-layer concern — the regex engine itself never sees the extra backslash.

```regex-practice/topics/regex/concepts/basics/README.md#L1-1
// WRONG — Java sees this as an illegal escape sequence (\d is not a Java escape)
Pattern.compile("\d");  // Does not compile

// CORRECT
Pattern.compile("\\d");
```

### 3. Confusing `\w` with `[a-zA-Z]`

`\w` in Java's default (non-Unicode) mode matches `[a-zA-Z0-9_]` — it includes **digits** and **underscore**. If you want only letters, use `[a-zA-Z]` or `\p{Alpha}`.

### 4. Recompiling patterns in a loop

```regex-practice/topics/regex/concepts/basics/README.md#L1-1
// WRONG — Pattern.compile() is expensive (parses and compiles the NFA). Never in hot loops.
for (String line : millionLines) {
    if (line.matches("\\d+")) { ... }  // compiles pattern on every call!
}

// CORRECT — compile once, reuse
Pattern DIGIT_PATTERN = Pattern.compile("\\d+");
for (String line : millionLines) {
    if (DIGIT_PATTERN.matcher(line).find()) { ... }
}
```

### 5. Assuming `.` matches newline

By default, `.` matches any character **except** newline (`\n`). Use `Pattern.DOTALL` or the inline flag `(?s)` to change this.

---

## Debugging Mindset

When your regex isn't working:

1. **Isolate the pattern** — test it in isolation on a minimal string before integrating.
2. **Check which API you're using** — `matches()` anchors; `find()` searches.
3. **Print the escaped pattern** — call `pattern.pattern()` to see what the engine received.
4. **Use a regex debugger** — [regex101.com](https://regex101.com) (select Java flavor) shows the NFA step-by-step.
5. **Test edge cases first** — empty string, single character, string made entirely of delimiter chars.
6. **Add `\Q...\E` for literal blocks** — if your literal contains metacharacters, wrap with `\Q` and `\E` instead of escaping each character.

---

## Real-World Usage Examples

| Domain | Pattern Type | Example |
|--------|-------------|---------|
| Log monitoring | Literal + level | Find `ERROR` or `CRITICAL` in log streams |
| Input sanitization | Digit detection | Validate that a PIN field contains digits |
| NLP preprocessing | Word extraction | Tokenize text before feeding to a model |
| API validation | Email/URL format | Reject malformed inputs before DB insert |
| Compilers / DSLs | Lexer | Tokenize source code into NUMBER, IDENT, OP tokens |
| Config parsing | Key=Value | Extract key-value pairs from `.properties` files |

---

## Quick Reference Card

| Concept | Syntax | Notes |
|---------|--------|-------|
| Literal | `abc` | Matches the exact string `abc` |
| Any char (except newline) | `.` | Use `(?s)` flag to include newline |
| Digit | `\d` → Java: `"\\d"` | Equivalent to `[0-9]` in ASCII mode |
| Word char | `\w` → Java: `"\\w"` | Equivalent to `[a-zA-Z0-9_]` in ASCII mode |
| Whitespace | `\s` → Java: `"\\s"` | Matches space, tab, newline, etc. |
| Zero or more | `*` | Greedy by default |
| One or more | `+` | Greedy by default |
| Zero or one | `?` | Greedy by default |
| Full-string match | `String.matches(regex)` | Anchors automatically |
| Substring search | `Pattern.compile(r).matcher(s).find()` | No automatic anchoring |
