# Hints — Debug Group Index Error

---

## Hint 1

Look at the broken code: it uses `m.group(0)`. In Java regex, `group(0)` always means the **entire matched substring**. For a date like `"2024-01-15"`, `group(0)` returns `"2024-01-15"` — the whole thing.

Your task: find the correct group index for the year.

---

## Hint 2

Count the opening parentheses in the pattern from left to right:

```
Pattern: (\d{4})-(\d{2})-(\d{2})
          ^        ^        ^
          1        2        3
```

- group(1) → `(\d{4})` → the **year**
- group(2) → `(\d{2})` first → the **month**
- group(3) → `(\d{2})` second → the **day**

---

## Hint 3

The fix is a one-character change: replace `group(0)` with `group(1)`:

```java
if (m.find()) return Optional.of(m.group(1)); // year is group 1
```

---

## Hint 4

For the no-match case, return `Optional.empty()`. The ternary expression works well here:

```java
return m.find() ? Optional.of(m.group(1)) : Optional.empty();
```

---

## Hint 5 (Reveal)

```java
Matcher m = Pattern.compile("(\\d{4})-(\\d{2})-(\\d{2})").matcher(input);
return m.find() ? Optional.of(m.group(1)) : Optional.empty();
```

`group(1)` = the text captured by the FIRST set of parentheses = `(\d{4})` = the 4-digit year.

To remember: group(0) is special — it's the entire match. Numbered capturing groups start at 1.
