# Match Vowels

**Difficulty:** Easy  
**Concept:** Character Classes  
**Concepts Tested:** Enumerated character class `[aeiou]`, `Pattern.CASE_INSENSITIVE` flag, `Matcher.find()` counting loop

---

## Problem Statement

Given a string, count and return the number of **vowel characters** in it. Vowels are: `a`, `e`, `i`, `o`, `u` — case-insensitive (`A`, `E`, `I`, `O`, `U` also count).

---

## Constraints

- Input is non-null.
- Case-insensitive: uppercase and lowercase vowels both count.
- Only the five traditional English vowels count (not `y`, not accented vowels like `é`).
- An empty string returns 0.

---

## Input / Output Examples

| Input | Output | Explanation |
|-------|--------|-------------|
| `"Hello World"` | `3` | e, o, o |
| `""` | `0` | No characters |
| `"AEIOU"` | `5` | All uppercase vowels |
| `"rhythm"` | `0` | No vowels |
| `"Beautiful"` | `5` | e, a, u, i, u |

---

## Edge Cases

- **Empty string** → `0`.
- **All consonants** `"rhythm"` → `0`.
- **All vowels** `"AEIOU"` → `5`.
- **Mixed case** `"Hello"` → `1` (only `e` and `o`: 2 vowels... wait: H-e-l-l-o → e, o → 2. Let me recount: "Beautiful" = B-e-a-u-t-i-f-u-l → e, a, u, i, u = 5.
- **Digits and punctuation** `"h3ll0!"` → `0`.

---

## Expected Time Complexity

- **O(n)** where n = string length.

---

## Real-World Relevance

Vowel counting appears in:
- **Linguistics analysis**: vowel-to-consonant ratio in text.
- **Text-to-speech preprocessing**: identify syllable boundaries.
- **Poem/lyrics tools**: analyze meter and rhyme schemes.
- **Password policies**: ensure a password isn't too "unpronounceable".

---

## Regex Thinking Process

1. **What are vowels?** The characters `a`, `e`, `i`, `o`, `u` — case-insensitive.
2. **Character class**: `[aeiou]` matches any one of these five characters.
3. **Case-insensitive**: Use `Pattern.CASE_INSENSITIVE` flag so `[aeiou]` also matches `A`, `E`, `I`, `O`, `U`.
4. **Counting**: Loop with `Matcher.find()` and increment a counter for each match.
5. **Pattern**: `[aeiou]` with `Pattern.CASE_INSENSITIVE`.

Alternatively, without the flag: `[aeiouAEIOU]` — explicit both cases.

---

## Common Mistakes

1. **Using `matches()` instead of `find()` loop** — `matches()` checks if the whole string is a vowel. You need `find()` to find all vowels.
2. **Not using CASE_INSENSITIVE** — `[aeiou]` alone misses uppercase vowels.
3. **Including `y`** — `y` is not a traditional vowel in this context.
4. **Counting the whole string as one match** — `[aeiou]+` would match runs of vowels as single tokens. Use `[aeiou]` (no quantifier) to match exactly one at a time, or use `[aeiou]+` and sum lengths.

---

## Debugging Advice

If your count is wrong:
- Print `m.group()` and `m.start()` in the loop to see exactly what's being matched.
- Check if the flag is applied: `Pattern.compile("[aeiou]", Pattern.CASE_INSENSITIVE)`.
- Test on `"AEIOU"` first — should return 5.
