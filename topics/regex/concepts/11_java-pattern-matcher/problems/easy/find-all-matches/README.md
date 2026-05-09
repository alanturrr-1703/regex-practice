# Find All Matches

**Difficulty**: Easy
**Concept**: Java Pattern & Matcher API
**Estimated Time**: 15–20 minutes

---

## Problem Statement

Given an input string and a regex pattern string, return a `List<String>` containing **all non-overlapping matches** found by scanning the input from left to right using `Matcher.find()`.

This is the foundational `while (matcher.find())` loop that every Java regex user must internalize. Every time you need to extract multiple occurrences of a pattern from a string, this loop is your tool.

---

## Method Signature

```java
public List<String> findAll(String input, String regex)
```

---

## Input / Output Examples

| Input | Regex | Output |
|---|---|---|
| `"one 1 two 2 three 3"` | `"\\d+"` | `["1", "2", "3"]` |
| `"hello"` | `"xyz"` | `[]` |
| `"aabbcc"` | `"[a-c]+"` | `["aabbcc"]` |
| `""` | `"\\w+"` | `[]` |
| `"abc123def456"` | `"[a-z]+"` | `["abc", "def"]` |

---

## Constraints

- `input` is never `null` (may be empty)
- `regex` is a valid Java regex pattern (no need to handle `PatternSyntaxException`)
- Return an empty list if there are no matches
- Matches are non-overlapping and returned in left-to-right order

---

## Edge Cases

- Empty input string → always returns `[]`
- Pattern that matches nothing → returns `[]`
- Pattern that matches the entire string once → returns a list with one element
- Pattern with `+` quantifier on entire input → one large match
- Consecutive matches with no gaps → each match is its own list entry

---

## Time Complexity

- O(n × m) in the worst case, where n = input length and m = pattern complexity
- For simple patterns (no backtracking), effectively O(n)

---

## Real-World Relevance

This loop is used in:
- **Log parsers**: extract all timestamps, IDs, or error codes from a log line
- **Config readers**: find all key=value pairs in a config file
- **Data extractors**: scrape phone numbers, emails, URLs from unstructured text
- **Tokenizers**: break structured text into typed tokens

---

## Regex Thinking Process

1. Compile the pattern into a `Pattern` object
2. Get a `Matcher` for the input
3. Loop: `while (matcher.find())` — each call to `find()` advances the internal position and returns `true` if another match was found
4. Inside the loop: `matcher.group()` returns the matched substring

Do NOT use `matcher.matches()` — that requires the entire input to match.

---

## Common Mistakes

1. **Using `matches()` instead of `find()`** — `matches()` requires the entire string to be consumed. `find()` finds a substring.
2. **Calling `group()` before `find()`** — `group()` throws `IllegalStateException` if no match attempt has been made yet.
3. **Recompiling the pattern on every call** — in production code, `Pattern.compile()` should be cached. For this problem, compiling inside the method is acceptable.
4. **Returning `null` instead of empty list** — always return an empty list, never null.

---

## Debugging Advice

If you're getting unexpected results:
- Add `System.out.println(m.start() + " " + m.group())` inside the loop to see where each match is occurring
- Test with a simple pattern like `"\\d"` (single digit) before testing `"\\d+"`
- Check that your regex string properly escapes backslashes: `"\\d+"` in Java becomes `\d+` as a regex
