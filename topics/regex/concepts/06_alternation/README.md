# Alternation

> **Core Question:** When a pattern has multiple possibilities, in what order does the engine try them — and why does order matter so much?

---

## What This Concept Teaches

Alternation is the regex `|` operator — the "OR" of patterns. It looks simple but hides significant engine behavior:

| Concept | Syntax | What it does |
|---|---|---|
| Simple alternation | `cat\|dog` | Matches "cat" or "dog" |
| Grouped alternation | `(?:cat\|dog)s` | Alternation scoped to a group, followed by "s" |
| Ordered matching | `cat\|catalog` | Matches "cat" in "catalog" and stops — NFA takes the first success |
| Alternation inside capture | `(http\|https)://` | Captures whichever branch matched |
| Character class vs alternation | `[aeiou]` vs `a\|e\|i\|o\|u` | Character class is faster for single-char alternatives |

---

## Why It Matters

Alternation is everywhere:
- Multi-format input parsing (`YYYY-MM-DD|MM/DD/YYYY`)
- Protocol matching (`http|https|ftp`)
- Log level detection (`DEBUG|INFO|WARN|ERROR|FATAL`)
- File extension validation (`.java|.py|.ts`)

But alternation is also the most common source of subtle bugs:
- **Ordering bugs** — short alternative eats input before the long one gets a chance
- **Precedence bugs** — `a|bc` is `a` OR `bc`, not `(a|b)c`
- **Performance bugs** — many alternatives with shared prefixes cause exponential backtracking

---

## Prerequisites

- `basics/` — `Pattern`, `Matcher`, `find()`, `matches()`
- `groups-and-capturing/` — `(?:...)` non-capturing groups, group scoping

---

## Problem Set

### Easy

| Problem | Key Skills |
|---|---|
| [`match-log-levels`](problems/easy/match-log-levels/) | Basic alternation, `\|`, word boundaries |
| [`validate-file-extension`](problems/easy/validate-file-extension/) | Alternation anchored to `$`, case-insensitive flag |

### Medium

| Problem | Key Skills |
|---|---|
| [`multi-format-date-parser`](problems/medium/multi-format-date-parser/) | Complex alternation, ordering, `(?:...\|...\|...)` |
| [`protocol-extractor`](problems/medium/protocol-extractor/) | Alternation inside a capture group, group index behavior |

### Hard

| Problem | Key Skills |
|---|---|
| [`alternation-order-bug`](problems/hard/alternation-order-bug/) | NFA ordering bug, short-circuit alternation, root-cause diagnosis |

---

## Key Mental Models

### Alternation is LEFT-BIASED: first success wins
```
Pattern:  cat|catalog
Input:    "catalog"

Engine at position 0:
  Try "cat" → matches positions 0-2 ✓ → SUCCESS, done.
  "catalog" is never tried for this position.

Result: match is "cat", leaving "alog" unconsumed.
```

### Grouping changes precedence dramatically
```
Pattern:  a|bc           → "a"  OR  "bc"   (| has lowest precedence)
Pattern:  (?:a|b)c       → ("a" OR "b") followed by "c"

Input: "ac"
  a|bc     → "a" matches at position 0 (bc never tried)
  (?:a|b)c → "ac" matches ("a" + "c")
```

### Alternation vs character class
```
[aeiou]     → single character, one of: a, e, i, o, u
a|e|i|o|u   → exactly the same semantics but much slower
              (5 NFA branches vs 1 character class transition)

[cat]       → single character: c, a, or t
cat|at|t    → entirely different! tries the WORDS "cat", "at", "t"
```

### The ordering fix for ambiguous alternatives
```
BROKEN:  [$€]\d+|[$€]\d+\.\d{2}
  "$10.50" → matches "$10" (shorter alternative wins first)

FIXED:   [$€]\d+\.\d{2}|[$€]\d+
  "$10.50" → matches "$10.50" (decimal tried first, succeeds)
  "$10"    → decimal fails, integer succeeds → "$10"
```

---

## Java Quick Reference

```java
// Basic alternation — must escape | in raw string is fine, but
// in a Java string literal the | needs no escaping
Pattern p = Pattern.compile("DEBUG|INFO|WARN|ERROR|FATAL");
Matcher m = p.matcher("2024-01-15 ERROR: disk full");
boolean found = m.find(); // true

// Alternation inside a non-capturing group with quantifier
Pattern ext = Pattern.compile(
    "(?i)\\.(?:java|py|js|ts|go)$"   // case-insensitive, anchored to end
);

// Alternation inside a capturing group
Pattern proto = Pattern.compile("(https?|ftp|sftp)://([^/\\s]+)");
Matcher pm = proto.matcher("ftp://files.example.com/data");
if (pm.find()) {
    String protocol = pm.group(1); // "ftp"
    String host     = pm.group(2); // "files.example.com"
}

// Alternation with CASE_INSENSITIVE flag
Pattern ci = Pattern.compile(
    "(?:DEBUG|INFO|WARN|ERROR|FATAL)",
    Pattern.CASE_INSENSITIVE
);
```

---

## Common Mistakes Preview

1. **`cat|catalog` order** — put the longer/more-specific alternative first
2. **Missing grouping around alternation** — `http|ftp://host` is `http` OR `ftp://host`, not `(http|ftp)://host`
3. **Alternation vs character class** — use `[aeiou]` not `a|e|i|o|u` for single characters
4. **Shared-prefix alternatives** — `color|colour` can be factored as `colo(?:r|ur)` for performance

---

## Read Next

- [`greedy-vs-lazy/`](../greedy-vs-lazy/) — how alternation interacts with backtracking
- [`regex-performance/`](../regex-performance/) — ReDoS and catastrophic alternation
- [`lookahead-lookbehind/`](../lookahead-lookbehind/) — zero-width alternatives
