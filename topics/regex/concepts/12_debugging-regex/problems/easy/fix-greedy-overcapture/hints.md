# Hints — Fix Greedy Overcapture

---

## Hint 1

The bug is in the `.*` portion of `<title>.*</title>`. The `.` matches ANY character (including `<` and `/`), and `*` is greedy — it matches as many characters as possible. So when you have two title tags, `.*` happily consumes the closing `</title>` of the first tag and the opening `<title>` of the second.

Print `m.start()` and `m.end()` to see exactly how much the broken pattern is consuming.

---

## Hint 2

There are two standard fixes for greedy overcapture:

**Option A**: Make the quantifier **lazy** by adding `?`:
- `.*` → `.*?`
- Lazy means: match the MINIMUM, not the maximum.

**Option B**: Use a **negated character class** to explicitly stop at `<`:
- `.*` → `[^<]*`
- This is "zero or more of any character except `<`".

---

## Hint 3

You also need a **capturing group** to extract just the title content (not the surrounding tags). Wrap the content-matching part in parentheses:
- `<title>(.*?)</title>` — group(1) is the title text
- `<title>([^<]*)</title>` — group(1) is the title text

---

## Hint 4

Use `matcher.find()` in a loop (not `matcher.matches()`). The HTML string is longer than just the tags — `matches()` would fail because it requires the entire input to be consumed by the pattern.

---

## Hint 5 (Reveal)

```java
// Option A: lazy quantifier
Pattern p = Pattern.compile("<title>(.*?)</title>");

// Option B: negated class (preferred for single-char delimiter)
Pattern p = Pattern.compile("<title>([^<]*)</title>");

List<String> titles = new ArrayList<>();
Matcher m = p.matcher(html);
while (m.find()) {
    titles.add(m.group(1)); // group(1) = content between tags
}
return titles;
```

Both options are correct. Use `[^<]*` for better performance in production.
