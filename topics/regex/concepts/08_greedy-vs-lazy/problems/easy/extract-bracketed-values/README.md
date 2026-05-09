# Extract Bracketed Values

**Difficulty:** Easy
**Concepts Tested:** Lazy `.*?` vs greedy `.*`, escaping `\[` and `\]`, multiple matches
**Concept:** greedy-vs-lazy

---

## Problem Statement

Given a string containing values wrapped in square brackets like `[hello][world]`, extract each value separately. Return a list of the values without the brackets.

The key challenge: a greedy `\[.*\]` pattern will match from the FIRST `[` to the LAST `]`, treating everything in between as one match. You need a lazy `\[.*?\]` to match each pair independently.

---

## Constraints

- Input may be empty → return empty list
- Brackets may be empty: `[]` → return `[""]`
- Content may be any character except `]` (don't worry about nested brackets)
- Multiple `[...]` blocks should each produce one entry

---

## Examples

### Example 1
**Input:** `"[123][456]"`
**Output:** `["123", "456"]`

### Example 2
**Input:** `"[hello]"`
**Output:** `["hello"]`

### Example 3
**Input:** `"no brackets"`
**Output:** `[]`

### Example 4
**Input:** `"[a][b][c]"`
**Output:** `["a", "b", "c"]`

---

## Edge Cases

- `"[]"` → `[""]` (empty bracket)
- `"text [value] more text"` → `["value"]`
- No brackets at all → `[]`
- `"[[nested]]"` — with lazy, first match is `[` to first `]` → `["[nested"]`; define this behavior

---

## Expected Time Complexity

O(n) — linear scan

---

## Real-World Relevance

- Parsing log formats like `[TIMESTAMP] message [context]`
- Extracting fields from bracket-delimited data
- Template systems with `[variable]` placeholders

---

## Regex Thinking Process

1. You need to match `[`, content, `]`.
2. `[` and `]` are metacharacters in regex — must escape them: `\[` and `\]`.
3. Content between them: `.*` (greedy) vs `.*?` (lazy).
4. Greedy fails on multiple pairs — spans from first `[` to last `]`.
5. Lazy stops at first `]` — correct.
6. Add a capture group for the content: `\[(.*?)\]`.

---

## Common Mistakes

1. Forgetting to escape `[` and `]` — they open/close a character class in regex
2. Using greedy `.*` — matches across bracket pairs
3. Returning `group(0)` (with brackets) instead of `group(1)` (content only)

---

## Debugging Advice

Test on `"[a][b]"` specifically. If you get `["a][b"]` (one match with content `a][b`), your pattern is greedy. If you get `["a", "b"]`, it's correct.
