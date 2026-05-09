# Extract Date Parts

**Difficulty:** Easy  
**Concepts Tested:** Capturing groups, digit quantifiers (`\d{n}`), `Matcher.find()`, group indexing

---

## Problem Statement

Given a string that may contain one or more dates in **ISO `YYYY-MM-DD` format**, find every date and return a list of `DateParts` objects — each holding the year, month, and day as separate `String` fields.

Your method signature:

```java
public List<DateParts> extractDates(String input)
```

`DateParts` is a static inner class (already provided in `Solution.java`) with `String year`, `String month`, and `String day` fields.

---

## Constraints

- Input may be `null`-free but could be empty — return an empty list.
- The regex does **not** validate ranges (month 13 or day 99 are valid matches).
- Multiple dates in one string should all be returned, in the order they appear.
- Dates may appear embedded in longer strings (timestamps, sentences, filenames).

---

## Input / Output Examples

| Input | Expected Output |
|---|---|
| `"Event on 2024-01-15 and 2023-12-31"` | `[DateParts("2024","01","15"), DateParts("2023","12","31")]` |
| `"no dates here"` | `[]` |
| `""` | `[]` |
| `"2024-13-99"` | `[DateParts("2024","13","99")]` — regex doesn't validate |
| `"Date: 1999-06-01."` | `[DateParts("1999","06","01")]` |
| `"2024-01-15T10:00:00"` | `[DateParts("2024","01","15")]` — partial match via `find()` |

---

## Edge Cases

- **Empty string** — return `[]`, no crash.
- **Date at end of string with trailing punctuation** — `find()` does not need anchors; it scans.
- **Embedded in ISO timestamp** — the date prefix is still matched; only the date portion is captured.
- **Out-of-range values** — regex is structural only; `13` and `99` are matched as valid two-digit sequences.

---

## Expected Time Complexity

`O(n)` where `n` is the length of the input string — one linear scan by the matcher.

---

## Real-World Relevance

Log files, database exports, and user-generated content often contain dates in mixed-format strings. Extracting structured date components (year, month, day) is a fundamental ETL and data-cleaning task. Named or indexed capturing groups let you pull each part directly without `String.split()` gymnastics.

---

## Regex Thinking Process

Build the pattern step by step:

1. **Year** — four digits: `\d{4}`
2. **Separator** — literal hyphen: `-`
3. **Month** — exactly two digits: `\d{2}`
4. **Separator** — literal hyphen: `-`
5. **Day** — exactly two digits: `\d{2}`
6. **Add capturing groups** around each component:
   `(\d{4})-(\d{2})-(\d{2})`
7. **Group index map:**
   - `group(0)` — full match e.g. `"2024-01-15"`
   - `group(1)` — year
   - `group(2)` — month
   - `group(3)` — day

---

## Common Mistakes

| Mistake | Why it fails |
|---|---|
| Using `matches()` instead of `find()` | `matches()` requires the whole string to match — misses embedded dates |
| Off-by-one on group indices | Groups are 1-indexed; `group(0)` is the whole match |
| Using `\d\d\d\d` instead of `\d{4}` | Works but harder to read; no functional difference |
| Forgetting to escape backslash in Java | Java strings need `\\d`, not `\d` |

---

## Debugging Advice

- Print `matcher.group(0)` first to confirm the full match is what you expect.
- Then print `group(1)`, `group(2)`, `group(3)` to verify group assignment.
- Use `Pattern.compile(pattern).matcher(input)` — keep pattern and matcher construction separate so you can log both.
- If you get `IllegalStateException: No match found`, you're calling `group()` before a successful `find()`.
