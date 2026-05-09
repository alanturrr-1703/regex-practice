# Hints: Collapse Whitespace

---

## Hint 1 (Gentle)

Think about what `\s` matches — it's not just the space character. It matches tabs, newlines, carriage returns, and more. To match a **run** of one or more whitespace characters, add `+` after it: `\s+`.

---

## Hint 2

Java's `String.replaceAll(regex, replacement)` replaces all non-overlapping matches of the regex with the replacement string. What would happen if you replaced every `\s+` match with a single space `" "`?

Try: `input.replaceAll("\\s+", " ")`

After this call, every run of whitespace becomes one space. But the string might still start or end with a space (if the original started/ended with whitespace).

---

## Hint 3

You need to handle leading and trailing whitespace separately. The simplest approach is to call `.trim()` after the `replaceAll`. `String.trim()` removes leading and trailing characters that are `<= '\u0020'` (space and ASCII control characters).

Combined: `input.replaceAll("\\s+", " ").trim()`

---

## Hint 4

Handle the edge cases:
- If `input` is `null`, decide what to return (the problem says return `null`, or return `""` — pick one and be consistent)
- If `input` is `""`, `replaceAll` on an empty string returns `""`, and `trim()` returns `""` — no special case needed
- If `input` is all whitespace like `"   "`, `replaceAll("\\s+", " ")` gives `" "`, then `.trim()` gives `""` — correct!

---

## Hint 5 (Near Solution)

Your complete implementation should be approximately:

```
1. Handle null: if input == null, return null (or "")
2. Collapse: result = input.replaceAll("\\s+", " ")
3. Trim:     result = result.trim()
4. Return result
```

All three steps can be chained: `input.replaceAll("\\s+", " ").trim()`

Note: For production code, compile the pattern as `static final Pattern` instead of using `String.replaceAll()` directly (which recompiles on every call). For this exercise, either approach is acceptable.
