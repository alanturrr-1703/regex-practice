# Extract Quoted Strings

**Difficulty:** Medium
**Concepts Tested:** Lazy `'.*?'`, escape-aware `(?:[^'\\]|\\.)*`, choosing the right approach
**Concept:** greedy-vs-lazy

---

## Problem Statement

Implement TWO methods for extracting single-quoted string contents:

**Method 1 — `extractSimpleQuoted`**: Extract content of `'...'` strings that do NOT contain escaped single quotes. Use lazy `'.*?'`. Simple and fast.

**Method 2 — `extractRobustQuoted`**: Extract content of `'...'` strings that MAY contain escaped single quotes `\'`. Lazy `'.*?'` would stop too early at the `\'`. You must use the escape-aware pattern: `'(?:[^'\\]|\\.)*'`.

---

## Constraints

- Input is never `null`, may be empty
- Simple method: assume no `\'` inside strings
- Robust method: correctly handles `\'` as an escape sequence
- Return content without surrounding quotes
- Do NOT de-escape the content

---

## Examples

### Simple method
- `"say 'hello' then 'bye'"` → `["hello", "bye"]`
- `"no quotes here"` → `[]`
- `"''"` → `[""]` (empty string)

### Robust method
- `"'it\'s fine'"` → `["it\'s fine"]` (the `\'` is inside the string)
- `"'one' and 'two'"` → `["one", "two"]`
- `"''"` → `[""]`

---

## Edge Cases

- Empty input → `[]`
- Empty quoted string `''` → `[""]`
- `\'` at end of string (incomplete escape) — define behavior

---

## Expected Time Complexity

O(n) — linear scan

---

## Real-World Relevance

- Shell command parsers (single-quoted args)
- SQL string literal parsers
- Configuration file parsers that use single-quoted values
- Template engines

---

## Regex Thinking Process

**Simple method:**
1. Open quote: `'`
2. Content: `.*?` (lazy — stop at first closing quote)
3. Close quote: `'`
4. Full pattern: `'(.*?)'`

**Robust method:**
1. Open quote: `'`
2. Content (escape-aware): `(?:[^'\\]|\\.)*`
   - `[^'\\]` — any char that is NOT quote and NOT backslash
   - `\\.` — backslash + any char (consuming escape sequences as a unit)
3. Close quote: `'`
4. Full pattern: `'((?:[^'\\]|\\.)*)'`
5. Java string: `"'((?:[^'\\\\]|\\\\.)*)'"`

---

## Common Mistakes

1. Using simple lazy for the robust method — stops at `\'` thinking it's the end
2. Forgetting the non-capturing group `(?:...)` around the alternation — quantifier applies to the whole group
3. Escaping `'` in Java — not needed! Single quotes don't need escaping in Java strings

---

## Debugging Advice

Test the robust method with this specific input: `"'don't stop'"`. The `'` in `don't` should NOT end the string. If it does, your pattern is not handling escape sequences correctly.
