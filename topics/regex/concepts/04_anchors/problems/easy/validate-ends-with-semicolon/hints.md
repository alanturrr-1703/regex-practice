# Hints: Validate Ends With Semicolon

---

## Hint 1 (Gentle)

You need to check if the string ends with `;` (with possibly trailing whitespace). Think about what the `$` anchor does: it matches the "end of the string." A pattern like `X$` means "X must appear right before the end."

---

## Hint 2

The pattern `;$` matches a semicolon at the very end of the string. But what about trailing whitespace? `"int x = 5;  "` has spaces after the semicolon. You need to allow whitespace characters AFTER the semicolon but BEFORE the end:

`;\s*$` — semicolon, then zero or more whitespace characters, then end of string.

---

## Hint 3

Use `find()` not `matches()`. The `$` restricts where the end of the match can be (must be at string end), but `find()` allows the match to start anywhere in the string.

With `matches()`, the pattern `;\s*$` would need to match the ENTIRE string, so only `";"` or `";  "` would match — not `"int x = 5;"`.

---

## Hint 4

Handle null input first:

```
if (input == null || input.isEmpty()) return false;
return PATTERN.matcher(input).find();
```

The empty-string check is optional — `find()` on an empty string with `;\s*$` would return false anyway.

---

## Hint 5 (Near Solution)

```
private static final Pattern PATTERN = Pattern.compile(";\\s*$");

public boolean endsWithSemicolon(String input) {
    if (input == null) return false;
    return PATTERN.matcher(input).find();
}
```

The `\s*` absorbs any whitespace between the `;` and the end-of-string anchor `$`. If there's no semicolon at all, or if content follows the semicolon that isn't whitespace, the pattern fails to match.
