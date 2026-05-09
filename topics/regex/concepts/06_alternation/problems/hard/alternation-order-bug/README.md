# Alternation Order Bug

**Difficulty:** Hard  
**Concept:** Alternation  
**Estimated Time:** 45–60 minutes

---

## Concepts Tested

- NFA alternation ordering: the left branch is committed to on success
- Why `shorter|longer` causes the longer pattern to never match at the same position
- Root-cause diagnosis: when a regex "almost works"
- Fixing alternation order: `longer|shorter`
- `BROKEN_PATTERN` is provided for study — `FIXED_PATTERN` is your task

---

## Problem Statement

A method `extractAmounts` tries to extract **currency amounts** from text. Amounts look like:
- `$10` — integer dollar amount
- `$10.50` — decimal dollar amount  
- `€20` — integer euro amount
- `€20.99` — decimal euro amount

The currency symbol is `$` or `€`.

A **broken** pattern is provided as a constant for educational purposes. The broken pattern correctly matches integer amounts but **incorrectly matches `$10` instead of `$10.50`** when the decimal form is present.

**Your tasks:**
1. Understand WHY the broken pattern fails (read the JavaDoc)
2. Define `FIXED_PATTERN` that correctly matches decimal-first, then integer fallback
3. Implement `extractAmounts(String input)` using the fixed pattern

---

## Method Signature

```java
public List<String> extractAmounts(String input)
```

---

## The Broken Pattern and Why It Fails

```
BROKEN:  [$€]\d+|[$€]\d+\.\d{2}

Applied to "$10.50":
  Position 0: try [$€]\d+
    - [$€] matches '$' ✓
    - \d+ matches "10" (stops at '.') ✓
    - Branch succeeds! Returns "$10"
  The decimal alternative [$€]\d+\.\d{2} is NEVER tried at position 0
  
Result: "$10" — the ".50" is left unmatched
```

**Root cause:** The integer alternative `[$€]\d+` succeeds first, so the NFA commits to it. The decimal alternative is only tried if the integer alternative FAILS — but `[$€]\d+` always succeeds whenever `[$€]\d+\.\d{2}` would match.

---

## The Fix

Swap the order so the decimal alternative is tried first:

```
FIXED:   [$€]\d+\.\d{2}|[$€]\d+

Applied to "$10.50":
  Position 0: try [$€]\d+\.\d{2}
    - [$€] matches '$', \d+ matches "10", \. matches '.', \d{2} matches "50"
    - Branch succeeds! Returns "$10.50" ✓

Applied to "$10":
  Position 0: try [$€]\d+\.\d{2}
    - [$€] matches '$', \d+ matches "10", \. needs '.' but hits end → fails
  try [$€]\d+
    - [$€] matches '$', \d+ matches "10" ✓
    - Returns "$10" ✓
```

---

## Input / Output Examples

| Input | Expected (with FIXED pattern) |
|---|---|
| `"$10.50 and $10"` | `["$10.50", "$10"]` |
| `"€20.99 total"` | `["€20.99"]` |
| `"no money"` | `[]` |
| `"$0.01"` | `["$0.01"]` |
| `"€5 and $100.00"` | `["€5", "$100.00"]` |

---

## Constraints

- `input` is never `null`
- Currency amounts use `$` or `€` as the symbol
- Integer amounts: one or more digits, no decimal
- Decimal amounts: one or more digits, literal `.`, exactly 2 decimal digits
- Return matches in order of appearance

---

## Edge Cases

- **`"$10"` before `"$10.50"`** in the same string: The NFA scans left to right, so `$10` is found at its position, and `$10.50` is found at its position. No interference.
- **`"$10.50"` with the BROKEN pattern** returns `"$10"` — the point of the exercise
- **`"$0.01"`**: `\d+` matches "0", `\.` matches ".", `\d{2}` matches "01" → `"$0.01"` ✓

---

## Time Complexity

- **O(n)** — single scan

---

## Real-World Relevance

This ordering bug is one of the most common real-world regex bugs. It appears in:
- **Price extractors**: matching `$9.99` vs `$9` — wrong order gets you `$9` from `$9.99`
- **IP address parsers**: matching `192.168` vs `192` — if integer alternative comes first
- **Version string parsers**: `1.2.3` vs `1.2` vs `1` — must try longest first
- **Tokenizers**: any time a token has a short and a long form

---

## Common Mistakes

| Mistake | Why It's Wrong |
|---|---|
| Using `\d+(?:\.\d{2})?` (optional decimal) | Actually works! But doesn't teach the ordering lesson — use explicit alternation |
| Keeping the broken order | The fix IS the learning objective |
| Forgetting to escape the dot | `\d+.\d{2}` matches "10X50" — use `\.` |
| Not anchoring the decimal digits count | `\d+` after the decimal could match 1 or 3+ digits; use `\d{2}` for exactly 2 |

---

## Debugging Advice

Test the broken pattern to observe the bug:
```java
Pattern broken = Pattern.compile("[$€]\\d+|[$€]\\d+\\.\\d{2}");
Matcher m = broken.matcher("$10.50");
m.find();
System.out.println(m.group()); // "$10" — wrong! Should be "$10.50"
```

Then test the fixed pattern:
```java
Pattern fixed = Pattern.compile("[$€]\\d+\\.\\d{2}|[$€]\\d+");
Matcher m2 = fixed.matcher("$10.50");
m2.find();
System.out.println(m2.group()); // "$10.50" — correct!
```

This is the essence of the lesson: NFA alternation is left-biased and commits to the first success.
