# Validate Simple Email

**Difficulty:** Medium  
**Concept:** Regex Basics  
**Concepts Tested:** Composing multi-part patterns, character class negation, quantifiers, `String.matches()` full-string semantics, TLD range validation

---

## Problem Statement

Validate that a string looks like a basic email address. An email is considered valid if it matches this structure:

1. **Local part**: one or more characters that are NOT `@` or whitespace
2. **At sign**: exactly one `@`
3. **Domain part**: one or more characters that are NOT `@` or whitespace
4. **Dot**: exactly one `.` (literal)
5. **TLD (Top-Level Domain)**: 2 to 6 ASCII letters only (`[a-zA-Z]{2,6}`)

This is NOT a full RFC 5321/5322 validator — it's a first-approximation pattern to teach you how to compose a multi-part regex from simpler building blocks.

---

## Constraints

- Input is non-null.
- Use `String.matches()` or `Matcher.matches()` — the pattern must match the **entire** string.
- The local part must have at least 1 character that is not `@`.
- The domain part must have at least 1 character that is not `@`, followed by a literal `.`.
- The TLD must be 2–6 ASCII letters only (no digits in TLD).
- Case-sensitive by default (both `com` and `COM` are valid TLDs under this rule).
- No whitespace allowed anywhere.

---

## Input / Output Examples

| Input | Output | Reason |
|-------|--------|--------|
| `"user@example.com"` | `true` | Standard valid email |
| `"bad@"` | `false` | No domain after `@` |
| `"@domain.com"` | `false` | Empty local part |
| `"user@domain.c"` | `false` | TLD has only 1 letter (minimum is 2) |
| `"user@domain.museum"` | `true` | 6-letter TLD is valid |
| `"user@domain.toolong"` | `false` | 7-letter TLD exceeds maximum |
| `"plainaddress"` | `false` | No `@` sign |
| `"user@domain.io"` | `true` | 2-letter TLD is valid |

---

## Edge Cases

- **Empty string** `""` → `false`.
- **Only `@`** `"@"` → `false` (empty local and empty domain).
- **Multiple `@`** `"a@b@c.com"` → the `[^@]+` class stops at the first `@`. The pattern would fail because after the domain `[^@]+` consumes `b`, then `\.` needs to match `@`. This should return `false`.
- **TLD with digits** `"user@domain.c0m"` → `false` (`[a-zA-Z]{2,6}` doesn't match digits).
- **No dot in domain** `"user@domaincom"` → `false` (no literal `.` before TLD).
- **Leading dot** `"user@.com"` → The domain `[^@]+` requires at least 1 non-`@` character before the dot. With the pattern `[^@\s]+\.[a-zA-Z]{2,6}`, a leading dot means `[^@\s]+` must match something before `\.`. `[^@\s]+` is greedy — with `"user@.com"`, after `@`, the domain pattern `[^@\s]+\.[a-zA-Z]{2,6}` requires at least one char before the dot. `.` is in `[^@\s]`, so the engine may match `.com` differently. Test this edge case carefully.

---

## Expected Time Complexity

- **O(n)** amortized for the regex match on a string of length n.
- `String.matches()` anchors at both ends, so the engine can fail fast for obviously invalid inputs.

---

## Real-World Relevance

Email validation is one of the most common uses of regex in web development. Key insight: **no regex can fully validate email addresses** per RFC 5321 — the full spec allows local parts with quoted strings, IP addresses, comments, and more. Production systems use a two-step approach:
1. Rough regex validation (reject obviously malformed input)
2. Send a confirmation email (the only true validation)

---

## Regex Thinking Process

Build the pattern piece by piece:

1. **Local part** (before `@`): any character that is not `@` or whitespace, one or more: `[^@\\s]+`
2. **At sign**: literal `@`
3. **Domain** (between `@` and last `.`): any character that is not `@` or whitespace, one or more: `[^@\\s]+`
4. **Dot**: literal `\\.` (in Java string: `"\\."`))
5. **TLD**: 2-6 letters: `[a-zA-Z]{2,6}`

Combined: `[^@\\s]+@[^@\\s]+\\.[a-zA-Z]{2,6}`

Used with `String.matches()`, which anchors at both ends — no need for `^` and `$`.

---

## Common Mistakes

1. **Using `find()` instead of `matches()`** — `find()` would accept `"not-email user@domain.com more-stuff"` because it finds the email portion. You want full-string validation.
2. **Using `.` instead of `\\.`** for the dot before TLD — `.` in regex means "any character", so `"user@domainXcom"` would match!
3. **Forgetting minimum TLD length** — `{2,6}` means 2 to 6. `{,6}` would allow empty TLDs.
4. **Using `[^@]+` and forgetting it can match dots** — `[^@]+` also matches `.`, which is fine for the domain part (handles `sub.domain.com`).
5. **Allowing whitespace in the local part** — `[^@]+` allows spaces. Use `[^@\\s]+` to exclude whitespace.

---

## Debugging Advice

Test each part of the pattern in isolation:
- Does `[^@\\s]+` match the local part?
- Does `[^@\\s]+\\.[a-zA-Z]{2,6}` match the domain+TLD?
- Then combine with `@` in between.

Print `pattern.pattern()` to see exactly what the engine received — this often reveals escaping mistakes.
