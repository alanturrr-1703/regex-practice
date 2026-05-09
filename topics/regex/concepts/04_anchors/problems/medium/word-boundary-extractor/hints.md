# Hints: Word Boundary Extractor

---

## Hint 1 (Gentle)

The pattern `"log"` (without boundaries) will match the letters `l-o-g` anywhere — inside "logger", "catalog", even "dialog". You need something that asserts "there's a word/non-word transition here." Think about the `\b` metacharacter.

---

## Hint 2

`\b` is a **zero-width assertion** — it doesn't consume characters. It only checks that the current position is at the boundary between a word character (`\w = [A-Za-z0-9_]`) and a non-word character (or string start/end).

Place `\b` on both sides of `log`: `\blog\b`

In Java string literal: `"\\blog\\b"` (each `\` needs doubling in Java strings).

---

## Hint 3

Use a `while (matcher.find())` loop and increment a counter for each match:

```
int count = 0;
Matcher m = PATTERN.matcher(input);
while (m.find()) {
    count++;
}
return count;
```

---

## Hint 4

Key edge cases to think about:
- `"logger"`: `\b` passes before `l` (start of word), `log` matches, but then `\b` after `g` FAILS (because next char is `g` — another word char). Count: 0. Correct.
- `"log."`: `\b` before `l` passes. `log` matches. `\b` after `g` — dot is non-word, YES boundary. Count: 1. Correct.
- `"log_file"`: `\b` before `l` passes. `log` matches. `\b` after `g` — underscore is `\w`, so FAIL. Count: 0.
- `"LOG"`: The pattern is lowercase `log`. Case-sensitive — no match.

---

## Hint 5 (Near Solution)

```
private static final Pattern PATTERN = Pattern.compile("\\blog\\b");

public int countWordLog(String input) {
    if (input == null) return 0;
    int count = 0;
    Matcher m = PATTERN.matcher(input);
    while (m.find()) {
        count++;
    }
    return count;
}
```

Important: do NOT use `Pattern.CASE_INSENSITIVE` — the problem requires case sensitivity.
The `\b` on both sides ensures only the exact standalone word "log" is counted.
