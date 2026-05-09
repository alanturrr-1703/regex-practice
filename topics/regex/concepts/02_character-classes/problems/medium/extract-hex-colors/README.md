# Extract Hex Colors

**Difficulty:** Medium  
**Concept:** Character Classes  
**Concepts Tested:** Multi-range character class `[0-9a-fA-F]`, exact repetition `{n}`, `Matcher.find()` extraction loop, anchoring with lookahead

---

## Problem Statement

Extract all **CSS hex color codes** from a string. A valid hex color for this problem is:
- `#` followed by **exactly 6** hexadecimal digits
- Hexadecimal digits are: `0-9`, `a-f`, `A-F`
- 3-digit hex shorthand (`#fff`) is NOT included — exactly 6 required
- Return all matches including the `#` prefix, in order of appearance

---

## Constraints

- Input is non-null but may be empty.
- Return matches as `List<String>`, including the `#`.
- If no hex colors are found, return an empty list.
- Case-sensitive for the hex digits (or use `CASE_INSENSITIVE` — either is fine).
- Do NOT match 3-digit hex codes.
- Do NOT match 7+ digit sequences (match exactly 6 hex digits).
- Hex color must start with `#` — bare hex sequences without `#` are not matches.

---

## Input / Output Examples

| Input | Output |
|-------|--------|
| `"color: #ff0000 and #ABCDEF"` | `["#ff0000", "#ABCDEF"]` |
| `"no colors here"` | `[]` |
| `"#xyz123"` | `[]` (x, y, z are not hex digits) |
| `"#fff"` | `[]` (only 3 hex digits after `#`) |
| `"background:#001122;"` | `["#001122"]` |

---

## Edge Cases

- **`#xyz123`**: `x`, `y`, `z` are not hex digits → no match.
- **`#fff`**: only 3 digits → no match.
- **`#AABBCC00`**: `#[0-9a-fA-F]{6}` matches `#AABBCC` (first 6), leaving `00`. If you don't add a lookahead, this partial match is returned. Consider whether this is correct behavior for your implementation.
- **No `#` prefix**: `"ff0000"` → no match (must start with `#`).
- **`#`** alone: `#` without any hex digits → no match.
- **Empty input** → `[]`.

---

## Expected Time Complexity

- **O(n)** where n = string length.

---

## Real-World Relevance

Hex color extraction is used in:
- **CSS linters and formatters**: find all color values in stylesheets.
- **Design tools**: extract color palettes from CSS/HTML.
- **Color accessibility checkers**: analyze contrast ratios.
- **Code review tools**: highlight color values in diffs.

---

## Regex Thinking Process

1. **Literal `#`**: the color starts with a `#` — just write `#`.
2. **Hex digit**: `[0-9a-fA-F]` — three ranges in one class: decimal digits, lowercase a-f, uppercase A-F.
3. **Exactly 6**: `{6}` — exact repetition quantifier.
4. **Combined**: `#[0-9a-fA-F]{6}`

Alternatively with `CASE_INSENSITIVE`: `#[0-9a-f]{6}` — cleaner but uses a flag.

**Boundary question**: should `#AABBCC` inside `#AABBCC00` be matched? The problem says "exactly 6 hex digits". With `find()`, the pattern `#[0-9a-fA-F]{6}` would match `#AABBCC` from `#AABBCC00`. To prevent this (only match if NOT followed by another hex digit), use a negative lookahead:

```
#[0-9a-fA-F]{6}(?![0-9a-fA-F])
```

For this problem, the simpler pattern without lookahead is acceptable — the test cases don't include this ambiguous scenario.

---

## Common Mistakes

1. **Writing `{6,}` instead of `{6}`** — `{6,}` means "6 or more", which would match `#AABBCC00` as the full 8 hex digits.
2. **Forgetting the `#`** — pattern matching bare hex digits without the `#` prefix.
3. **Using `[0-9a-f]` without `CASE_INSENSITIVE`** — misses uppercase hex like `#ABCDEF`.
4. **Using `.{6}` instead of `[0-9a-fA-F]{6}`** — `.` matches any character, so `#xxxxxx` would incorrectly match.

---

## Debugging Advice

Test your pattern on `"#ff0000"` first. It should return exactly one match: `"#ff0000"`.

Then test on `"#xyz123"` — should return no match.

Then test on `"no colors"` — should return empty list.

Print `m.group()` and `m.start()` in the loop to trace each match.
