# ReDoS-Safe Log Processor

**Difficulty**: Hard
**Concept**: Production-grade ReDoS prevention with possessive quantifiers and atomic groups

---

## Problem Statement

Implement `processLogs(List<String> lines)` that parses a list of log lines in a complex
format and returns a list of `LogRecord` objects. Lines that don't match the format are
silently skipped.

### Log Format

```/dev/null/format.txt#L1-3
TIMESTAMP LEVEL ["optional quoted context"] MESSAGE

Examples:
2024-01-15 ERROR ["user-service"] Connection timeout
2024-01-15 INFO startup complete
2024-01-15 WARN ["db-pool"] Pool exhausted: 100/100 connections used
```

Fields:
- **TIMESTAMP**: date `YYYY-MM-DD` (fixed format)
- **LEVEL**: uppercase word (`ERROR`, `INFO`, `WARN`, `DEBUG`, `TRACE`)
- **CONTEXT** (optional): a double-quoted string `["anything here"]`
- **MESSAGE**: the rest of the line

---

## The Broken Pattern (DO NOT USE)

```/dev/null/broken.java#L1-8
// BROKEN_PATTERN — catastrophic on adversarial input:
// ^(\d{4}-\d{2}-\d{2}) ([A-Z]+) (\[\".*?\"\] )?(.+)$
//
// Why broken: (\[\".*?\"\] )? uses lazy .*? inside an optional group.
// On input like:  2024-01-15 ERROR ["aaaaaaaaaaaaaaaaaaa
//   (unclosed quote)
// The lazy .*? inside (\[\".*?\"\] )? will try many positions for the closing "
// before failing. While lazy is better than greedy here, the combination of
// optional group + backtracking + failure can still produce bad behavior
// on adversarial inputs. Use possessive [^"]* instead.
```

### The Safe Pattern

Replace `.*?` inside the quoted context with `[^"]*+` (non-quote chars, possessively):
```/dev/null/safe.txt#L1-2
(\["[^"]*+"\] )?
```
`[^"]*+` matches all non-quote chars possessively — it will NEVER backtrack to give
back characters, making the optional group deterministic.

---

## Constraints

- Input may be `null` → return empty list.
- Malformed lines (not matching format) → silently skip.
- `LogRecord` must contain: `timestamp`, `level`, `context` (may be null), `message`.
- Your pattern must be ReDoS-safe — a performance test validates < 500ms for 1000 lines.
- Use `"[^"]*+"` not `".*?"` for the quoted context.

---

## Input / Output Examples

| Input Line | timestamp | level | context | message |
|-----------|-----------|-------|---------|---------|
| `"2024-01-15 ERROR [\"user-svc\"] timeout"` | `"2024-01-15"` | `"ERROR"` | `"user-svc"` | `"timeout"` |
| `"2024-01-15 INFO startup"` | `"2024-01-15"` | `"INFO"` | `null` | `"startup"` |
| `"bad line"` | skipped | — | — | — |
| `"2024-01-15 WARN [\"\"] empty context"` | `"2024-01-15"` | `"WARN"` | `""` | `"empty context"` |

---

## Time Complexity

- Safe: O(N) per line.
- Adversarial test: 1000 lines processed in < 500ms total.

---

## Real-World Relevance

Production log processors in services like Splunk, Datadog, and Fluentd parse millions
of log lines per second. ReDoS in a log processor would degrade the entire observability
pipeline. This problem mirrors real incidents where log parsers caused cascading failures.

---

## Common Mistakes

- Using `.*?` for the quoted section — lazy `.*?` reduces backtracking but doesn't
  eliminate it on adversarial input like unclosed quotes.
- Not making the context group optional — many log lines have no context.
- Forgetting to strip the brackets `["..."]` from the context value — extract just the
  inner string.
- Returning null list instead of empty list.
