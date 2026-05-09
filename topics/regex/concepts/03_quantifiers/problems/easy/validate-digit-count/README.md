# Problem: Validate Digit Count

**Difficulty**: Easy  
**Concept**: Quantifiers  
**Skills Tested**: `{n}` exact quantifier, negative lookahead `(?!\d)`, negative lookbehind `(?<!\d)`

---

## Problem Statement

Given a string, return `true` if it contains a run of **exactly 5 consecutive digits** somewhere inside it, and `false` otherwise.

The catch: a naïve `\d{5}` pattern will match 5 digits even if they are part of a longer sequence (e.g., it will match the first 5 digits of `"123456"`). You must enforce that the 5-digit run is **not adjacent to any other digit** — neither before nor after.

---

## Constraints

- Input may be `null` — treat `null` as `false` (no match)
- Input may be empty — return `false`
- The 5-digit run may appear anywhere in the string (beginning, middle, end)
- There may be multiple digit runs; only one needs to qualify
- Surrounding characters may be anything (letters, symbols, spaces)
- The solution must use a single regex with `find()`

---

## Input / Output Examples

| Input | Output | Reason |
|---|---|---|
| `"abc12345def"` | `true` | Exactly 5 digits surrounded by letters |
| `"abc123456def"` | `false` | 6 consecutive digits — too many |
| `"12345"` | `true` | Exactly 5 digits, string boundary is not a digit |
| `"1234"` | `false` | Only 4 consecutive digits |
| `"code12345zip"` | `true` | 5 digits surrounded by letters |
| `"x1234567y"` | `false` | 7 consecutive digits |
| `"1234 12345"` | `true` | Second run is exactly 5 digits |
| `""` | `false` | Empty string |

---

## Edge Cases

- `"012345"` → `false` (6 digits starting with 0 — still 6)
- `"12345 67890"` → `true` (two separate 5-digit runs, both valid)
- `"1234512345"` → `false` (10 consecutive digits)
- `null` → `false`
- `"     12345     "` → `true` (spaces are not digits)
- `"-12345-"` → `true` (dashes are not digits)

---

## Expected Time Complexity

O(n) where n is the length of the input string. The regex engine makes a single pass with lookahead/lookbehind checks at each position.

---

## Real-World Relevance

This exact problem appears in:
- **ZIP code validation** within a larger text (finding exactly 5-digit codes, not 9-digit ZIP+4)
- **Credit card detection** (finding exactly N-digit runs in freetext)
- **Log parsing** where specific field widths are required
- **Data extraction pipelines** where "exactly N digits" is a field marker

---

## Regex Thinking Process

**Step 1**: Start with `\d{5}` — this matches any 5 consecutive digits, but also matches the first 5 digits of `"1234567"`.

**Step 2**: We need to assert that NO digit precedes our match. Add a negative lookbehind: `(?<!\d)\d{5}`. Now `"123456"` at position 0 — the lookbehind at position 0 passes (nothing before is a digit), but we still match `"12345"` and the `"6"` after is ignored.

**Step 3**: We need to assert that NO digit follows our match. Add a negative lookahead: `(?<!\d)\d{5}(?!\d)`. Now `"123456"` fails because after `"12345"` comes `"6"` — the lookahead fails.

**Step 4**: Verify: `"abc12345def"` — lookbehind at position 3 sees `'c'` (not a digit ✓), matches `"12345"`, lookahead sees `'d'` (not a digit ✓). Returns `true`.

---

## Common Mistakes

1. **Using `\d{5}` alone**: Always matches prefix of longer runs. Must use boundary assertions.
2. **Using `\b` instead of `(?<!\d)`**: Word boundaries (`\b`) treat digits as word characters, so there IS a boundary between a letter and a digit but NOT between two digits. `\b` would create a boundary between `"abc"` and `"12345"`, but it also creates one between `"12345"` and `"a"`. For digit-only boundaries, `(?<!\d)` and `(?!\d)` are more precise.
3. **Using `matches()`**: `matches()` requires the entire string to match. Since input contains surrounding text, you need `find()`.
4. **Forgetting null check**: Always handle `null` input before calling `.length()` or passing to `Matcher`.

---

## Debugging Advice

If your pattern is matching when it shouldn't:
- Print `matcher.group()`, `matcher.start()`, `matcher.end()` to see exactly what matched and where
- Test each component: does `\d{5}` alone match? Does adding `(?!\d)` stop the 6-digit case?
- Use `regex101.com` (Java flavor) to visualize the match steps

If your pattern is NOT matching when it should:
- Check that you're calling `find()` not `matches()`
- Verify the lookahead/lookbehind syntax has no typos
