# Validate Alphanumeric

**Difficulty:** Easy  
**Concept:** Character Classes  
**Concepts Tested:** Range notation `[a-zA-Z0-9]`, `+` quantifier, `String.matches()` / `Matcher.matches()` for full-string validation

---

## Problem Statement

Return `true` if the string consists **entirely** of alphanumeric characters (ASCII letters and digits), with a **minimum length of 1**. Spaces, underscores, punctuation, and any other characters make the string invalid.

---

## Constraints

- Input is non-null.
- **Minimum length 1**: empty string returns `false`.
- Only ASCII letters `a-z`, `A-Z` and digits `0-9` are allowed — no spaces, underscores, or special characters.
- This is a **full-string validation** — even one invalid character anywhere in the string makes it invalid.

---

## Input / Output Examples

| Input | Output | Reason |
|-------|--------|--------|
| `"Hello123"` | `true` | Letters and digits only |
| `"Hello 123"` | `false` | Space is not alphanumeric |
| `""` | `false` | Length 0 is invalid |
| `"abc!"` | `false` | `!` is not alphanumeric |
| `"Z9"` | `true` | Single letter + single digit |
| `"1234"` | `true` | Digits only |
| `"abc"` | `true` | Letters only |
| `"_hello"` | `false` | Underscore is not alphanumeric |

---

## Edge Cases

- **Empty string** → `false` (length < 1).
- **Single letter** `"a"` → `true`.
- **Single digit** `"0"` → `true`.
- **Underscore** `"hello_world"` → `false` (underscore is NOT alphanumeric in this context, even though `\w` includes it).
- **Hyphen** `"hello-world"` → `false`.
- **All uppercase** `"ABC"` → `true`.
- **All lowercase** `"abc"` → `true`.
- **Mixed** `"aB3"` → `true`.

---

## Expected Time Complexity

- **O(n)** where n = string length. The regex engine scans the string once.

---

## Real-World Relevance

Alphanumeric validation is used for:
- **Username validation**: usernames often require alphanumeric-only format.
- **Product codes**: SKU numbers, coupon codes.
- **API keys**: tokens that must not contain special characters.
- **Slug generation**: URL-safe identifiers.

---

## Regex Thinking Process

1. **What characters are allowed?** ASCII letters (`a-z`, `A-Z`) and digits (`0-9`).
2. **Character class**: `[a-zA-Z0-9]` — three ranges in one class.
3. **Minimum one**: use `+` quantifier (one or more).
4. **Full-string match**: use `String.matches()` or `Matcher.matches()` — the pattern must cover the entire string.
5. **Pattern**: `[a-zA-Z0-9]+`

Note: `\w` would include underscore `_`, which is NOT alphanumeric. Use the explicit ranges.

---

## Common Mistakes

1. **Using `\w+`** — `\w` matches `[a-zA-Z0-9_]` (includes underscore). `"hello_world".matches("\\w+")` → `true` but should be `false`.
2. **Using `find()` instead of `matches()`** — `find()` would accept `"hello!"` because it finds `"hello"`. You need the whole string to be alphanumeric.
3. **Forgetting `+`** — `[a-zA-Z0-9]` alone matches exactly one character. With `matches()`, this would only accept single-character strings.
4. **Checking length separately** — the `+` quantifier already requires length ≥ 1. No separate length check needed.

---

## Debugging Advice

If `""` returns `true`: your quantifier is `*` (zero or more) instead of `+` (one or more).

If `"hello_world"` returns `true`: you're using `\w` instead of `[a-zA-Z0-9]`.

If `"Hello123 "` returns `true`: you're using `find()` instead of `matches()`. The trailing space is being ignored.
