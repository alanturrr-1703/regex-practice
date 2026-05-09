# Possessive Quantifier Usage

**Difficulty**: Medium
**Concept**: Possessive quantifiers `++`, `*+` for non-backtracking tokenization

---

## Problem Statement

Implement a log line tokenizer that parses lines in the following format:

```/dev/null/format.txt#L1-1
HH:MM:SS SEVERITY message text here
```

Examples:
```/dev/null/examples.txt#L1-3
14:23:01 ERROR some message here
09:00:00 INFO  server started
23:59:59 WARN  disk usage above 80%
```

Return a `TokenizedLine` record/class with three fields:
- `timestamp`: the time string (e.g., `"14:23:01"`)
- `severity`: the severity word (e.g., `"ERROR"`)
- `message`: the rest of the line after the severity and space (e.g., `"some message here"`)

If the line does not match the format → return `Optional.empty()`.

### Key Requirement: Use Possessive Quantifiers

The timestamp consists of `\d` characters and `:` — use `[\d:]++` (possessive).
The severity consists of `[A-Z]` characters — use `[A-Z]++` (possessive).
The message is the rest — use `.++` or `.*+` (possessive) for the remainder.

These are safe with possessive because the character sets are **disjoint** from what
follows (timestamp ends before a space; severity ends before a space).

---

## Constraints

- Input may be `null` → return `Optional.empty()`.
- A line with the wrong format → return `Optional.empty()`.
- Timestamps must be in `HH:MM:SS` format (8 chars of `\d` and `:`).
- Severity must be one or more uppercase letters.
- Message may be empty (if line is `"HH:MM:SS SEVERITY"` with no following text).
- Do NOT use `String.split()` — use a single compiled `Pattern` with possessive quantifiers.

---

## Input / Output Examples

| Input | timestamp | severity | message |
|-------|-----------|----------|---------|
| `"14:23:01 ERROR some message"` | `"14:23:01"` | `"ERROR"` | `"some message"` |
| `"09:00:00 INFO  server started"` | `"09:00:00"` | `"INFO"` | ` server started"` |
| `"not a log line"` | — | — | `Optional.empty()` |
| `null` | — | — | `Optional.empty()` |

---

## Why Possessive Is Safe Here

```/dev/null/safe-analysis.txt#L1-12
Pattern: ([\d:]++) ([A-Z]++) (.++)

1. [\d:]++ greedily consumes all digits and colons — possessively
   After it: a space must follow. Digits/colons and space are disjoint.
   Possessive is safe: no reason to give back a digit/colon to try to match a space.

2. [A-Z]++ greedily consumes all uppercase letters — possessively
   After it: a space must follow. Uppercase and space are disjoint.
   Possessive is safe.

3. .++ greedily consumes the rest of the line
   It's at the end — possessive is trivially safe.
```

If this were `([\d:]+) ([A-Z]+) (.+)` (greedy, not possessive), the engine would still
work correctly but would spend microseconds backtracking through the quantifiers when
there's a space in the timestamp (which there isn't, since digits/colons can't be spaces).
Possessive makes the "no backtracking" explicit and guaranteed.

---

## Time Complexity

O(N) per line, zero backtracking due to possessive quantifiers.

---

## Real-World Relevance

Possessive quantifiers are the idiomatic way to implement **tokenizers** in Java regex.
Log processors, protocol parsers, and CSV tokenizers all benefit from possessive
quantifiers when character sets are provably disjoint.

---

## Common Mistakes

- Using greedy `+` instead of `++` — still works but misses the teaching point.
- Using possessive on OVERLAPPING sets: `[a-z]++[a-z]` — will never match because the
  possessive quantifier eats all lowercase letters, leaving none for the second `[a-z]`.
- Forgetting to escape the dot in `.++` — but `.` (any char) is intentional for the message.
