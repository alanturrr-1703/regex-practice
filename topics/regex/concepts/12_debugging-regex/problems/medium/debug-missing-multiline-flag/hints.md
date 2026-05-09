# Hints — Debug Missing Multiline Flag

---

## Hint 1

The bug is a missing flag. Look at the broken pattern: `Pattern.compile("^#.*")`. The `^` anchor here means "absolute start of the input string". For a multiline string, the start of line 2, 3, etc. is NOT position 0 of the entire string.

To make `^` match at the start of EVERY line, you need `Pattern.MULTILINE`.

---

## Hint 2

Add `Pattern.MULTILINE` as the second argument to `Pattern.compile()`:

```java
Pattern.compile("^#.*", Pattern.MULTILINE)
```

Now `^` matches at position 0 AND at any position immediately after a `\n`.

---

## Hint 3

Use the standard `find()` loop to collect all matches:

```java
List<String> result = new ArrayList<>();
Matcher m = Pattern.compile("^#.*", Pattern.MULTILINE).matcher(text);
while (m.find()) {
    result.add(m.group());
}
return result;
```

---

## Hint 4

What about `.*` matching `\r` on Windows? The pattern `^#.*` with MULTILINE stops at `\n` (because `.` doesn't match `\n` by default), but it WILL match `\r` if present. For robustness, you could use `^#[^\r\n]*` to explicitly exclude carriage returns. For this problem, `\n`-delimited input is assumed, so `^#.*` is fine.

---

## Hint 5 (Reveal)

```java
public List<String> extractCommentLines(String text) {
    List<String> result = new ArrayList<>();
    Matcher m = Pattern.compile("^#.*", Pattern.MULTILINE).matcher(text);
    while (m.find()) {
        result.add(m.group());
    }
    return result;
}
```

The inline flag equivalent: `Pattern.compile("(?m)^#.*")` — identical behavior.
