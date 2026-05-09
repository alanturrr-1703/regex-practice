# Java Regex — Deep Learning Repository

> **You will not copy-paste regex. You will *think* regex.**

A self-contained, progressive curriculum for mastering Regular Expressions in Java — from first principles through production-level usage. Not a cheat sheet. A thinking system.

---

## What's Inside

| Layer | What you build |
|---|---|
| **Reading** | `README.md` + `notes.md` per concept — engine internals, backtracking, Java-specific traps |
| **Problems** | 60 problems across 12 concepts, 3 difficulty tiers each |
| **Starter code** | Every problem has a `Solution.java` that compiles but throws `UnsupportedOperationException` |
| **Failing tests** | Every problem has a `SolutionTest.java` — tests fail until *you* implement the solution |
| **Hints** | Progressive `hints.md` in every problem — from a gentle nudge to a near-solution |

---

## Curriculum

```
01_basics                 → literal matching, Pattern vs Matcher, find() vs matches()
02_character-classes      → [abc], [^abc], ranges, \d \w \s, Unicode \p{L}
03_quantifiers            → *, +, ?, {n,m}, greedy defaults, catastrophic backtracking intro
04_anchors                → ^, $, \b, \A, \Z, MULTILINE mode
05_groups-and-capturing   → (), (?:), named groups, backreferences
06_alternation            → |, ordering, grouping effects, NFA branching
07_escaping               → two-layer escaping, \\\\ in Java, Pattern.quote()
08_greedy-vs-lazy         → .*  vs .*?  vs possessive *+, negated class [^x]*
09_lookahead-lookbehind   → (?=), (?!), (?<=), (?<!), zero-width assertions
10_regex-performance      → ReDoS, Pattern caching, atomic groups, possessive quantifiers
11_java-pattern-matcher   → Full Pattern/Matcher API, appendReplacement, split, flags
12_debugging-regex        → Systematic diagnosis — greedy bugs, wrong flags, catastrophic patterns
```

---

## Problem Map

Each concept has **5 problems** across 3 tiers:

| Concept | Easy | Medium | Hard |
|---|---|---|---|
| `01_basics` | match-literal-string · detect-digit | extract-words · validate-simple-email | simple-lexer |
| `02_character-classes` | match-vowels · validate-alphanumeric | extract-hex-colors · custom-character-range | unicode-identifier |
| `03_quantifiers` | validate-digit-count · collapse-whitespace | extract-version-numbers · parse-repeated-tokens | catastrophic-backtracking-debug |
| `04_anchors` | validate-starts-with-http · validate-ends-with-semicolon | multiline-section-parser · word-boundary-extractor | log-line-anchor-parser |
| `05_groups-and-capturing` | extract-date-parts · capture-first-word | named-groups-log-parser · backreference-duplicate-word | nested-groups-csv-parser |
| `06_alternation` | match-log-levels · validate-file-extension | multi-format-date-parser · protocol-extractor | alternation-order-bug |
| `07_escaping` | match-literal-dot · count-special-regex-chars | extract-ip-addresses · java-escape-sequence-validator | string-literal-extractor |
| `08_greedy-vs-lazy` | greedy-vs-lazy-html-tags · extract-bracketed-values | extract-quoted-strings · multiline-comment-extractor | csv-field-tokenizer |
| `09_lookahead-lookbehind` | password-strength-validator · find-word-not-followed-by | extract-prices · log-severity-extractor | overlapping-pattern-finder |
| `10_regex-performance` | pattern-cache-refactor · identify-catastrophic-pattern | optimize-email-validator · possessive-quantifier-usage | redos-safe-log-processor |
| `11_java-pattern-matcher` | find-all-matches · extract-groups-from-match | split-keeping-delimiter · replace-with-custom-logic | streaming-line-processor |
| `12_debugging-regex` | fix-greedy-overcapture · fix-matches-vs-find | debug-group-index-error · debug-missing-multiline-flag | debug-catastrophic-log-parser |

---

## Running Tests

Requires **Java 17+** and the included Gradle wrapper (`./gradlew`).

```bash
# Run one specific problem
./gradlew :01_basics:easy:detect-digit:test

# Run all problems in one concept
./gradlew :01_basics:easy:match-literal-string:test :01_basics:easy:detect-digit:test

# Run everything (most will fail — that's the point)
./gradlew test --continue

# Run a single test method
./gradlew :01_basics:easy:detect-digit:test --tests "com.example.SolutionTest.testSingleDigitReturnsTrue"
```

The module path format is always:
```
:<concept-folder>:<difficulty>:<problem-folder>:test
```

---

## How to Use This Repo

1. **Read** `topics/regex/concepts/01_basics/README.md` — understand the concept and the mental model
2. **Study** `notes.md` — engine internals, Java specifics, edge cases, interview traps
3. **Pick a problem** — start with `easy/`, move to `medium/`, then `hard/`
4. **Read** the problem `README.md` — constraints, examples, regex thinking process
5. **Run the tests** — they all fail before you start
6. **Implement** `Solution.java` — make the tests pass
7. **Check** `hints.md` only when stuck — hints are progressive, not giveaways
8. **Commit** your solution and move to the next problem

---

## Repository Structure

```
regex-practice/
├── README.md                          ← you are here
├── build.gradle                       ← JUnit 5 config (shared across all problems)
├── settings.gradle                    ← auto-discovers all 60 problem modules
├── gradlew / gradlew.bat
└── topics/
    └── regex/
        ├── README.md                  ← full curriculum overview + progress tracker
        └── concepts/
            ├── 01_basics/
            │   ├── README.md          ← concept overview
            │   ├── notes.md           ← deep teaching (400-600 lines)
            │   └── problems/
            │       ├── easy/
            │       │   └── detect-digit/
            │       │       ├── README.md       ← problem statement
            │       │       ├── hints.md        ← progressive hints
            │       │       └── src/
            │       │           ├── main/java/com/example/Solution.java
            │       │           └── test/java/com/example/SolutionTest.java
            │       ├── medium/
            │       └── hard/
            ├── 02_character-classes/
            ├── 03_quantifiers/
            │   ...
            └── 12_debugging-regex/
```

---

## Prerequisites

- Java 17+ installed
- No prior regex experience needed — start at `01_basics`
- Basic Java string/method knowledge

---

## Tech Stack

| Tool | Version |
|---|---|
| Java | 17+ |
| Gradle | 9.4.1 (wrapper included) |
| JUnit | 5.10.2 |
