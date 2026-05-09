# Concept: Greedy vs Lazy Quantifiers

> *"Greedy quantifiers ask: 'How much can I take?' Lazy quantifiers ask: 'How little can I get away with?' Neither is universally correct — the right choice depends entirely on what you're parsing."*

---

## Overview

Every quantifier in regex — `*`, `+`, `?`, `{n,m}` — has a default **mode**: greedy. Greedy means the engine tries to match as much input as possible. Java (and most regex flavors) also support a **lazy** (reluctant) mode by appending `?` to the quantifier, which matches as little as possible. Java also supports **possessive** mode with `+`, which matches as much as possible and never gives anything back.

Understanding the difference is not just academic. Choosing the wrong mode is the single most common cause of:
- Regex matching the wrong portions of text
- Regex that works on simple tests but fails on real data
- Regex that is catastrophically slow on certain inputs

---

## Why This Concept Matters

- `<.*>` on `<b>text</b>` matches the **whole thing** (greedy spans from first `<` to last `>`)
- `<.*?>` on the same input matches only `<b>` (lazy stops at the first `>`)
- `<[^>]*>` on the same input also matches only `<b>` — and is faster than lazy

These three patterns have very different behavior. You must know which one to reach for and why.

---

## Roadmap

| Problem | Difficulty | Core Skill |
|---|---|---|
| greedy-vs-lazy-html-tags | Easy | `<b>.*?</b>` — lazy vs greedy over tag pairs |
| extract-bracketed-values | Easy | `\[.*?\]` — lazy to get each bracket pair |
| extract-quoted-strings | Medium | `'.*?'` vs `'(?:[^'\\]|\\.)*'` — lazy vs negated class |
| multiline-comment-extractor | Medium | `/*.*?*/` with DOTALL — multiline lazy |
| csv-field-tokenizer | Hard | Precise character class matching — neither greedy nor lazy, but exact |

---

## Prerequisites

- `basics/` — find() loop, Pattern/Matcher
- `character-classes/` — negated classes `[^...]`
- `escaping/` — `\\.` for escape-aware matching

---

## Common Mistakes

| Mistake | Example | Fix |
|---|---|---|
| Greedy `.+` spanning across delimiters | `<b>.*</b>` matches from first `<b>` to last `</b>` | Use `<b>.*?</b>` or `<b>[^<]*</b>` |
| Lazy `.*?` stopping too early | `'.*?'` stops at first `'`, misses `'\''` if there are escaped quotes | Use `'(?:[^'\\]|\\.)*'` |
| Thinking lazy = "shortest overall match" | Lazy is leftmost-first, not globally shortest | Understand the engine model |
| Using `.*` inside a group with `*` outside | `(.*)* ` on bad input | Catastrophic backtracking — use a negated class |
| Forgetting DOTALL for multiline content | `/*.*?*/` won't match multiline comments without `Pattern.DOTALL` | Add `Pattern.DOTALL` flag |

---

## Debugging Mindset

When greedy/lazy behavior is unexpected, ask:
1. **What is the engine trying to maximize or minimize?** Greedy: the match length. Lazy: the match length.
2. **What is my delimiter?** If you know the delimiter, a negated character class `[^delimiter]` is almost always better than lazy.
3. **Is the content allowed to span lines?** If yes, you need `Pattern.DOTALL` for `.` to match newlines.
4. **Is there escaping inside the delimiters?** If yes, neither greedy nor lazy works — use the `[^"\\]|\\.` pattern.

---

## Real-World Usage

- **HTML tag extraction**: `<tag>.*?</tag>` — lazy to stop at the first closing tag
- **Quoted string extraction**: `"[^"]*"` — negated class (faster than lazy)
- **Block comment extraction**: `\/\*.*?\*\/` with DOTALL — lazy + multiline
- **CSV parsing**: precise character class to match quoted or unquoted fields
- **Log field extraction**: `\[.*?\]` to extract content from bracketed fields
