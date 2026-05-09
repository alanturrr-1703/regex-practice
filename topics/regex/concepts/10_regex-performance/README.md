# Regex Performance & ReDoS Prevention

## Concept Overview

Regex performance is not an afterthought — poorly written patterns have taken down production
systems. This section teaches you to reason about how Java's NFA engine executes patterns,
recognize catastrophic backtracking before it bites you, and apply concrete fixes.

## Why This Matters

- `String.matches()` and `String.replaceAll()` recompile the pattern on every call.
- Nested quantifiers on overlapping character sets trigger exponential backtracking.
- ReDoS (Regular Expression Denial of Service) is a real attack vector.
- Caching `Pattern` objects as static fields is a trivial, high-impact optimization.

## Key Concepts

| Concept | Description |
|---------|-------------|
| NFA backtracking | Java uses an NFA — flexible but potentially exponential |
| Catastrophic backtracking | Nested quantifiers on overlapping sets = O(2^n) |
| ReDoS | Crafted input triggers catastrophic backtracking -> DoS |
| Pattern caching | `Pattern.compile()` is expensive — do it once, statically |
| Possessive quantifiers | `*+`, `++` — give up ownership immediately, no backtracking |
| Atomic groups | `(?>...)` — same effect as possessive, more explicit |

## Problems in This Section

| Problem | Difficulty | Key Technique |
|---------|-----------|---------------|
| [pattern-cache-refactor](problems/easy/pattern-cache-refactor/) | Easy | Static Pattern field, avoid recompilation |
| [identify-catastrophic-pattern](problems/easy/identify-catastrophic-pattern/) | Easy | Detect structural ReDoS warning signs |
| [optimize-email-validator](problems/medium/optimize-email-validator/) | Medium | Replace backtracking-heavy pattern with safe alternative |
| [possessive-quantifier-usage](problems/medium/possessive-quantifier-usage/) | Medium | Use `++`, `*+` for non-overlapping tokenization |
| [redos-safe-log-processor](problems/hard/redos-safe-log-processor/) | Hard | Full ReDoS-safe log parser with performance test |

## Quick Reference

```/dev/null/quick.txt#L1-1
// NEVER do this in a loop (recompiles every iteration):
if (line.matches("\\d{3}-\\d{4}")) { ... }

// ALWAYS do this:
private static final Pattern PHONE = Pattern.compile("\\d{3}-\\d{4}");
if (PHONE.matcher(line).matches()) { ... }

// CATASTROPHIC — exponential on "aaaaaaaaaaaa!":
(a+)+         (.+)+         (a*b?)+

// SAFE alternatives:
a++           [a-z]++       [^"]*+
```
