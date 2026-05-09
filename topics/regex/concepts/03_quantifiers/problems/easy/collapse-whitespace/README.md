# Problem: Collapse Whitespace

**Difficulty**: Easy  
**Concept**: Quantifiers  
**Skills Tested**: `\s+` quantifier, `replaceAll()`, trim behavior with regex

---

## Problem Statement

Given a string, normalize its whitespace by:
1. Collapsing any sequence of one or more whitespace characters into a single space `' '`
2. Trimming any leading or trailing whitespace

Return the normalized string.

Whitespace includes spaces, tabs (`\t`), newlines (`\n`), carriage returns (`\r`), and other Unicode whitespace characters matched by `\s`.

---

## Constraints

- Input may be `null` — return `null` for null input (or treat as empty, your choice — document it)
- Input may be empty — return `""` 
- Input may be all whitespace — return `""`
- Multiple types of whitespace may appear together (e.g., `"\t \n"` is three whitespace chars)
- The result should never start or end with a space
- The result should never contain two consecutive spaces

---

## Input / Output Examples

| Input | Output | Reason |
|---|---|---|
| `"hello   world"` | `"hello world"` | Three spaces collapsed to one |
| `"  spaces  "` | `"spaces"` | Leading and trailing spaces trimmed |
| `"a\t\tb"` | `"a b"` | Two tabs collapsed to one space |
| `""` | `""` | Empty string stays empty |
| `"no change"` | `"no change"` | Single spaces, nothing to do |
| `"a\n\nb"` | `"a b"` | Two newlines → single space |
| `"  "` | `""` | All whitespace → empty |
| `"a  b  c"` | `"a b c"` | Multiple runs each collapsed |

---

## Edge Cases

- `"\t"` (single tab) → `""` (trimmed away)
- `"a"` → `"a"` (unchanged)
- `"a b"` (already normalized) → `"a b"`
- Mixed: `"  hello\t\tworld\n"` → `"hello world"`
- Multiple words: `"one  two   three    four"` → `"one two three four"`

---

## Expected Time Complexity

O(n) — `replaceAll` with `\s+` makes a single pass through the string.

---

## Real-World Relevance

Whitespace normalization is one of the most common string-processing tasks in production:
- **Search indexing**: normalize user query terms before matching
- **CSV/TSV parsing**: fields may have inconsistent spacing
- **Configuration file loading**: values may have leading/trailing whitespace
- **HTML attribute parsing**: multiple spaces between words are valid HTML
- **Natural language processing**: tokenization always normalizes whitespace first

---

## Regex Thinking Process

**Step 1**: `\s` matches any single whitespace character. To match ONE OR MORE: `\s+`.

**Step 2**: `replaceAll("\\s+", " ")` replaces every run of whitespace with a single space. After this, the string has no multi-space runs. But it may still start/end with a single space.

**Step 3**: Trim the result. In Java: `String.trim()` removes leading/trailing ASCII whitespace. Or chain another `replaceAll`: `replaceAll("^\\s+|\\s+$", "")`.

**Step 4**: Combine: `input.replaceAll("\\s+", " ").trim()`

**Order matters**: Replace first, then trim. If you trim first, you might split on internal runs. If you replace first and then trim, everything flows naturally.

---

## Common Mistakes

1. **Using `" +"` instead of `\s+`**: A literal space `" "` only matches space characters, not tabs, newlines, etc.
2. **Forgetting to trim**: After collapsing, `"  hello"` becomes `" hello"` (one leading space). Still wrong.
3. **Trimming before replacing**: This doesn't cause bugs, but it's logically asymmetric. Replace then trim is cleaner.
4. **Using `split()` and `join()`**: Works but is slower and more verbose than a single `replaceAll`. The regex approach is idiomatic.
5. **`String.trim()` vs `String.strip()`**: In Java 11+, `strip()` handles Unicode whitespace. `trim()` only handles ASCII whitespace (`<= \u0020`). `\s` in regex matches Unicode whitespace, so `strip()` is the more consistent pair — but for this problem, either works on typical input.

---

## Debugging Advice

Test edge cases explicitly:
- What does your method return for `"  "` (only spaces)?
- Does `"\t\n\r"` (mixed whitespace) normalize correctly?
- Run `System.out.println("[" + result + "]")` to see if leading/trailing spaces sneak in
