# Hints — Validate File Extension

Work through these one at a time. Stop as soon as you feel unblocked.

---

## Hint 1 — Escape the dot

In regex, `.` is a metacharacter matching ANY character. To match a literal dot (`.`), escape it: `\.`.

In Java: `"\\."` — the first `\\` is one backslash in the string, which the regex engine interprets as an escape.

---

## Hint 2 — Anchor to end of string

Use `$` to require the extension to be at the very end of the filename:

```
\.(java|py|js|ts|go)$
```

Without `$`, `"file.javascript"` would match `.js` (as a substring).  
With `$`, `"file.javascript"` fails because after `js` there is still `avascript` before end-of-string.

Java string: `"\\.(?:java|py|js|ts|go)$"`

---

## Hint 3 — Case-insensitive matching

Use `Pattern.CASE_INSENSITIVE` flag:

```java
Pattern.compile("\\.(?:java|py|js|ts|go)$", Pattern.CASE_INSENSITIVE);
```

Or use the inline flag `(?i)` inside the pattern:

```java
Pattern.compile("(?i)\\.(?:java|py|js|ts|go)$");
```

Both are equivalent.

---

## Hint 4 — `find()` or `matches()`?

Both work for this problem:
- `matcher.find()` — searches for the pattern anywhere in the string; the `$` anchor still enforces end-of-string
- `matcher.matches()` — requires the full string to match; you'd need `.*` at the start: `".*\\.(?:java|py|js|ts|go)"`

Either approach is fine; `find()` with `$` is simpler.

---

## Hint 5 — Complete structure

```java
private static final Pattern SOURCE_FILE_PATTERN =
    Pattern.compile("\\.(?:java|py|js|ts|go)$", Pattern.CASE_INSENSITIVE);

public boolean isSourceFile(String filename) {
    return SOURCE_FILE_PATTERN.matcher(filename).find();
}
```
