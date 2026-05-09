# Extract Words

**Difficulty:** Medium  
**Concept:** Regex Basics  
**Concepts Tested:** `[a-zA-Z]` character class, `+` quantifier, `Matcher.find()` loop pattern, distinction between `\w` and `[a-zA-Z]`

---

## Problem Statement

Given a string, extract all **words** from it and return them as a list of strings. For this problem, a "word" is defined as a maximal contiguous sequence of **alphabetic characters only** — letters `a-z` and `A-Z`. Digits, underscores, punctuation, and spaces are NOT part of a word.

The key learning moment: `\w` matches `[a-zA-Z0-9_]` (includes digits and underscore), but this problem wants only `[a-zA-Z]`. You need to understand the difference.

---

## Constraints

- Input is non-null but may be empty.
- Only ASCII letters count. Unicode letters are out of scope for this problem.
- Return words in the order they appear in the input.
- Digits within a "word-like" sequence act as separators: `"Java9Rocks"` → `["Java", "Rocks"]`.
- Underscores act as separators: `"foo_bar"` → `["foo", "bar"]`.
- Punctuation, whitespace, and digits are all separators.
- Return an empty list if no words are found.
- Minimum word length is 1 (a single letter counts as a word).

---

## Input / Output Examples

| Input | Output |
|-------|--------|
| `"Hello, world! 123"` | `["Hello", "world"]` |
| `"foo_bar"` | `["foo", "bar"]` |
| `"  "` | `[]` |
| `"Java9Rocks"` | `["Java", "Rocks"]` |
| `"abc"` | `["abc"]` |
| `"123"` | `[]` |
| `"a"` | `["a"]` |
| `"a1b2c3"` | `["a", "b", "c"]` |

---

## Edge Cases

- **Empty string** → `[]`.
- **Only digits** `"123"` → `[]` (no letters at all).
- **Only spaces/punctuation** `"!!! ???"` → `[]`.
- **Single letter** `"a"` → `["a"]`.
- **Mixed case** `"Hello"` → `["Hello"]` (not split by case change).
- **Underscore as separator** `"foo_bar"` → `["foo", "bar"]`.
- **Digit as separator** `"Java9Rocks"` → `["Java", "Rocks"]`.

---

## Expected Time Complexity

- **O(n)** where n = string length. The `Matcher.find()` loop processes each character at most once.

---

## Real-World Relevance

Word extraction is used in:
- **NLP tokenization**: splitting raw text into word tokens before analysis.
- **Search indexing**: extract terms from documents to build inverted indexes.
- **Spell checking**: identify word tokens to check against a dictionary.
- **Code analysis**: extract identifiers from comments or strings.
- **Keyword extraction**: find meaningful words in text, ignoring punctuation.

---

## Regex Thinking Process

1. **What is a "word" here?** One or more consecutive letters: `[a-zA-Z]+`.
2. **Why not `\w+`?** `\w` = `[a-zA-Z0-9_]`. It would return `"foo_bar"` as one token and `"Java9Rocks"` as one token. We want only letters.
3. **Pattern:** `[a-zA-Z]+` — one or more ASCII letters.
4. **How to find all occurrences?** Use the `Matcher.find()` loop pattern.
5. **Each call to `find()` returns the next match.** Collect `m.group()` into a list.

---

## Common Mistakes

1. **Using `\w+` instead of `[a-zA-Z]+`** — `\w` includes digits and underscore; `"foo_bar"` would be one token instead of two.
2. **Using `split()` on spaces** — `split("\\s+")` splits only on whitespace; punctuation like `,` and `!` would remain attached to words.
3. **Calling `matches()` instead of `find()`** — `matches()` checks the whole string against the pattern, which won't give you individual words.
4. **Forgetting to loop** — a single `find()` call gives only the first match. You need `while (m.find())`.
5. **Adding `m.group()` before calling `find()`** — `find()` must be called first to position the matcher.

---

## Debugging Advice

If you're getting words with digits or underscores attached:
- Check your pattern. Are you using `[a-zA-Z]+` or `\w+`?

If you're getting only the first word:
- Check that you're looping with `while (m.find())`, not just a single `if (m.find())`.

If you're getting empty strings in the result:
- Your pattern may allow zero-length matches. `[a-zA-Z]+` (with `+`) never matches empty.

To debug, print `m.group()` and `m.start()` inside the loop to see what's being matched and where.
