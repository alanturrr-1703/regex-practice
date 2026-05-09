# Concept: Anchors

## Overview

Anchors are **zero-width position assertions** in regex. Unlike character matchers (`\d`, `[A-Z]`, `.`), anchors do not consume characters from the input string — they simply assert something *about the current position*. A match succeeds only if the anchor's position condition is satisfied.

The most important anchors are:
- `^` — start of string (or start of line in multiline mode)
- `$` — end of string (or end of line in multiline mode)
- `\b` — word boundary (transition between word/non-word character)
- `\A` — absolute start of input
- `\Z`, `\z` — absolute end of input

Anchors are what transform a "search" into a "validation." Without them, even a highly specific pattern will match anywhere it can in the input.

---

## Why This Concept Matters

Anchors are the difference between a security check that works and one that is trivially bypassed. They appear in every serious validation task:

- **URL validation**: Does the string START with `https://`?
- **Code parsing**: Find lines that START with a comment marker
- **Log analysis**: Extract structured records that START at line beginnings
- **Word isolation**: Find the word "log" but not "logger" or "catalog"

Without anchors, `Pattern.compile("\\d{5}").matcher("abc12345xyz").find()` returns `true` — probably not what you want for ZIP code validation. With `^\\d{5}$`, `matches()` returns the correct `false`.

The subtleties of `^` vs `\A`, and what happens to these anchors under `MULTILINE` mode, trip up even experienced engineers. These mistakes cause subtle bugs in production parsers.

---

## Prerequisites

Before working through these problems, you should be comfortable with:

- Java's `Pattern` and `Matcher` API
- `Pattern.compile(regex)`, `matcher.matches()`, `matcher.find()`
- Basic quantifiers (`+`, `*`, `?`, `{n,m}`)
- Character classes and escape sequences (`\d`, `\w`, `\s`)
- The concept of flags in Java regex (`Pattern.MULTILINE`, `Pattern.CASE_INSENSITIVE`, etc.)

---

## Roadmap — What You'll Learn, In Order

1. **`^` and `$` in default mode** — Anchors to the full string's start and end
2. **`matches()` vs `find()` with anchors** — Why they interact differently
3. **`Pattern.MULTILINE`** — How `^` and `$` change behavior with multi-line input
4. **`\A`, `\Z`, `\z`** — Absolute anchors that never change behavior regardless of flags
5. **`\b` word boundary** — Position between a word character and a non-word character
6. **`\B` non-word boundary** — The complement: position inside a word
7. **Combining anchors with groups and alternation** — Real-world patterns
8. **Anchors with `find()` in loops** — How anchors constrain repeated searches

---

## Common Mistakes

### 1. Using `find()` when you meant `matches()`

```
Pattern: ^\d{5}$
Input:   "abc12345xyz"
find():  FALSE  — correct! The ^ and $ force start/end of string
matches(): FALSE — also correct for "abc12345xyz"

Pattern: \d{5}
Input:   "abc12345xyz"
find():  TRUE — finds "12345" inside the string
matches(): FALSE — the full string is not just 5 digits
```

A common mistake: writing `pattern.matcher(input).find()` for validation without anchors. It passes inputs that should fail.

### 2. Assuming `^` always means "start of string"

With `Pattern.MULTILINE`:
```
Pattern (MULTILINE): ^foo
Input:   "bar\nfoo\nbaz"
find():  TRUE — 'foo' is at the start of the second LINE
```

Without `Pattern.MULTILINE`:
```
Pattern: ^foo
Input:   "bar\nfoo\nbaz"
find():  FALSE — 'foo' is not at the start of the ENTIRE STRING
```

### 3. `$` matches before a trailing newline

In Java (and most engines), `$` matches at the end of the string OR just before a `\n` at the end. This can cause subtle issues:

```
Pattern: \d+$
Input:   "123\n"  (string ending with newline)
find():  TRUE — $ can match before the trailing \n
```

Use `\z` (lowercase) if you need to match only at the absolute last character.

### 4. `\b` is position-sensitive, not character-sensitive

`\b` does not match a character. It matches a **zero-width position** where one side is a word character (`[A-Za-z0-9_]`) and the other is not (or is the start/end of the string).

`\blog\b` matches `"log"` in `"log the event"` but NOT in `"logger"` (because after `"log"` comes `"g"`, a word character — no boundary there).

### 5. Forgetting that `\b` is case-sensitive for word chars

`\b` always uses the definition `\w = [A-Za-z0-9_]`. It does not change with `CASE_INSENSITIVE`. A pattern `\blog\b` will NOT match `"LOG"` — that requires `Pattern.CASE_INSENSITIVE` or the inline flag `(?i)`.

### 6. Using `^` and `$` in DOTALL mode expecting they change

`Pattern.DOTALL` only affects `.` (making it match newlines). It does NOT affect `^` and `$`. You need `Pattern.MULTILINE` for `^`/`$` to work line-by-line.

---

## Debugging Mindset for Anchors

When an anchor-related pattern misbehaves:

1. **Print `matcher.start()` and `matcher.end()`** — See exactly where the match occurred
2. **Check your flags** — Is `MULTILINE` set? Should it be? Is `DOTALL` confusing you?
3. **Use `matches()` vs `find()` deliberately** — Know which one you want
4. **Test with explicit newlines** — If your input may have `\n`, test with `"line1\nline2\n"`
5. **Use `\A` and `\z` for absolute anchors** — They never change behavior, making code more predictable than `^`/`$`
6. **Test `\b` with adjacent characters** — Is the character before your match a word char? After?

---

## Real-World Usage Examples

| Use Case | Pattern | Key Anchor |
|---|---|---|
| Validate URL scheme | `^https?://` | `^` |
| Validate 5-digit ZIP | `^\d{5}$` | `^`, `$` |
| Extract section headers | `(?m)^#{1,6}\s+.+$` | `^`, `$` with MULTILINE |
| Find standalone "null" | `\bnull\b` | `\b` |
| Validate email format | `\A[\w.+%-]+@[\w-]+\.[a-z]{2,}\z` | `\A`, `\z` |
| Parse log-line levels | `(?m)^\[(?:DEBUG|INFO|WARN|ERROR)\]` | `^` with MULTILINE |
| Find Java variable names | `\b[a-z][a-zA-Z0-9]*\b` | `\b` |
| Config line (not comment) | `(?m)^[^#]` | `^` with MULTILINE |

---

## Problems in This Folder

| Difficulty | Problem | Core Skill |
|---|---|---|
| Easy | validate-starts-with-http | `^` anchor for string start |
| Easy | validate-ends-with-semicolon | `$` anchor for string end |
| Medium | multiline-section-parser | `^` with `Pattern.MULTILINE` |
| Medium | word-boundary-extractor | `\b` word boundary |
| Hard | log-line-anchor-parser | Combining all anchor types with structured parsing |

Work through in order — each problem builds on the previous ones.
