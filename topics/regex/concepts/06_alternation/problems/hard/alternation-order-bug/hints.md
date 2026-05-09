# Hints — Alternation Order Bug

Work through these one at a time. Stop as soon as you feel unblocked.

---

## Hint 1 — Understand the NFA left-bias rule

In an NFA regex engine, alternation `a|b` always tries the LEFT alternative first. If the left alternative succeeds, the right alternative is NEVER tried at that position.

This means `[$€]\d+|[$€]\d+\.\d{2}`:
- For input `"$10.50"`, the left `[$€]\d+` matches `"$10"` at position 0 → success
- The right `[$€]\d+\.\d{2}` is never tried at position 0
- The match is `"$10"`, not `"$10.50"`

---

## Hint 2 — The fix: swap the order

Put the more specific (longer) alternative FIRST:

```
[$€]\d+\.\d{2}|[$€]\d+
```

Now for `"$10.50"`:
- Try `[$€]\d+\.\d{2}` → matches `"$10.50"` → success ✓

For `"$10"`:
- Try `[$€]\d+\.\d{2}` → `\.\d{2}` fails (no dot) → fail
- Try `[$€]\d+` → matches `"$10"` → success ✓

---

## Hint 3 — The `€` character in a Java string

The euro sign `€` is a regular Unicode character (U+20AC). You can use it directly in Java string literals if your file is UTF-8 (which it should be):

```java
Pattern.compile("[$€]\\d+\\.\\d{2}|[$€]\\d+")
```

Alternatively, use the Unicode escape:
```java
Pattern.compile("[$\u20ac]\\d+\\.\\d{2}|[$\u20ac]\\d+")
```

---

## Hint 4 — The dot must be escaped

In `\d+\.\d{2}`, the `\.` is an escaped dot — matches a literal `.`.  
In Java: `"\\d+\\.\\d{2}"`.

Without escaping: `\d+.\d{2}` — the `.` matches ANY character, so `$10X50` would match!

---

## Hint 5 — Complete solution structure

```java
// FIXED_PATTERN: decimal alternative FIRST, then integer
public static final String FIXED_PATTERN = "[$€]\\d+\\.\\d{2}|[$€]\\d+";

private static final Pattern PATTERN = Pattern.compile(FIXED_PATTERN);

public List<String> extractAmounts(String input) {
    List<String> results = new ArrayList<>();
    Matcher m = PATTERN.matcher(input);
    while (m.find()) {
        results.add(m.group());
    }
    return results;
}
```

Study `BROKEN_PATTERN` in the class to understand what not to do and why.
