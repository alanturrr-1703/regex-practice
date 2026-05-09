# Java Regex Mastery — Deep Learning Repository

> **Philosophy:** You will not copy-paste regex. You will *think* regex.  
> Every pattern you write should feel like reading a grammar, not casting a spell.

---

## What This Repository Is

This is a self-contained, progressive curriculum for mastering **Regular Expressions in Java** from first principles through production-level usage. It is not a cheat sheet. It is not a reference card. It is a thinking system.

You will develop:
- Parser-engineer intuition for how regex engines *actually* work
- The ability to read and write non-trivial patterns without a tool
- A debugging mindset for when patterns fail silently or catastrophically
- Java-specific knowledge of `Pattern`, `Matcher`, flags, and escaping rules
- Real-world pattern design skills applicable to log parsers, validators, tokenizers, and data pipelines

---

## How to Use This Repository

```
topics/regex/
├── README.md              ← You are here
├── concepts/              ← One folder per concept, work through in order
│   ├── 01_basics/
│   ├── 02_character-classes/
│   ├── 03_quantifiers/
│   ├── 04_anchors/
│   ├── 05_groups-and-capturing/
│   ├── 06_alternation/
│   ├── 07_escaping/
│   ├── 08_greedy-vs-lazy/
│   ├── 09_lookahead-lookbehind/
│   ├── 10_regex-performance/
│   ├── 11_java-pattern-matcher/
│   └── 12_debugging-regex/
└── solutions/             ← Only populated when you explicitly request them
```

**For each concept:**
1. Read `README.md` — understand the concept, the motivation, and the mental model
2. Read `notes.md` — go deep on the engine, edge cases, Java specifics
3. Work through `problems/easy/` first, then `medium/`, then `hard/`
4. Each problem has a `README.md`, `hints.md`, `Solution.java` (starter), and `SolutionTest.java` (failing tests)
5. Your job is to make the tests pass — but more importantly, to *understand why*

---

## Curriculum Roadmap

```
Level 0 — Foundation
┌─────────────────────────────────────────────────────────────┐
│  basics                 → What regex is; literal matching;  │
│                           Pattern vs matches() vs find()    │
│  character-classes      → [abc], [^abc], ranges, shorthands │
│  quantifiers            → *, +, ?, {n,m}; greedy defaults   │
│  anchors                → ^, $, \b, \A, \Z, multiline mode  │
└─────────────────────────────────────────────────────────────┘

Level 1 — Structure
┌─────────────────────────────────────────────────────────────┐
│  groups-and-capturing   → (), (?:), named groups, backrefs  │
│  alternation            → |, ordering, grouping effects     │
│  escaping               → \., Java double-escape, raw chars │
└─────────────────────────────────────────────────────────────┘

Level 2 — Control
┌─────────────────────────────────────────────────────────────┐
│  greedy-vs-lazy         → *?, +?, possessive, backtracking  │
│  lookahead-lookbehind   → (?=), (?!), (?<=), (?<!), limits  │
└─────────────────────────────────────────────────────────────┘

Level 3 — Engineering
┌─────────────────────────────────────────────────────────────┐
│  regex-performance      → ReDoS, caching, atomic groups     │
│  java-pattern-matcher   → API mastery, flags, split, replace│
│  debugging-regex        → Systematic diagnosis & repair     │
└─────────────────────────────────────────────────────────────┘
```

---

## Prerequisites

- Java 11+ (Java 17 recommended)
- JUnit 5 on the test classpath
- Basic Java string manipulation knowledge
- No prior regex experience required — start at `basics/`

---

## Core Java Regex Classes

| Class | Role |
|---|---|
| `java.util.regex.Pattern` | Compiled regex engine object |
| `java.util.regex.Matcher` | Stateful engine execution against a specific input |
| `java.util.regex.PatternSyntaxException` | Thrown on invalid regex syntax |
| `String.matches()` | Convenience — full-string match only, compiles every call |
| `String.replaceAll()` | Convenience — compiles every call, avoid in loops |

---

## Mental Model: The Regex Engine Is a State Machine

Every regex is compiled into a **nondeterministic finite automaton (NFA)**. When you call `matcher.find()`, the engine:

1. Positions itself at the current start offset in the input
2. Attempts to follow the NFA states, character by character
3. If it fails, it **backtracks** — trying alternative paths it saved earlier
4. Reports success when it reaches an accepting state
5. Advances past the match and repeats

Understanding this model is the difference between writing correct, performant patterns and writing patterns that *seem* to work but destroy production systems.

---

## Common Mistakes (Global)

| Mistake | Why It Hurts |
|---|---|
| `String.matches()` expects full-string match | Silent miss when you expect `find()` semantics |
| Forgetting Java requires double-backslash `\\d` | `PatternSyntaxException` or literal match |
| Recompiling patterns in a loop | 10-100x slower than caching `Pattern.compile()` |
| Greedy `.+` consuming delimiters | Parser extracts wrong segments |
| Catastrophic backtracking with nested quantifiers | JVM hangs, ReDoS vulnerability |
| Using `^`/`$` without `MULTILINE` flag | Anchors only match string edges, not line edges |
| Group index off-by-one (group 0 = entire match) | Wrong capture extracted |

---

## Debugging Philosophy

When a pattern fails:
1. **Simplify** — strip the pattern to its smallest failing case
2. **Visualize** — draw what the engine should consume at each step
3. **Isolate** — test individual sub-patterns before composing
4. **Instrument** — add print statements inside `while (matcher.find())` loops
5. **Question assumptions** — is `matches()` vs `find()` correct here?

---

## Real-World Applications in This Curriculum

- Log line parsers (Apache, nginx, application logs)
- Email and URL validators
- Semantic version extractors
- CSV and TSV parsers with escaping
- HTML/XML token extractors (not full parsers — and you'll understand why)
- Password strength validators
- Tokenizers for expression languages
- Config file parsers
- Data pipeline field extractors

---

## Progress Tracker

| Concept | Notes Read | Easy Done | Medium Done | Hard Done |
|---|---|---|---|---|
| 01_basics | [ ] | [ ] | [ ] | [ ] |
| 02_character-classes | [ ] | [ ] | [ ] | [ ] |
| 03_quantifiers | [ ] | [ ] | [ ] | [ ] |
| 04_anchors | [ ] | [ ] | [ ] | [ ] |
| 05_groups-and-capturing | [ ] | [ ] | [ ] | [ ] |
| 06_alternation | [ ] | [ ] | [ ] | [ ] |
| 07_escaping | [ ] | [ ] | [ ] | [ ] |
| 08_greedy-vs-lazy | [ ] | [ ] | [ ] | [ ] |
| 09_lookahead-lookbehind | [ ] | [ ] | [ ] | [ ] |
| 10_regex-performance | [ ] | [ ] | [ ] | [ ] |
| 11_java-pattern-matcher | [ ] | [ ] | [ ] | [ ] |
| 12_debugging-regex | [ ] | [ ] | [ ] | [ ] |

---

*Start at `concepts/01_basics/README.md`.*
