# CSV Field Tokenizer

**Difficulty:** Hard
**Concepts Tested:** Precise character class matching (not greedy, not lazy — but exact), CSV parsing logic
**Concept:** greedy-vs-lazy

---

## Problem Statement

Parse a multi-line CSV string into a `List<List<String>>` — one inner list per row, one string per field. Rules:

1. Fields are separated by commas (`,`)
2. Fields may be **double-quoted** — a quoted field is enclosed in `"..."` and may contain commas inside
3. Fields may be **empty** — `a,,b` → `["a", "", "b"]`
4. Each row is on its own line (separated by `\n`)
5. Quoted fields do NOT contain escaped quotes (simplified — no `""` escape inside)

**The key lesson**: neither greedy `.*` nor lazy `.*?` is the right tool here. The correct approach uses a **precise character class** that describes exactly what each field type can contain:
- Quoted field: `"([^"]*)"` — any chars that are NOT `"`
- Unquoted field: `([^,\n]*)` — any chars that are NOT `,` or newline

---

## Constraints

- Input is never `null`, may be empty → return empty list
- Quoted fields: no escaped quotes inside (simplified)
- Empty fields: allowed between consecutive commas
- Rows separated by `\n`
- Last row may or may not have trailing newline

---

## Examples

### Example 1
**Input:** `"a,b,c"`
**Output:** `[["a", "b", "c"]]`

### Example 2
**Input:** `"\"quoted,field\",plain"` (Java string representing: `"quoted,field",plain`)
**Output:** `[["quoted,field", "plain"]]`

### Example 3
**Input:** `"a,,b"`
**Output:** `[["a", "", "b"]]`

### Example 4
**Input:** `"row1a,row1b\nrow2a,row2b"`
**Output:** `[["row1a", "row1b"], ["row2a", "row2b"]]`

---

## Edge Cases

- Empty string → `[]`
- Single value: `"hello"` → `[["hello"]]`
- Single comma: `","` → `[["", ""]]`
- Quoted empty: `"\"\""` → `[[""]]`
- Multiple rows with different field counts
- Trailing newline: `"a,b\n"` — define behavior (ignore empty trailing row or include it)

---

## Expected Time Complexity

O(n * m) — n rows, m average fields per row

---

## Real-World Relevance

- Importing CSV data files
- Parsing log files with structured CSV format
- ETL pipelines (extract, transform, load)
- Config files with comma-separated values

---

## Regex Thinking Process

**Why not lazy?**
`".*?"` for quoted fields would work on simple cases but is unnecessary when you know the content: it can't contain `"`. Using `[^"]*` is faster and more explicit.

**Why not greedy `.*`?**
`.*` would consume across field boundaries (commas and row boundaries).

**The precise approach:**
Each field is either:
- A quoted field: starts and ends with `"`, contains `[^"]*` inside
- An unquoted field: contains `[^,\n]*`

Pattern for one field token: `"([^"]*)"` or `([^,\n]*)`

**Processing strategy:**
Process each line independently. For each line, use a regex that finds each field token:
`"([^"]*)"` captures quoted field content into group 1.
`([^,\n]*)` captures unquoted field content (which may be empty) into group 2.

Or use `Pattern.split()` carefully — but splitting on `,` naively breaks quoted fields with commas inside.

---

## Common Mistakes

1. Splitting on `,` directly — breaks quoted fields that contain commas
2. Using `.*?` inside `"..."` — works but `[^"]*` is cleaner and faster
3. Forgetting to handle empty fields
4. Off-by-one on trailing empty fields from `split()`
5. Forgetting to strip the quotes from quoted fields

---

## Debugging Advice

Test `"quoted,field",plain` (one quoted field and one plain). If you get 3 fields instead of 2, your pattern is splitting on the comma inside the quoted field. The quoted field must be matched as a unit before splitting.
