# Problem: Extract Version Numbers

**Difficulty**: Medium  
**Concept**: Quantifiers  
**Skills Tested**: `{n,m}` range quantifier, negative lookahead for boundary enforcement, `Matcher.find()` loop, `List<String>` extraction

---

## Problem Statement

Given a string, extract all **semantic version strings** of the form `major.minor.patch` and return them as a `List<String>`.

A valid version number for this problem:
- Each component (major, minor, patch) is **1 to 3 digits** long
- Components are separated by literal dots
- The version must **NOT** be surrounded by other digits or dots (e.g., `"1.2.3.4"` should NOT yield a match because it is followed by another `.` and digit component)

---

## Constraints

- Return an empty list if no valid version strings are found
- Input may be `null` — return an empty list
- Versions may appear anywhere in the string (beginning, middle, end)
- Components are 1-3 digits: `1`, `12`, `123` are valid; `1234` is not
- A version followed immediately by a dot+digit (like `1.2.3.4`) is NOT a valid match
- A version preceded immediately by a digit is NOT a valid match (e.g., `01.2.3` — the leading `0` makes it part of a larger sequence)

---

## Input / Output Examples

| Input | Output |
|---|---|
| `"version 1.2.3 and 10.0.5"` | `["1.2.3", "10.0.5"]` |
| `"v2.0.0-beta"` | `["2.0.0"]` |
| `"1.2.3.4"` | `[]` — followed by `.4`, violating the boundary |
| `"no version here"` | `[]` |
| `"release 3.14.1 done"` | `["3.14.1"]` |
| `"from 0.1.0 to 1.0.0"` | `["0.1.0", "1.0.0"]` |

---

## Edge Cases

- `"1.2.3.4"` → `[]` because the match `"1.2.3"` is followed by `.4`
- `"99.999.1"` → `[]` because `999` is 3 digits (valid), but — actually this should match: `["99.999.1"]`. Wait — `{1,3}` allows 1–3 digits. `999` is 3 digits, valid.
- `"1234.5.6"` → `[]` because `1234` has 4 digits
- `"1.2"` → `[]` because it's only two components
- `""` → `[]`
- `null` → `[]`
- Multiple versions: `"v1.0.0 and v2.3.4"` → `["1.0.0", "2.3.4"]`

---

## Expected Time Complexity

O(n) for a single scan via `Matcher.find()` loop.

---

## Real-World Relevance

Parsing version numbers from release notes, changelogs, dependency files, and build logs is an extremely common task in DevOps and build tooling:
- Parsing `CHANGELOG.md` to extract all mentioned versions
- Scanning a `pom.xml` or `build.gradle` text dump
- Log analysis: which versions were mentioned in error logs?
- Dependency audits: find all `x.y.z` references in a codebase

---

## Regex Thinking Process

**Step 1**: A version is `\d{1,3}\.\d{1,3}\.\d{1,3}`. This matches 1-3 digits, dot, 1-3 digits, dot, 1-3 digits.

**Step 2**: The problem is `"1.2.3.4"`. At position 0, `\d{1,3}` matches `"1"`, then `\.` matches `"."`, then `\d{1,3}` matches `"2"`, then `\.` matches `"."`, then `\d{1,3}` matches `"3"`. The match is `"1.2.3"`. But this is immediately followed by `".4"` — not what we want.

**Step 3**: Add a negative lookahead after the pattern: `(?!\.\d)` — "not followed by a dot and a digit." This prevents `"1.2.3"` from matching when followed by `".4"`.

**Step 4**: Add a negative lookbehind before the pattern: `(?<!\d)` — "not preceded by a digit." This prevents `"234.5.6"` from matching as `"34.5.6"` or `"4.5.6"`.

**Step 5**: Full pattern: `(?<!\d)\d{1,3}\.\d{1,3}\.\d{1,3}(?!\.\d)(?!\d)`

Note: You need BOTH `(?!\.\d)` (don't match if followed by `.X`) AND `(?!\d)` (don't match if followed by digit, e.g., `1.2.3456`).

---

## Common Mistakes

1. **Forgetting `{1,3}` on all three components**: Using `\d+` allows any number of digits; `\d{1,3}` enforces the 3-digit max.
2. **Escaping the dot**: In a regex, `.` matches ANY character. Use `\.` to match a literal dot. In Java string: `"\\."`.
3. **Not handling `"1.2.3.4"`**: Without a lookahead after the patch component, `"1.2.3.4"` would wrongly return `"1.2.3"`.
4. **Using `matches()` instead of `find()` in a loop**: You need `find()` in a `while` loop to collect all occurrences.
5. **Not returning an empty list for null**: Don't throw a NullPointerException — return `Collections.emptyList()`.

---

## Debugging Advice

- Test `"1.2.3.4"` explicitly — if it returns `["1.2.3"]`, your lookahead is missing
- Test `"1234.5.6"` — if it matches, your lookbehind is missing or incorrect
- Print each `matcher.group()` and `matcher.start()` to see exactly what the engine found
- Add components one at a time to your pattern and verify each change
