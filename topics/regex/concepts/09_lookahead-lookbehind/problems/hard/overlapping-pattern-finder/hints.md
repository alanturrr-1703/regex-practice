# Hints — Overlapping Pattern Finder

---

## Hint 1 — Why Normal find() Doesn't Work

`Matcher.find()` advances the cursor past the END of each match. For pattern `"aa"` in
`"aaaa"`, after matching at position 0, the cursor jumps to position 2 — skipping the
overlapping match at position 1.

To detect overlaps, you need the engine to advance by only 1 char after each match.

---

## Hint 2 — The Zero-Width Lookahead Trick

A lookahead `(?=X)` is zero-width — it matches a POSITION, not characters. After a
zero-width match, Java's `Matcher.find()` advances by exactly 1 char to avoid an infinite
loop. This is the key property we exploit:

```/dev/null/hint2.txt#L1-2
Pattern: (?=(aa))
Input:   "aaaa"
After match at position 0: cursor advances to position 1 (not 2!)
```

---

## Hint 3 — Building the Lookahead Pattern

Wrap your target pattern in a lookahead with a capturing group:
```/dev/null/hint3.txt#L1-2
"(?=(" + Pattern.quote(pattern) + "))"
```

`Pattern.quote(s)` escapes metacharacters in `s`, treating it as a literal string.
The inner group `(...)` lets you retrieve the matched text via `group(1)`.

---

## Hint 4 — Counting Loop

```/dev/null/hint4.txt#L1-7
Pattern p = Pattern.compile("(?=(" + Pattern.quote(pattern) + "))");
Matcher m = p.matcher(input);
int count = 0;
while (m.find()) {
    count++;
}
return count;
```

---

## Hint 5 — Guard Against Empty Pattern

An empty pattern string `""` would match at every position in the string (N+1 times
for a string of length N). Guard against it:
```/dev/null/hint5.txt#L1-2
if (input == null || input.isEmpty() || pattern == null || pattern.isEmpty())
    return 0;
```
