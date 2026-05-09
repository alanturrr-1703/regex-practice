# Hints: Validate Digit Count

Work through these hints one at a time. Try to solve the problem before reading the next hint.

---

## Hint 1 (Gentle)

Start by thinking about what `\d{5}` matches. Write a test: does it match `"12345"` in isolation? Does it also match inside `"123456"`? Once you see the problem, you'll know what extra constraint is needed.

---

## Hint 2

The problem is that `\d{5}` is greedy about *what it matches* but does not care about what *surrounds* the match. You need assertions about the characters **adjacent** to your 5-digit run — specifically, you need to assert that those adjacent characters are NOT digits.

Think about lookahead `(?!...)` and lookbehind `(?<!...)`. These are **zero-width** — they check a condition without consuming characters.

---

## Hint 3

You need two assertions:
- Something **before** the 5 digits: assert the character before is not a digit
- Something **after** the 5 digits: assert the character after is not a digit

A **negative lookbehind** checks what came before the current position: `(?<!\d)` means "the character before this position is NOT a digit."

A **negative lookahead** checks what comes after the match: `(?!\d)` means "the character at this position is NOT a digit."

Put them together around `\d{5}`.

---

## Hint 4

The pattern is: `(?<!\d)\d{5}(?!\d)`

In Java, you need to escape the backslashes in a string literal: `"(?<!\\d)\\d{5}(?!\\d)"`

Use `Pattern.compile(...)` and `matcher.find()` — NOT `matcher.matches()`. The `find()` method searches anywhere in the string. `matches()` requires the entire string to match the pattern.

Don't forget to handle `null` input — check for `null` before creating the Matcher.

---

## Hint 5 (Near Solution)

Structure your solution like this:

```
1. If input is null, return false
2. Compile the pattern: (?<!\d)\d{5}(?!\d)
3. Create a Matcher on the input
4. Return matcher.find()
```

The pattern works because:
- `(?<!\d)` ensures no digit immediately precedes the 5-digit run
- `\d{5}` matches exactly 5 digits
- `(?!\d)` ensures no digit immediately follows the 5-digit run

Store the pattern as a `static final` field for efficiency (no need to recompile on every call).
