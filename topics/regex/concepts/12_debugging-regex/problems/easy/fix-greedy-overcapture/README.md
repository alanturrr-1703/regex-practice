# Fix Greedy Overcapture

**Difficulty**: Easy — Debugging
**Concept**: Greedy quantifier bug; lazy `.*?` vs negated class `[^<]*`
**Estimated Time**: 20–25 minutes

---

## Problem Statement

A broken method tries to extract the title content from HTML `<title>` tags. It uses a greedy `.*` pattern that spans across multiple tags on the same line.

**Broken code** (shown as a comment in Solution.java):
```
// BROKEN: Pattern.compile("<title>.*</title>")
```

**What goes wrong**: on input `"<title>First</title><title>Second</title>"`, the greedy `.*` matches `"First</title><title>Second"` — everything from the first `<title>` opening to the **last** `</title>` closing. Only ONE match is returned instead of two, and the content is completely wrong.

**Your task**: fix the pattern to correctly extract each title's content separately.

---

## Method Signature

```java
public List<String> extractTitles(String html)
```

---

## Input / Output Examples

| Input | Output |
|---|---|
| `"<title>Hello</title>"` | `["Hello"]` |
| `"<title>A</title><title>B</title>"` | `["A", "B"]` |
| `"no titles here"` | `[]` |
| `"<title></title>"` | `[""]` (empty title content) |
| `"<title>Page One</title> text <title>Page Two</title>"` | `["Page One", "Page Two"]` |

---

## Constraints

- `html` is never null
- Tags are lowercase (`<title>`, not `<TITLE>`)
- Title content may be empty
- Tags appear on a single line (no multiline titles needed)
- Return just the content between tags, not the tags themselves

---

## Why the Broken Pattern Fails

```
Pattern: <title>.*</title>
Input:   <title>First</title><title>Second</title>

.*  greedily consumes: "First</title><title>Second"
    (everything until it can still match the LAST </title>)

Result: ONE match with content = "First</title><title>Second"
```

The `.*` (greedy) matches as much as possible, and since `.*` can match `<`, `/`, and any other character, it happily swallows the closing `</title>` of the first tag and the opening `<title>` of the second.

## Two Correct Fixes

**Fix 1 — Lazy quantifier**: `<title>(.*?)</title>`

`.*?` matches the MINIMUM characters needed for the overall pattern to succeed. It stops as soon as it sees the next `</title>`.

**Fix 2 — Negated character class**: `<title>([^<]*)</title>`

`[^<]*` matches "any character except `<`". Since `</title>` starts with `<`, this pattern structurally CANNOT span past a `<`. More explicit and slightly more efficient (no backtracking).

---

## Common Mistakes

1. **Forgetting the capturing group** — `<title>.*?</title>` finds the full match but you need `group(1)` for just the content. Your pattern needs `(.*?)` or `([^<]*)`.
2. **Using `matches()` instead of `find()`** — `matches()` requires the entire HTML string to be just `<title>...</title>`. Use `find()` to locate titles within a larger HTML string.
3. **Using `.*` without `?`** — this is exactly the bug. The lazy `?` is crucial.

---

## Debugging Advice

Paste the broken pattern into regex101.com with the two-title input. Under "Match information", you'll see it returns exactly one match spanning the entire string. Switch `.*` to `.*?` and watch it correctly return two matches.
