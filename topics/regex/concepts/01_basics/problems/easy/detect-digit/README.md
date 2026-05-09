# Detect Digit

**Difficulty:** Easy  
**Concept:** Regex Basics  
**Concepts Tested:** `\d` shorthand class, Java double-escaping, `Matcher.find()`, substring presence detection

---

## Problem Statement

Given a string, return `true` if it contains **at least one digit character** (0–9), and `false` otherwise.

The core lesson here is NOT the logic (that's trivial). The lesson is **Java's double-escaping requirement**: to pass `\d` to the regex engine, you must write `"\\d"` in a Java string literal.

---

## Constraints

- Input is non-null.
- An empty string `""` should return `false` (contains no digits).
- Spaces, punctuation, and letters are not digits.
- Only ASCII digits `0`–`9` count (not Arabic-Indic digits or other Unicode numerals). Use `\d` in default mode.

---

## Input / Output Examples

| Input | Output |
|-------|--------|
| `"hello123"` | `true` |
| `"hello"` | `false` |
| `""` | `false` |
| `"   9   "` | `true` |
| `"abc!@#"` | `false` |
| `"5"` | `true` |
| `"1abc"` | `true` |
| `"abc9"` | `true` |

---

## Edge Cases

- **Empty string**: no characters, so no digits → `false`.
- **Only spaces**: spaces are not digits → `false`.
- **Only special characters** (`!@#$%`): not digits → `false`.
- **Digit at start**: `"1abc"` → `true`.
- **Digit at end**: `"abc9"` → `true`.
- **Single digit**: `"5"` → `true`.
- **All digits**: `"12345"` → `true`.
- **Digit surrounded by spaces**: `"   9   "` → `true` (space is not a digit but digit is still present).

---

## Expected Time Complexity

- **O(n)** where n = string length. The engine scans characters left-to-right and stops at the first digit it finds.
- For strings with a digit early, the engine stops early. For strings with no digit, the engine scans the entire string.

---

## Real-World Relevance

Digit detection is used in:
- **Password strength validation**: require at least one digit.
- **Input sanitization**: detect if a "text-only" field accidentally contains numbers.
- **Data classification**: distinguish pure-text fields from mixed-content fields.
- **Preprocessing for NLP**: flag tokens that are numeric or mixed alphanumeric.
- **Config file parsing**: determine if a value is numeric before parsing it.

---

## Regex Thinking Process

1. **What are we looking for?** Any single digit character.
2. **What regex matches a digit?** `\d` — the digit shorthand class (equivalent to `[0-9]` in ASCII mode).
3. **Java escaping**: In a Java string literal, `\` starts an escape sequence. To get a literal `\` into the string (which the regex engine then reads as `\d`), you write `\\`. So the Java string `"\\d"` becomes the regex `\d`.
4. **How to search for a digit anywhere?** Use `Matcher.find()` — it searches the entire string for the first occurrence.
5. **Do we need to find all digits?** No. One is enough. A single `find()` call suffices.

**Pattern:** `"\\d"` (the Java string), which the engine receives as `\d`.

---

## Common Mistakes

1. **Writing `"\d"`** — Java compiler error or unexpected behavior. `\d` is not a Java escape sequence (like `\n`, `\t`). You must write `"\\d"`.
2. **Using `matches()` with `"\\d"`** — this would check if the entire string IS a single digit. `"hello123".matches("\\d")` → `false`. Use `find()` or use `".*\\d.*"` with `matches()`.
3. **Using `matches(".*\\d.*")`** — this technically works but is slower and a code smell. Prefer `Pattern.compile("\\d").matcher(input).find()`.
4. **Forgetting that `\d` is ASCII-only by default** — won't match `٣` (Arabic-Indic 3). For this problem that's intentional.

---

## Debugging Advice

If the method always returns `false`:
1. Print the Java string you're using: `System.out.println("\\d")` → prints `\d`. That's what you want.
2. Test: `Pattern.compile("\\d").matcher("abc5def").find()` → should be `true`.
3. Confirm you're calling `find()`, not `matches()`.

If the code doesn't compile:
1. Check for `"\d"` — this won't compile because `\d` is not a valid Java escape sequence.
2. Fix: `"\\d"`.
