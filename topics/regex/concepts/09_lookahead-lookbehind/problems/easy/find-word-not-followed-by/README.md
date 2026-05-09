# Find Word Not Followed By

**Difficulty**: Easy
**Concept**: Negative Lookahead `(?!...)`

---

## Problem Statement

Implement `countStandaloneFile(String input)` that counts how many times the word
`"file"` appears in the input string where it is **NOT** immediately followed by
`"name"` or `"path"`.

- `"file"` in `"filepath"` → does NOT count (followed by `"path"`)
- `"file"` in `"filename"` → does NOT count (followed by `"name"`)
- `"file"` by itself or followed by anything else → DOES count

Return the count of standalone occurrences.

---

## Constraints

- Input may be `null` — return `0`.
- Matching is case-sensitive: `"File"` is not the same as `"file"`.
- `"file"` is matched literally — no word-boundary requirement unless you choose to add one.
  (See edge cases for why you might want `\b`.)
- The occurrences may overlap with other text but not with each other.

---

## Input / Output Examples

| Input | Expected | Explanation |
|-------|----------|-------------|
| `"read file from filepath"` | `1` | First "file" is standalone; "file" in "filepath" is followed by "path" |
| `"no match here"` | `0` | No "file" in input |
| `"file file file"` | `3` | All three are standalone |
| `"filename and file"` | `1` | "file" in "filename" is followed by "name"; second "file" is standalone |
| `""` | `0` | Empty string |
| `null` | `0` | Null input |
| `"filetype and filename"` | `0` | "filetype" → followed by "type" (not excluded, but "type" != name/path)... wait |

> Note on the last row: `"filetype"` — `"file"` is followed by `"type"`, not `"name"` or `"path"`,
> so it DOES count. Re-read: `"filetype and filename"` → `"file"` in `"filetype"` counts (followed by `"type"`),
> `"file"` in `"filename"` does NOT count. Result: **1**.

---

## Clarified Examples

| Input | Expected | Notes |
|-------|----------|-------|
| `"filetype and filename"` | `1` | `filetype` counts, `filename` does not |
| `"file"` | `1` | Single occurrence, standalone |
| `"filenamepath"` | `0` | "file" followed by "name" → excluded |
| `"file_backup"` | `1` | "file" followed by `_` → not excluded |

---

## Edge Cases

- Consecutive occurrences: `"file file"` → 2.
- Substring within longer words: `"profile"` — contains `"file"` preceded by `"pro"`. The
  negative lookahead only cares about what comes **after** the match, not before. So `"profile"` → 1
  (the `"file"` inside it is not followed by `"name"` or `"path"`). If you want to exclude
  substrings, add `\bfile` at the start.
- Case sensitivity: `"File"`, `"FILE"` → not matched.

---

## Time Complexity

O(N) — single linear scan with the Matcher.

---

## Real-World Relevance

Negative lookahead is essential when searching for tokens that conflict with compound forms.
Common examples in parsing:
- Find `int` but not `interface` or `integer`
- Find `null` but not `nullptr` or `nullopt`
- Find `get` but not `getClass` or `getTime`

This pattern is ubiquitous in lexer implementations and log analysis tools.

---

## Regex Thinking Process

Step 1: Match the word "file": `file`

Step 2: Exclude matches followed by "name" or "path":
        Use a negative lookahead: `file(?!name|path)`

Step 3: Count all non-overlapping matches using `Matcher.find()` in a while loop.

Step 4: Consider whether you want `\bfile(?!name|path)` to avoid matching substrings
        embedded in larger words like "profile" → your choice based on requirements.

---

## Common Mistakes

- Using `find()` once instead of in a loop — only finds the first occurrence.
- Using `matches()` instead of `find()` — `matches()` requires the ENTIRE string to match.
- Forgetting that `(?!name|path)` is zero-width — it does not skip "name"/"path", it just
  rejects the current `file` match position.
- Off-by-one: `"filenames"` contains `"filename"` inside it. The lookahead `(?!name)` fires
  as soon as `"name"` is the next four characters, regardless of what follows.

---

## Debugging Advice

Print `matcher.group()` and `matcher.start()` for each match to verify which occurrences
are being found. Use the pattern `file(?!name|path)` with `Matcher.find()` in a debug loop.
