# Multiline Comment Extractor

**Difficulty:** Medium
**Concepts Tested:** Lazy `.*?` with `Pattern.DOTALL`, multiline content matching
**Concept:** greedy-vs-lazy

---

## Problem Statement

Given a string of source code, extract the **content** of all `/* ... */` block comments. Comments may span multiple lines. Return the raw content between the markers (without trimming).

This problem has two challenges combined:
1. **Lazy**: use `.*?` so multiple comments are extracted separately (not one spanning from first `/*` to last `*/`)
2. **DOTALL**: use `Pattern.DOTALL` so `.` matches newline characters (required for multiline comments)

---

## Constraints

- Input is never `null`, may be empty → return empty list
- Comments return their raw content (with spaces and newlines)
- Multiple comments → one result per comment
- `/**/` → one result: `[""]`

---

## Examples

### Example 1
**Input:** `"code /* comment */ more"`
**Output:** `[" comment "]` (spaces preserved)

### Example 2
**Input:** `"/* first */ code /* second */"`
**Output:** `[" first ", " second "]`

### Example 3
**Input:** `"/* line1\nline2 */"`
**Output:** `[" line1\nline2 "]` (newline preserved)

### Example 4
**Input:** `"no comments here"`
**Output:** `[]`

---

## Edge Cases

- `"/**/"` → `[""]`
- Empty input → `[]`
- Multiple multiline comments → all extracted separately
- Without DOTALL: multiline comments would produce NO match

---

## Expected Time Complexity

O(n) — linear scan with DOTALL

---

## Real-World Relevance

- Code formatters and minifiers (removing comments)
- Documentation generators (extracting Javadoc content)
- Code analysis tools (finding TODOs in comments)
- Transpilers that strip comments before processing

---

## Regex Thinking Process

1. Opening marker: `/*` → in regex: `/\*` → Java string: `"/\\*"`
2. Content: `.*?` (lazy — stops at first `*/`)
3. Closing marker: `*/` → in regex: `\*/` → Java string: `"\\*/"`
4. Must use DOTALL for multiline: `Pattern.compile("/\\*(.*?)\\*/", Pattern.DOTALL)`
5. Content is in capture group 1

---

## Common Mistakes

1. **Forgetting DOTALL** — multiline comments not matched at all
2. **Using greedy** — spans from first `/*` to last `*/`
3. **Not using a capture group** — returns the whole match including markers
4. **Escaping `/`** — not necessary in Java regex; only `*` needs escaping

---

## Debugging Advice

If multiline comments don't match at all: you forgot DOTALL.
If multiple comments merge into one: you're using greedy `.*` not lazy `.*?`.
Test `"/* a */ b /* c */"` — should produce TWO matches.
