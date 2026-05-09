# Count Special Regex Characters

**Difficulty:** Easy
**Concepts Tested:** Escaping all 14 metacharacters inside a character class
**Concept:** escaping

---

## Problem Statement

Given a string, count how many characters in it are **regex metacharacters** — characters that have special meaning in a regular expression pattern.

The 14 regex metacharacters are: `. * + ? ^ $ { } [ ] | ( ) \`

Your regex must match each of these characters **literally** inside a character class `[...]`. Getting the escaping right for a character class containing all 14 metacharacters is a substantial escaping exercise.

---

## Constraints

- Input string may be empty → return 0
- Input string is never `null`
- Count each occurrence individually: `".*"` → 2 (one dot, one star)
- The backslash `\` counts — but in Java strings, a single backslash is represented as `"\\"`

---

## Examples

### Example 1
**Input:** `"a.b*c"`
**Output:** `2` (the dot and the asterisk)

### Example 2
**Input:** `"hello"`
**Output:** `0`

### Example 3
**Input:** `"$(test)"`
**Output:** `3` (dollar sign, open paren, close paren)

### Example 4
**Input:** `".*+?^${}[]|()\\"` (Java string — the last two chars are backslash)
**Output:** `14` (one of each metacharacter)

---

## Edge Cases

- Empty string → 0
- String with only non-special characters → 0
- String with only special characters → length of string
- A single backslash character in the input → count 1
  - Java string to represent one backslash: `"\\"`
- The `-` inside a character class is itself tricky to place correctly

---

## Expected Time Complexity

O(n) — one pass through the string

---

## Real-World Relevance

- Sanitizing user-supplied regex patterns
- Building regex escaping utilities
- Code analysis tools that detect regex metacharacters in data

---

## Regex Thinking Process

1. You need a character class `[...]` that matches ANY ONE of the 14 metacharacters.
2. Inside `[...]`, most metacharacters lose their special meaning — but not all:
   - `\` still escapes the next character
   - `^` at the start negates the class
   - `-` between two chars creates a range
   - `]` closes the class
3. Strategy: place `-` at the very start or end of the class to make it literal. Escape `]` as `\]`. Escape `\` as `\\`. Put `^` not at the start.
4. The character class will look like (in regex notation): `[][.*+?^${}|()\\]` (note `]` and `[` at start of class).
5. In Java string: you must double every backslash.

---

## Common Mistakes

1. Forgetting that `\` inside a character class must be escaped as `\\` (Java string: `\\\\`)
2. Putting `-` between two characters creates a range, not a literal hyphen
3. Putting `^` first in the class negates it instead of including it literally
4. Forgetting `[` — it is also a metacharacter and must be in your list

---

## Debugging Advice

Build the character class incrementally. Start with just `[.]` (matches dot only), verify it works, then add one metacharacter at a time. This isolates which character is causing the escaping issue.
