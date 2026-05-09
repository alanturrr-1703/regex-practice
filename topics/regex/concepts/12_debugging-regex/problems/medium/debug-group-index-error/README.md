# Debug Group Index Error

**Difficulty**: Medium — Debugging
**Concept**: `group(0)` is the entire match; `group(1)` is the first capturing group
**Estimated Time**: 20–25 minutes

---

## Problem Statement

A broken method tries to parse a `YYYY-MM-DD` date and return only the year. The broken code uses `matcher.group(0)` — which returns the **entire matched string**, not the first capturing group.

**Broken code**:
```java
// BROKEN:
// Matcher m = Pattern.compile("(\\d{4})-(\\d{2})-(\\d{2})").matcher(input);
// if (m.find()) return Optional.of(m.group(0)); // BUG: should be group(1)
```

**What goes wrong**: `group(0)` always returns the full match — for input `"2024-01-15"`, it returns `"2024-01-15"` instead of just `"2024"`.

**Your task**: fix the group index so the method returns just the year.

---

## Method Signature

```java
public Optional<String> extractYear(String input)
```

---

## Input / Output Examples

| Input | Output |
|---|---|
| `"2024-01-15"` | `Optional.of("2024")` |
| `"no date"` | `Optional.empty()` |
| `"2023-12-31 event"` | `Optional.of("2023")` |
| `""` | `Optional.empty()` |

---

## Constraints

- `input` is never null
- Return `Optional.empty()` when no `YYYY-MM-DD` date is found in the input
- Return `Optional.of(year)` where year is a 4-character string (e.g., `"2024"`)
- The date may be embedded in a larger string — use `find()` not `matches()`

---

## The Core Group Index Lesson

For pattern `(\d{4})-(\d{2})-(\d{2})` matching `"2024-01-15"`:

| Index | Call | Returns |
|---|---|---|
| 0 | `group(0)` or `group()` | `"2024-01-15"` (entire match) |
| 1 | `group(1)` | `"2024"` (year — first `(...)`) |
| 2 | `group(2)` | `"01"` (month — second `(...)`) |
| 3 | `group(3)` | `"15"` (day — third `(...)`) |

Groups are numbered by the position of their **opening parenthesis**, starting at 1. Group 0 is always the entire match. **There is no group index 0 for a capturing group.**

---

## Common Mistakes

1. **Using `group(0)` for the first capturing group** — this is the bug in this problem. Fix it to `group(1)`.
2. **Not using Optional correctly** — return `Optional.of(year)` on success, `Optional.empty()` on failure.
3. **Using `matches()` instead of `find()`** — the date may be in the middle of a sentence.

---

## Debugging Advice

Add a diagnostic print:
```java
if (m.find()) {
    System.out.println("group(0)=" + m.group(0));  // entire date
    System.out.println("group(1)=" + m.group(1));  // year
    System.out.println("group(2)=" + m.group(2));  // month
    System.out.println("group(3)=" + m.group(3));  // day
}
```
Seeing the actual values of each group index makes the bug immediately visible.
