# Match Literal String

**Difficulty:** Easy  
**Concept:** Regex Basics  
**Concepts Tested:** Literal pattern matching, `Matcher.find()` vs `Pattern.matches()`, case sensitivity, substring search

---

## Problem Statement

Given a list of strings (log lines), return a new list containing only those strings that contain the exact **case-sensitive** literal substring `"ERROR"`.

This problem intentionally has no wildcards, no character classes, and no special regex constructs. The entire lesson is about one deceptively fundamental question: **which Java API do you use to search for a substring using regex?**

---

## Constraints

- The input list is non-null but may be empty.
- Elements in the list are non-null but may be empty strings.
- The search is **case-sensitive**: `"error"` and `"Error"` do NOT match.
- The substring `"ERROR"` may appear anywhere in the string (start, middle, end, multiple times).
- The string `"ERRORS"` contains `"ERROR"` as a substring — it **should** match.
- Preserve the original order of matching elements in the returned list.
- The returned list must never be null.

---

## Input / Output Examples

| Input | Output |
|-------|--------|
| `["ERROR: disk full", "error: low mem", "CRITICAL ERROR", "no problem", ""]` | `["ERROR: disk full", "CRITICAL ERROR"]` |
| `[]` | `[]` |
| `["all good", "warning: disk"]` | `[]` |
| `["ERROR", "ERRORS found", "my_ERROR_code"]` | `["ERROR", "ERRORS found", "my_ERROR_code"]` |

---

## Edge Cases

- **Empty string** `""`: does not contain `"ERROR"`, so it must NOT be returned.
- **Exact match** `"ERROR"`: the entire string IS the substring — must be returned.
- **Embedded in a word** `"ERRORS"`: substring match — must be returned.
- **Lowercase** `"error"`: case-sensitive mismatch — must NOT be returned.
- **Multiple occurrences** `"ERROR ERROR"`: still just one element in the list, returned once.
- **Only whitespace** `"   "`: does not contain `"ERROR"` — not returned.

---

## Expected Time Complexity

- **O(n × m)** where n = number of strings, m = average string length.
- Each string is scanned linearly by the regex engine for the substring.
- Pattern compilation is O(k) where k = pattern length (tiny, constant here).

---

## Real-World Relevance

This exact operation is the heart of **log aggregation pipelines**. Systems like Splunk, Loki, and Elasticsearch do this at millions of lines per second. The underlying operation is always: "does this log line contain a specific level keyword?"

In Java, this pattern is used in:
- `java.util.logging` filter predicates
- Log4j appender filters
- Cloud function triggers that watch log streams
- Security alert systems scanning for `"EXCEPTION"`, `"CRITICAL"`, `"FATAL"`

---

## Regex Thinking Process

1. **What are we matching?** A literal string `"ERROR"` — no special characters.
2. **Where can the match appear?** Anywhere in the string — we need a **substring search**.
3. **Which API gives us substring search?** `Matcher.find()`, NOT `String.matches()` or `Matcher.matches()`.
4. **Do we need special flags?** No — the default is case-sensitive, which is what we want.
5. **Pattern:** Just the literal string `"ERROR"`.

The key learning: in Java, `"ERROR".matches("ERROR")` returns `true`, but `"ERROR: disk full".matches("ERROR")` returns `false` because `matches()` requires a full-string match. You need `find()`.

---

## Common Mistakes

1. **Using `String.matches()`** — anchors at both ends. `"ERROR: disk full".matches("ERROR")` → `false`.
2. **Using `Pattern.matches("ERROR", line)`** — same trap, still full-string match.
3. **Using `String.contains("ERROR")`** — this works but doesn't use regex at all, defeating the learning purpose. The point is to know how regex enables this.
4. **Recompiling the Pattern inside the loop** — correctness is fine, but performance suffers. Compile once, reuse.
5. **Returning null instead of an empty list** when no matches are found.

---

## Debugging Advice

If your output is empty when you expect matches:
1. Print `line.getClass().getName()` and `line.length()` to confirm you're getting real strings.
2. Try `Pattern.compile("ERROR").matcher(line).find()` in a scratch program with a single known-good input.
3. Verify you're calling `find()`, not `matches()`.
4. Print the compiled pattern: `pattern.pattern()` — should print exactly `ERROR`.

If you get too many matches:
1. You may have made the match case-insensitive by accident (e.g., used `Pattern.CASE_INSENSITIVE`).
2. Verify the exact literal you're matching — is it `"ERROR"` or something else?
