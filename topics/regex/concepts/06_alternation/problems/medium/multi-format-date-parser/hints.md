# Hints — Multi-Format Date Parser

Work through these one at a time. Stop as soon as you feel unblocked.

---

## Hint 1 — Build each format pattern independently

Before combining, verify each format alone:

```java
Pattern f1 = Pattern.compile("\\d{4}-\\d{2}-\\d{2}");
Pattern f2 = Pattern.compile("\\d{2}/\\d{2}/\\d{4}");
Pattern f3 = Pattern.compile("\\d{1,2}\\s+(?:Jan|Feb|Mar|...)\\s+\\d{4}");
```

Run each against a sample string. Confirm they match correctly before combining.

---

## Hint 2 — The 12 month abbreviations for Format 3

The month part of Format 3:
```
(?:Jan|Feb|Mar|Apr|May|Jun|Jul|Aug|Sep|Oct|Nov|Dec)
```

Don't miss any month. Count them: Jan, Feb, Mar, Apr, May, Jun, Jul, Aug, Sep, Oct, Nov, Dec = 12.

---

## Hint 3 — Combining with non-capturing alternation

Wrap all three in a single `(?:...|...|...)`:

```java
private static final Pattern DATE_PATTERN = Pattern.compile(
    "(?:" +
    "\\d{4}-\\d{2}-\\d{2}" +                          // Format 1
    "|" +
    "\\d{2}/\\d{2}/\\d{4}" +                          // Format 2
    "|" +
    "\\d{1,2}\\s+(?:Jan|Feb|Mar|Apr|May|Jun|Jul|Aug|Sep|Oct|Nov|Dec)\\s+\\d{4}" +  // Format 3
    ")"
);
```

---

## Hint 4 — Collecting matches

With a non-capturing outer group, `matcher.group()` (same as `group(0)`) is the full match:

```java
List<String> results = new ArrayList<>();
Matcher m = DATE_PATTERN.matcher(input);
while (m.find()) {
    results.add(m.group());  // or m.group(0)
}
```

---

## Hint 5 — Why ordering can matter

For this problem, the three formats are distinguishable by their separators:
- Format 1 uses `-`
- Format 2 uses `/`
- Format 3 uses spaces and month names

So ordering doesn't affect correctness here. But as a general rule:  
**If two alternatives can both match at the same position, put the longer/more-specific one first.**
