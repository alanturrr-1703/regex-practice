# Problem: Validate Ends With Semicolon

**Difficulty**: Easy  
**Concept**: Anchors  
**Skills Tested**: `$` anchor at string end, `\s*` for optional trailing whitespace, `find()` with anchors

---

## Problem Statement

Given a string, return `true` if it ends with a semicolon (`;`), optionally followed by trailing whitespace. Use the `$` anchor.

---

## Constraints

- Input may be `null` — return `false`
- Input may be empty — return `false`
- Trailing whitespace (spaces, tabs) after the semicolon is acceptable — still return `true`
- A semicolon that is NOT the last non-whitespace character returns `false`

---

## Input / Output Examples

| Input | Output | Reason |
|---|---|---|
| `"int x = 5;"` | `true` | Ends with `;` |
| `"int x = 5;  "` | `true` | `;` then whitespace — still valid |
| `"int x = 5"` | `false` | No `;` at end |
| `""` | `false` | Empty string |
| `";"` | `true` | Just a semicolon |
| `"a;b"` | `false` | `;` in the middle, not at end |
| `"a; b"` | `false` | `;` followed by non-whitespace character |
| `"int x = 5;\t"` | `true` | Tab after `;` is whitespace |

---

## Edge Cases

- `";"` → `true` (just a semicolon)
- `";   "` → `true` (semicolon with trailing spaces)
- `" ;"` → `true` (leading space, semicolon at end — valid)
- `"a;b"` → `false` (semicolon is not at the effective end)
- `null` → `false`

---

## Expected Time Complexity

O(n) to scan backward from end.

---

## Real-World Relevance

Detecting semicolons at the end of statements is used in:
- **Code linters**: enforce statement-ending semicolons in C, Java, JavaScript
- **SQL validators**: ensure queries are properly terminated
- **Config file parsers**: some formats require line-ending semicolons
- **ANTLR/grammar tools**: tokenizer checks for statement terminators

---

## Regex Thinking Process

**Step 1**: The pattern must match `;` near the end of the string. The `$` anchor matches the end.

**Step 2**: But there may be trailing whitespace after the `;`. Allow zero or more whitespace characters after the `;`: `;  \s*$` — the `\s*` matches zero or more whitespace chars, then `$` asserts end of string.

**Step 3**: Full pattern: `;\s*$`

**Step 4**: Use `find()` — the `$` anchor ensures the whitespace at the end matches the string's terminus.

**Java detail**: Java's `$` by default matches the end of the string OR just before a trailing `\n`. Since we use `\s*$`, trailing newlines are absorbed by `\s*` anyway — no special handling needed.

---

## Common Mistakes

1. **Using `";"` alone**: Matches any semicolon anywhere. Must add `\s*$` for end-of-string enforcement.
2. **Forgetting `\s*`**: `";$"` would fail for `"int x = 5;  "` (trailing spaces).
3. **Using `matches()` with `;\s*$`**: `matches()` requires the full string to match. A pattern of `;\s*$` with `matches()` would only match a string that IS just `;\s*`. Use `find()` instead.
4. **Using `trim()` before testing**: That's a workaround, not a regex solution. The `$` anchor should handle it.

---

## Debugging Advice

- Test `"a; b"` — if it returns `true`, your `$` is not properly anchored
- Test `"int x = 5;  "` — if it returns `false`, you're missing `\s*`
- Test `"a;b"` — should be `false`; if `true`, the anchor is missing
