# Concept: Quantifiers

## Overview

Quantifiers are the engine controls of a regex pattern. They dictate **how many times** the preceding element (a character, a group, a character class) must match for the overall pattern to succeed. Without quantifiers, every regex element matches **exactly once** — which is almost never what you want in real-world parsing.

Quantifiers transform regex from a character-matching tool into a genuine language for describing repetition structures: "one or more digits," "optionally followed by a decimal," "between 3 and 5 uppercase letters." They are the bridge between matching a fixed string and matching the *shape* of data.

---

## Why This Concept Matters

Every production regex you will ever write involves quantifiers. Consider:

- **Validation**: Is this a valid IP address? (`\d{1,3}` four times)
- **Parsing**: Extract all version numbers from a release log (`\d+\.\d+\.\d+`)
- **Normalization**: Collapse all runs of whitespace to a single space (`\s+`)
- **Security**: A naively written quantifier combination can cause your server to hang for minutes on adversarial input — a real attack vector called ReDoS (Regular Expression Denial of Service)

Understanding quantifiers at the engine level — not just syntactically — separates engineers who copy-paste regexes from engineers who design them with confidence.

---

## Prerequisites

Before working through these problems, you should be comfortable with:

- Basic regex syntax: literal characters, `.` (any char), `\d`, `\w`, `\s` and their negations
- Java's `Pattern` and `Matcher` API (`Pattern.compile()`, `matcher.matches()`, `matcher.find()`, `matcher.group()`)
- The difference between `matches()` (full-string) and `find()` (substring search)
- Basic character classes: `[abc]`, `[a-z]`, `[^abc]`

---

## Roadmap — What You'll Learn, In Order

1. **Fixed quantifiers** — `{n}`: match exactly n times. The simplest form, no ambiguity.
2. **Open-ended quantifiers** — `{n,}`: at least n times. Introduces the concept of unbounded matching.
3. **Range quantifiers** — `{n,m}`: between n and m times. The general form.
4. **Shorthand quantifiers** — `?` (0 or 1), `*` (0 or more), `+` (1 or more). Memorized idioms.
5. **Greedy behavior** — the default: try the maximum, backtrack if needed.
6. **Lazy (non-greedy) quantifiers** — add `?`: try the minimum, expand if needed.
7. **Lookahead and lookbehind with quantifiers** — enforce "exactly N, not more, not less" in context.
8. **Catastrophic backtracking** — how certain quantifier combinations create exponential time complexity.

---

## Common Mistakes

### 1. `\d{5}` matches 5 digits INSIDE a longer run

```
Pattern: \d{5}
Input:   "1234567"
Result:  MATCH (matches "12345" at position 0)
```

The pattern matches a 5-digit substring. You almost certainly want the match to fail here. Fix: use lookahead/lookbehind — `(?<!\d)\d{5}(?!\d)`.

### 2. Forgetting `+` requires at least one match

`\d*` matches the empty string. If you require at least one digit, use `\d+`. The silent empty match is a very common source of bugs — `replaceAll("\\d*", "")` will not behave as expected.

### 3. Greedy matching eating too much

```
Pattern: <.+>
Input:   "<b>bold</b>"
Result:  MATCH: "<b>bold</b>"  (the entire string!)
```

The `.+` greedily consumes everything, then the engine backtracks just enough to find the final `>`. Use `<.+?>` (lazy) or better, `<[^>]+>` (negated class — more explicit and faster).

### 4. `{n,m}` — the comma has no spaces

`{3, 5}` is NOT the same as `{3,5}` in Java. Java actually throws a `PatternSyntaxException` for `{3, 5}`. No spaces inside braces.

### 5. Using `{1,}` instead of `+`

They are equivalent, but `+` is cleaner and more readable. The engine treats them identically.

### 6. Anchoring omissions in validation

`pattern.matcher(input).find()` vs `pattern.matcher(input).matches()`. Using `find()` for validation without `^` and `$` anchors will happily match a 5-digit run inside a 20-character string, reporting "valid" when you wanted an exact match.

---

## Debugging Mindset for Quantifiers

When a quantifier-heavy pattern misbehaves, ask these questions in order:

1. **Am I matching too much?** — Is the quantifier greedy when it should be lazy? Is it unbounded (`+`) when it should be bounded (`{1,8}`)?
2. **Am I matching too little or nothing?** — Did I use `*` (zero or more) when I meant `+` (one or more)? Is my character class too restrictive?
3. **Am I matching at the wrong position?** — Did I forget anchors? `find()` will match anywhere in the string.
4. **Am I matching across boundaries I didn't intend?** — Does `\d{5}` hit inside a 10-digit number? Add `(?<!\d)` before and `(?!\d)` after.
5. **Is this going to hang on adversarial input?** — If your pattern has nested quantifiers (like `(a+)+`), test with a string that cannot match and measure the time. It may be exponential.

Use `Matcher.find()` in a loop with `matcher.group()` to see *what* matched and *where* (via `matcher.start()` / `matcher.end()`). Don't just test `true`/`false`.

---

## Real-World Usage Examples

| Use Case | Pattern Sketch | Key Quantifier |
|---|---|---|
| Validate a 6-digit PIN | `^\d{6}$` | `{6}` |
| Extract IP address octets | `\d{1,3}` | `{1,3}` |
| Match optional file extension | `\.\w+` after a stem | `+` on `\w` |
| Normalize whitespace | `\s+` → `" "` | `+` |
| Match base64 encoded block | `[A-Za-z0-9+/]{4}` repeating | `{4}` |
| Extract quoted strings | `"[^"]*"` | `*` (zero or more non-quote) |
| Semantic version parsing | `\d{1,3}\.\d{1,3}\.\d{1,3}` | `{1,3}` |
| Optional area code | `(\(\d{3}\)\s?)?` | `?` on group |

---

## Problems in This Folder

| Difficulty | Problem | Core Skill |
|---|---|---|
| Easy | validate-digit-count | `{n}` with lookahead/lookbehind for exact count |
| Easy | collapse-whitespace | `\s+` with `replaceAll` |
| Medium | extract-version-numbers | `{n,m}` with boundary enforcement |
| Medium | parse-repeated-tokens | Character class + `{n,m}` for structured formats |
| Hard | catastrophic-backtracking-debug | Diagnose and fix exponential backtracking |

Start with the easy problems to build intuition, then move to medium. Read `notes.md` before attempting the hard problem.
