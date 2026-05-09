# Debug Catastrophic Log Parser

**Difficulty**: Hard — Debugging
**Concept**: ReDoS — catastrophic backtracking; safe pattern rewriting
**Estimated Time**: 60–90 minutes

---

## Problem Statement

A production log parser is **hanging** on certain inputs. The broken parser uses a pattern that causes catastrophic backtracking — exponential NFA execution time when the expected `:` separator is absent.

**The broken pattern**:
```java
// BROKEN — catastrophic on adversarial input
Pattern.compile("^((\\w+\\s*)+):\\s*(.*?)$")
```

**The adversarial input that hangs it**:
```
"word word word word word word word word word word word word!"
```
(12 words followed by `!`, no `:`)

**Your tasks**:
1. Read and understand WHY the broken pattern is catastrophic (explained below)
2. Rewrite the pattern to be ReDoS-safe
3. Implement `parseLogs(List<String>)` using the safe pattern

---

## Why the Broken Pattern Is Catastrophic

The label part of the broken pattern is `(\w+\s*)+`:

```
Outer: (      )+   — one or more iterations
Inner: (\w+\s*)    — one or more word chars, then zero or more spaces
```

For the string `"word1 word2"`:
- `\w+` can match `"word1"`, `\s*` matches `" "`, that's one iteration
- `\w+` can match `"word1 word2"` (wait, `\w` doesn't match spaces... actually `\w+` is `[A-Za-z0-9_]+`)

Actually: `\w+\s*` can match `"word1 "` or `"word1"` (with empty `\s*`). Then the next iteration handles the rest.

For `"word1 word2"`:
- One iteration: `\w+` = `"word1"`, `\s*` = `" "`, then second iteration: `\w+` = `"word2"`, `\s*` = `""`
- One iteration: `\w+` = `"word1"`, `\s*` = `""` (space NOT consumed), then second: fails because `\w+` needs `\w` but sees `" "` — no, wait, the space IS a non-word character so the second `\w+` would fail here and backtrack...

The critical issue: `(\w+\s*)` can match `"word1 "` as `\w+="word1", \s*=" "` OR as `\w+="word1", \s*=""` leaving the space for the next iteration to try. This creates multiple valid decompositions of the SAME input. The engine tries all possible decompositions before concluding failure when no `:` is found.

**Result**: O(2^n) work for n words before the unmatched character at the end.

---

## Method Signature

```java
public List<LogEntry> parseLogs(List<String> lines)
```

`LogEntry` is an inner class with fields `label` (String) and `message` (String).

---

## Input / Output Examples

| Input Line | LogEntry |
|---|---|
| `"ERROR: disk full"` | `label="ERROR", message="disk full"` |
| `"INFO: user logged in"` | `label="INFO", message="user logged in"` |
| `"word word!"` | *(skipped — no colon)* |
| `""` | *(skipped — empty)* |

---

## Constraints

- `lines` is never null (may be empty)
- Valid lines match `label: message` where label is non-empty text before `:` and message is text after `: ` (with optional leading whitespace)
- Malformed lines (no `:`, empty label) are silently skipped
- The safe pattern must complete in well under 200ms even on adversarial inputs

---

## Safe Pattern Strategies

### Strategy 1: Negated Character Class (Simplest)

```java
Pattern.compile("^([^:]+):\\s*(.*)$")
```

`[^:]+` = "one or more characters that are NOT `:`". Linear time — no backtracking possible because `[^:]` and `:` are mutually exclusive.

### Strategy 2: Non-Overlapping Structure

```java
Pattern.compile("^(\\w+(?:\\s+\\w+)*):\\s*(.*)$")
```

`\w+(?:\s+\w+)*` = "a word, optionally followed by (spaces + word) repetitions". Here `\s+` (one or more spaces) and `\w+` (word chars) cannot overlap, so decomposition is unique — linear time.

### Strategy 3: Possessive Quantifiers (Java supports these)

```java
Pattern.compile("^((\\w++\\s*+)++):\\s*(.*)$")
```

`\w++` and `\s*+` use possessive quantifiers — they never backtrack. Once a character is consumed, it's committed. Safe but less readable.

**Recommendation**: Use Strategy 1 (`[^:]+`) — it's the simplest, most readable, and most performant.

---

## Time Complexity

- Safe pattern: O(n) per line, where n = line length
- Broken pattern: O(n) best case, O(2^n) worst case on adversarial input

---

## Real-World Relevance

ReDoS (Regular Expression Denial of Service) is a real attack:
- Cloudflare's global outage in 2019 was caused by a catastrophic regex in a WAF rule
- Many WAF (Web Application Firewall) vendors have had ReDoS vulnerabilities
- OWASP lists ReDoS as a vulnerability category
- Whenever you write regex for user-supplied data, you MUST verify it's not catastrophic

---

## Performance Test

Your implementation must pass:
```java
@Test
void testAdversarialInputCompletesQuickly() {
    // 12 words followed by "!" — no colon — would hang the broken pattern
    String adversarial = "word word word word word word word word word word word word!";
    assertTimeout(Duration.ofMillis(200), () -> {
        List<LogEntry> result = solution.parseLogs(List.of(adversarial));
        assertTrue(result.isEmpty()); // malformed line is skipped
    });
}
```

---

## Common Mistakes

1. **Using the broken pattern** — always choose the safe rewrite
2. **Not skipping malformed lines** — lines without `:` must be skipped, not throw exceptions
3. **Using group(0) instead of group(1)** — label is in group(1), message in group(2)
4. **Empty label edge case** — `": message"` has an empty label; decide whether to skip it (this problem skips it because `[^:]+` requires at least one character)
