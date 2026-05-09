# Identify Catastrophic Pattern

**Difficulty**: Easy
**Concept**: Detecting ReDoS-vulnerable regex patterns via adversarial testing

---

## Problem Statement

Implement `isSafePattern(String regex)` that returns `true` if the pattern is likely
safe (completes quickly on adversarial input), and `false` if it is potentially
catastrophically backtracking.

The detection strategy: run the pattern against a crafted adversarial input
`"aaaaaaaaaaaaaaaaaaaaaaaaaaa!"` (25 'a's followed by '!') inside a separate thread
with a **100ms timeout**. If the match doesn't complete in time → the pattern is unsafe.

---

## Constraints

- If `regex` is `null` or an invalid pattern (throws `PatternSyntaxException`) → return `false`.
- Use a timeout-based approach: run match in `ExecutorService`, call `future.get(100, MILLISECONDS)`.
- If `TimeoutException` → cancel the future, return `false`.
- The timeout (100ms) is a heuristic, not a proof of safety.

---

## Known Catastrophic Patterns (Expected: false)

| Pattern | Reason |
|---------|--------|
| `(a+)+` | Classic nested quantifier — O(2^N) |
| `(.+)+` | Same, with wildcard |
| `(a*)+` | Nested with zero-or-more |
| `([a-z]+)+` | Nested quantifier on character class |

## Known Safe Patterns (Expected: true)

| Pattern | Reason |
|---------|--------|
| `\d+` | Single quantifier, no nesting |
| `[a-z]+` | Single quantifier |
| `(?:a+)b` | `b` terminates the quantifier cleanly |
| `a++` | Possessive — no backtracking |

---

## Important Note on `(?:a+)b`

`(?:a+)b` is safe because: after `a+` consumes all 'a's, it tries to match 'b'. On input
`"aaaa!"`, the 'b' fails, then `a+` backs off one 'a' at a time — but this is linear, not
exponential, because there is only ONE quantifier. The catastrophic case requires **nested**
quantifiers (a quantifier inside a group that itself has a quantifier).

---

## Input / Output Examples

| regex | Expected | Reason |
|-------|----------|--------|
| `"(a+)+"` | `false` | Nested quantifier — catastrophic |
| `"\\d+"` | `true` | Safe — no nesting |
| `"(.+)+"` | `false` | Catastrophic |
| `"[a-z]+"` | `true` | Safe |
| `"(?:a+)b"` | `true` | Linear backtracking only |
| `null` | `false` | Null input |
| `"[invalid"` | `false` | Invalid pattern syntax |

---

## Time Complexity

The isSafePattern check itself runs in O(1) wall-clock time bounded by the timeout (100ms).
It may take up to 100ms for a catastrophic pattern to time out.

---

## Real-World Relevance

This is exactly the kind of check a security library or API gateway might perform before
accepting a user-provided regex. Tools like:
- OWASP ReDoS Checker
- `safe-regex` (npm)
- `vuln-regex-detector`

...use similar adversarial testing. Structural analysis (looking for nested quantifiers
on overlapping sets) is also used but harder to implement correctly.

---

## Regex Thinking Process

Step 1: Accept a pattern string. Try `Pattern.compile(regex)` to validate it.

Step 2: Build adversarial input: `"a".repeat(25) + "!"`. This forces the engine to try
        all partitions of the 'a' sequence before failing at '!'.

Step 3: Run in a separate thread with timeout. If it completes → safe. If it times out → unsafe.

Step 4: Clean up the thread regardless of outcome.

---

## Common Mistakes

- Not cancelling/shutting down the executor after timeout — this leaks threads.
- Using `Thread.sleep()` and `interrupt()` instead of `ExecutorService` — harder to manage.
- Setting timeout too low (1ms) — even safe patterns might time out on a slow machine.
- Setting timeout too high (5s) — this problem becomes slow to test.
