# Hints — Match Vowels

Work through these hints one at a time.

---

## Hint 1 — What to Match

You need to find individual vowel characters. Define what a vowel is in terms of a character set: the letters `a`, `e`, `i`, `o`, `u`.

In regex, a set of specific characters is expressed as an **enumerated character class**: `[aeiou]`.

This matches exactly ONE character — whichever one of those five is at the current position.

---

## Hint 2 — Case Sensitivity

The problem is case-insensitive. You can handle this in two ways:

**Option A**: List both cases explicitly: `[aeiouAEIOU]`

**Option B**: Use the case-insensitive flag: `[aeiou]` with `Pattern.CASE_INSENSITIVE`

```
Pattern p = Pattern.compile("[aeiou]", Pattern.CASE_INSENSITIVE);
```

Both approaches are correct. Option B is more concise. Option A is more portable.

---

## Hint 3 — Counting With find()

You need to count occurrences, not just detect presence. Use the `find()` loop and increment a counter:

```
int count = 0;
Matcher m = pattern.matcher(input);
while (m.find()) {
    count++;
}
return count;
```

Each call to `find()` advances to the next match. The loop runs once per vowel found.

---

## Hint 4 — Empty String

For an empty string, the `while (m.find())` loop never executes (there are no characters to match), so `count` remains 0. This naturally handles the empty case — no special check needed.

---

## Hint 5 — Complete Structure

```
private static final Pattern VOWEL =
    Pattern.compile("[aeiou]", Pattern.CASE_INSENSITIVE);

public int countVowels(String input) {
    int count = 0;
    Matcher m = VOWEL.matcher(input);
    while (m.find()) {
        count++;
    }
    return count;
}
```
