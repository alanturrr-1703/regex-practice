# Extract Prices

**Difficulty**: Medium
**Concept**: Positive Lookbehind `(?<=...)` with alternation

---

## Problem Statement

Implement `extractAmounts(String input)` that extracts **numeric amounts** from a string
where those amounts are **immediately preceded** by a currency symbol (`$` or `€`).

Return a `List<String>` containing only the numeric parts (digits and decimal point) —
**not** the currency symbol itself.

A valid numeric amount:
- One or more digits: `\d+`
- Optionally followed by a decimal point and exactly two digits: `\.\d{2}`

Examples: `"19.99"`, `"100"`, `"25.00"`, `"1000"`

If there is a space between the currency symbol and the number (e.g., `"€ 50"`), it does
**not** match — the symbol must be immediately adjacent to the digits.

---

## Constraints

- Currency symbols supported: `$` and `€` (that's U+0024 and U+20AC).
- Return an empty list for null input or no matches.
- Order of results must match order of appearance in the string (left to right).
- Amounts must be immediately preceded by `$` or `€` with no space.
- The currency symbol is NOT included in the returned strings.

---

## Input / Output Examples

| Input | Expected |
|-------|----------|
| `"total: $19.99 and €25.00"` | `["19.99", "25.00"]` |
| `"no currency here"` | `[]` |
| `"$100 off today"` | `["100"]` |
| `"€ 50 off"` | `[]` (space between € and 50) |
| `"$10 and $20 and $30"` | `["10", "20", "30"]` |
| `null` | `[]` |
| `"€0.99 and $1234.56"` | `["0.99", "1234.56"]` |

---

## Edge Cases

- Currency symbol at end of string with no following digits: `"price is $"` → `[]`.
- Amount with more or fewer than 2 decimal places: `"$1.5"` and `"$1.567"` — your pattern
  must decide whether to match these. The spec says exactly two decimal digits, so `"$1.5"`
  → only `"1"` would match (the `.5` is not `.\d{2}`). Think carefully about greediness.
- Zero: `"$0"` → `["0"]`.
- Large numbers: `"$1000000"` → `["1000000"]`.

---

## Time Complexity

O(N) — single linear scan.

---

## Real-World Relevance

Price extraction from unstructured text is a core NLP/parsing task in:
- Receipt/invoice processing
- Financial data scraping
- E-commerce feed normalization
- Chatbot intent extraction ("I paid $42.99 for that")

Lookbehind lets you assert the currency context without including it in the extracted value,
which simplifies downstream parsing (no need to strip the symbol).

---

## Regex Thinking Process

Step 1: I want to match the number. The basic number pattern:
        `\d+(?:\.\d{2})?`  — digits, optionally followed by `.` and exactly 2 digits.

Step 2: I want this only when preceded by `$` or `€`.
        Lookbehind: `(?<=\$|€)`

Step 3: Java requires fixed-length lookbehind. Is `\$|€` fixed-length?
        `\$` = 1 char, `€` = 1 char (U+20AC is BMP, so 1 Java `char`). ✅ Equal length.
        This is allowed in Java 8+.

Step 4: Combined: `(?<=\$|€)\d+(?:\.\d{2})?`

Step 5: Use `Matcher.find()` in a loop; `matcher.group()` gives the matched amount.

---

## Common Mistakes

- Including the currency symbol in the return value — the lookbehind asserts but doesn't capture.
- Using `(?<=[$€])` — both `$` and `€` are single chars, so this works too; it's equivalent.
- `(?<=\$|€)` vs `(?<=[$€])`: functionally identical here. Character class `[$€]` is slightly
  more readable; alternation `\$|€` is more explicit.
- Matching `"€ 50"` — the space breaks adjacency. Only test with `Matcher.find()`, not `find()`
  after a `reset()` to a substring.
- Variable-length lookbehind trap: `(?<=USD)` is fixed (3 chars), `(?<=[A-Z]{1,3})` is not
  (in Java 8). Stick to fixed-length or use Java 14+.

---

## Debugging Advice

Visualize what the lookbehind is checking: at each position where `\d` could start, the
lookbehind checks one character backward. If that character is `$` or `€`, the assertion
passes. Walk through your test strings manually at the character level.
