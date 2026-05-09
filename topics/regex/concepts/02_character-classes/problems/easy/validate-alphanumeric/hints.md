# Hints — Validate Alphanumeric

Work through these hints one at a time.

---

## Hint 1 — It's a Full-String Validation

You need the ENTIRE string to be alphanumeric. This is the key architectural choice: you need `matches()` (full-string), not `find()` (substring search).

Ask yourself: if the input is `"abc!"`, and your method returns `true`, what went wrong? (Answer: `find()` found `"abc"` and returned true, ignoring the `!`.)

---

## Hint 2 — What Characters Are Allowed

The allowed characters are:
- Lowercase letters: `a` through `z`
- Uppercase letters: `A` through `Z`
- Digits: `0` through `9`

In regex, these are three **ranges** inside a single character class:

```
[a-zA-Z0-9]
```

Note: DO NOT use `\w`. It includes underscore `_`. Underscore is NOT alphanumeric.

---

## Hint 3 — The Quantifier

You need the ENTIRE string to match, and the string must be at least 1 character long. The `+` quantifier means "one or more":

```
[a-zA-Z0-9]+
```

With `matches()`, this pattern must match the entire string. If the string is empty, `+` requires at least one character, so it returns `false`. 

---

## Hint 4 — String.matches() vs Matcher.matches()

Two equally correct approaches:

**Approach 1:** Direct `String.matches()` (compiles the pattern every call — fine for low-frequency use):
```
return input.matches("[a-zA-Z0-9]+");
```

**Approach 2:** Static `Pattern` with `Matcher.matches()` (preferred for high-frequency use):
```
private static final Pattern ALNUM = Pattern.compile("[a-zA-Z0-9]+");
return ALNUM.matcher(input).matches();
```

---

## Hint 5 — Why Length Is Already Handled

You might think you need `if (input.isEmpty()) return false`. You don't. The regex `[a-zA-Z0-9]+` with `matches()` requires at least 1 alphanumeric character for the match to succeed. An empty string won't match — `+` demands at least 1. This is the power of using `+` instead of `*`.
