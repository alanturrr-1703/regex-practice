# Hints — Extract Groups From Match

Work through these hints in order. Try to solve the problem with as few hints as possible.

---

## Hint 1

The pattern you need is `(\w+)=(\w+)`. Parentheses in regex create **capturing groups**. Everything inside `(...)` is captured and accessible via `matcher.group(n)`.

Think about: how many groups does this pattern have, and what does each one capture?

---

## Hint 2

After a successful `matcher.find()`:
- `matcher.group(0)` — the entire matched text (e.g., `"name=Alice"`)
- `matcher.group(1)` — the first group — the text matched by the first `(\w+)` — the KEY
- `matcher.group(2)` — the second group — the text matched by the second `(\w+)` — the VALUE

Groups are numbered from 1 (not 0). Group 0 is always the full match.

---

## Hint 3

The structure is the same as the `findAll` problem, but inside the loop you now extract two groups:

```java
while (m.find()) {
    String key   = m.group(1);
    String value = m.group(2);
    // put key → value into your map
}
```

Initialize a `LinkedHashMap` (preserves insertion order) or `HashMap` before the loop.

---

## Hint 4

The pattern `(\w+)=(\w+)` automatically skips non-matching text (spaces, words without `=`). The `find()` loop simply won't match them — you don't need any extra filtering logic.

---

## Hint 5 (Reveal)

```java
Map<String, String> result = new LinkedHashMap<>();
Pattern p = Pattern.compile("(\\w+)=(\\w+)");
Matcher m = p.matcher(input);
while (m.find()) {
    result.put(m.group(1), m.group(2));
}
return result;
```

The `\\w` in the Java string becomes `\w` in the actual regex. That's the double-escape rule: every `\` in a Java regex string literal must be written as `\\`.
