# Hints — Extract Date Parts

Work through these one at a time. Stop as soon as you feel unblocked.

---

## Hint 1 — What the pattern looks like (conceptual)

A date `YYYY-MM-DD` has three parts separated by hyphens:
- Year: exactly 4 digits
- Month: exactly 2 digits
- Day: exactly 2 digits

In regex, "exactly N digits" is written as `\d{N}`.  
In a Java string, the backslash must be doubled: `\\d{4}`.

Write the full pattern without capturing groups first, just to verify it matches a date.

---

## Hint 2 — Adding capturing groups

Once you have the base pattern matching a date, wrap each component in `(...)` to capture it:

```
(\d{4})-(\d{2})-(\d{2})
  ^1       ^2      ^3
```

In Java: `"(\\d{4})-(\\d{2})-(\\d{2})"`

Compile it with `Pattern.compile(...)` and store it as a field (not inside the method — prefer a `static final` constant).

---

## Hint 3 — Iterating all matches

`matcher.find()` returns `true` each time it finds the next match. Use it in a loop:

```java
while (matcher.find()) {
    String year  = matcher.group(1);
    String month = matcher.group(2);
    String day   = matcher.group(3);
    // build a DateParts and add to the result list
}
```

Do NOT use `matcher.matches()` — that requires the entire input to be a date.

---

## Hint 4 — Putting it together

```java
private static final Pattern DATE_PATTERN =
    Pattern.compile("(\\d{4})-(\\d{2})-(\\d{2})");

public List<DateParts> extractDates(String input) {
    List<DateParts> results = new ArrayList<>();
    Matcher matcher = DATE_PATTERN.matcher(input);
    while (matcher.find()) {
        results.add(new DateParts(
            matcher.group(1),
            matcher.group(2),
            matcher.group(3)
        ));
    }
    return results;
}
```

---

## Hint 5 — Edge cases to verify

- `""` → the while loop never executes → returns empty list ✓
- `"no dates"` → `find()` returns false immediately → returns empty list ✓
- `"2024-13-99"` → matches fine — the regex checks shape, not semantics ✓
- `"2024-01-01 2024-02-02"` → `find()` is called twice → two results ✓
