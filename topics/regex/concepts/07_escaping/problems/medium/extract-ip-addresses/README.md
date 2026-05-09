# Extract IP Addresses

**Difficulty:** Medium
**Concepts Tested:** Escaping `\\.` in multi-part patterns, boundary assertions, `{n,m}` quantifier
**Concept:** escaping

---

## Problem Statement

Extract all IPv4 addresses from a string. An IPv4 address has the form:
`N.N.N.N` where each `N` is a sequence of 1–3 digits.

Your pattern must correctly escape the dots between octets as **literal dots** (`\\.` in Java). If you use unescaped `.`, it will match any character between digit groups (e.g., "1X2Y3Z4" would incorrectly match).

Additionally, your pattern must not match partial addresses:
- `"1.2.3.4.5"` should NOT produce a match (5 octet groups, too many)
- `"1.2.3"` should NOT match (only 3 groups)

Use word boundaries or negative lookahead/lookbehind to enforce these constraints.

**Note:** The regex does NOT validate that each octet is 0–255. That is a semantic check, not a structural one. "999.999.999.999" will match — that is acceptable for this problem.

---

## Constraints

- Input string is never `null`, may be empty → return empty list
- Addresses are structurally validated (4 groups of 1-3 digits, separated by literal dots)
- Numeric range (0-255) is NOT validated
- Adjacent/overlapping addresses in input text should each be found

---

## Examples

### Example 1
**Input:** `"server at 192.168.1.1 and 10.0.0.1"`
**Output:** `["192.168.1.1", "10.0.0.1"]`

### Example 2
**Input:** `"no ips here"`
**Output:** `[]`

### Example 3
**Input:** `"127.0.0.1"`
**Output:** `["127.0.0.1"]`

### Example 4
**Input:** `"1.2.3.4 and 255.255.255.0"`
**Output:** `["1.2.3.4", "255.255.255.0"]`

---

## Edge Cases

- `"1.2.3.4.5"` — five groups. Expected: `[]` (the presence of a 5th dot-digit after the 4th octet disqualifies the match using lookahead/lookbehind)
- `"1.2.3"` — only three groups → not matched
- `"a1.2.3.4b"` — letters adjacent to the address. With word boundaries, this may or may not match. Define your behavior: use `(?<!\d)` and `(?!\d)` to ensure no digit immediately before/after the address
- `"0.0.0.0"` → matched (structurally valid)
- `"999.999.999.999"` → matched (out of range numerically but pattern doesn't check range)

---

## Expected Time Complexity

O(n) — linear scan through the string

---

## Real-World Relevance

- Log file analysis: extracting client IPs from web server logs
- Network monitoring: finding IPs mentioned in alerts
- Security tools: scanning documents for IP address leakage
- Config file parsing: extracting server addresses

---

## Regex Thinking Process

**Step 1:** Match one octet: `\d{1,3}` (1 to 3 digits)

**Step 2:** Match a literal dot between octets: `\.` (regex) → `\\.` (Java string)

**Step 3:** Four octets joined by three dots:
- Regex: `\d{1,3}\.\d{1,3}\.\d{1,3}\.\d{1,3}`
- Java string: `"\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}"`

**Step 4:** Add boundaries so "1.2.3.4.5" doesn't match:
- Use `(?<!\d)` before and `(?!\.)` plus `(?!\d)` after
- Or use `\b` — but `\b` is between `\w` and non-`\w`, and digits are `\w`, so `\b` works at the edges

**Step 5:** Assemble and test with edge cases.

---

## Common Mistakes

1. **Unescaped dot** — `"\d{1,3}.\d{1,3}..."` matches "1X2Y3Z4" because `.` is any char
2. **No boundary check** — `"1.2.3.4.5"` matches the first four groups
3. **Using `\b` between digits and `.`** — `\b` only asserts between `\w` and `\W`. The dot is `\W`, so `\b` between digit and dot works. But `\b` at the very end after the last digit is what prevents `"1.2.3.4.5"` — test this carefully

---

## Debugging Advice

Test your pattern against these specific inputs and check each:
- `"192.168.1.1"` → should match
- `"1.2.3.4.5"` → should NOT match (or should only match `"1.2.3.4"` with proper boundary)
- `"1X2X3X4"` → should NOT match (no dots)

If "1.2.3.4.5" matches, your boundary condition on the right side is too weak. Add `(?!\\.\\d)` (not followed by dot+digit) after the last octet group.
