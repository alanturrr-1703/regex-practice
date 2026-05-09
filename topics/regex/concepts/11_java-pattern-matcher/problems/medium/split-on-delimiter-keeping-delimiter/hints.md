# Hints — Split on Delimiter Keeping Delimiter

Work through these hints in order.

---

## Hint 1

`Pattern.split()` cannot keep the delimiter. You need a manual approach using `Matcher.find()`. Think about what regex pattern would match each "token + delimiter" as a single unit.

The token before a semicolon can be zero or more non-semicolon characters: `[^;]*`. The semicolon itself is literal `;`. So the pattern for "any text followed by a semicolon" is: `[^;]*;`.

---

## Hint 2

Run the pattern `[^;]*;` with `find()` on `"a;b;c"` and observe:
- Match 1: `"a;"` at positions [0, 2)
- Match 2: `"b;"` at positions [2, 4)
- No more matches

The text `"c"` starting at position 4 is the **remaining** text after the last match. You need to add it to the result separately, after the loop.

---

## Hint 3

Track `matcher.end()` after each match — this tells you where the last match ended. After the loop, `input.substring(lastEnd)` gives you the remaining text. Add it **only if it is non-empty**.

```java
int lastEnd = 0;
while (m.find()) {
    results.add(m.group()); // adds text + ";"
    lastEnd = m.end();
}
String remaining = input.substring(lastEnd);
if (!remaining.isEmpty()) {
    results.add(remaining);
}
```

---

## Hint 4

What about `""` (empty input)?
- No matches found
- `lastEnd` stays 0
- `remaining = input.substring(0)` = `""`
- `"".isEmpty()` is `true` → we don't add it
- Result is `[]` ✓

What about `";leading"`?
- `[^;]*;` matches `";"` (zero non-semicolons + one `;`) at [0, 1)
- Remaining from position 1: `"leading"`
- Result: `[";", "leading"]` ✓

---

## Hint 5 (Reveal)

```java
List<String> results = new ArrayList<>();
if (input.isEmpty()) return results;

Pattern p = Pattern.compile("[^;]*;");
Matcher m = p.matcher(input);
int lastEnd = 0;
while (m.find()) {
    results.add(m.group());
    lastEnd = m.end();
}
String remaining = input.substring(lastEnd);
if (!remaining.isEmpty()) {
    results.add(remaining);
}
return results;
```

The empty check at the top is optional (the algorithm handles it naturally), but makes intent explicit.
