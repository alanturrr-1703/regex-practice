# Extract Groups From Match

**Difficulty**: Easy
**Concept**: Java Pattern & Matcher API — Capturing Groups
**Estimated Time**: 20–25 minutes

---

## Problem Statement

Given a string containing `key=value` pairs (e.g., `"name=Alice age=30 city=Boston"`), extract all key-value pairs and return them as a `Map<String, String>`.

Use the capturing group pattern `(\w+)=(\w+)` where:
- `matcher.group(1)` is the key
- `matcher.group(2)` is the value

This problem teaches the most important group API: `group(int n)` where n starts at 1 (not 0).

---

## Method Signature

```java
public Map<String, String> extractKeyValuePairs(String input)
```

---

## Input / Output Examples

| Input | Output |
|---|---|
| `"name=Alice age=30"` | `{"name": "Alice", "age": "30"}` |
| `""` | `{}` |
| `"no pairs here"` | `{}` |
| `"x=1 y=2 z=3"` | `{"x": "1", "y": "2", "z": "3"}` |
| `"key=value extra"` | `{"key": "value"}` (extra has no `=`) |

---

## Constraints

- `input` is never null (may be empty)
- Keys and values consist only of word characters (`\w` = `[A-Za-z0-9_]`)
- If the same key appears multiple times, the last value wins (standard `Map.put` behavior)
- Tokens that don't match the `word=word` pattern are silently ignored

---

## Edge Cases

- Empty string → empty map
- String with words but no `=` signs → empty map
- Multiple spaces between pairs → handled naturally (the pattern ignores whitespace)
- `"a=b=c"` → matches `a=b` (the second `=c` is a separate non-matching fragment)
- Value contains digits → returned as a string (no type conversion needed)

---

## Time Complexity

- O(n) for n = input length, one pass through the input

---

## Real-World Relevance

This exact pattern appears in:
- **HTTP header parsing**: extracting `Content-Type: application/json; charset=utf-8`
- **Query string parsing**: extracting `key=value` pairs from URL parameters
- **Config file readers**: reading `key = value` lines from `.properties` files
- **Log structured data**: extracting `field=value` pairs from structured log lines

---

## Regex Thinking Process

Pattern: `(\w+)=(\w+)`

1. `(\w+)` — capturing group 1: one or more word characters (the KEY)
2. `=` — literal equals sign (the separator)
3. `(\w+)` — capturing group 2: one or more word characters (the VALUE)

After a successful `find()`:
- `matcher.group(0)` or `matcher.group()` — entire match, e.g., `"name=Alice"`
- `matcher.group(1)` — the key, e.g., `"name"`
- `matcher.group(2)` — the value, e.g., `"Alice"`

---

## Common Mistakes

1. **Using `group(0)` for the key** — group(0) is the entire match (`"name=Alice"`), not the key
2. **Using `group(1)` for the value** — group(1) is the key; group(2) is the value
3. **Forgetting to escape backslash in Java strings** — `\\w` in Java string = `\w` in regex
4. **Using `split("=")` instead of regex groups** — split loses the pairing information when there are multiple pairs
5. **Calling `group()` without a successful `find()` first** — always call `find()` and check its return value

---

## Debugging Advice

- Use `System.out.println("Key: " + m.group(1) + " Val: " + m.group(2))` inside the loop
- Verify group numbering by first printing `m.group(0)` — it should show the full `key=value` match
- If you get `IllegalStateException`, you're calling `group()` before `find()` or after `find()` returns false
