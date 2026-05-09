# Hints — Extract Words

Work through these hints one at a time. Only reveal the next hint if you are genuinely stuck.

---

## Hint 1 — Define "Word" Precisely

The problem defines a word as a maximal contiguous sequence of **alphabetic characters only**. Before you write any code, confirm you understand this by tracing through examples manually:

- `"foo_bar"` — underscore is NOT alphabetic. So we get `"foo"` and `"bar"`.
- `"Java9Rocks"` — `9` is NOT alphabetic. So we get `"Java"` and `"Rocks"`.
- `"Hello, world!"` — `,`, ` `, `!` are NOT alphabetic. We get `"Hello"` and `"world"`.

Now: what character class in regex matches "alphabetic only" (no digits, no underscores)?

---

## Hint 2 — The Right Character Class

`\w` matches `[a-zA-Z0-9_]` — it includes digits and underscores. That's NOT what you want.

You need: `[a-zA-Z]` — exactly letters, nothing else.

A "word" in this problem is one or more letters: `[a-zA-Z]+`.

Write this as a Java string: `"[a-zA-Z]+"`.

Test it manually: does `[a-zA-Z]+` match just the letters and stop at `9`, `_`, `,`?

---

## Hint 3 — The find() Loop Pattern

You need to find ALL occurrences of the pattern in the string, not just the first one. The standard pattern for this in Java is:

```
Pattern p = Pattern.compile("[a-zA-Z]+");
Matcher m = p.matcher(input);
List<String> results = new ArrayList<>();
while (m.find()) {
    results.add(m.group());  // m.group() returns the text of the current match
}
return results;
```

Key: `while (m.find())` — the loop continues as long as more matches exist. Each call to `find()` advances past the previous match.

---

## Hint 4 — What m.group() Returns

After a successful `find()`, `m.group()` (equivalently `m.group(0)`) returns the **matched text** — the exact substring that the pattern matched.

- After matching `"Hello"` in `"Hello, world!"`, `m.group()` returns `"Hello"`.
- `m.start()` returns the index where the match begins.
- `m.end()` returns the index just past the end of the match.

You only need `m.group()` for this problem.

---

## Hint 5 — Empty Input and Return Value

For empty input or input with no letters, your `while (m.find())` loop simply never executes, and `results` remains empty. Return the empty list — that's correct behavior.

Make sure you're returning a `List<String>`, not null. An empty `ArrayList<>` (or `List.of()`) is the correct return value when there are no matches.

The complete structure:

```
private static final Pattern WORD = Pattern.compile("[a-zA-Z]+");

public List<String> extractWords(String input) {
    Matcher m = WORD.matcher(input);
    List<String> words = new ArrayList<>();
    while (m.find()) {
        words.add(m.group());
    }
    return words;
}
```
