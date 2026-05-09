# Unicode Identifier

**Difficulty:** Hard  
**Concept:** Character Classes  
**Concepts Tested:** Unicode category classes `\p{L}`, `\p{N}`, `Pattern.UNICODE_CHARACTER_CLASS`, negative lookbehind `(?<!...)`, identifier start vs continuation rules

---

## Problem Statement

Extract all **valid Unicode identifiers** from a string. A Unicode identifier is defined as:

1. **Starts with**: an underscore `_` OR any Unicode letter (`\p{L}` — covers all scripts: Latin, Greek, CJK, Arabic, etc.)
2. **Continues with**: zero or more of: underscore `_`, Unicode letter `\p{L}`, or Unicode numeric digit `\p{N}`
3. **Not preceded by** an identifier character (to avoid matching the tail of something like `"123bad"` and returning `"bad"`)

Return all matches as a `List<String>`, in order of appearance.

---

## Constraints

- Input is non-null but may be empty.
- Use `Pattern.UNICODE_CHARACTER_CLASS` flag for full Unicode support.
- A token that starts immediately after a digit or letter (with no separator) is **NOT** a valid identifier start. For example, in `"123abc"`, `"abc"` should NOT be returned because it's immediately preceded by a digit.
- Underscore at the start is valid: `"_private"` → `["_private"]`.
- Return an empty list if no identifiers are found.

---

## Input / Output Examples

| Input | Output |
|-------|--------|
| `"hello wörld café"` | `["hello", "wörld", "café"]` |
| `"x1 y2 123bad"` | `["x1", "y2"]` |
| `"_private _1"` | `["_private", "_1"]` |
| `"日本語 english"` | `["日本語", "english"]` |

---

## Edge Cases

- **`"123bad"`**: digits precede `"bad"` → `"bad"` is NOT returned. The preceding `3` is `\p{N}` which is an identifier continuation char, so the lookbehind fires and the match is rejected.
- **`"_"`**: single underscore → `["_"]` (valid identifier).
- **`"123"`**: all digits, none starts with `\p{L}` or `_` → `[]`.
- **CJK characters** (`"日本語"`): CJK characters are in `\p{L}` (category Lo = Other_Letter) → matched as an identifier.
- **Empty string** → `[]`.

---

## Expected Time Complexity

- **O(n)** for the regex scan.

---

## Real-World Relevance

Unicode identifier matching is used in:
- **International programming language parsers**: Java, Python 3, Rust allow Unicode identifiers.
- **Code analysis tools**: find all variable/function names in source code, including non-ASCII.
- **Database schema validators**: check that table/column names are valid identifiers.
- **Text processing for internationalized apps**: extract proper nouns or named entities from multilingual text.

---

## Regex Thinking Process

### Step 1: What Is a Unicode Letter?

`\p{L}` — the Unicode General Category for Letter. Includes:
- Latin: `a-z`, `A-Z`, `é`, `ñ`, `ü`
- Greek: `α`, `β`, `γ`
- CJK: `日`, `本`, `語`
- Arabic, Cyrillic, Devanagari, etc.

`\p{N}` — the Unicode General Category for Number. Includes decimal digits from all scripts.

### Step 2: The Identifier Pattern

Start: `[_\p{L}]` — underscore or Unicode letter
Continue: `[_\p{L}\p{N}]*` — underscore, Unicode letter, or Unicode number

Raw pattern (regex notation): `[_\p{L}][_\p{L}\p{N}]*`

### Step 3: Preventing Mid-Identifier Matches

The problem: `find()` on `"123bad"` without anchoring would find `"bad"` starting at position 3. But `"bad"` is preceded by `"3"` (a `\p{N}`), so it's in the middle of what should be a mixed `NUMBER+LETTER` token.

Solution: **negative lookbehind** `(?<![_\p{L}\p{N}])` — fails if the character immediately before the match is an identifier character.

Full pattern: `(?<![_\p{L}\p{N}])[_\p{L}][_\p{L}\p{N}]*`

### Step 4: Unicode Character Class Flag

Use `Pattern.UNICODE_CHARACTER_CLASS` to ensure `\p{L}`, `\p{N}` behave as full Unicode categories. Without this flag, some Java builds may not process them correctly for all scripts.

---

## Common Mistakes

1. **Using `\w` instead of `[_\p{L}\p{N}]`** — `\w` in ASCII mode misses non-ASCII letters. `\p{L}` is the correct Unicode-aware equivalent.
2. **Forgetting the lookbehind** — `find()` on `"123bad"` returns `"bad"` without it.
3. **Using `\b` (word boundary) instead of lookbehind** — `\b` uses `\w` for boundary detection, which may not correctly handle the transition between ASCII digits and Unicode letters. The explicit lookbehind is more reliable.
4. **Not using `UNICODE_CHARACTER_CLASS` flag** — may cause `\p{L}` and `\p{N}` to behave incorrectly for some scripts.
5. **Allowing digits at the start** — `[_\p{L}\p{N}]+` would match identifiers starting with a digit. The first character must be `[_\p{L}]`, not `\p{N}`.

---

## Debugging Advice

Test each character class in isolation:
- Does `\p{L}` match `'日'`? → Yes (CJK letter).
- Does `\p{L}` match `'3'`? → No (digit, not letter).
- Does `\p{N}` match `'1'`? → Yes.

Test the lookbehind:
- On `"123bad"`, does `(?<![_\p{L}\p{N}])[_\p{L}]` match starting at `'b'`? → No, because `'3'` before it is `\p{N}`.
- On `" bad"`, does it match `"bad"`? → Yes, because `' '` (space) is not `[_\p{L}\p{N}]`.
