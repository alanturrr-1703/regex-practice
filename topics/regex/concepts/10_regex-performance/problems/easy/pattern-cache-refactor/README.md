# Pattern Cache Refactor

**Difficulty**: Easy
**Concept**: Static Pattern compilation — never recompile in a loop

---

## Problem Statement

A naive implementation of phone number filtering recompiles the regex pattern on every
call to `String.matches()`. This is expensive: `Pattern.compile()` runs NFA construction,
which takes 1–100 microseconds.

```/dev/null/broken.java#L1-6
// BROKEN — recompiles pattern on EVERY iteration:
for (String line : lines) {
    if (line.matches("\\d{3}-\\d{4}")) {
        result.add(line);
    }
}
```

Your task: implement `filterPhoneNumbers(List<String> inputs)` that returns only the
strings matching the phone pattern `\d{3}-\d{4}` (exactly 3 digits, hyphen, 4 digits),
using a **static `Pattern` field** compiled once.

---

## Constraints

- Input may be `null` → return an empty list.
- Return a new `List<String>` (not the input list or a view of it).
- The pattern must be a **static final** field on the class — not local to the method.
- Use `Pattern.matcher(input).matches()` — not `String.matches()`.
- Order of results must match order of appearance in the input list.

---

## The Broken Pattern (Reference)

```/dev/null/broken-ref.java#L1-7
// BROKEN — DO NOT implement this way:
public List<String> filterPhoneNumbers(List<String> inputs) {
    List<String> result = new ArrayList<>();
    for (String s : inputs) {
        if (s.matches("\\d{3}-\\d{4}")) {  // recompiles every call!
            result.add(s);
        }
    }
    return result;
}
```

---

## Input / Output Examples

| Input | Expected |
|-------|----------|
| `["123-4567", "abc-defg", "999-0000", "12-345"]` | `["123-4567", "999-0000"]` |
| `[]` | `[]` |
| `["bad"]` | `[]` |
| `null` | `[]` |
| `["000-0000"]` | `["000-0000"]` |
| `["123-456", "1234-567"]` | `[]` (wrong digit counts) |

---

## Edge Cases

- `null` elements inside the list — if the list contains `null` strings, handle gracefully
  (skip them or check for null before matching).
- Empty string `""` → no match.
- Exactly matching `\d{3}-\d{4}` but with extra chars: `"123-45678"` → no match
  (`.matches()` requires full-string match).

---

## Time Complexity

- Pattern compile: O(P) once at class load, where P is pattern length.
- Per-call: O(N × L) where N is list size and L is average string length.
- vs. broken version: O(N × (P + L)) — compilation cost P repeated N times.

---

## Real-World Relevance

In production services processing millions of requests per day, even microsecond-level
savings matter at scale. Pattern compilation cost is also a hidden GC pressure source —
each `String.matches()` call allocates a `Pattern` and `Matcher` object. Caching the
`Pattern` halves allocations and eliminates compilation overhead.

This refactor pattern applies to every use of:
- `String.matches(regex)` inside a loop or frequently-called method
- `String.replaceAll(regex, ...)` inside a loop
- `String.split(regex)` inside a loop

---

## Regex Thinking Process

The pattern `\d{3}-\d{4}`:
- `\d{3}` = exactly 3 digits
- `-` = literal hyphen
- `\d{4}` = exactly 4 digits

With `Matcher.matches()`, the pattern must match the ENTIRE string (implicit `^...$`
anchoring), so `"123-45678"` won't match even though it starts with `\d{3}-\d{4}`.

---

## Common Mistakes

- Defining the `Pattern` as an instance field — it gets reconstructed each time the class
  is instantiated. It must be `static`.
- Using `find()` instead of `matches()` — `find()` allows partial matching; `matches()`
  requires the full string to match.
- Forgetting to handle null in the list (if the list contains null strings).
