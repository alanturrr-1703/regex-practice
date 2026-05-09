# Hints: Validate Starts With HTTP

---

## Hint 1 (Gentle)

Both `"http://"` and `"https://"` differ only in one character: the `'s'` in `https`. The `?` quantifier makes the preceding character optional. So `https?://` matches both forms.

Now, how do you make sure this appears at the very beginning of the string?

---

## Hint 2

The `^` anchor in regex means "assert that we are at the start of the string." Place it before your pattern: `^https?://`

When combined with `find()`, the engine only attempts a match at position 0. If the string doesn't start with `http://` or `https://`, `find()` returns `false`.

---

## Hint 3

In Java, compile your pattern as: `Pattern.compile("^https?://")`

Then use `matcher.find()` (NOT `matches()`):
- `find()` searches for the pattern anywhere — but `^` restricts it to position 0
- `matches()` requires the ENTIRE string to match `^https?://` — that would fail for `"http://example.com"` because there's content after `://`

---

## Hint 4

Handle `null` before creating the `Matcher`:

```
if (input == null) return false;
return PATTERN.matcher(input).find();
```

An alternative without regex: `input != null && (input.startsWith("http://") || input.startsWith("https://"))` — but the problem asks for regex practice, so use the regex approach.

---

## Hint 5 (Near Solution)

```
private static final Pattern PATTERN = Pattern.compile("^https?://");

public boolean startsWithHttp(String input) {
    if (input == null) return false;
    return PATTERN.matcher(input).find();
}
```

The `^` ensures the match only starts at position 0. The `s?` makes the `s` optional. The `://` matches the literal scheme separator.
