# Concept: Debugging Regex

## Overview

Writing a regex is the easy part. Debugging why it doesn't work — or worse, why it works on some inputs but silently fails on others — is the hard part. This concept turns debugging regex from a frustrating guessing game into a systematic engineering discipline.

You will learn a repeatable framework: **SIMPLIFY → VISUALIZE → ISOLATE → INSTRUMENT → VERIFY**. You will learn the exact categories of bugs that cause 95% of all regex failures. You will learn how to use tooling, how to reduce test cases, and how to write tests that catch regressions before they reach production.

---

## Why This Concept Matters

Regex bugs are among the most insidious in software engineering because:

1. **They fail silently** — a misconfigured pattern often returns false negatives (misses valid data) rather than throwing an exception
2. **They are input-dependent** — a pattern that works on your test data can fail on production data with edge cases
3. **They can cause security vulnerabilities** — greedy overcapture can expose unintended data; catastrophic backtracking is a real DoS vector
4. **String.matches() vs find() confusion is endemic** — this single mistake accounts for a huge fraction of production regex bugs

---

## Prerequisites

- Java `Pattern` and `Matcher` API (see `java-pattern-matcher/notes.md`)
- All available flags (`MULTILINE`, `DOTALL`, `CASE_INSENSITIVE`)
- Capturing groups and group indexing
- Understanding of how greedy quantifiers work

---

## Roadmap — What You'll Learn, In Order

1. **The Debugging Framework** — A systematic process to apply every time a regex misbehaves
2. **Bug Category 1** — `matches()` vs `find()` confusion
3. **Bug Category 2** — Greedy overcapture (`.* ` eating too much)
4. **Bug Category 3** — Missing flags (`MULTILINE`, `DOTALL`)
5. **Bug Category 4** — Wrong group index (`group(0)` vs `group(1)`)
6. **Bug Category 5** — Catastrophic backtracking
7. **Tooling** — regex101, IntelliJ, print-based instrumentation
8. **Writing Regression Tests** — How to write tests that catch real bugs

---

## Problems in This Folder

| Difficulty | Problem | Bug Being Fixed |
|---|---|---|
| Easy | fix-greedy-overcapture | `.*` eating across multiple tags |
| Easy | fix-matches-vs-find | `String.matches()` vs `Matcher.find()` |
| Medium | debug-group-index-error | `group(0)` vs `group(1)` off-by-one |
| Medium | debug-missing-multiline-flag | Missing `Pattern.MULTILINE` on `^` |
| Hard | debug-catastrophic-log-parser | ReDoS via catastrophic backtracking |

Each problem gives you the **broken code**, explains what should happen, and asks you to fix it. This mirrors the real debugging workflow.
