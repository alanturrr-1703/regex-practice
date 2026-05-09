# Concept: Java Pattern & Matcher API

## Overview

Java's `java.util.regex` package exposes two primary classes that together form the complete interface to Java's NFA-based regex engine:

- **`Pattern`** — an immutable, thread-safe compiled representation of a regex. Expensive to create, cheap to reuse.
- **`Matcher`** — a stateful engine that applies a `Pattern` to a specific input. NOT thread-safe. Create one per call.

Understanding the separation between these two objects is the first key insight. Every performance problem and every thread-safety bug in Java regex code comes from misunderstanding which object does what.

---

## Why This Concept Matters

`Pattern` and `Matcher` are used in virtually every Java application that processes text: log parsers, config readers, validation layers, data extractors, template engines. The API surface is small but the semantics are deep.

You will be asked about this in interviews. You will debug production issues caused by this. Master it once; use it forever.

---

## Prerequisites

- Java `String` methods (`split`, `replaceAll`, `matches`) — and why they are NOT always sufficient
- Basic regex syntax: character classes, quantifiers, anchors, groups
- The concept of a compiled NFA engine (see `basics/notes.md`)

---

## Roadmap — What You'll Learn, In Order

1. **Pattern compilation** — `compile(String)`, `compile(String, int)`, all flags
2. **The core `find()` loop** — iterating over all matches in a string
3. **Capturing groups** — `group()`, `group(int)`, `group(String)` and their index semantics
4. **Position tracking** — `start()`, `end()` for substring extraction
5. **Matcher state management** — `reset()`, `reset(CharSequence)`, `find(int)`
6. **Replacement API** — `replaceAll`, `appendReplacement`, `appendTail` for custom logic
7. **Split API** — `Pattern.split()` vs manual `Matcher.find()` splitting
8. **Java 9+ streaming** — `Matcher.results()` for functional pipelines
9. **Error handling** — `PatternSyntaxException`
10. **Thread-safety patterns** — where to store `Pattern`, where NOT to store `Matcher`

---

## Common Mistakes

### 1. Calling `String.replaceAll()` with user-supplied input

`String.replaceAll(regex, replacement)` compiles the regex on every call, is vulnerable to `PatternSyntaxException` if the input is not a valid regex, and interprets `$` and `\` in the replacement string as special. Always use a compiled `Pattern` for production code.

### 2. Storing a `Matcher` as a static or instance field

`Matcher` is stateful. It tracks the current position in the input, the last match region, and captured groups. Sharing one across threads causes race conditions. Create a new `Matcher` per call with `pattern.matcher(input)` or reset an existing one with `matcher.reset(input)`.

### 3. Using `matcher.matches()` when you meant `matcher.find()`

`matches()` requires the **entire input** to match the pattern. `find()` searches for the pattern anywhere in the input. This is the single most common regex bug in production Java code.

### 4. Calling `matcher.group()` before calling `find()` or `matches()`

`group()` throws `IllegalStateException` if no match attempt has been made yet. Always call `find()` or `matches()` first and check the return value before calling `group()`.

### 5. Assuming `group(1)` is the whole match

`group(0)` (or `group()`) is the entire match. `group(1)` is the first capturing group. Off-by-one on group indices is a classic interview trip-wire.

### 6. Not caching patterns

`Pattern.compile()` parses the regex string, validates it, and compiles it into an internal NFA automaton structure. This is CPU-intensive. Patterns used repeatedly should be stored as `static final` fields.

---

## Problems in This Folder

| Difficulty | Problem | Core Skill |
|---|---|---|
| Easy | find-all-matches | `while (matcher.find())` core loop |
| Easy | extract-groups-from-match | `group(1)`, `group(2)` capturing |
| Medium | split-on-delimiter-keeping-delimiter | `start()`, `end()`, manual split logic |
| Medium | replace-with-custom-logic | `appendReplacement()` + `appendTail()` |
| Hard | streaming-line-processor | Multiple patterns, `reset()`, chained transforms |

Work through in order — each problem introduces the next layer of the API.
