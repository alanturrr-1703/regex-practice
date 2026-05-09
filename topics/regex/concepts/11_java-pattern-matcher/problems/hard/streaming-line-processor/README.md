# Streaming Line Processor

**Difficulty**: Hard
**Concept**: Java Pattern & Matcher API — Multiple Patterns, reset(), Chaining
**Estimated Time**: 60–90 minutes

---

## Problem Statement

Process a list of log lines. For each line, apply three transformations in sequence:

1. **Mask email addresses** — replace any email with `[EMAIL]`
2. **Mask IPv4 addresses** — replace any IPv4 address with `[IP]`
3. **Timestamps** (`HH:MM:SS` format) are identified but **preserved as-is** in the output

Return the list of transformed lines.

**Why this is hard**: You must use multiple compiled `Pattern` objects, understand how to chain transformations correctly, and implement helper methods that the main `processLines` method composes.

---

## Method Signatures

```java
public List<String> processLines(List<String> lines)
String extractTimestamp(String line)   // helper: returns "HH:MM:SS" or null
String maskEmails(String line)         // helper: returns line with [EMAIL] replacements
String maskIPs(String line)            // helper: returns line with [IP] replacements
```

---

## Input / Output Examples

| Input Line | Output Line |
|---|---|
| `"09:30:00 user@example.com from 192.168.1.1"` | `"09:30:00 [EMAIL] from [IP]"` |
| `"no special content here"` | `"no special content here"` |
| `"contact admin@corp.org for help"` | `"contact [EMAIL] for help"` |
| `"server at 10.0.0.1 is down"` | `"server at [IP] is down"` |

---

## Constraints

- `lines` is never null (may be empty)
- Transformations are applied in order: emails first, then IPs
- Timestamps are NOT masked — they pass through unchanged
- A line may contain zero, one, or many emails/IPs
- Patterns should be compiled as `static final` fields (compile once, reuse)

---

## Pattern Definitions (Your Job to Implement)

You need to define patterns for:

| Entity | Pattern Hint |
|---|---|
| **Timestamp** (HH:MM:SS) | Two digits, colon, two digits, colon, two digits: `\d{2}:\d{2}:\d{2}` |
| **Email** | `[a-zA-Z0-9._%+\-]+@[a-zA-Z0-9.\-]+\.[a-zA-Z]{2,}` |
| **IPv4** | Four groups of 1-3 digits separated by dots: `\b\d{1,3}\.\d{1,3}\.\d{1,3}\.\d{1,3}\b` |

---

## Edge Cases

- Empty list → empty list
- Line with no special content → returned unchanged
- Line with multiple emails → all replaced with `[EMAIL]`
- Line with multiple IPs → all replaced with `[IP]`
- Line with both email and IP → both masked in one pass through `processLines`

---

## Time Complexity

- O(n × m) where n = total characters across all lines, m = pattern complexity
- Pattern compilation is O(1) amortized (done once at class load)

---

## Real-World Relevance

This is a production data pipeline pattern:
- **Log scrubbing**: remove PII from logs before storing
- **GDPR compliance**: mask personal data in transit
- **Security**: prevent credentials from appearing in log aggregation systems

---

## The Pattern Caching Pattern

```java
// CORRECT: compile once at class load, share across all threads
private static final Pattern EMAIL_PATTERN =
    Pattern.compile("[a-zA-Z0-9._%+\\-]+@[a-zA-Z0-9.\\-]+\\.[a-zA-Z]{2,}");

// In a method:
public String maskEmails(String line) {
    return EMAIL_PATTERN.matcher(line).replaceAll("[EMAIL]");
}
```

**Why static final**: `Pattern.compile()` is expensive. If called inside a hot loop (once per line), it wastes significant CPU. Static final fields are initialized exactly once.

---

## Matcher.reset() for Reuse

If you're processing thousands of lines and want to avoid creating a new `Matcher` object per line, use `matcher.reset(newInput)`:

```java
private final Matcher emailMatcher = EMAIL_PATTERN.matcher("");

public String maskEmails(String line) {
    // Reset to new input, same pattern — reuses the Matcher object
    return emailMatcher.reset(line).replaceAll("[EMAIL]");
}
```

Note: this only works safely in a single-threaded context. In multi-threaded use, create a new `Matcher` per call.

---

## Common Mistakes

1. **Compiling patterns inside `processLines` or helper methods** — this negates all performance benefits
2. **Applying transformations in wrong order** — if you mask IPs first, an IP inside a (hypothetical) email won't be found. Apply in a logical order: emails, then IPs.
3. **Using `replaceAll(String)` on String class** — `String.replaceAll` compiles the pattern every call. Use the Pattern's `matcher().replaceAll()`.
4. **Forgetting to handle empty list** — `if lines is empty, return empty list` — but this is handled automatically by iterating over an empty list.
5. **Not null-checking `extractTimestamp`** — it returns null when no timestamp is present.
