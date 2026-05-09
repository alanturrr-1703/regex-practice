# Greedy vs Lazy HTML Tags

**Difficulty:** Easy
**Concepts Tested:** Lazy quantifier `.*?`, greedy overcapture, multiple matches with `find()`
**Concept:** greedy-vs-lazy

---

## Problem Statement

Given an HTML string, extract the **content** of all `<b>...</b>` bold tag pairs. Return a list of content strings (without the `<b>` and `</b>` tags).

The challenge: using greedy `.*` will consume across multiple tag pairs. Using lazy `.*?` stops at the first closing `</b>`, producing one match per pair.

---

## The Bug to Understand

**Broken pattern**: `<b>(.*)</b>` (greedy)
On input `<b>one</b> and <b>two</b>`:
- Greedy `.*` eats `one</b> and <b>two`
- Then matches the last `</b>`
- Result: ONE match — content: `one</b> and <b>two` ← wrong

**Correct pattern**: `<b>(.*?)</b>` (lazy)
- Lazy `.*?` matches `one`, stops at first `</b>`
- First match: `one`
- Next find(): matches `two`
- Result: TWO matches — `["one", "two"]` ← correct

---

## Constraints

- Input may be empty → return empty list
- Tags are case-sensitive (`<b>` not `<B>`)
- Tags may be empty: `<b></b>` → content is `""`
- Content may contain other HTML tags (e.g., `<b>with <i>inner</i></b>`)
- Content cannot contain `</b>` — so lazy is sufficient (no need for escape handling)

---

## Examples

### Example 1
**Input:** `"<b>hello</b>"`
**Output:** `["hello"]`

### Example 2
**Input:** `"<b>one</b> and <b>two</b>"`
**Output:** `["one", "two"]`

### Example 3
**Input:** `"no bold text"`
**Output:** `[]`

### Example 4
**Input:** `"<b>with <i>inner</i> tag</b>"`
**Output:** `["with <i>inner</i> tag"]`

---

## Edge Cases

- `"<b></b>"` → `[""]` (empty bold content)
- `"<b>a</b><b>b</b><b>c</b>"` → `["a", "b", "c"]`
- No closing tag: `"<b>unclosed"` → `[]` (lazy requires `</b>` to match)
- Nested `<b>` (malformed): `"<b>outer <b>inner</b> end</b>"` → `["inner"]` first, then lazy finds `" end"` — define clearly in your tests

---

## Expected Time Complexity

O(n) — linear scan through the string

---

## Real-World Relevance

- HTML scraping and text extraction
- Templating engines that process HTML
- Documentation parsers
- Log message formatters that use bold markers

---

## Regex Thinking Process

1. The outer structure is `<b>` ... `</b>`. Fixed delimiters.
2. Can the content contain `</b>`? No (well-formed HTML). So lazy is sufficient.
3. Pattern: `<b>(.*?)</b>` — capture group 1 is the content.
4. In Java string: `"<b>(.*?)</b>"`
5. Use `matcher.group(1)` to get content (not `group(0)` which includes tags).

---

## Common Mistakes

1. **Greedy `.*`** — produces one match spanning from first `<b>` to last `</b>`
2. **Using `group(0)` instead of `group(1)`** — returns the match including `<b>...</b>` tags
3. **Using `matches()` instead of `find()`** — `matches()` requires full-string match

---

## Debugging Advice

If you only get one match on input with multiple pairs:
→ You are using greedy `.*` not lazy `.*?`. Add the `?` after `*`.

If your result includes the `<b>` tags:
→ You are returning `group(0)`. Return `group(1)` (the capture group content only).
