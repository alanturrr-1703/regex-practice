# Hints — Match Literal String

Work through these hints one at a time. Only reveal the next hint if you are genuinely stuck.

---

## Hint 1 — The Core Question

Before writing any code, answer this question: **where** in the string does `"ERROR"` need to appear?

It can appear at the beginning, in the middle, or at the end. It doesn't need to be the entire string.

Now ask yourself: which Java regex API requires the **whole string** to match, and which one finds the pattern **anywhere inside** the string?

Look up `Matcher.matches()` and `Matcher.find()` in the Java docs. The distinction between these two methods is the entire lesson.

---

## Hint 2 — Pattern Compilation

You need a `Pattern` object. Since you're iterating over a list, compile the pattern **once** before the loop, not inside it:

```
Pattern p = Pattern.compile(??);  // what goes here?
```

The pattern is just the literal string you're looking for. No special syntax needed — `"ERROR"` as a Java string literal is a perfectly valid regex that matches the exact text `ERROR`.

---

## Hint 3 — The Matcher.find() Loop Structure

For each string in the list, create a `Matcher` and call `find()`:

```
for (String line : lines) {
    Matcher m = pattern.matcher(line);
    if (m.find()) {
        // this line contains the substring
    }
}
```

`find()` searches the entire string for the pattern anywhere. It returns `true` if found, `false` otherwise. You don't need to loop on `find()` here — you just need one call per line to decide whether to include it in the result.

---

## Hint 4 — Building the Result

Use a `List` to accumulate results. `ArrayList` is fine. Add lines that pass the `find()` test. At the end, return the list.

If no lines match, the list will be empty — that's correct. Return `List.of()` or an empty `ArrayList`, but **never return null**.

The return type is `List<String>`, and you can declare your accumulator as:

```
List<String> result = new ArrayList<>();
```

---

## Hint 5 — Almost There

Here is the structure of the full solution (key parts blanked out — fill them in yourself):

```
private static final Pattern PATTERN = Pattern.compile(/* the literal */);

public List<String> filterErrors(List<String> lines) {
    List<String> result = new ArrayList<>();
    for (String line : lines) {
        if (PATTERN.matcher(line)./* which method? */()) {
            result.add(line);
        }
    }
    return result;
}
```

The `PATTERN` should be declared `static final` outside the method. The method call on `Matcher` that does a substring search — not a full-string match — is the key you need.
