# Hints — Find All Matches

Work through these hints in order. Try to solve the problem with as few hints as possible.

---

## Hint 1

The core building block you need is `Matcher.find()`. Look at the method signature:

```
boolean find()
```

It returns `true` if another match was found, advancing the internal position. This return value is exactly what you need for a `while` loop condition.

---

## Hint 2

The pattern for iterating over all matches in Java is always:

```java
Pattern p = Pattern.compile(regex);
Matcher m = p.matcher(input);
while (m.find()) {
    // m.group() is the current match
}
```

Your job is to collect `m.group()` into a list on each iteration.

---

## Hint 3

Use `new ArrayList<>()` to collect results. Initialize it before the loop, add inside the loop, return after the loop. The list will be empty if `find()` never returns `true` — which handles the "no matches" and "empty input" cases automatically.

---

## Hint 4

The difference between `matcher.find()` and `matcher.matches()`:
- `matches()` — requires the ENTIRE input string to match the pattern
- `find()` — searches for ANY substring matching the pattern

For this problem you need `find()`. Using `matches()` will give wrong results on most test cases.

---

## Hint 5 (Reveal)

The complete structure:

```java
List<String> results = new ArrayList<>();
Pattern p = Pattern.compile(regex);
Matcher m = p.matcher(input);
while (m.find()) {
    results.add(m.group());
}
return results;
```

This is the canonical "find all matches" loop. Memorize it.
