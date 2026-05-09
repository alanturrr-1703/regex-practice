# Match Literal Dot

**Difficulty:** Easy
**Concepts Tested:** Escaping — `\\.` vs `.`, `Matcher.find()`
**Concept:** escaping

---

## Problem Statement

Given a list of strings, return a new list containing only those strings that contain at least one **literal dot character** (`.`).

This sounds trivial — but the challenge is in the regex. The dot (`.`) is one of the most powerful metacharacters in regex: it matches **any character** except a newline. Using it unescaped will match far more than you intend.

Your regex must use `\\.` (Java string: two backslashes + dot) to match a literal dot.

---

## Constraints

- The input list may be empty → return an empty list
- Individual strings may be empty → skip them (they can't contain a dot)
- Strings may contain multiple dots → include if at least one dot exists
- Do not mutate the input list
- Input list is never `null`, but individual strings may be `null` → skip them gracefully

---

## Examples

### Example 1
**Input:** `["hello.world", "helloXworld", "3.14", "nodot"]`
**Output:** `["hello.world", "3.14"]`

### Example 2
**Input:** `["."]`
**Output:** `["."]`

### Example 3
**Input:** `["", "abc", "   "]`
**Output:** `[]`

### Example 4
**Input:** `["a.b.c", "version.2.0", "plain"]`
**Output:** `["a.b.c", "version.2.0"]`

---

## Edge Cases

- Empty string `""` → not matched
- String of only spaces `"   "` → not matched
- String of only dots `"..."` → matched
- Dot at start `".gitignore"` → matched
- Dot at end `"file."` → matched
- `null` element in list → skip it

---

## Expected Time Complexity

O(n * m) — n = number of strings, m = average length

---

## Real-World Relevance

- Checking if a filename has an extension
- Filtering log lines that contain IP addresses (which have dots)
- Validating hostnames contain at least one dot
- Searching for decimal numbers in text

---

## Regex Thinking Process

1. What are you looking for? A literal dot character.
2. What does unescaped `.` mean in regex? ANY character. That is wrong.
3. How do you escape a dot? In regex: `\.`. In Java string: `"\\."`.
4. Should you use `matches()` or `find()`? `find()` — you want substring search, not full-string match.
5. Final pattern Java string: `"\\."`

---

## Common Mistakes

1. Using `"."` → matches ANY character, every non-empty string passes
2. Using `String.matches("\\.")` → requires full string to be just a dot
3. Forgetting that `find()` is needed for substring search

---

## Debugging Advice

Print your compiled pattern to verify it:
```
System.out.println(pattern.pattern());
// Should output: \.
// If it outputs: .   you forgot the backslash
```
If every non-empty string passes your filter, you are using `.` instead of `\\.`.
