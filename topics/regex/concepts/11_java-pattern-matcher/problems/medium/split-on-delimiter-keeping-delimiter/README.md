# Split on Delimiter Keeping Delimiter

**Difficulty**: Medium
**Concept**: Java Pattern & Matcher API — `start()`, `end()`, manual split
**Estimated Time**: 30–40 minutes

---

## Problem Statement

Split a string on semicolons, but **keep the semicolons attached to the preceding token**.

Examples:
- `"a;b;c"` → `["a;", "b;", "c"]`
- `"trailing;"` → `["trailing;"]`
- `"a;;b"` → `["a;", ";", "b"]`

This cannot be done correctly with `Pattern.split()` — that method discards delimiters entirely. You must use `Matcher.find()` with `matcher.start()` and `matcher.end()` to build the tokens manually.

**Delimiter definition**: a semicolon attaches to the text that immediately precedes it (which may be empty).

---

## Method Signature

```java
public List<String> splitKeepingDelimiter(String input)
```

---

## Defined Behavior for Edge Cases

| Input | Output | Reason |
|---|---|---|
| `"a;b;c"` | `["a;", "b;", "c"]` | Each token includes its trailing semicolon |
| `"abc"` | `["abc"]` | No semicolon — entire string is one token |
| `";leading"` | `[";", "leading"]` | Semicolon has empty preceding text → `";"` token |
| `"trailing;"` | `["trailing;"]` | Trailing token has no following text → no empty string appended |
| `"a;;b"` | `["a;", ";", "b"]` | Two consecutive semicolons: `"a;"`, then empty+`";"`, then `"b"` |
| `""` | `[]` | Empty input → empty list |

---

## Constraints

- `input` is never null
- Semicolons are the only delimiter
- Do NOT add trailing empty strings to the result (i.e., if the remaining text after the last semicolon is empty, omit it)
- Empty input returns an empty list

---

## Time Complexity

- O(n) for n = input length

---

## Real-World Relevance

- **CSV-like parsing** where you need to know which field owns the delimiter
- **Tokenizers** that attach punctuation to the preceding word
- **Code formatters** that keep semicolons with their preceding statement
- Understanding this problem deeply teaches why `Pattern.split()` is fundamentally limited

---

## Why Pattern.split() Is Wrong Here

`Pattern.compile(";").split("a;b;c")` returns `["a", "b", "c"]` — the semicolons are gone.
Even with lookahead tricks (`split("(?<=;)")`), the behavior at the boundaries differs from what we need for consecutive semicolons.

The only correct approach is `Matcher.find()` with manual token extraction using the match position.

---

## Algorithm Sketch

The pattern `[^;]*;` matches: zero or more non-semicolon characters followed by one semicolon.

```
"a;b;c" against [^;]*;
Match 1: "a;"  (positions [0, 2))
Match 2: "b;"  (positions [2, 4))
Remaining after last match end (4): "c"
```

After the loop, append the remaining substring (from last `matcher.end()` to `input.length()`) **only if it is non-empty**.

---

## Common Mistakes

1. **Using `Pattern.split()`** — does not keep delimiters
2. **Appending trailing empty string** — if input ends with `;`, the remaining text after the last match is `""`. Do NOT add it.
3. **Wrong pattern** — `";"` alone matches only the semicolon, not the preceding text. You need `[^;]*;` to capture the whole token-plus-delimiter.
4. **Off-by-one on `matcher.end()`** — `end()` is exclusive (one past the last character). `input.substring(lastEnd)` from `matcher.end()` is correct.

---

## Debugging Advice

Print the match positions to verify:
```java
while (m.find()) {
    System.out.println("[" + m.start() + "," + m.end() + ") = '" + m.group() + "'");
}
```
This immediately shows if your pattern is capturing the right tokens.
