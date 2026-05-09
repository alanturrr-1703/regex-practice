# Custom Character Range

**Difficulty:** Medium  
**Concept:** Character Classes  
**Concepts Tested:** Combining custom ranges `[a-m0-4]`, `+` quantifier, `Matcher.find()` extraction, range boundaries

---

## Problem Statement

Given a string of "encoded data", extract all **restricted tokens** — maximal contiguous sequences of characters that belong to the restricted character set:
- Lowercase letters **`a` through `m`** (inclusive)
- Digits **`0` through `4`** (inclusive)

Characters outside this set (e.g., `n`-`z`, `5`-`9`, uppercase, punctuation) break the token. Return a list of all such tokens, in order of appearance.

---

## Constraints

- Input is non-null but may be empty.
- Return only tokens that contain at least 1 matching character (`+` quantifier).
- The ranges are: `a`-`m` (letters `a` through `m`) and `0`-`4` (digits `0` through `4`).
- Return the matched substrings, not the indices.
- Characters outside both ranges act as separators/delimiters.

---

## Input / Output Examples

| Input | Output | Explanation |
|-------|--------|-------------|
| `"abc123xyz567"` | `["abc123"]` | `a,b,c` ∈ a-m; `1,2,3` ∈ 0-4; `x` ∉ → stop; `5,6,7` ∉ 0-4 |
| `"hello"` | `["hell"]` | `h,e,l,l` ∈ a-m; `o` ∉ a-m (o > m) |
| `"aaa444"` | `["aaa444"]` | `a,a,a` ∈ a-m; `4,4,4` ∈ 0-4 (4 is the max) |
| `"xyz999"` | `[]` | `x,y,z` ∉ a-m; `9,9,9` ∉ 0-4 |

---

## Edge Cases

- **`"o"`** → `[]` (letter `o` is beyond `m`, not in range).
- **`"n"`** → `[]` (letter `n` is just beyond `m`).
- **`"m"`** → `["m"]` (letter `m` is at the boundary, included).
- **`"5"`** → `[]` (digit `5` is beyond `4`, not in range).
- **`"4"`** → `["4"]` (digit `4` is at the boundary, included).
- **`"0"`** → `["0"]` (digit `0` is included).
- **Empty string** → `[]`.

---

## Expected Time Complexity

- **O(n)** where n = string length.

---

## Real-World Relevance

Custom character ranges appear in:
- **Encoding schemes**: only certain characters are allowed in encoded payloads (Base32, Base64 variants).
- **Data validation**: restricted character sets in structured data formats.
- **Tokenizers for domain-specific languages**: tokens that can only use a subset of characters.
- **Security filters**: allowlist-based input sanitization.

---

## Regex Thinking Process

1. **What are the allowed characters?** Lowercase `a-m` and digits `0-4`.
2. **How to express this in a character class?** Combine two ranges: `[a-m0-4]`.
3. **What is a "token"?** One or more consecutive allowed characters: `[a-m0-4]+`.
4. **How to find all tokens?** `Matcher.find()` loop.

**Range boundary check:**
- `a-m`: `a`=97, `m`=109. Includes a,b,c,d,e,f,g,h,i,j,k,l,m. Excludes n(110),o,p...z.
- `0-4`: `0`=48, `4`=52. Includes 0,1,2,3,4. Excludes 5(53),6,7,8,9.

**Pattern:** `[a-m0-4]+`

---

## Common Mistakes

1. **Using `[a-z0-9]+`** — too broad; includes letters n-z and digits 5-9.
2. **Confusing range direction** — `[m-a]` is invalid (m > a in code points). Ranges must be ascending.
3. **Missing the `+`** — `[a-m0-4]` without `+` matches single characters; you want maximal runs.
4. **Not testing boundary characters** — always test `m`, `n`, `4`, `5` specifically.

---

## Debugging Advice

Test boundary characters:
- Does the pattern match `'m'`? → Yes (m ≤ m).
- Does it match `'n'`? → No (n > m).
- Does it match `'4'`? → Yes (4 ≤ 4).
- Does it match `'5'`? → No (5 > 4).

Print `(int)'m'` = 109 and `(int)'n'` = 110 to verify the boundary.
