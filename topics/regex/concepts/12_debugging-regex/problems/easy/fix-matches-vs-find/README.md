# Fix matches() vs find()

**Difficulty**: Easy — Debugging
**Concept**: `String.matches()` vs `Matcher.find()`; negative lookahead/lookbehind
**Estimated Time**: 15–20 minutes

---

## Problem Statement

A broken method checks if a string **contains** a 5-digit ZIP code. The broken implementation uses `String.matches("\\d{5}")` — which is wrong because `matches()` requires the ENTIRE string to match, not just a substring.

**Broken code** (shown as a comment in Solution.java):
```java
// BROKEN: matches() requires the FULL string to match "\\d{5}"
return input.matches("\\d{5}");
```

**What goes wrong**:
- `"ZIP: 90210 is in CA"` returns `false` — should be `true` (contains ZIP)
- `"90210"` returns `true` — correct, but only by accident (the whole string IS 5 digits)

**Your task**: fix the method to use `Pattern` + `Matcher.find()` and correctly detect a 5-digit ZIP code anywhere in the string, while NOT matching 6+ digit numbers.

---

## Method Signature

```java
public boolean containsZipCode(String input)
```

---

## Input / Output Examples

| Input | Output | Reason |
|---|---|---|
| `"ZIP: 90210 is in CA"` | `true` | Contains standalone 5-digit ZIP |
| `"90210"` | `true` | The entire string is a 5-digit ZIP |
| `"9021"` | `false` | Only 4 digits |
| `"902109"` | `false` | 6 consecutive digits — no isolated 5-digit group |
| `""` | `false` | Empty string, no ZIP |

---

## Constraints

- `input` is never null
- A "ZIP code" for this problem means exactly 5 consecutive digits, not adjacent to other digits
- Use `Pattern.compile(...)` + `matcher.find()`, not `input.matches(...)`

---

## Why "902109" Should Return false

If we just used `Pattern.compile("\\d{5}").matcher("902109").find()`, it would return `true` because `\d{5}` matches the first 5 digits of "902109" — "90210" — even though "902109" is clearly a 6-digit number, not a ZIP code.

To prevent this, use:
- `(?<!\d)` — negative lookbehind: the 5 digits must NOT be preceded by a digit
- `(?!\d)` — negative lookahead: the 5 digits must NOT be followed by a digit

Combined: `(?<!\d)\d{5}(?!\d)` — matches exactly 5 consecutive digits that are not part of a longer number.

---

## The Core Lesson

| Method | What it does |
|---|---|
| `input.matches(regex)` | Returns true only if the ENTIRE string matches the pattern |
| `Pattern.compile(regex).matcher(input).find()` | Returns true if ANY SUBSTRING of input matches the pattern |

This distinction is the most common regex bug in Java. Know it cold.

---

## Common Mistakes

1. **Using `matches()` for substring search** — always use `find()` when you want "contains this pattern"
2. **Not anchoring the digit count** — `\d{5}` without lookaheads matches inside longer digit strings
3. **Forgetting to escape in Java string** — `\\d` for `\d`, `\\{5\\}` — no, just `\\d{5}`, curly braces don't need escaping in most contexts

---

## Debugging Advice

Test your implementation against all 5 cases. The critical test is `"902109" → false`. If it returns true, you're missing the lookahead/lookbehind.
