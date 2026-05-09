# Overlapping Pattern Finder

**Difficulty**: Hard
**Concept**: Zero-width lookahead as a scanner for overlapping matches

---

## Problem Statement

Implement `countOverlapping(String input, String pattern)` that counts how many times
`pattern` appears in `input` as an **overlapping** substring.

In normal (non-overlapping) matching, after finding a match at position `i`, the engine
advances past the end of the match. With overlapping matching, the next match can start
at position `i+1` — even if that overlaps with the previous match.

**Example**:
- Input: `"abababa"`, Pattern: `"aba"`
- Positions: 0 (`aba`), 2 (`aba`), 4 (`aba`) — 3 overlapping occurrences
- Normal `indexOf` / `find()` would return only 2 (positions 0 and 4)

You must use the **lookahead trick**: wrap `pattern` in a lookahead inside a capturing
group to make the match zero-width:

```/dev/null/trick.txt#L1-2
(?=(PATTERN))
// The outer (?=...) is zero-width; the inner (PATTERN) captures without consuming.
```

---

## Constraints

- `input` may be null or empty → return `0`.
- `pattern` may be null or empty → return `0`.
- `pattern` is treated as a **literal string** — you must escape regex metacharacters in it
  (use `Pattern.quote(pattern)` or `Pattern.compile(Pattern.quote(pattern))`).
- No need to handle multi-line patterns.
- Pattern length can be 1 to `input.length()`.

---

## Input / Output Examples

| input | pattern | Expected | Explanation |
|-------|---------|----------|-------------|
| `"abababa"` | `"aba"` | `3` | Positions 0, 2, 4 |
| `"aaaa"` | `"aa"` | `3` | Positions 0, 1, 2 |
| `"hello"` | `"ll"` | `1` | Position 2 only (no overlap possible) |
| `"abcdef"` | `"xyz"` | `0` | Pattern not present |
| `""` | `"a"` | `0` | Empty input |
| `"a"` | `"a"` | `1` | Single character match |
| `"aaa"` | `"a"` | `3` | Single-char pattern, 3 occurrences |

---

## Edge Cases

- Pattern longer than input: `("ab", "abc")` → `0`.
- Pattern equals input: `("aba", "aba")` → `1`.
- All same character: `("aaaa", "a")` → `4`.
- Input contains regex metacharacters treated as literals: `("a+b", "+")` → `1` (the `+`
  is a literal `+`, not a quantifier).

---

## Time Complexity

O(N × M) where N is input length and M is pattern length. The lookahead inner NFA
re-scans M characters at each position. For patterns that are simple literals, the
JVM regex engine may optimize this to O(N) with Boyer-Moore or similar.

---

## Real-World Relevance

Overlapping pattern detection is critical in:
- **Bioinformatics**: finding all occurrences of a DNA motif in a genome sequence where
  overlapping occurrences are biologically meaningful (e.g., `ATATAT` contains `ATAT`
  twice).
- **Security scanning**: finding overlapping tokens in protocol stream analysis.
- **Text compression**: counting overlapping runs for LZ77-style dictionaries.
- **Test coverage**: verifying all overlapping substrings in a trie.

The lookahead trick is the idiomatic Java/PCRE solution to this problem.

---

## Regex Thinking Process

Step 1: Normal `find()` advances past each match. For `"aaaa"` with pattern `"aa"`:
        - Match at 0: "aa" consumed, cursor moves to 2.
        - Match at 2: "aa" consumed, cursor moves to 4.
        - Total: 2 matches. But we want 3 (positions 0, 1, 2).

Step 2: The problem is that `find()` consumes the matched text. Solution: use a
        zero-width assertion. Lookaheads are zero-width.

Step 3: Wrap the pattern in `(?=(PATTERN))`. The outer `(?=...)` is zero-width.
        `find()` advances 1 char (minimum advance after a zero-width match).
        Group 1 captures the matched text.

Step 4: Build the lookahead pattern from the input pattern:
        `"(?=(" + Pattern.quote(pattern) + "))"`

Step 5: Count matches by running `find()` in a loop.

---

## Common Mistakes

- Forgetting `Pattern.quote()` — if `pattern` contains `.`, `+`, `*`, etc., they would
  be treated as regex metacharacters, not literals.
- Building the regex as `"(?=" + pattern + ")"` without a capturing group — the match
  is always zero-length, `group()` returns `""`. Use `group(1)` for the actual text.
- Using `matches()` instead of `find()` — `matches()` requires the full string to match.
- Not handling null or empty `pattern` — `Pattern.quote("")` creates a zero-length lookahead
  which would match at every position.

---

## Debugging Advice

Add a debug loop that prints `m.start()` and `m.group(1)` for each match:
```/dev/null/debug.java#L1-6
while (m.find()) {
    System.out.println("Match at " + m.start() + ": " + m.group(1));
    count++;
}
```
This lets you visually verify the positions being detected.
