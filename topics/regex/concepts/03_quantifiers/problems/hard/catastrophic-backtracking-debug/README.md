# Problem: Catastrophic Backtracking Debug

**Difficulty**: Hard  
**Concept**: Quantifiers  
**Skills Tested**: Understanding catastrophic backtracking, diagnosing exponential-time patterns, fixing ReDoS vulnerabilities, possessive quantifiers

---

## Problem Statement

You are given a broken regex implementation that uses the pattern `(a+)+b`. This pattern exhibits **catastrophic backtracking** — on inputs like `"aaaaaaaaaaaaaaac"` (many a's followed by something other than b), the engine explores an exponential number of backtracking paths, causing the program to hang.

Your task:
1. **Understand** WHY the pattern `(a+)+b` causes exponential backtracking
2. **Implement** `matchesPattern(String input)` which returns `true` if and only if the input is one or more `'a'` characters followed by exactly one `'b'` (and nothing else)
3. **Use a safe pattern** that does NOT exhibit catastrophic backtracking — it must complete in under 100ms even on adversarial inputs like 20+ `'a'`s followed by `'c'`

---

## Constraints

- `null` input → return `false`
- Empty string → return `false`
- The string must be EXACTLY: one or more `'a'`s followed by one `'b'` — nothing before, nothing after
- Your solution must NOT use the broken pattern `(a+)+b`
- Your solution must complete in < 100ms on `"aaaaaaaaaaaaaaaaac"` (20 a's + c)
- Use `Pattern.matches()` or `pattern.matcher(input).matches()` (full-string match)

---

## Input / Output Examples

| Input | Output | Reason |
|---|---|---|
| `"ab"` | `true` | One `a`, one `b` |
| `"aaab"` | `true` | Three `a`s, one `b` |
| `"b"` | `false` | No `a` before `b` |
| `"aaa"` | `false` | No `b` at end |
| `""` | `false` | Empty |
| `"aab"` | `true` | Two `a`s, one `b` |
| `"aabb"` | `false` | Two `b`s |
| `"cab"` | `false` | Starts with `c` |

---

## Edge Cases

- `"aaaaaaaaaaaaaaac"` → `false` (AND must return in < 100ms)
- `"aaaaaaaaaaaaaaab"` → `true`
- `null` → `false`
- `"b"` → `false` (no `a` required before `b`)
- `"ba"` → `false` (wrong order)

---

## Expected Time Complexity

O(n) — the safe pattern should process the string in linear time with no backtracking.

---

## Real-World Relevance

ReDoS (Regular Expression Denial of Service) is a real vulnerability class:
- **CVE-2019-20149**: `kind-of` npm package — catastrophic backtracking DoS
- **CVE-2021-23343**: `path-parse` npm package — ReDoS via malformed paths
- **Cloudflare outage (July 2019)**: A single regex with catastrophic backtracking caused a global CDN outage for 27 minutes
- **OWASP** lists ReDoS as a significant web application security risk

Production systems must audit all regexes that process user-supplied input for backtracking risks. This problem teaches you to recognize and fix the pattern.

---

## Why `(a+)+b` is Catastrophic — The Analysis

The outer `+` quantifier iterates over groups. The inner `a+` can match 1 or more `a`s per group. For an input of `n` `a`s:

The engine tries to partition `n` `a`s into groups, where each group has 1+ `a`s:
```
n=4: (a)(a)(a)(a), (aa)(a)(a), (a)(aa)(a), (a)(a)(aa),
     (aaa)(a), (a)(aaa), (aa)(aa), (aaaa) = 2^3 = 8 ways
n=20: 2^19 = 524,288 ways
```

Every partition is explored when the final `b` match fails. This is **O(2^n)** complexity.

---

## The Safe Alternatives

**Option 1: Simplify the pattern**
`(a+)+` is equivalent to `a+` for matching purposes. Remove the redundant group: use `a+b`.

**Option 2: Use possessive quantifier**
`(a++)b` or `a++b` — the possessive `++` never gives back matched `a`s, preventing the exponential exploration.

**Option 3: Use atomic group**
`(?>a+)b` — once the atomic group commits to matching all `a`s, it never releases them.

For this problem, use the simplest correct approach: `^a+b$` with `matches()`.

---

## Common Mistakes

1. **Copying the broken pattern**: The comment in the code is there to explain the problem, not to use.
2. **Using `find()` instead of `matches()`**: This would match `"ab"` inside `"xabc"`. The problem says the string MUST be exactly `a+b`.
3. **Not testing the timeout**: Implement the method, then run the timeout test to verify it's actually fast.
4. **Over-engineering the fix**: The simplest fix (`a+b` with `matches()`) is the best fix.

---

## Debugging Advice

To see catastrophic backtracking in action (on the BROKEN pattern):
- Uncomment the broken implementation
- Run the timeout test with a small delay limit (e.g., 10ms)
- Increase the number of `a`s from 5 to 15 and watch the time grow exponentially
- This is the "evil input" technique used by security researchers to test for ReDoS

To verify your fix is safe:
- Run the timeout test — it must pass reliably
- Test with 30+ `a`s followed by `c` to be sure there's no subtle backtracking
