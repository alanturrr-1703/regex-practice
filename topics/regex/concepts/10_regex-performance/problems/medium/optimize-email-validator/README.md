# Optimize Email Validator

**Difficulty**: Medium
**Concept**: Rewriting a catastrophically backtracking email pattern as a safe one

---

## Problem Statement

A slow, potentially catastrophic email validator pattern is provided (in a comment in
the starter code). Your task is to implement a **correct, safe** email validator.

### The Slow Pattern (DO NOT USE)

```/dev/null/slow.java#L1-2
// SLOW_PATTERN: ^([a-z]+[a-z0-9]*)*@([a-z0-9]+\.)+[a-z]{2,6}$
// The local part ([a-z]+[a-z0-9]*)* is catastrophic:
// ([a-z]+[a-z0-9]*) can match any [a-z0-9]+ string in multiple ways,
// and the outer * creates exponential ambiguity.
// Attack: "aaaaaaaaaaaaaaa@" — no domain, forces exhaustive backtracking.
```

### Your Safe Implementation

Implement `isValidEmail(String email)` using a safe pattern. A valid email for this
problem must match:
- Local part: one or more alphanumeric chars, dots, underscores, percent, plus, hyphen
  (`[a-zA-Z0-9._%+\-]+`)
- `@` symbol
- Domain: one or more labels separated by dots (`[a-zA-Z0-9\-]+` each)
- TLD: 2 or more letters (`[a-zA-Z]{2,}`)

---

## Constraints

- Input may be `null` → return `false`.
- Input longer than 254 characters → return `false` (RFC 5321 limit — also prevents ReDoS).
- Matching is case-sensitive (or case-insensitive with `CASE_INSENSITIVE` flag — your choice).
- The pattern must NOT exhibit catastrophic backtracking on adversarial input.
- Include an `assertTimeout` test to verify this (see test file).

---

## Input / Output Examples

| Input | Expected |
|-------|----------|
| `"user@example.com"` | `true` |
| `"bad-email"` | `false` |
| `"@nodomain"` | `false` |
| `"user@.com"` | `false` |
| `"user.name+tag@example.co.uk"` | `true` |
| `""` | `false` |
| `null` | `false` |
| `"aaaaaaaaaaaaaaaaaaa@"` (adversarial) | `false` AND completes fast |

---

## Why the Slow Pattern is Catastrophic

`([a-z]+[a-z0-9]*)` can match "abc123" as:
- `([a-z]+="abc", [a-z0-9]*="123")`
- `([a-z]+="a", [a-z0-9]*="bc123")`
- `([a-z]+="ab", [a-z0-9]*="c123")`
- etc.

The outer `(...)*` means this group can repeat, creating 2^N ways to partition the local part.
On `"aaaaaaaaaaaaaaa@"`, the `@` triggers failure, and the engine tries all 2^N paths.

### Why the Safe Pattern is Safe

`[a-zA-Z0-9._%+\-]+` has only ONE quantifier at the top level. There is exactly ONE way
to match any given string of `[a-zA-Z0-9._%+\-]` characters — greedily, all of them.
No ambiguity → no exponential behavior.

---

## Time Complexity

- Safe: O(N) where N is the email length.
- Slow: O(2^N) on adversarial input.

---

## Real-World Relevance

Email validation is one of the most ReDoS-vulnerable operations in web applications.
The OWASP Top 10 lists ReDoS under "Injection" risks. Many web frameworks have shipped
vulnerable email validators. Always:
1. Bound input length before matching.
2. Use non-nested, non-ambiguous patterns.
3. Test with adversarial input.

---

## Common Mistakes

- Copying `SLOW_PATTERN` from the comment — this defeats the entire purpose.
- Not bounding input length — even a safe pattern can be slow on a 10MB string.
- Over-engineering the pattern with complex alternation — keep it flat and simple.
- Using `String.matches()` — must use the static `Pattern` field instead.
