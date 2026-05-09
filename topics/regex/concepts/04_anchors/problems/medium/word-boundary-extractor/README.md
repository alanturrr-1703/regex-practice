# Problem: Word Boundary Extractor

**Difficulty**: Medium  
**Concept**: Anchors  
**Skills Tested**: `\b` word boundary assertion, distinction from partial-word matching, case sensitivity

---

## Problem Statement

Given a string, count the number of occurrences of the word `"log"` as a **standalone word** — not as part of a longer word such as `"logger"`, `"catalog"`, or `"dialog"`. Return the count as an integer.

The match is **case-sensitive**: `"LOG"` or `"Log"` do not count.

---

## Constraints

- Input may be `null` — return `0`
- Input may be empty — return `0`
- Match must be case-sensitive
- `"log"` next to punctuation (`"log."`, `"log!"`, `"log,"`) counts — punctuation is a non-word character, creating a boundary
- `"log"` followed by underscore or digit does NOT count — those are word characters

---

## Input / Output Examples

| Input | Output | Reason |
|---|---|---|
| `"log the event"` | `1` | One standalone `log` |
| `"logger logs events"` | `0` | `logger` contains `log` but no boundary; `logs` adds `s` after |
| `"log and log again"` | `2` | Two standalone `log` occurrences |
| `"catalog logger"` | `0` | `log` inside both — no word boundary |
| `"LOG"` | `0` | Case-sensitive — uppercase not counted |
| `"log."` | `1` | Dot is a non-word character — boundary exists |
| `"log_file"` | `0` | Underscore is a word character — no boundary after `log` |

---

## Edge Cases

- `"log log log"` → `3`
- `"(log)"` → `1` (parentheses are non-word)
- `"log1"` → `0` (digit after `log` is a word character — no boundary)
- `"1log"` → `0` (digit before `log` is a word character — no boundary)
- `"blog"` → `0` (`b` is a word character, so no boundary before `log`)
- `""` → `0`
- `null` → `0`

---

## Expected Time Complexity

O(n) — a single `find()` loop.

---

## Real-World Relevance

Word boundary matching is critical in:
- **Code search tools**: find the variable `log` but not `logger` in a codebase
- **Log analyzers**: count specific log-level keywords without hitting longer words
- **NLP tokenizers**: isolate tokens without stemming
- **Search engines**: "whole word" checkbox uses `\b` semantics
- **IDE refactoring**: "rename symbol" uses word boundaries to avoid partial matches

---

## Regex Thinking Process

**Step 1**: Without `\b`, `"log"` matches inside `"logger"`. Try it: `Pattern.compile("log").matcher("logger").find()` returns `true`. That's wrong.

**Step 2**: `\b` is a zero-width assertion that matches at the transition between `\w` and `\W` (or string boundaries). It doesn't consume any characters.

**Step 3**: Pattern `\blog\b` — the first `\b` ensures `log` starts at a word boundary (either start of string, or preceded by a non-word char). The second `\b` ensures `log` ends at a word boundary (either end of string, or followed by a non-word char).

**Step 4**: Apply to `"logger"`: `\b` before `l` — position 0, start of string, YES boundary. Then `log` matches positions 0-2. Then `\b` — position 3, between `g` (word) and `g` (word, it's `ge`... wait, position 3 is `g`, position 4 is `e`) — both sides are word characters, NO boundary. Pattern fails. Correct!

**Step 5**: Apply to `"log."`: `\b` before `l` — position 0, YES. `log` matches 0-2. `\b` after `g` — position 3, between `g` (word) and `.` (non-word), YES boundary. Match found!

---

## Common Mistakes

1. **Using `"log"` without `\b`**: Matches inside longer words. Always add `\b` for word isolation.
2. **Thinking `\b` is about spaces**: `\b` is about `\w`/`\W` transitions, not just spaces. `"log."` has a boundary; `"log_"` does not.
3. **Using case-insensitive flag accidentally**: The test expects `"LOG"` to return `0`. Don't add `Pattern.CASE_INSENSITIVE`.
4. **Counting with `split()`**: You could split on `\b`, but a counter with `find()` is cleaner and more efficient.

---

## Debugging Advice

- Test `"catalog"` — if it returns 1, your `\b` before `log` is wrong or missing
- Test `"logger"` — if it returns 1, your `\b` after `log` is wrong or missing
- Test `"log."` — if it returns 0, you may have added `\w` instead of `\b`
- Print `matcher.start()` for each match to verify positions
