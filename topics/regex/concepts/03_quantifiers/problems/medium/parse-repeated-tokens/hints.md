# Hints: Parse Repeated Tokens

---

## Hint 1 (Gentle)

Start by identifying the pattern for a single valid field: it must contain only uppercase letters (A–Z), and must be between 1 and 8 characters long. Which character class matches uppercase letters? Which quantifier enforces "1 to 8"?

---

## Hint 2

The basic pattern is `[A-Z]{1,8}`. Try it on `"AAA|BBB|CCC"` — it works. But try it on `"TOOLONGFIELD|OK"`. What happens? The engine matches `"TOOLONGF"` (first 8 chars) from `"TOOLONGFIELD"`, which is wrong.

The problem: `{1,8}` is a maximum, not an exact count. The engine matches up to 8 and stops, not caring that more uppercase letters follow.

---

## Hint 3

You need to assert that the match is a **complete uppercase token** — not adjacent to more uppercase letters:
- Before: no uppercase letter should precede the match
- After: no uppercase letter should follow the match

This sounds like negative lookbehind before and negative lookahead after. What do those look like for the character class `[A-Z]`?

---

## Hint 4

The assertions are:
- `(?<![A-Z])` — the character BEFORE the match is NOT an uppercase letter
- `(?![A-Z])` — the character AFTER the match is NOT an uppercase letter

Combined: `(?<![A-Z])[A-Z]{1,8}(?![A-Z])`

In Java: `"(?<![A-Z])[A-Z]{1,8}(?![A-Z])"`

Test this with `"TOOLONGFIELD"`: the engine tries to match at position 0, takes 8 chars (`"TOOLONGF"`), then the lookahead sees `'I'` — uppercase, fails. The engine advances to position 1, lookbehind sees `'T'` — uppercase, fails. And so on for all positions. No match. Correct!

---

## Hint 5 (Near Solution)

Use a `while (matcher.find())` loop to collect all valid fields. Structure:

```
if input is null → return empty list
compile pattern: (?<![A-Z])[A-Z]{1,8}(?![A-Z])
create Matcher
while (matcher.find()):
    add matcher.group() to results list
return results list
```

Note: the pipe `|` characters are naturally skipped because they don't match `[A-Z]`. Lowercase letters are also skipped. Only valid uppercase tokens of the right length get collected.
