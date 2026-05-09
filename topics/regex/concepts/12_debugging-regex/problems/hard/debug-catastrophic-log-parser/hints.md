# Hints — Debug Catastrophic Log Parser

---

## Hint 1

The broken pattern `^((\w+\s*)+):\s*(.*?)$` has a catastrophically backtracking label portion: `(\w+\s*)+`.

The root cause: `\w+\s*` can consume `"word "` in two ways:
- `\w+` matches `"word"`, `\s*` matches `" "` (consuming the space)
- `\w+` matches `"word"`, `\s*` matches `""` (leaving the space)

When the engine needs to fail (no `:` found), it must try BOTH paths for every word, leading to 2^n attempts for n words.

---

## Hint 2

The simplest fix: replace `(\w+\s*)+` with `[^:]+`.

`[^:]+` = "one or more characters that are NOT a colon". This is inherently linear — the engine just walks forward until it sees `:` or runs out of characters. No backtracking is possible because `[^:]` and `:` don't overlap.

```java
Pattern.compile("^([^:]+):\\s*(.*)$")
```

---

## Hint 3

With the safe pattern `^([^:]+):\s*(.*)$`:
- group(1) = the label (everything before `:`)
- group(2) = the message (everything after `: ` and optional spaces)

The loop:
```java
while (m.find()) {
    String label = m.group(1).trim(); // trim in case label has trailing spaces
    String message = m.group(2);
    entries.add(new LogEntry(label, message));
}
```

Or if you use `matches()` per line (since the pattern anchors with `^` and `$`):
```java
if (m.matches()) { ... }
```

---

## Hint 4

Malformed lines (no `:`) should be silently skipped. With `find()` or `matches()`, if the line doesn't match, just don't add anything to the result list.

For the performance test: the adversarial input `"word word word word...!"` has no `:`, so the safe pattern simply fails to match and you skip it — taking O(n) time, not O(2^n).

---

## Hint 5 (Reveal)

```java
private static final Pattern LOG_PATTERN =
    Pattern.compile("^([^:]+):\\s*(.*)$");

public List<LogEntry> parseLogs(List<String> lines) {
    List<LogEntry> entries = new ArrayList<>();
    for (String line : lines) {
        Matcher m = LOG_PATTERN.matcher(line);
        if (m.matches()) {
            entries.add(new LogEntry(m.group(1).trim(), m.group(2)));
        }
        // malformed lines are silently skipped
    }
    return entries;
}
```

Note: `m.matches()` works here because the pattern is anchored with `^` and `$` and we're applying it to one line at a time. Alternatively, use `m.find()` — both work since the pattern spans the entire line.
