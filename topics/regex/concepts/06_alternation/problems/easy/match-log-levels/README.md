# Match Log Levels

**Difficulty:** Easy  
**Concept:** Alternation  
**Estimated Time:** 10–20 minutes

---

## Concepts Tested

- Basic alternation `DEBUG|INFO|WARN|ERROR|FATAL`
- Word boundaries `\b` to prevent partial word matching
- `matcher.find()` for substring search vs `matcher.matches()` for full-string
- Why `[DEBUG|INFO]` is WRONG (character class vs alternation)
- Case-sensitivity: the pattern is case-sensitive by design

---

## Problem Statement

Given a string, return `true` if it contains any of the standard log level keywords as a **standalone word** (not as part of a longer word):

`DEBUG`, `INFO`, `WARN`, `ERROR`, `FATAL`

The match is **case-sensitive**: `"debug"` should NOT match. `"DEBUG"` should.

---

## Method Signature

```java
public boolean containsLogLevel(String input)
```

---

## Constraints

- `input` is never `null`
- Keywords must appear as whole words (use word boundaries `\b`)
- Case-sensitive: only uppercase keywords match
- Return `false` for empty strings

---

## Input / Output Examples

| Input | Output | Reason |
|---|---|---|
| `"DEBUG: starting server"` | `true` | `DEBUG` is a match |
| `"INFORMATION: something"` | `false` | `INFORMATION` is not in the list |
| `"An ERROR occurred"` | `true` | `ERROR` is a match |
| `"debugging is fun"` | `false` | `debug` is lowercase, case-sensitive |
| `""` | `false` | Empty string |
| `"FATAL system crash"` | `true` | `FATAL` is a match |
| `"WARNING: disk low"` | `false` | `WARNING` is not in the list (only `WARN`) |
| `"INFO"` | `true` | Single keyword matches |

---

## Edge Cases

- **`"INFORMATION"`**: contains `INFO` as a prefix but NOT as a standalone word. `\bINFO\b` requires a word boundary after `INFO` — the `R` in `INFORMATION` is a word char, so no boundary. Result: `false`.
- **`"WARNING"`**: contains `WARN` as a prefix. `\bWARN\b` fails because `I` follows immediately. Result: `false`.
- **Case mismatch**: `"error"` → `false`. The pattern is case-sensitive.
- **Empty string**: `find()` returns false.

---

## Time Complexity

- **O(n)** — single scan to find first match; stops at first hit

---

## Real-World Relevance

- **Log aggregation**: filter lines by severity level (e.g., only show WARN and ERROR)
- **Alerting rules**: trigger an alert if a log line contains FATAL or ERROR
- **Log parsing**: classify lines by severity for different processing pipelines

---

## Regex Thinking Process

1. **What are the alternatives?** `DEBUG`, `INFO`, `WARN`, `ERROR`, `FATAL`

2. **How do I combine them?** Alternation: `DEBUG|INFO|WARN|ERROR|FATAL`

3. **Common mistake: `[DEBUG|INFO|WARN]`** — This is a character class, not alternation!  
   It matches ONE character from the set: D, E, B, U, G, |, I, N, F, O, W, A, R.  
   That is NOT what you want.  
   The correct syntax is `(?:DEBUG|INFO|WARN|ERROR|FATAL)` or just `DEBUG|INFO|WARN|ERROR|FATAL`.

4. **Why word boundaries?** Without `\b`, `"INFORMATION"` would match `INFO` (substring).  
   With `\bINFO\b`, the engine requires a non-word character (or start/end of string) on both sides.

5. **Use `find()`, not `matches()`**: The keyword may appear anywhere in the string. `matches()` requires the entire string to match the pattern.

---

## Common Mistakes

| Mistake | Why It's Wrong |
|---|---|
| `[DEBUG\|INFO\|WARN\|ERROR\|FATAL]` | This is a character class matching one char from the set |
| Missing word boundaries `\b` | `"INFORMATION"` would match `INFO` without boundaries |
| Using `matches()` | Requires the entire string to be one of the keywords |
| Adding `(?i)` flag | The problem specifies case-sensitive matching |
| `"WARNNING"` should match `WARN` | It should NOT — word boundary prevents partial matches |

---

## Debugging Advice

Test each alternative alone first:
```java
Pattern p = Pattern.compile("\\bDEBUG\\b");
System.out.println(p.matcher("DEBUG: test").find());      // true
System.out.println(p.matcher("DEBUGGING").find());        // false (BUGGING follows)
System.out.println(p.matcher("debug").find());            // false (case-sensitive)
```

Then combine all alternatives and test the boundary cases.
