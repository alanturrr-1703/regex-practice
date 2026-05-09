# Debug Missing Multiline Flag

**Difficulty**: Medium — Debugging
**Concept**: `Pattern.MULTILINE` changes `^` and `$` to match line boundaries
**Estimated Time**: 20–30 minutes

---

## Problem Statement

A broken method tries to find all "comment lines" — lines beginning with `#` — in a multiline string. The broken pattern uses `^#.*` without `Pattern.MULTILINE`, so `^` only anchors to the absolute start of the entire string.

**Broken code**:
```java
// BROKEN: ^#.* without MULTILINE only anchors to position 0 of entire input
Pattern broken = Pattern.compile("^#.*");
```

**What goes wrong**: in `"# comment\ncode line\n# another comment"`, the broken pattern either:
- Finds ONE match if the string starts with `#` (only the first line)
- Finds ZERO matches if the string doesn't start with `#`

**Your task**: fix the pattern to find ALL comment lines anywhere in the multiline string.

---

## Method Signature

```java
public List<String> extractCommentLines(String text)
```

---

## Input / Output Examples

| Input | Output |
|---|---|
| `"# comment\ncode line\n# another comment"` | `["# comment", "# another comment"]` |
| `"no comments\nhere"` | `[]` |
| `"# only one"` | `["# only one"]` |
| `""` | `[]` |

---

## Constraints

- `text` is never null
- Lines are separated by `\n`
- A comment line starts with `#` as the first character on the line
- Return the full content of each comment line (including the `#`)

---

## How MULTILINE Changes ^ Behavior

Without `Pattern.MULTILINE`:
- `^` = "start of the entire input string" (position 0 only)
- `$` = "end of the entire input string" (position length or before final `\n`)

With `Pattern.MULTILINE`:
- `^` = "start of any line" (position 0, or any position immediately after `\n`)
- `$` = "end of any line" (any position immediately before `\n`, or end of input)

This is a common source of bugs whenever you're processing multi-line text. The flag name `MULTILINE` specifically refers to this anchor behavior — it does NOT affect `.` (use `DOTALL` for that).

---

## Two Equivalent Syntax Options

```java
// Option 1: flag constant
Pattern.compile("^#.*", Pattern.MULTILINE)

// Option 2: inline flag
Pattern.compile("(?m)^#.*")
```

Both are equivalent. The inline flag `(?m)` can be embedded at any position in the pattern.

---

## Common Mistakes

1. **Confusing MULTILINE with DOTALL** — `MULTILINE` changes `^`/`$`, `DOTALL` changes `.`. They are different and independent.
2. **Using `matches()` instead of `find()`** — `matches()` requires the entire string to match `#.*`, which would fail for multiline input.
3. **Forgetting the flag entirely** — just adding the flag to an otherwise-correct pattern is the entire fix here.

---

## Debugging Advice

Add a diagnostic print to see where `^` is actually anchoring:
```java
Matcher m = Pattern.compile("^#.*").matcher("code\n# comment\nmore");
System.out.println("find()=" + m.find()); // false — ^ doesn't see line 2
```
Then add `Pattern.MULTILINE`:
```java
Matcher m = Pattern.compile("^#.*", Pattern.MULTILINE).matcher("code\n# comment\nmore");
while (m.find()) System.out.println("[" + m.start() + "]: " + m.group()); // finds it
```
