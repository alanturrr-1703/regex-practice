# Replace With Custom Logic

**Difficulty**: Medium
**Concept**: Java Pattern & Matcher API — `appendReplacement()` + `appendTail()`
**Estimated Time**: 30–40 minutes

---

## Problem Statement

Replace all numbers in a string by **doubling their value**.

- `"I have 3 cats and 10 dogs"` → `"I have 6 cats and 20 dogs"`
- `"100 and 200"` → `"200 and 400"`

This **cannot** be done with `replaceAll(String)` because the replacement string is not a fixed value — it depends on the matched number. You must use `Matcher.appendReplacement()` and `Matcher.appendTail()` to apply custom Java logic for each match.

---

## Method Signature

```java
public String doubleNumbers(String input)
```

---

## Input / Output Examples

| Input | Output |
|---|---|
| `"I have 3 cats"` | `"I have 6 cats"` |
| `"0 items"` | `"0 items"` |
| `"100 and 200"` | `"200 and 400"` |
| `"no numbers"` | `"no numbers"` |
| `"mixed 5 and 50"` | `"mixed 10 and 100"` |

---

## Constraints

- Input is never null
- Numbers are non-negative integers (no decimals, no negatives)
- Numbers fit in a Java `long` (no `BigInteger` needed)
- Non-numeric text passes through unchanged
- Empty string returns empty string

---

## Edge Cases

- `"0"` → `"0"` (0 doubled is 0)
- `"no numbers"` → `"no numbers"` (no substitution)
- `"1000000000"` → `"2000000000"` (large numbers)
- Numbers adjacent to punctuation: `"3cats"` → `"6cats"` (word boundary not required)
- Multiple consecutive numbers (rare, but the `find()` loop handles them naturally)

---

## Time Complexity

- O(n) for n = input length

---

## Real-World Relevance

`appendReplacement` / `appendTail` is used whenever per-match custom logic is needed:
- **Template engines**: substitute `{{variableName}}` with values from a map
- **SQL parameter binding**: replace `?` placeholders with escaped values
- **Text processors**: convert unit values (e.g., `"5 miles"` → `"8.0 km"`)
- **Code generators**: transform identifiers or literals in source code

---

## The appendReplacement/appendTail Pattern

```java
Pattern p = Pattern.compile("\\d+");
Matcher m = p.matcher(input);
StringBuffer sb = new StringBuffer();
while (m.find()) {
    // Compute replacement for THIS specific match
    long value = Long.parseLong(m.group());
    String replacement = String.valueOf(value * 2);
    // appendReplacement: appends the gap text + the replacement
    m.appendReplacement(sb, replacement);
}
// appendTail: appends everything after the last match
m.appendTail(sb);
return sb.toString();
```

**Key insight**: `appendReplacement` appends two things:
1. The text between the end of the previous match and the start of this match (the "gap")
2. The replacement string you provide

So you never need to manually track where in the input you are.

---

## Common Mistakes

1. **Forgetting `appendTail`** — without it, the text after the last match is lost
2. **Using `replaceAll(String)` with a lambda** — Java 9+ supports this, but you should learn the classic `appendReplacement` pattern first
3. **Not handling the case where there are no numbers** — `appendTail` after zero `find()` calls just appends the full input, which is correct
4. **Integer overflow** — use `Long.parseLong`, not `Integer.parseInt`, to be safe with large numbers
5. **`$` in replacement string** — if your replacement string contains `$` or `\`, it will be misinterpreted by `appendReplacement`. Use `Matcher.quoteReplacement()` if the replacement might contain these characters.

---

## Debugging Advice

Print inside the loop:
```java
System.out.println("Matched: '" + m.group() + "' → replacing with: '" + replacement + "'");
```
This confirms which numbers are being found and what they're being replaced with.
