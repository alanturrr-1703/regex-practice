# Hints — Capture First Word

Work through these one at a time. Stop as soon as you feel unblocked.

---

## Hint 1 — The right API call

You don't want `matcher.matches()` — that requires the entire input to match the pattern.  
You want `matcher.find()`, which scans forward and stops at the first match.

`find()` returns `true` if it found a match, `false` if it didn't. Check the return value before calling `group()`.

---

## Hint 2 — The pattern

A word character in regex is `\w` — it matches `[a-zA-Z0-9_]`.  
To match one or more: `\w+`.  
To capture it: `(\w+)`.

In a Java string: `"(\\w+)"`

---

## Hint 3 — group(0) vs group(1)

With pattern `(\w+)` applied to `"  hello world"`:
- `find()` skips the leading spaces and finds "hello"
- `group(0)` = `"hello"` (entire match — the spaces are NOT in the match because the pattern doesn't include them)
- `group(1)` = `"hello"` (captured group 1 — same as group(0) here)

Now try with `\s*(\w+)` applied to `"  hello world"`:
- `find()` matches `"  hello"` (spaces + word)
- `group(0)` = `"  hello"` (entire match including spaces)
- `group(1)` = `"hello"` (only the captured word)

**Lesson:** `group(0)` and `group(1)` are only different when the pattern has elements outside the capturing group.

---

## Hint 4 — Handling the no-match case

```java
if (matcher.find()) {
    return Optional.of(matcher.group(1));
} else {
    return Optional.empty();
}
```

This is correct and handles empty strings and all-punctuation strings.

---

## Hint 5 — Complete structure

```java
private static final Pattern WORD_PATTERN = Pattern.compile("(\\w+)");

public Optional<String> captureFirstWord(String input) {
    Matcher matcher = WORD_PATTERN.matcher(input);
    if (matcher.find()) {
        return Optional.of(matcher.group(1));
    }
    return Optional.empty();
}
```
