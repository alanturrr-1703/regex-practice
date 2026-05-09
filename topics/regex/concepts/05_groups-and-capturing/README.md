# Groups and Capturing

> **Core Question:** How does a regex engine *remember* what it matched?  
> Answer: capturing groups — the bridge between "does this match?" and "what are the parts?"

---

## What This Concept Teaches

Without groups, regex is a predicate: *yes/no*. With groups, regex is a *parser*: it extracts structured fields from unstructured text. This concept covers:

| Feature | Syntax | What it does |
|---|---|---|
| Capturing group | `(pattern)` | Matches and stores the text in a numbered slot |
| Non-capturing group | `(?:pattern)` | Groups for structure/quantifier without storing |
| Named group | `(?<name>pattern)` | Capturing group with a human-readable label |
| Backreference | `\1`, `\2`, … | Matches the same text captured by group N |
| Nested groups | `((a)(b))` | Groups-within-groups; numbering goes left-to-right by `(` |

---

## Why It Matters

Every real-world regex task — log parsing, date extraction, URL decomposition, config file reading — requires extracting fields, not just checking for a match. Capturing groups are the fundamental mechanism for that. Named groups make the code self-documenting. Backreferences enable patterns that are impossible to express otherwise (e.g., finding repeated words).

---

## Prerequisites

- `basics/` — `Pattern`, `Matcher`, `find()`, `matches()`
- `character-classes/` — `\d`, `\w`, `\s`, ranges
- `quantifiers/` — `*`, `+`, `?`, `{n,m}`, greedy vs lazy

---

## Problem Set

### Easy

| Problem | Key Skills |
|---|---|
| [`extract-date-parts`](problems/easy/extract-date-parts/) | Numbered groups, `group(1/2/3)`, iterating matches |
| [`capture-first-word`](problems/easy/capture-first-word/) | `group(0)` vs `group(1)`, single-group pattern, Optional |

### Medium

| Problem | Key Skills |
|---|---|
| [`named-groups-log-parser`](problems/medium/named-groups-log-parser/) | `(?<name>...)`, `matcher.group("name")`, real log format |
| [`backreference-duplicate-word`](problems/medium/backreference-duplicate-word/) | `\1` backreference, `CASE_INSENSITIVE`, word boundaries |

### Hard

| Problem | Key Skills |
|---|---|
| [`nested-groups-csv-parser`](problems/hard/nested-groups-csv-parser/) | Group ordering complexity, alternation within groups, `(?:...)` |

---

## Key Mental Models

### Group 0 is always the whole match
```
Pattern:  (\d{4})-(\d{2})-(\d{2})
Input:    "2024-01-15"
group(0) = "2024-01-15"   ← entire match
group(1) = "2024"
group(2) = "01"
group(3) = "15"
```

### Groups are numbered by their OPENING parenthesis, left-to-right
```
Pattern:  (a(b(c)))
           1 2 3
group(1) = "abc"
group(2) = "bc"
group(3) = "c"
```

### Non-capturing groups are invisible to group numbering
```
Pattern:  (?:a)(b)(c)
                1  2
group(1) = "b"
group(2) = "c"
```

### Backreferences match the same TEXT, not the same pattern
```
Pattern:  (\w+)\s+\1
Input:    "hello hello"   → matches (backreference "hello" == captured "hello")
Input:    "hello world"   → no match (backreference "hello" != "world")
```

---

## Java Quick Reference

```java
// Compile and match
Pattern p = Pattern.compile("(\\d{4})-(\\d{2})-(\\d{2})");
Matcher m = p.matcher("Event: 2024-01-15");

while (m.find()) {
    String year  = m.group(1);   // "2024"
    String month = m.group(2);   // "01"
    String day   = m.group(3);   // "15"
    int startPos = m.start(1);   // position of year in input
    int endPos   = m.end(1);     // exclusive end position
}

// Named groups
Pattern np = Pattern.compile("(?<year>\\d{4})-(?<month>\\d{2})-(?<day>\\d{2})");
Matcher nm = np.matcher("2024-01-15");
if (nm.find()) {
    String year = nm.group("year");   // "2024"
}

// How many capturing groups does the pattern have?
int count = p.groupCount(); // 3 (does NOT count group 0)
```

---

## Common Mistakes Preview

1. **Off-by-one on group index** — `group(0)` is the whole match; named groups start at 1
2. **Using `(?:...)` then trying to access it by index** — non-capturing groups have no index
3. **`matcher.group(n)` returns `null`** — group participated in the pattern but the alternative branch didn't match
4. **Forgetting `\\1` double-backslash in Java strings** — `\1` is an octal escape in Java; you need `\\1`

---

## Read Next

After completing the problems here, continue with:
- [`alternation/`](../alternation/) — how `|` works inside and outside groups
- [`greedy-vs-lazy/`](../greedy-vs-lazy/) — controlling how much groups consume
- [`lookahead-lookbehind/`](../lookahead-lookbehind/) — zero-width assertions that interact with groups
