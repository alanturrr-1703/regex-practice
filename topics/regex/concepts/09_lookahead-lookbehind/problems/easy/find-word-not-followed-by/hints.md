# Hints — Find Word Not Followed By

---

## Hint 1 — What Tool to Use

A **negative lookahead** `(?!X)` asserts that pattern X does NOT match at the current
position. It is zero-width (does not consume characters).

To match `"file"` only when NOT followed by `"name"`:
```/dev/null/hint1.txt#L1-1
file(?!name)
```

---

## Hint 2 — Handling Multiple Exclusions

To exclude `"name"` OR `"path"`, use alternation inside the lookahead:
```/dev/null/hint2.txt#L1-1
file(?!name|path)
```

Alternation inside a lookahead works exactly like regular alternation — the engine tries
each alternative in order.

---

## Hint 3 — Counting All Occurrences

`Matcher.matches()` tests the whole string. You want `Matcher.find()` which scans for
the pattern anywhere in the string. Call it in a loop:

```/dev/null/hint3.txt#L1-6
Matcher m = PATTERN.matcher(input);
int count = 0;
while (m.find()) {
    count++;
}
return count;
```

---

## Hint 4 — Null Safety

Check `if (input == null) return 0;` before compiling/running the matcher.

---

## Hint 5 — Static Pattern Field

Compile the pattern once as a static field, not inside the method:
```/dev/null/hint5.txt#L1-1
private static final Pattern FILE_PATTERN = Pattern.compile("file(?!name|path)");
```
