# Regex Character Classes

## Overview

A **character class** is a set of characters enclosed in square brackets `[...]`. It tells the regex engine: "match exactly ONE character from this set." Character classes are the primary mechanism for expressing "match this kind of character" rather than "match this exact character."

Understanding character classes deeply means understanding:

1. **Enumerated classes**: `[aeiou]` — list each acceptable character explicitly.
2. **Range notation**: `[a-z]` — match any character between `a` and `z` in Unicode code point order.
3. **Negation**: `[^abc]` — match any character that is NOT in the set.
4. **Shorthand classes**: `\d`, `\w`, `\s` — pre-defined commonly needed sets.
5. **POSIX/Unicode classes**: `\p{L}`, `\p{N}`, `\p{Alpha}` — Unicode-category-aware matching.

---

## Why This Concept Matters

Character classes are the backbone of almost every non-trivial regex. Without them, you are limited to literal matches and `.` (any char). With them, you can express:

- "a hex digit" (`[0-9a-fA-F]`)
- "a lowercase letter" (`[a-z]` or `\p{Lower}`)
- "not a digit" (`[^\d]` or `\D`)
- "a Unicode letter from any script" (`\p{L}`)
- "a vowel" (`[aeiouAEIOU]`)

Every tokenizer, validator, parser, and scraper you write will use character classes. Getting them right — knowing the difference between `\w` and `[a-zA-Z]`, between `\d` and `\p{N}` — is the difference between code that works in production and code that silently mis-handles international input.

---

## Roadmap — What You'll Learn, In Order

| Step | Topic | Problem |
|------|-------|---------|
| 1 | Enumerated character class; case-insensitive flag | `match-vowels` (Easy) |
| 2 | Range notation; `matches()` for full-string validation | `validate-alphanumeric` (Easy) |
| 3 | Hex digit ranges; exact quantifier `{n}` | `extract-hex-colors` (Medium) |
| 4 | Combining custom ranges; extraction with `find()` | `custom-character-range` (Medium) |
| 5 | Unicode categories `\p{L}`, `\p{N}`; word boundaries | `unicode-identifier` (Hard) |

---

## Prerequisites

- Regex Basics concept (especially `find()` loop, `matches()` semantics, double-escaping)
- Java 11+
- Understanding of Unicode code points vs. Java `char`

---

## Common Mistakes

### 1. Misplacing `^` inside a class

`^` has two meanings in regex:
- **Outside** a character class: anchors to start of string (or line in MULTILINE mode).
- **Inside** a character class, at the start: **negates** the class.

```
[^abc]   → matches any char that is NOT a, b, or c
[a^bc]   → matches a, ^, b, or c  (^ is LITERAL when not at position 0 inside [...])
^[abc]   → anchored at start: first char must be a, b, or c
```

### 2. Hyphen position matters critically

Inside `[...]`, a hyphen `-` is a range operator when placed between two characters. To match a literal hyphen, place it at the start or end:

```
[a-z]    → range: a through z
[-az]    → literal hyphen, a, or z
[az-]    → literal a, z, or hyphen (hyphen at end = literal)
[a\-z]   → escaped hyphen between a and z  (also works)
```

### 3. Character class ranges are based on Unicode code points

`[A-z]` does NOT mean "letters A through z". It means "any character with code point between 65 (A) and 122 (z)", which includes `[`, `\`, `]`, `^`, `_`, and backtick — characters between uppercase Z (90) and lowercase a (97) in Unicode.

```
// WRONG — includes non-letter characters
[A-z]

// CORRECT — explicit letter ranges
[A-Za-z]
```

### 4. Forgetting that `\d` doesn't mean all Unicode digits

In Java's default mode, `\d` matches only `[0-9]`. Arabic-Indic digits (٠١٢٣٤٥٦٧٨٩), Devanagari digits (०१२३४५६७८९), etc. are NOT matched.

### 5. Using `.` when you want "any character in a set"

`.` matches any character except newline — that's NOT a character class, it's a wildcard. If you want "any vowel", you need `[aeiou]`, not `.`.

### 6. Confusing `\p{Alpha}` and `\p{L}`

`\p{Alpha}` is a POSIX class — in Java's default mode it matches only ASCII letters `[a-zA-Z]`. `\p{L}` is a Unicode category — it matches ANY Unicode letter from any script. They are fundamentally different in scope.

---

## Debugging Mindset

When a character class isn't matching what you expect:

1. **Verify the range endpoints**: Print `(int)'a'` and `(int)'m'` to see the code points. A range `[a-m]` includes chars with code points 97 through 109.

2. **Check for negation issues**: `[^0-9]` matches non-digits. Did you want `[0-9]`?

3. **Test a single character**: If `[a-fA-F]` isn't matching, test each character individually: does `[a-f]` match? Does `[A-F]`?

4. **Use `Pattern.CASE_INSENSITIVE`**: Instead of writing both `[a-zA-Z]`, you can write `[a-z]` with the flag. But be careful — with Unicode-aware case folding, this can match more than you expect.

5. **Check Unicode mode**: If you need to match Unicode letters, you may need `\p{L}` or `Pattern.UNICODE_CHARACTER_CLASS` with `\w`.

---

## Real-World Usage Examples

| Domain | Character Class Used | Example Pattern |
|--------|---------------------|-----------------|
| Hex color parsing in CSS | `[0-9a-fA-F]` | `#[0-9a-fA-F]{6}` |
| HTML attribute values | `[^"']` | `attr="[^"]*"` |
| CSV parsing | `[^,\n]` | `[^,\n]+` |
| Password validation | `[!@#$%^&*]` | Contains at least one special char |
| International name validation | `\p{L}` | Full name with any Unicode letters |
| Markdown header detection | `[#]{1,6}` | `^[#]{1,6}\s` |
| Log level extraction | `[A-Z]+` | `\[(ERROR|WARN|INFO|DEBUG)\]` |
| Phone number normalization | `[^0-9]` | Strip non-digits from phone input |

---

## Quick Reference

| Syntax | Meaning |
|--------|---------|
| `[abc]` | Match a, b, or c (enumerated) |
| `[a-z]` | Match any char a through z (range) |
| `[^abc]` | Match any char NOT a, b, or c (negated) |
| `[a-zA-Z]` | Match any ASCII letter |
| `[a-zA-Z0-9]` | Match any ASCII alphanumeric |
| `[0-9a-fA-F]` | Match any hex digit |
| `\d` | `[0-9]` (ASCII) or `\p{Nd}` (Unicode mode) |
| `\w` | `[a-zA-Z0-9_]` (ASCII) or Unicode word char |
| `\s` | `[ \t\n\r\f\x0B]` (ASCII) or Unicode whitespace |
| `\p{L}` | Any Unicode letter (all scripts) |
| `\p{N}` | Any Unicode numeric character |
| `\p{Alpha}` | ASCII letters `[a-zA-Z]` |
| `\p{Lower}` | ASCII lowercase `[a-z]` |
| `\p{Upper}` | ASCII uppercase `[A-Z]` |
| `\p{Digit}` | ASCII digits `[0-9]` |
| `\p{Alnum}` | ASCII alphanumeric `[a-zA-Z0-9]` |
