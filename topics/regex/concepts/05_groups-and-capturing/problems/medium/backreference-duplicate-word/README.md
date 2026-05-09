# Backreference Duplicate Word Finder

**Difficulty:** Medium  
**Concept:** Groups and Capturing  
**Estimated Time:** 25–40 minutes

---

## Concepts Tested

- Backreferences `\1` — matching the same text captured earlier in the same pattern
- `\\1` in Java string literals (double-backslash required)
- `Pattern.CASE_INSENSITIVE` flag and its interaction with backreferences
- Word boundary `\b` to avoid matching within longer words
- Returning the captured word (not the duplicate pair)

---

## Problem Statement

Find all **consecutive duplicate words** in a string — pairs where the same word appears twice in a row, separated only by whitespace. Return a `List<String>` of the duplicated word (just the word once, not the entire `"word word"` pair).

Matching is **case-insensitive**: `"Hello hello"` counts as a duplicate pair.

**Important:** "Consecutive" means the two instances are adjacent (separated only by `\s+`), not just anywhere in the string. `"the cat the"` has no duplicate because `the` is not adjacent to itself.

---

## Method Signature

```java
public List<String> findDuplicateWords(String input)
```

---

## Constraints

- `input` is never `null`
- Duplicates are word-boundary-delimited: `\bword\b` — only whole words
- Comparison is case-insensitive
- Return the word as it was captured from the first occurrence (preserves original casing)
- The same word can appear as a duplicate multiple times in the input

---

## Input / Output Examples

| Input | Output |
|---|---|
| `"the the cat sat"` | `["the"]` |
| `"it is is done"` | `["is"]` |
| `"no duplicates here"` | `[]` |
| `"Hello hello world"` | `["Hello"]` — case-insensitive, first occurrence casing returned |
| `"aaa"` | `[]` — single token, not a duplicate word |
| `"a a b b"` | `["a", "b"]` |
| `""` | `[]` |

---

## Edge Cases

- **`"aaa"`** — this is one word token, not `"a"` + `"a"` + `"a"`. The backreference matches the same text, not individual characters. `(\w+)\s+\1` would try to match "aaa" then whitespace — which isn't there.
- **Case difference** (`"Hello hello"`) — with `CASE_INSENSITIVE`, the backreference `\1` matches case-insensitively. The returned value is from `group(1)`, which is the *first* occurrence ("Hello").
- **Three consecutive same words** (`"the the the"`) — the first pair `"the the"` is matched; after advancing past it, the remaining `" the"` has no duplicate partner. Result: `["the"]`.
- **Empty string** — `find()` returns false → empty list.

---

## Time Complexity

- **O(n)** — single pass through the input by the NFA

---

## Real-World Relevance

- **Spell/grammar checkers**: detecting accidental repeated words
- **Document quality tools**: flagging copy-paste errors in reports
- **Test fixtures**: detecting copy-paste errors in config files or scripts
- **NLP preprocessing**: removing stutters and repeated tokens

---

## Regex Thinking Process

1. **How do I capture a word?** `(\w+)` — captures one or more word chars in group 1

2. **How do I match the separator?** `\s+` — one or more whitespace characters

3. **How do I require the same word again?** `\1` — backreference to group 1.  
   The engine takes whatever text was captured by group 1 and tries to match that exact text at the current position.

4. **How do I avoid matching within longer words?** Add word boundaries: `\b(\w+)\s+\1\b`

5. **Java syntax for the backreference?** In a Java string: `"\\1"` (the `\\` is the regex `\`)

6. **How to make it case-insensitive?** Pass `Pattern.CASE_INSENSITIVE` to `Pattern.compile()`.  
   With this flag, `\1` matches case-insensitively too.

Full pattern: `"\\b(\\w+)\\s+\\1\\b"` with `Pattern.CASE_INSENSITIVE`

---

## Common Mistakes

| Mistake | Why It's Wrong |
|---|---|
| Using `\1` without double-backslash in Java | `"\1"` is an octal escape (SOH char) in Java strings; use `"\\1"` |
| Forgetting word boundaries | Without `\b`, "hello helloing" could partially match |
| Using `group(0)` | Returns `"the the"` — the full match; use `group(1)` to get just `"the"` |
| Not using `CASE_INSENSITIVE` | `"Hello hello"` would not match without the flag |
| Using `matches()` instead of `find()` | `matches()` requires the entire input to be two duplicate words |

---

## Debugging Advice

Test the backreference in isolation:
```java
Pattern p = Pattern.compile("\\b(\\w+)\\s+\\1\\b", Pattern.CASE_INSENSITIVE);
Matcher m = p.matcher("the the");
System.out.println(m.find());    // true
System.out.println(m.group(0));  // "the the"
System.out.println(m.group(1));  // "the"
```

Verify the case-insensitive backreference:
```java
Matcher m = p.matcher("Hello hello");
m.find();
System.out.println(m.group(1));  // "Hello" (from group 1, the first occurrence)
```
