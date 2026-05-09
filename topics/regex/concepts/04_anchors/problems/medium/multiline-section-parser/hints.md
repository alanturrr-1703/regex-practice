# Hints: Multiline Section Parser

---

## Hint 1 (Gentle)

By default, `^` in a Java regex matches only position 0 — the very start of the string. Try this: compile `^>>` and use `find()` on `"first\n>> second"`. Does it find `>> second`? It should NOT without MULTILINE. That tells you what flag you need.

---

## Hint 2

`Pattern.MULTILINE` changes the meaning of `^` and `$`:
- `^` now matches at position 0 AND immediately after every `\n`
- `$` now matches at the end of the string AND immediately before every `\n`

Compile with: `Pattern.compile("^>>(.+)$", Pattern.MULTILINE)`

The `(.+)` in the middle is a capturing group — it captures everything between `>>` and the end of the line.

---

## Hint 3

Use a `while (matcher.find())` loop. For each match, call `matcher.group(1)` to get the text captured by the first group — that's everything after `>>` on the line.

Then call `.trim()` on it to remove leading/trailing spaces.

---

## Hint 4

Handle the edge cases:
- `null` input: return empty list before creating a `Matcher`
- Lines starting with `>>` but having no content: `">> "` — `(.+)` requires at least 1 char, so `">> "` would capture `" "` which trims to `""`. Decide whether to add empty strings to the result — for this problem, skip empty results (only add if `!trimmed.isEmpty()`).

---

## Hint 5 (Near Solution)

```
private static final Pattern SECTION_PATTERN =
    Pattern.compile("^>>(.+)$", Pattern.MULTILINE);

public List<String> extractSections(String text) {
    if (text == null) return Collections.emptyList();
    List<String> results = new ArrayList<>();
    Matcher m = SECTION_PATTERN.matcher(text);
    while (m.find()) {
        String content = m.group(1).trim();
        if (!content.isEmpty()) {
            results.add(content);
        }
    }
    return results;
}
```

The MULTILINE flag is the critical ingredient. Without it, only the very first line starting with `>>` (if it's the first line of the string) would be found.
