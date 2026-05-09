# Hints — Nested Groups CSV Parser

Work through these one at a time. Stop as soon as you feel unblocked.

---

## Hint 1 — The core insight: two kinds of fields

A CSV field is one of two things:
- **Quoted**: `"content here"` — content may contain commas
- **Unquoted**: `content` — content cannot contain commas or quotes

Write a separate regex for each, verify each works, then combine with `|`.

---

## Hint 2 — Pattern for each field type

Quoted field — match `"..."` and capture the inner content:
```
"([^"]*)"
```
- `"` matches the opening quote (literal — no escaping needed in Java regex)
- `([^"]*)` captures everything except another quote (non-greedy by exclusion)
- `"` matches the closing quote

In Java: `"\"([^\"]*)\""`

Unquoted field — capture everything that's not a comma and not a quote:
```
([^,"]*)
```
In Java: `"([^\",]*)"`

---

## Hint 3 — Combining with alternation

Put the quoted alternative FIRST (more specific), then the unquoted alternative:
```
"([^"]*)"   ← group 1 captures quoted content
|([^,"]*)   ← group 2 captures unquoted content
```

Java string: `"\"([^\"]*)\"   |   ([^\",]*)"`  (remove spaces)  
`"\"([^\"]*)\"  |([^\",]*)"`

But wait — you also need to skip the comma separator between fields. One approach: match the optional leading comma as part of the pattern, but not capture it.

Better approach: use `find()` and detect field boundaries by matching the pattern against the whole line, consuming each field (with its comma) in turn.

A common technique: match `,?"([^"]*)"` or `,?([^,"]*)` to also consume the comma, using `,?` to make the comma optional (for the first field).

Pattern: `(?:^|,)(?:"([^"]*)"|([^,"]*))`  
- `(?:^|,)` — start of string or comma (non-capturing)
- `"([^"]*)"` — quoted field: group 1
- `|([^,"]*)` — unquoted field: group 2

---

## Hint 4 — Extracting the field value

Since only one branch of the alternation fires, check which group is non-null:

```java
while (matcher.find()) {
    String field;
    if (matcher.group(1) != null) {
        field = matcher.group(1);  // quoted: content without the quotes
    } else {
        field = matcher.group(2);  // unquoted: the raw value (may be empty)
    }
    results.add(field);
}
```

---

## Hint 5 — Complete pattern and structure

```java
// Pattern: start-of-string or comma, then either:
//   "([^"]*)"  → quoted field content in group 1
//   ([^,"]*)   → unquoted field content in group 2 (may be empty)
private static final Pattern CSV_FIELD =
    Pattern.compile("(?:^|,)(?:\"([^\"]*)\"|([^\",]*))");

public List<String> parseCsvLine(String line) {
    List<String> results = new ArrayList<>();
    Matcher matcher = CSV_FIELD.matcher(line);
    while (matcher.find()) {
        if (matcher.group(1) != null) {
            results.add(matcher.group(1));   // quoted content
        } else {
            results.add(matcher.group(2));   // unquoted content (possibly "")
        }
    }
    return results;
}
```

The key: `(?:^|,)` is a non-capturing group that anchors each field match to either the start of the string or a comma separator. This prevents the pattern from matching sub-parts of fields.
