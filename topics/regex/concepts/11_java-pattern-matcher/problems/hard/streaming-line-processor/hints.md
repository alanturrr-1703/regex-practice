# Hints — Streaming Line Processor

Work through these hints in order.

---

## Hint 1

Start by defining three `static final Pattern` constants at the top of the class. Replace the `null` stubs with actual compiled patterns. Test each pattern independently against a sample input before wiring them together.

Focus on these patterns:
- Timestamp: `\d{2}:\d{2}:\d{2}` (matches HH:MM:SS)
- Email: `[a-zA-Z0-9._%+\-]+@[a-zA-Z0-9.\-]+\.[a-zA-Z]{2,}`
- IP: `\b\d{1,3}\.\d{1,3}\.\d{1,3}\.\d{1,3}\b`

---

## Hint 2

Implement the three helper methods independently before wiring them into `processLines`:

- `maskEmails(line)` → `EMAIL_PATTERN.matcher(line).replaceAll("[EMAIL]")`
- `maskIPs(line)` → `IP_PATTERN.matcher(line).replaceAll("[IP]")`
- `extractTimestamp(line)` → use `TIMESTAMP_PATTERN.matcher(line)`, call `find()`, return `group()` or null

Test each helper before moving to `processLines`.

---

## Hint 3

`processLines` just applies the transformations in sequence for each line:

```java
List<String> result = new ArrayList<>();
for (String line : lines) {
    String transformed = maskEmails(line);
    transformed = maskIPs(transformed);
    result.add(transformed);
}
return result;
```

The timestamp is preserved (not masked) so you don't need to call `extractTimestamp` in `processLines` — timestamps simply pass through `maskEmails` and `maskIPs` unchanged since they don't match either pattern.

---

## Hint 4

For `extractTimestamp`, use `find()` (not `matches()`!) since the timestamp may be embedded in a longer line:

```java
String extractTimestamp(String line) {
    Matcher m = TIMESTAMP_PATTERN.matcher(line);
    return m.find() ? m.group() : null;
}
```

---

## Hint 5 (Reveal — patterns only, implementation is yours)

The static final patterns to use:

```java
static final Pattern TIMESTAMP_PATTERN =
    Pattern.compile("\\b\\d{2}:\\d{2}:\\d{2}\\b");

static final Pattern EMAIL_PATTERN =
    Pattern.compile("[a-zA-Z0-9._%+\\-]+@[a-zA-Z0-9.\\-]+\\.[a-zA-Z]{2,}");

static final Pattern IP_PATTERN =
    Pattern.compile("\\b\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}\\b");
```

Note the double-escaping of `\b`, `\d`, `\.` in Java string literals.
