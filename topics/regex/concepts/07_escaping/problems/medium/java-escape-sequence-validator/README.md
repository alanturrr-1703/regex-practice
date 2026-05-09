# Java Escape Sequence Validator

**Difficulty:** Medium
**Concepts Tested:** Matching literal backslashes (`\\\\`), the deepest layer of Java regex escaping
**Concept:** escaping

---

## Problem Statement

Given a string that represents the **content** of a Java string literal (i.e., after the outer double-quotes are removed), validate that every backslash character in it forms a **valid Java string escape sequence**.

Valid Java string escape sequences are: `\n`, `\t`, `\r`, `\\`, `\"`, `\'`, `\0`

Any backslash followed by a character NOT in this list is **invalid** (e.g., `\x`, `\q`, `\p`).

If the string contains no backslashes at all, it is valid.

Return `true` if all escape sequences are valid (or if there are no backslashes), `false` otherwise.

---

## The Deep Escaping Challenge

This problem involves the most intense backslash escaping in all of Java regex. To match a **literal backslash** in the input string:

- The regex engine needs to see: `\\` (two chars)
- So the Java string must contain: `\\\\` (four chars)
- Which in Java source code is written as: `"\\\\"` (six chars including quotes)

To then check what follows the backslash, you need to combine this with a lookahead or a character class.

---

## Constraints

- Input is never `null`
- Input may be empty → return `true` (no backslashes = no invalid escapes)
- Input contains the raw characters (a `\n` escape means the actual backslash character followed by `n`, NOT a newline)
- Do NOT de-escape the input — work with it as-is

---

## Examples

### Example 1
**Input:** `"hello"` (no backslashes)
**Output:** `true`

### Example 2
**Input:** `"line1\nline2"` — this Java string contains the chars: `l i n e 1 \ n l i n e 2`
**Output:** `true` (backslash followed by `n` is valid `\n`)

### Example 3
**Input:** `"bad\xescape"` — contains backslash followed by `x`
**Output:** `false` (`\x` is not a valid Java escape)

### Example 4
**Input:** `"\\\\"` — this Java string contains two backslashes: `\ \`
**Output:** `true` (each backslash is followed by another backslash, forming `\\`)

---

## Edge Cases

- Empty string → `true`
- A lone backslash at end of string (no character following it) → `false` (incomplete escape)
- `"\n"` — in Java source this is a string with a single newline CHARACTER. The backslash is not in the string. This is NOT the same as the input `"\\n"` which contains a backslash followed by `n`.
- `"\\"` in Java source → single backslash character in memory → this is the input "a backslash followed by nothing at end" — actually no. The input to your method is whatever Java string object you pass. In tests, `"\\n"` as Java source = backslash + n character.

---

## Expected Time Complexity

O(n) — linear scan

---

## Real-World Relevance

- Java code analyzers and linters
- Template engines that process Java-style escape sequences
- Compilers and transpilers that validate string literal content
- Test frameworks that check generated source code

---

## Regex Thinking Process

**Goal:** Find any backslash that is NOT followed by a valid escape character (`n t r \\ " ' 0`).

**Approach 1 — Find invalid escapes:**
Match the pattern: `\\[^ntr\\'\"0]` or `\\` at end of string.
If any match exists → return false.

**Approach 2 — Find valid escapes only:**
Match the pattern: `\\[ntr\\'\"0]` repeatedly.
If there are any backslashes NOT matched by this → return false.

**The key Java string**: to match one backslash in the input, your Java string needs `"\\\\"`.
Then the following character check is a character class `[ntr\\\"'0]`.

Full "find invalid" pattern in Java string: `"\\\\[^ntr\\\\\"'0]|\\\\$"` — but be very careful about counting.

**Recommended approach**: find any backslash followed by an invalid char OR backslash at end.

---

## Common Mistakes

1. Confusing the backslash levels — `"\n"` in Java source = newline character (NOT backslash+n)
2. Writing `"\\"` thinking it matches one backslash in the input — it does, but you need `"\\\\"` to match a literal backslash AS A REGEX
3. Forgetting the case of a backslash at end of string (incomplete escape)
4. Testing with Java string `"\n"` (actual newline) instead of `"\\n"` (backslash + n)

---

## Debugging Advice

Build a test harness that prints what's in each test string:
```
String input = "\\n";  // backslash + n
System.out.println("Length: " + input.length()); // should be 2
System.out.println("Char 0: " + (int)input.charAt(0)); // 92 = backslash
System.out.println("Char 1: " + (int)input.charAt(1)); // 110 = 'n'
```
This clarifies exactly what characters are in your input before you write the regex.
