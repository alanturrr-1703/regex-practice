# Problem: Parse Repeated Tokens

**Difficulty**: Medium  
**Concept**: Quantifiers  
**Skills Tested**: Character class + `{n,m}` quantifier, boundary enforcement, `List<String>` extraction

---

## Problem Statement

A data format uses pipe-separated (`|`) fields. Each **valid** field consists of **1 to 8 uppercase ASCII letters** (A–Z only). Your task: extract only the valid fields from the string and return them as a `List<String>`.

Invalid fields (lowercase letters, digits, mixed case, too long) must be silently skipped. Leading and trailing pipes are allowed and do not cause errors.

---

## Constraints

- Input may be `null` — return an empty list
- Input may be empty — return an empty list
- Fields are separated by `|`, but the exact number and position of pipes is flexible
- A valid field is ONLY uppercase letters, length 1–8 inclusive
- A field longer than 8 uppercase letters is NOT valid — do not match a prefix of it
- Lowercase letters make a field invalid (even if some letters are uppercase)
- Return list in the order fields appear in the input

---

## Input / Output Examples

| Input | Output | Notes |
|---|---|---|
| `"AAA\|BBB\|CCC"` | `["AAA", "BBB", "CCC"]` | All valid |
| `"\|HELLO\|"` | `["HELLO"]` | Leading/trailing pipes ignored |
| `"TOOLONGFIELD\|OK"` | `["OK"]` | `TOOLONGFIELD` (12 chars) is invalid |
| `"aaa\|BBB"` | `["BBB"]` | `aaa` is lowercase — invalid |
| `""` | `[]` | Empty input |
| `"A\|BC\|DEF"` | `["A", "BC", "DEF"]` | Short fields are fine |
| `"ABCDEFGH\|ABCDEFGHI"` | `["ABCDEFGH"]` | 8 chars valid, 9 chars invalid |
| `null` | `[]` | Null safe |

---

## Edge Cases

- `"A"` → `["A"]` (single letter, minimum valid)
- `"ABCDEFGH"` → `["ABCDEFGH"]` (exactly 8 letters, maximum valid)
- `"ABCDEFGHI"` → `[]` (9 letters — invalid, do NOT match prefix "ABCDEFGH")
- `"|||"` → `[]` (only pipes, no fields)
- `"mix3d|UPPER"` → `["UPPER"]` (mixed is invalid, pure uppercase is valid)
- `"HELLO WORLD"` — space separates into two potential tokens; only uppercase letter sequences are matched

---

## Expected Time Complexity

O(n) — single scan with `Matcher.find()`.

---

## Real-World Relevance

This kind of format is common in:
- **EDI (Electronic Data Interchange)** messages, which use structured field separators
- **Fixed-format banking/financial files** where field types are encoded by character class
- **Legacy mainframe exports** where fields are uppercase-only identifiers
- **Protocol parsers** where token type is determined by character set

---

## Regex Thinking Process

**Step 1**: The basic pattern for a valid field is `[A-Z]{1,8}` — 1 to 8 uppercase letters.

**Step 2**: The problem: `"TOOLONGFIELD"` is 12 characters. The pattern `[A-Z]{1,8}` matches `"TOOLONGF"` (first 8) and reports it as a valid field. That's wrong — the entire token is invalid.

**Step 3**: You need to assert that the match is NOT adjacent to more uppercase letters:
- Before: `(?<![A-Z])` — not preceded by an uppercase letter
- After: `(?![A-Z])` — not followed by an uppercase letter

With both assertions: `(?<![A-Z])[A-Z]{1,8}(?![A-Z])`

**Step 4**: Now `"TOOLONGFIELD"` — at position 0, `(?<![A-Z])` passes (start of string), `[A-Z]{1,8}` matches `"TOOLONGF"` (greedy, up to 8), `(?![A-Z])` checks next char — it's `'I'` (uppercase) — FAIL. No match. Skip ahead — similar failures at all positions within "TOOLONGFIELD". Correct behavior!

**Step 5**: `"OK"` — at appropriate position, `(?<![A-Z])` passes (preceded by `|`), `[A-Z]{1,8}` matches `"OK"` (2 chars), `(?![A-Z])` checks next — end of string or pipe — PASS. Match returned.

---

## Common Mistakes

1. **Using `\w` instead of `[A-Z]`**: `\w` matches letters, digits, and underscore — too broad.
2. **Forgetting the upper-bound assertion**: Without `(?![A-Z])`, a 12-letter field matches as an 8-letter prefix.
3. **Using `split("|")` and then validating**: This works but is slower and more verbose. The regex approach handles all edge cases in one pass.
4. **Not handling null**: Java's `Matcher` will throw `NullPointerException` if the input is null. Check first.
5. **Using `matches()` instead of `find()`**: `matches()` requires the full string to match the pattern.

---

## Debugging Advice

- Test `"TOOLONGFIELD"` explicitly — print `matcher.group()` if a match is found. If it prints `"TOOLONGF"`, you're missing the `(?![A-Z])` lookahead.
- Test `"A"` — if it doesn't match, your lookbehind is too aggressive.
- Test with `"|||"` — should return empty list, not throw an exception.
