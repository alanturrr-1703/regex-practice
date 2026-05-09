# Hints — Named Groups Log Parser

Work through these one at a time. Stop as soon as you feel unblocked.

---

## Hint 1 — Named group syntax

Instead of `(\d+)` (numbered), use `(?<name>pattern)` (named):

```java
// Numbered: access via group(1)
Pattern.compile("(\\d{3})");

// Named: access via group("status")
Pattern.compile("(?<status>\\d{3})");
```

Both work identically in the NFA — the name is just an alias for the slot number.  
Access the value with `matcher.group("status")`.

---

## Hint 2 — Building the pattern field by field

Compose the pattern segment by segment. Each piece, with Java escaping:

```
IP:         (?<ip>\S+)
space-dash: \s+-\s+-\s+
Date:       \[(?<date>[^\]]+)\]
space:      \s+
Request:    "(?<method>\w+)\s+(?<path>[^\s"]+)\s+HTTP/[\d.]+"
space:      \s+
Status:     (?<status>\d{3})
space:      \s+
Bytes:      (?<bytes>\S+)
```

In Java string form: `"(?<ip>\\S+)\\s+-\\s+-\\s+\\[(?<date>[^\\]]+)\\]\\s+\"(?<method>\\w+)\\s+(?<path>[^\\s\"]+)\\s+HTTP/[\\d.]+\"\\s+(?<status>\\d{3})\\s+(?<bytes>\\S+)"`

---

## Hint 3 — `matches()` vs `find()`

Since a log line should match the pattern from start to finish, use `matcher.matches()` (or anchor with `^...$` with `find()`). Using `find()` without anchors would match a sub-portion and give wrong results.

---

## Hint 4 — Returning Optional

```java
Matcher m = PATTERN.matcher(line);
if (m.matches()) {
    return Optional.of(new LogEntry(
        m.group("ip"),
        m.group("date"),
        m.group("method"),
        m.group("path"),
        m.group("status"),
        m.group("bytes")
    ));
}
return Optional.empty();
```

---

## Hint 5 — The bracket escape pitfall

In Java regex, `[` starts a character class. To match a literal `[`, escape it: `\\[`.  
Similarly, `]` ends a character class. To match a literal `]`, use `\\]`.

For "anything except `]`", use the character class `[^\\]]` in Java, which is `[^\]]` in regex.

The date portion: `\\[(?<date>[^\\]]+)\\]`
