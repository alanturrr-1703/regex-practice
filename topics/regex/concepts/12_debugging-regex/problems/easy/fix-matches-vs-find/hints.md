# Hints — Fix matches() vs find()

---

## Hint 1

`String.matches(regex)` is equivalent to `Pattern.compile(regex).matcher(input).matches()`. The key word is `matches()` — this method requires the ENTIRE input to match the pattern, as if the pattern were anchored with `^` and `$`.

For "does this string CONTAIN a ZIP code?", you need `find()` — which searches for a substring match.

---

## Hint 2

Switch from:
```java
return input.matches("\\d{5}");
```
To:
```java
return Pattern.compile("\\d{5}").matcher(input).find();
```

This passes most test cases. But test with `"902109"` — it returns `true` incorrectly because `\d{5}` matches the first 5 digits of `"902109"`.

---

## Hint 3

To prevent matching a 5-digit substring inside a longer number, you need to assert that the 5 digits are NOT surrounded by other digits. Use lookaheads and lookbehinds:

- `(?<!\d)` — negative lookbehind: "not preceded by a digit"
- `(?!\d)` — negative lookahead: "not followed by a digit"

These are zero-width assertions — they don't consume characters; they just check the surrounding context.

---

## Hint 4

The complete fixed pattern is: `(?<!\d)\d{5}(?!\d)`

In a Java string literal: `"(?<!\\d)\\d{5}(?!\\d)"`

Test it against:
- `"902109"` → find() returns false (the `(?!\d)` lookahead fails — position 5 is `"9"`, a digit)
- `"90210"` → find() returns true (no digit before or after)
- `"ZIP: 90210"` → find() returns true

---

## Hint 5 (Reveal)

```java
private static final Pattern ZIP_PATTERN =
    Pattern.compile("(?<!\\d)\\d{5}(?!\\d)");

public boolean containsZipCode(String input) {
    return ZIP_PATTERN.matcher(input).find();
}
```

Cache the `Pattern` as a static final field for production use. The pattern `(?<!\d)\d{5}(?!\d)` reads: "5 digits that are not preceded by a digit and not followed by a digit."
