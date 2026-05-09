# Capture First Word

**Difficulty:** Easy  
**Concept:** Groups and Capturing  
**Estimated Time:** 10–20 minutes

---

## Concepts Tested

- `group(0)` vs `group(1)` — the critical difference
- Single capturing group `(\w+)` — what it captures vs what the whole match is
- `matcher.find()` returning the first match (not looping)
- Returning `Optional<String>` for absent matches
- How `find()` scans forward past leading non-word characters

---

## Problem Statement

Given a string, return the **first word** as an `Optional<String>`. A "word" is any non-empty sequence of word characters (`\w+` — letters, digits, underscore). If the string contains no word characters at all, return `Optional.empty()`.

Leading non-word characters (spaces, punctuation) should be skipped automatically by `find()`.

---

## Method Signature

```java
public Optional<String> captureFirstWord(String input)
```

---

## Constraints

- `input` is never `null` (but may be empty or contain only non-word characters)
- Return `Optional.empty()` if no word is found
- Return `Optional.of(word)` where `word` is the first contiguous run of `\w` characters

---

## Input / Output Examples

| Input | Output |
|---|---|
| `"hello world"` | `Optional.of("hello")` |
| `"  spaces first"` | `Optional.of("spaces")` — leading spaces skipped by find() |
| `"123 num first"` | `Optional.of("123")` — digits are word characters (`\w` = `[a-zA-Z0-9_]`) |
| `"!@#$%"` | `Optional.empty()` — no word characters |
| `""` | `Optional.empty()` |
| `"a"` | `Optional.of("a")` |
| `"_underscore"` | `Optional.of("_underscore")` — underscore is a `\w` character |

---

## Edge Cases

- **All punctuation** (`"!@#$%"`): no `\w` match → `Optional.empty()`
- **Empty string**: `find()` returns false → `Optional.empty()`
- **Digits first** (`"123 abc"`): `\w+` matches `"123"` — digits are word chars
- **Underscore** (`"_private"`): underscore is `\w` → returned as part of the word
- **Single character**: `"a"` → `Optional.of("a")`

---

## Time Complexity

- **O(n)** — `find()` scans the input once and stops at the first match

---

## Real-World Relevance

- **Tokenizers**: grab the first token from an expression
- **Command parsers**: extract the command name from a command-line string
- **Text normalization**: find the first word in a potentially messy input field

---

## Regex Thinking Process

1. **What is a word?** `\w+` — one or more word characters
2. **How do I capture it?** Wrap in a group: `(\w+)`
3. **Should I use a capturing group at all?**  
   For this problem, you have two options:
   - Use `(\w+)` and return `group(1)` — teaches the capture/group concept
   - Use `\w+` and return `group(0)` — works, but group(0) == group(1) here  
   **The lesson:** when there's only one group and no leading characters in the pattern, `group(0)` and `group(1)` are identical.  
   But if you used `\s*(\w+)`, then `group(0)` = `"  spaces"` and `group(1)` = `"spaces"` — they differ!

4. **How to handle the "not found" case?** Check the return value of `find()`:
   - `find()` returns `true` → first match found → return `Optional.of(group(1))`
   - `find()` returns `false` → no match → return `Optional.empty()`

---

## Common Mistakes

| Mistake | Why It's Wrong |
|---|---|
| Using `matcher.matches()` | Only matches if the ENTIRE string is `\w+`; fails for "hello world" |
| Not checking the return value of `find()` | Calling `group()` when `find()` returned false → `IllegalStateException` |
| Returning `group(0)` with pattern `\s*(\w+)` | `group(0)` includes the leading whitespace; `group(1)` is just the word |
| Returning `Optional.of(null)` | If `group(1)` is null and you wrap it, `Optional.of` throws NPE — use `ofNullable` |
| Using `[a-zA-Z]+` instead of `\w+` | Misses digits and underscore which are word chars |

---

## Debugging Advice

Test the pattern alone:
```java
Pattern p = Pattern.compile("(\\w+)");
Matcher m = p.matcher("  hello");
System.out.println(m.find());    // true
System.out.println(m.group(0));  // "hello"
System.out.println(m.group(1));  // "hello"  ← same here because group wraps the entire match
```

Compare with `\s*(\w+)`:
```java
Pattern p = Pattern.compile("\\s*(\\w+)");
Matcher m = p.matcher("  hello");
m.find();
System.out.println(m.group(0));  // "  hello"  ← includes spaces
System.out.println(m.group(1));  // "hello"    ← just the word
```

This demonstrates why `group(0)` ≠ `group(1)` when the pattern has leading components outside the group.
