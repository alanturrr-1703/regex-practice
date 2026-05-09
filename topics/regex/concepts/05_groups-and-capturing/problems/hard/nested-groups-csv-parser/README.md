# Nested Groups CSV Parser

**Difficulty:** Hard  
**Concept:** Groups and Capturing  
**Estimated Time:** 45–75 minutes

---

## Concepts Tested

- Alternation within capturing groups: `("([^"]*)"|(...))`
- Non-capturing groups `(?:...)` for structure without overhead
- Group ordering complexity (which group number maps to which field)
- Unmatched groups returning `null` (alternation where only one branch fires)
- Iterative field extraction with `find()` on a delimiter-separated line

---

## Problem Statement

Parse a **single CSV line** where fields may optionally be quoted. Quoted fields may contain commas. Fields may be empty. Return a `List<String>` of field values, with surrounding quotes removed.

### CSV rules for this problem:
1. Fields are separated by commas `,`
2. A field may be enclosed in double quotes `"..."` 
3. A quoted field may contain commas (they are not field separators)
4. Quoted fields may be empty `""`
5. Unquoted fields cannot contain commas
6. There are no escaped quotes (`""` inside quotes) for this problem

---

## Method Signature

```java
public List<String> parseCsvLine(String line)
```

---

## Input / Output Examples

| Input | Output |
|---|---|
| `one,"two, with comma","three",four` | `["one", "two, with comma", "three", "four"]` |
| `"quoted","",plain` | `["quoted", "", "plain"]` |
| `a,b,c` | `["a", "b", "c"]` |
| `"only one"` | `["only one"]` |
| `a,,b` | `["a", "", "b"]` |
| `"has, comma","normal"` | `["has, comma", "normal"]` |

---

## Constraints

- Input is never `null`
- Fields are separated by exactly one comma (no leading/trailing whitespace in unquoted fields unless actually present)
- No escaped quotes within quoted fields
- An empty unquoted field is an empty string between two commas (or at start/end)

---

## Edge Cases

- **Empty quoted field** `""`: should return `""` (empty string, not `null`)
- **Empty unquoted field** (two consecutive commas `,,`): should return `""` (empty string)
- **Single field, no comma**: returns a one-element list
- **All fields quoted**: all quotes stripped

---

## Time Complexity

- **O(n)** — single pass through the line

---

## Real-World Relevance

CSV parsing is ubiquitous. Real CSV libraries (OpenCSV, Apache Commons CSV) handle many more edge cases (escaped quotes, multiline fields, BOM), but the core challenge of "quoted field may contain the delimiter" is exactly this problem. Understanding the regex approach teaches the limits of regex for recursive/nested structures.

---

## Regex Thinking Process

The insight: each field is either:
- A **quoted field**: starts with `"`, contains anything except `"`, ends with `"`
- An **unquoted field**: anything except `,` and `"`

Pattern for one field (without the separator):
```
"([^"]*)"       ← quoted: group captures content without quotes
|               ← OR
([^,"]*)        ← unquoted: group captures content (no comma, no quote)
```

**Complication:** This is an alternation with two different capturing groups. Only one branch fires per match, so the other group returns `null`. You must check which group matched:

```java
String field = matcher.group(1) != null ? matcher.group(1) : matcher.group(2);
```

Where:
- `group(1)` = content of a quoted field (or `null` if unquoted branch fired)
- `group(2)` = content of an unquoted field (or `null` if quoted branch fired)

**Full field pattern with separator:**
```
"([^"]*)"   matches a quoted field, captures content in group 1
|([^,"]*)   matches an unquoted field (possibly empty), captures in group 2
```

Then use `find()` in a loop. Each match is one field.

**Why this is hard:**  
After the last field, if the input ends with `,`, there's an implied empty field. The empty unquoted pattern `([^,"]*)` matches empty strings, which can cause infinite loops if not handled with care.

---

## Common Mistakes

| Mistake | Why It's Wrong |
|---|---|
| Using `split(",")` directly | Splits on commas inside quoted fields |
| Checking only `group(1)` | If the unquoted branch fires, `group(1)` is `null` |
| Forgetting empty unquoted fields | `a,,b` must yield `["a","","b"]` |
| Infinite loop on empty matches | If the pattern matches empty string, `find()` can loop at the same position |
| Using `group(2)` for quoted content | `group(2)` is the unquoted branch; quoted content is `group(1)` |

---

## Debugging Advice

Print all groups on every find() call:
```java
while (matcher.find()) {
    System.out.println("Match: [" + matcher.group(0) + "]");
    System.out.println("  g1 (quoted):   " + matcher.group(1));
    System.out.println("  g2 (unquoted): " + matcher.group(2));
    System.out.println("  position: " + matcher.start() + "-" + matcher.end());
}
```

This reveals which branch is firing for each field.
