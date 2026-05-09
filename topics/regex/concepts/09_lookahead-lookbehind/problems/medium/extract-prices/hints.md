# Hints — Extract Prices

---

## Hint 1 — Number Pattern

Start with just the numeric part. A price is one or more digits, optionally with a decimal:
```/dev/null/hint1.txt#L1-1
\d+(?:\.\d{2})?
```
Use a non-capturing group `(?:...)` for the decimal part.

---

## Hint 2 — Positive Lookbehind Syntax

A positive lookbehind `(?<=X)` asserts X matches immediately before the current position.
To require `$` or `€` immediately before your number:
```/dev/null/hint2.txt#L1-1
(?<=\$|€)\d+(?:\.\d{2})?
```
Note: `$` must be escaped as `\$` outside a character class (it means end-of-string otherwise).

---

## Hint 3 — Java Lookbehind Length Constraint

Java 8+ requires lookbehind to be **fixed-length**. Check: is `\$|€` fixed-length?
- `\$` matches exactly 1 char.
- `€` matches exactly 1 char (it's a BMP code point: 1 Java `char`).
Both alternatives are the same length → Java allows this alternation in lookbehind. ✅

---

## Hint 4 — Collecting Results

```/dev/null/hint4.txt#L1-8
List<String> results = new ArrayList<>();
Matcher m = PATTERN.matcher(input);
while (m.find()) {
    results.add(m.group());   // group() returns only the matched number, not the symbol
}
return results;
```

---

## Hint 5 — Null Safety and Empty List

Check null first. `Collections.emptyList()` or `new ArrayList<>()` are both acceptable
for empty results. Make your return type `List<String>` (not `ArrayList<String>`).
