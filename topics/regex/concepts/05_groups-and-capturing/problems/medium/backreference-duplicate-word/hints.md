# Hints — Backreference Duplicate Word

Work through these one at a time. Stop as soon as you feel unblocked.

---

## Hint 1 — What a backreference is

A backreference `\1` in a pattern means: "at this position, the input must contain the same text that group 1 captured earlier in this match."

It matches the **same text**, not the same pattern. If group 1 captured "hello", then `\1` will only match the literal string "hello" at that point.

---

## Hint 2 — Java syntax for backreferences

In Java string literals, every backslash must be doubled. So:
- Regex `\1` → Java string `"\\1"`
- Regex `\b(\w+)` → Java string `"\\b(\\w+)"`

Full pattern string: `"\\b(\\w+)\\s+\\1\\b"`

---

## Hint 3 — Case-insensitive matching

Pass `Pattern.CASE_INSENSITIVE` as the second argument to `Pattern.compile()`:
```java
Pattern p = Pattern.compile("\\b(\\w+)\\s+\\1\\b", Pattern.CASE_INSENSITIVE);
```

With this flag, the backreference `\1` also matches case-insensitively. "Hello" and "hello" are treated as the same text.

---

## Hint 4 — What to add to the result list

Use `matcher.group(1)` — this gives you just the first word (not the entire `"word word"` pair).

```java
while (matcher.find()) {
    results.add(matcher.group(1));
}
```

---

## Hint 5 — Complete structure

```java
private static final Pattern DUPLICATE_PATTERN =
    Pattern.compile("\\b(\\w+)\\s+\\1\\b", Pattern.CASE_INSENSITIVE);

public List<String> findDuplicateWords(String input) {
    List<String> results = new ArrayList<>();
    Matcher matcher = DUPLICATE_PATTERN.matcher(input);
    while (matcher.find()) {
        results.add(matcher.group(1));
    }
    return results;
}
```

**Why does `"aaa"` return `[]`?**  
The pattern `\b(\w+)\s+\1\b` requires whitespace (`\s+`) between the two instances. "aaa" has no whitespace — there is no "word then space then same word" pattern. It's one continuous token.
