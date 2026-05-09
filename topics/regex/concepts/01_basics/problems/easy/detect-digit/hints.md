# Hints — Detect Digit

Work through these hints one at a time. Only reveal the next hint if you are genuinely stuck.

---

## Hint 1 — The Double-Escape Trap

This is a Java-specific problem before it's a regex problem. Write this in a scratch program and observe:

```
System.out.println("\d");    // What does this print? Does it compile?
System.out.println("\\d");   // What does this print?
```

The regex shorthand for "a digit" is `\d`. But in a Java string literal, `\` starts a Java escape sequence. `\d` is not a valid Java escape — so you need `\\` to get a single `\` through to the regex engine. The Java string `"\\d"` is received by the regex engine as `\d`.

Think: Java layer first, then regex layer.

---

## Hint 2 — Which Method?

You only need to know if the string **contains** at least one digit — not where, not how many.

Which method on `Matcher` returns `true` if the pattern is found **anywhere** in the string?

- `matches()` — requires the entire string to match.
- `find()` — searches for the pattern anywhere in the string.

For "does it contain at least one digit?" you want the search-anywhere method.

---

## Hint 3 — The Pattern

The regex pattern for "one digit character" in Java's default mode is just `\d` (the engine sees this). In your Java source code, this becomes the string literal `"\\d"`.

You don't need `\\d+` (one or more digits), because you just need to find ONE digit anywhere. A single `\d` is sufficient.

---

## Hint 4 — Putting It Together

Compile the pattern once (outside the method if you want production quality, or inside for simplicity):

```
Pattern p = Pattern.compile("\\d");
Matcher m = p.matcher(input);
return m.find();
```

That's essentially the entire solution structure. `find()` returns `true` if the pattern matches anywhere, `false` if not. No loop needed — one `find()` is sufficient.

---

## Hint 5 — One-Liner Approach

The full solution can be as short as:

```
return Pattern.compile("\\d").matcher(input).find();
```

Or for production code where you care about not recompiling the pattern:

```
private static final Pattern DIGIT = Pattern.compile("\\d");

public boolean containsDigit(String input) {
    return DIGIT.matcher(input).find();
}
```

Both are correct. The static-final version is preferred when the method is called frequently.
