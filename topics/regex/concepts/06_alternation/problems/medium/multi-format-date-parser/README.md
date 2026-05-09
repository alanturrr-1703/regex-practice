# Multi-Format Date Parser

**Difficulty:** Medium  
**Concept:** Alternation  
**Estimated Time:** 25–40 minutes

---

## Concepts Tested

- Complex alternation with `(?:...|...|...)`
- Ordering sensitivity: why the most specific pattern should come first
- Non-capturing groups for structure without capture overhead
- `matcher.group()` (group 0 = whole match) to return matched raw text
- Three distinct date format sub-patterns combined into one alternation

---

## Problem Statement

Given a string, extract all dates that appear in any of these three formats:

| Format | Pattern | Example |
|---|---|---|
| 1 | `YYYY-MM-DD` | `2024-01-15` |
| 2 | `MM/DD/YYYY` | `01/15/2024` |
| 3 | `DD Mon YYYY` | `15 Jan 2024` |

Return each match as the **raw string** exactly as it appears in the input (no reformatting).

---

## Method Signature

```java
public List<String> extractDates(String input)
```

---

## Constraints

- `input` is never `null`
- All three formats may appear mixed in one string
- The month in Format 3 is a 3-letter English abbreviation (Jan, Feb, Mar, Apr, May, Jun, Jul, Aug, Sep, Oct, Nov, Dec)
- No calendar validation required
- Return raw matched strings

---

## Input / Output Examples

| Input | Output |
|---|---|
| `"Scheduled: 2024-01-15"` | `["2024-01-15"]` |
| `"Due: 01/15/2024"` | `["01/15/2024"]` |
| `"Meeting on 15 Jan 2024"` | `["15 Jan 2024"]` |
| `"Formats: 2024-01-15 and 01/15/2024 and 15 Jan 2024"` | `["2024-01-15", "01/15/2024", "15 Jan 2024"]` |
| `"no dates"` | `[]` |
| `""` | `[]` |

---

## Edge Cases

- **Format ambiguity**: `"2024-01-15"` cannot be confused with `"01/15/2024"` because they use different separators. Format 3 is distinguished by the month name.
- **Format 3 with 1-digit day**: `"5 Jan 2024"` should match (use `\d{1,2}`)
- **Multiple formats in one string**: all should be found

---

## Time Complexity

- **O(n)** — single scan; NFA tries alternatives at each position

---

## Real-World Relevance

- **Document parsing**: dates in reports may not be in a standard format
- **Email extraction**: meeting invites mix `"January 5th, 2024"` style with ISO dates
- **Legacy data migration**: old databases store dates in regionally different formats

---

## Regex Thinking Process

1. **Write each format independently:**
   - Format 1: `\d{4}-\d{2}-\d{2}`
   - Format 2: `\d{2}/\d{2}/\d{4}`
   - Format 3: `\d{1,2}\s+(?:Jan|Feb|Mar|Apr|May|Jun|Jul|Aug|Sep|Oct|Nov|Dec)\s+\d{4}`

2. **Combine with alternation inside a non-capturing group:**
   ```
   (?:\d{4}-\d{2}-\d{2}|\d{2}/\d{2}/\d{4}|\d{1,2}\s+(?:Jan|...)...\d{4})
   ```

3. **Ordering**: Format 1 and Format 2 cannot match at the same position (different separators). Format 3 starts with `\d{1,2}\s+` which could theoretically match the `\d{2}` in Format 2 at some positions, but Format 2's slashes prevent confusion. Order doesn't matter for correctness here, but putting longer/more-specific patterns first is best practice.

4. **Return `matcher.group()`** (= `group(0)`) since there are no capturing groups — just the non-capturing outer group.

---

## Common Mistakes

| Mistake | Why It's Wrong |
|---|---|
| Using a capturing group instead of `(?:...)` | Creates group(1), shifts indices |
| Returning `group(1)` when pattern uses `(?:...)` | `group(1)` is null or wrong; use `group(0)` or `group()` |
| Forgetting `\s+` in Format 3 | "15Jan2024" has no space, but "15 Jan 2024" requires `\s+` |
| Hardcoding spaces instead of `\s+` | Fails if there are multiple spaces or tabs |
| Forgetting all 12 month abbreviations | Missing months causes no-match for those dates |

---

## Debugging Advice

Test each format sub-pattern in isolation first:
```java
// Test Format 3:
Pattern p3 = Pattern.compile("\\d{1,2}\\s+(?:Jan|Feb|Mar|Apr|May|Jun|Jul|Aug|Sep|Oct|Nov|Dec)\\s+\\d{4}");
Matcher m = p3.matcher("Meeting on 15 Jan 2024");
System.out.println(m.find());     // true
System.out.println(m.group());    // "15 Jan 2024"
```
Confirm each works alone, then combine.
