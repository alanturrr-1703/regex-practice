# Problem: Multiline Section Parser

**Difficulty**: Medium  
**Concept**: Anchors  
**Skills Tested**: `^` with `Pattern.MULTILINE`, capturing groups in multiline context, `find()` loop

---

## Problem Statement

Given a multiline string (lines separated by `\n`), extract all lines that **start with** `">>"`. Return the content of those lines (without the `>>` prefix, trimmed of leading/trailing whitespace) as a `List<String>`.

Lines that do not start with `>>` are ignored.

---

## Constraints

- Lines are separated by `\n`
- Input may be `null` — return an empty list
- Input may have no `>>` lines — return an empty list
- The `>>` must be at the start of the line (use `^` with `MULTILINE`)
- Strip the `>>` from the result; trim the remaining content
- Preserve order of appearance

---

## Input / Output Examples

| Input | Output |
|---|---|
| `">> title\ncontent\n>> another"` | `["title", "another"]` |
| `"no sections\nhere"` | `[]` |
| `">> only one"` | `["only one"]` |
| `">> First\n>> Second\n>> Third"` | `["First", "Second", "Third"]` |
| `"content\n>> Section A\nmore content\n>> Section B"` | `["Section A", "Section B"]` |

---

## Edge Cases

- `">>no space"` → `["no space"]` (no space after `>>` — still valid, trim)
- `">> "` → `[""]` or `[]` depending on your trim logic — document your choice
- `"  >> not at start"` → `[]` — the `>>` must be at the start of the line, not after spaces
- `""` → `[]`
- `null` → `[]`
- A `>>` embedded mid-line (`"text >> more"`) → NOT a section header — `^` prevents it

---

## Expected Time Complexity

O(n) for a single MULTILINE `find()` pass.

---

## Real-World Relevance

Multiline section parsing is the foundation of many text processing tools:
- **Markdown parsers**: extract blockquotes (lines starting with `>`)
- **Email parsers**: extract quoted reply content (lines starting with `>`)
- **Git diff parsers**: extract changed lines (starting with `+` or `-`)
- **Makefile parsers**: extract rules, targets, and recipes
- **Configuration loaders**: extract sections starting with specific markers

---

## Regex Thinking Process

**Step 1**: Without `MULTILINE`, `^>>` only matches if the ENTIRE string starts with `>>`. On `"first\n>> second"`, `^` doesn't match after `\n`.

**Step 2**: Add `Pattern.MULTILINE`. Now `^` matches at position 0 AND after each `\n`. The pattern `^>>` will match at the start of each line that begins with `>>`.

**Step 3**: Capture the content after `>>` using a group: `^>>(.+)$`. The `(.+)` captures everything after `>>` up to the end of the line. The `$` (also affected by MULTILINE) matches at line ends.

**Step 4**: In the `find()` loop, use `matcher.group(1)` to get the captured content, then `.trim()` it.

**Java code sketch**:
```java
Pattern p = Pattern.compile("^>>(.+)$", Pattern.MULTILINE);
Matcher m = p.matcher(text);
while (m.find()) {
    results.add(m.group(1).trim());
}
```

---

## Common Mistakes

1. **Forgetting `Pattern.MULTILINE`**: Without it, `^` only matches position 0. Most lines starting with `>>` will be missed.
2. **Using `\n` in the pattern instead of MULTILINE**: You could split on `\n` and test each line separately, but that defeats the purpose of this exercise.
3. **Not using a capturing group**: Without `(.+)`, you can only get the full match (including `>>`). Use `group(1)` to skip the prefix.
4. **Not trimming**: `">> title"` — `group(1)` captures `" title"` (with leading space). Trim it.

---

## Debugging Advice

- Test with `">> title\ncontent"` — is `"content"` (the non-`>>` line) excluded?
- Test with `"  >> late"` — if it's included, your `^` is not requiring start-of-line
- Print `m.start()` and `m.group()` for each match to understand what the MULTILINE flag changed
