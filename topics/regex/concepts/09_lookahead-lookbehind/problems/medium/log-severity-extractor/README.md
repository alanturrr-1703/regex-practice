# Log Severity Extractor

**Difficulty**: Medium
**Concept**: Lookbehind with Java 8 variable-length workaround using capturing groups

---

## Problem Statement

Implement `extractWarningsAndErrors(String logText)` that processes a multi-line log
string and returns the **message text** for all lines whose severity is `WARN` or `ERROR`.

Log format per line:
```/dev/null/format.txt#L1-1
[SEVERITY] message text here
```

Examples:
```/dev/null/examples.txt#L1-4
[ERROR] disk full
[INFO] system ok
[WARN] low memory
[DEBUG] connection established
```

Expected output for the above: `["disk full", "low memory"]`

You must return only the message text — **not** the `[SEVERITY] ` prefix.

---

## Constraints

- Input is a multi-line string; lines are separated by `\n`.
- Only extract messages for severity `WARN` and `ERROR` (exact match, uppercase).
- Return messages in the order they appear (top to bottom).
- Return an empty list for null, empty, or no-matching-severity input.
- Trailing/leading whitespace in messages should be trimmed or handled naturally by
  the pattern.

---

## Input / Output Examples

| Input | Expected |
|-------|----------|
| `"[ERROR] disk full\n[INFO] system ok\n[WARN] low memory"` | `["disk full", "low memory"]` |
| `"[DEBUG] nothing"` | `[]` |
| `""` | `[]` |
| `null` | `[]` |
| `"[WARN] single"` | `["single"]` |
| `"[ERROR] a\n[ERROR] b\n[ERROR] c"` | `["a", "b", "c"]` |

---

## The Lookbehind Challenge

You might want to write:
```/dev/null/naive.txt#L1-2
(?<=\[WARN\] |\[ERROR\] ).*
```

But this is a **variable-length lookbehind** — `[WARN] ` is 7 chars, `[ERROR] ` is 8 chars.
This will throw `PatternSyntaxException` in Java 8-13.

### Two Solutions

**Solution A (Java 14+)**: bounded variable-length lookbehind works:
```/dev/null/solutionA.txt#L1-1
(?<=\[(?:WARN|ERROR)\] ).*
```
The lookbehind `\[(?:WARN|ERROR)\] ` can be 7 or 8 chars — allowed in Java 14+.

**Solution B (All Java versions)**: Use a **capturing group** instead:
```/dev/null/solutionB.txt#L1-2
\[(?:WARN|ERROR)\] (.+)
// Extract group(1) for the message
```
This is the idiomatic approach and preferred for portability.

The problem teaches you BOTH approaches. Implement Solution B (capturing group) for maximum
compatibility.

---

## Edge Cases

- Line with severity but empty message: `"[WARN] "` — consider trimming; test what your
  pattern returns.
- Line without bracket format: `"WARN: message"` — should not match.
- Multiple spaces after bracket: `"[ERROR]  double space"` — match depends on your pattern.
- Severity in message body: `"[INFO] received ERROR packet"` — only the INFO line, so no match.

---

## Time Complexity

O(N) where N is total length of the log string.

---

## Real-World Relevance

Log parsing is one of the most common uses of regex in production systems:
- Log aggregators (Splunk, Logstash, Fluentd) use patterns like this to extract fields.
- Alert systems scan for ERROR/WARN to trigger notifications.
- Audit pipelines extract messages for compliance reporting.

Understanding the lookbehind length restriction — and how to work around it — is essential
for writing portable Java regex.

---

## Regex Thinking Process

Step 1: Each matching line starts with `[WARN] ` or `[ERROR] `.
        Pattern for the prefix: `\[(?:WARN|ERROR)\] `

Step 2: I want everything after the prefix. Since prefix isn't included in match,
        use a capturing group: `\[(?:WARN|ERROR)\] (.+)`

Step 3: Apply with `MULTILINE` flag so `find()` scans line by line? Or split by `\n`?
        Easiest: use `Pattern.compile(pattern, Pattern.MULTILINE)` and let `.+` stop at `\n`
        (`.` doesn't match `\n` by default — perfect for line-by-line extraction).

Step 4: Collect `matcher.group(1)` for each match.

---

## Common Mistakes

- Attempting variable-length lookbehind on Java 8 → `PatternSyntaxException`.
- Using `.` with `DOTALL` flag — then `.+` will cross line boundaries.
- Matching `[WARN]` without escaping brackets: `[WARN]` means character class `W`, `A`,
  `R`, `N` — you need `\[WARN\]`.
- Forgetting the space after `]` — `\[WARN\](.+)` would include the space in group 1.
