# Problem: Log Line Anchor Parser

**Difficulty**: Hard  
**Concept**: Anchors  
**Skills Tested**: `^`/`$` with `Pattern.MULTILINE`, strict format enforcement via anchors, capturing groups for structured extraction, `LogEntry` object construction

---

## Problem Statement

Given a multiline log string, parse all valid log lines and return them as a `List<LogEntry>`. Lines that do not match the expected format must be **silently ignored**.

**Valid log line format** (exact):
```
[LEVEL] YYYY-MM-DD HH:MM:SS - Message text
```

Where:
- `LEVEL` is one of: `DEBUG`, `INFO`, `WARN`, `ERROR` (uppercase, inside square brackets)
- Date is `YYYY-MM-DD` (4-digit year, 2-digit month, 2-digit day)
- Time is `HH:MM:SS` (2-digit hour, 2-digit minute, 2-digit second)
- Separator is ` - ` (space-dash-space)
- Message is any non-empty text to the end of the line

---

## Constraints

- Input may be `null` — return an empty list
- Input may have no valid lines — return an empty list
- Each valid line contributes exactly one `LogEntry`
- Malformed lines (wrong level, wrong date format, missing parts) are ignored
- Order of results matches order of lines in input

---

## Input / Output Examples

Single valid line:
```
[INFO] 2024-01-15 10:30:45 - Server started
```
Returns: `[LogEntry{level="INFO", date="2024-01-15", time="10:30:45", message="Server started"}]`

Mixed valid and invalid:
```
[INFO] 2024-01-15 10:30:45 - Server started
this is not a log line
[WARN] 2024-01-15 11:00:00 - High memory usage
```
Returns two `LogEntry` objects (the middle line is skipped).

Invalid level:
```
[TRACE] 2024-01-15 10:00:00 - Not a valid level
```
Returns empty list (TRACE is not a recognized level).

---

## Edge Cases

- Partial match (e.g., `" [INFO] ..."` with leading space) → NOT matched (the `^` requires line start)
- Extra content after the message → the message `.+` greedily consumes everything to line end
- Empty message (`"[INFO] 2024-01-15 10:00:00 - "`) → not matched since `.+` requires at least 1 char
- `null` → empty list
- Empty string → empty list
- All malformed lines → empty list

---

## Expected Time Complexity

O(n) — single MULTILINE `find()` pass through the log text.

---

## Real-World Relevance

Structured log parsing is a core task in:
- **Log aggregation systems** (Logstash, Fluentd, Filebeat) — extract fields from raw text
- **Monitoring dashboards** — parse error rates by log level
- **Security auditing** — detect anomalous log patterns
- **CI/CD pipelines** — parse build logs for ERROR entries
- **Microservice observability** — correlate log lines across services

---

## Regex Thinking Process

**Step 1**: The pattern must match exactly one log line. The line starts with `[`, then a level, then `]`. Use `\[` and `\]` (bracket escaping) with alternation: `\[(DEBUG|INFO|WARN|ERROR)\]`.

**Step 2**: Date part: `\d{4}-\d{2}-\d{2}` — 4 digits, hyphen, 2 digits, hyphen, 2 digits.

**Step 3**: Time part: `\d{2}:\d{2}:\d{2}` — 2 digits, colon, 2 digits, colon, 2 digits.

**Step 4**: Separator: ` - ` (space-dash-space, literal).

**Step 5**: Message: `.+` — one or more of any character (but NOT newline in default mode), greedily consumes to end of line.

**Step 6**: The `^` at start (with MULTILINE) ensures each line is anchored. The `$` at end ensures the entire line is consumed — no partial matches.

**Full pattern**: `^\[(DEBUG|INFO|WARN|ERROR)\] (\d{4}-\d{2}-\d{2}) (\d{2}:\d{2}:\d{2}) - (.+)$`

Compiled with: `Pattern.MULTILINE`

**Groups**: 1 = level, 2 = date, 3 = time, 4 = message

---

## Common Mistakes

1. **Forgetting MULTILINE**: Without it, `^` only matches position 0 and `$` only matches the end — you'd only ever parse the first line.
2. **Not escaping `[` and `]`**: `[DEBUG]` without escaping is a character class! Use `\[DEBUG\]`.
3. **Using `.+` without MULTILINE limitation**: `.` does not match newlines by default, so `.+` matches exactly one line. That's correct here — don't add DOTALL.
4. **Not using `$` at end**: Without `$`, a malformed line like `[INFO] garbage extra` could partially match.
5. **Using numbered groups incorrectly**: Group 1 is level, 2 is date, 3 is time, 4 is message. Don't mix them up.

---

## Debugging Advice

- Test with a single valid line first — does `parse` return one `LogEntry`?
- Test a line with a space before `[` — it should be ignored (^ requires line start)
- Test `[TRACE] ...` — should be ignored (only DEBUG/INFO/WARN/ERROR are valid)
- Print `matcher.group(1)`, `matcher.group(2)`, etc. to verify group extraction
- If ALL lines are being skipped, check that `Pattern.MULTILINE` is set
