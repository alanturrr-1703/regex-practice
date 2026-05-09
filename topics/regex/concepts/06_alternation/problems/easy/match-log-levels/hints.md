# Hints — Match Log Levels

Work through these one at a time. Stop as soon as you feel unblocked.

---

## Hint 1 — Alternation syntax

To match "DEBUG or INFO or WARN or ERROR or FATAL", use `|`:

```
DEBUG|INFO|WARN|ERROR|FATAL
```

In a Java string: `"DEBUG|INFO|WARN|ERROR|FATAL"` — the pipe character needs no escaping.

---

## Hint 2 — The character class trap (do NOT do this)

```java
// WRONG — this is a character class:
Pattern.compile("[DEBUG|INFO|WARN|ERROR|FATAL]");
// Matches ONE char from: D, E, B, U, G, |, I, N, F, O, W, A, R, T, L
// NOT the words "DEBUG" or "INFO"!
```

The correct approach is alternation (bare `|`) or with grouping `(?:...)`.

---

## Hint 3 — Adding word boundaries

Without `\b`, `"INFORMATION"` would match `INFO` as a substring.  
Add `\b` before and after the alternation:

```
\b(?:DEBUG|INFO|WARN|ERROR|FATAL)\b
```

Java string: `"\\b(?:DEBUG|INFO|WARN|ERROR|FATAL)\\b"`

The `(?:...)` groups the alternation so the `\b` applies to the whole matched word, not just the last alternative.

---

## Hint 4 — `find()` not `matches()`

`matches()` requires the ENTIRE input string to match the pattern. For `"DEBUG: message"`, the colon and rest of the string would prevent a match.

Use `find()` to search for the pattern anywhere in the string:

```java
return matcher.find();
```

---

## Hint 5 — Complete structure

```java
private static final Pattern LOG_LEVEL_PATTERN =
    Pattern.compile("\\b(?:DEBUG|INFO|WARN|ERROR|FATAL)\\b");

public boolean containsLogLevel(String input) {
    return LOG_LEVEL_PATTERN.matcher(input).find();
}
```
