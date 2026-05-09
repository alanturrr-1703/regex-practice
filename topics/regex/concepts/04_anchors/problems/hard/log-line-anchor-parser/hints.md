# Hints: Log Line Anchor Parser

---

## Hint 1 (Gentle)

Start by identifying the components of the format: `[LEVEL] YYYY-MM-DD HH:MM:SS - Message`

Each component maps to a regex sub-pattern:
- `[LEVEL]` → brackets around one of DEBUG/INFO/WARN/ERROR
- `YYYY-MM-DD` → four digits, hyphen, two digits, hyphen, two digits
- `HH:MM:SS` → two digits, colon, two digits, colon, two digits
- ` - ` → literal space-dash-space
- `Message` → any non-empty text

What character do you need to escape for `[` and `]` in regex?

---

## Hint 2

Square brackets `[` and `]` are special in regex — they start/end character classes. To match a literal bracket, you must escape it: `\[` and `\]`.

So `[INFO]` in regex is `\[INFO\]`. The level alternation: `\[(DEBUG|INFO|WARN|ERROR)\]`.

The date: `\d{4}-\d{2}-\d{2}`. The time: `\d{2}:\d{2}:\d{2}`.

Put it together:
`\[(DEBUG|INFO|WARN|ERROR)\] \d{4}-\d{2}-\d{2} \d{2}:\d{2}:\d{2} - .+`

---

## Hint 3

This pattern needs to match entire lines, not partial content. Add anchors:
- `^` at the start (with `Pattern.MULTILINE`) — matches line start
- `$` at the end (with `Pattern.MULTILINE`) — matches line end

`^\[(DEBUG|INFO|WARN|ERROR)\] \d{4}-\d{2}-\d{2} \d{2}:\d{2}:\d{2} - .+$`

Compile with: `Pattern.compile(pattern, Pattern.MULTILINE)`

Without MULTILINE, `^` only matches position 0 and you'd only ever parse the first line.

---

## Hint 4

Add capturing groups around the parts you want to extract:
```
^\[(DEBUG|INFO|WARN|ERROR)\] (\d{4}-\d{2}-\d{2}) (\d{2}:\d{2}:\d{2}) - (.+)$
```

- Group 1: level (`matcher.group(1)`)
- Group 2: date (`matcher.group(2)`)
- Group 3: time (`matcher.group(3)`)
- Group 4: message (`matcher.group(4)`)

Use a `while (matcher.find())` loop to parse each valid line. For each match, create a `new LogEntry(matcher.group(1), matcher.group(2), matcher.group(3), matcher.group(4))` and add it to the results list.

---

## Hint 5 (Near Solution)

```
private static final Pattern LOG_PATTERN = Pattern.compile(
    "^\\[(DEBUG|INFO|WARN|ERROR)\\] (\\d{4}-\\d{2}-\\d{2}) " +
    "(\\d{2}:\\d{2}:\\d{2}) - (.+)$",
    Pattern.MULTILINE
);

public List<LogEntry> parse(String logText) {
    if (logText == null) return Collections.emptyList();
    List<LogEntry> entries = new ArrayList<>();
    Matcher m = LOG_PATTERN.matcher(logText);
    while (m.find()) {
        entries.add(new LogEntry(
            m.group(1),  // level
            m.group(2),  // date
            m.group(3),  // time
            m.group(4)   // message
        ));
    }
    return entries;
}
```

Key points:
- MULTILINE makes `^` and `$` work per-line (not per-string)
- `\[` and `\]` match literal brackets (in Java: `\\[` and `\\]`)
- `.+` in the message does NOT match newlines (default behavior) — so it correctly stops at line end
- Malformed lines simply don't match the pattern and are silently skipped
