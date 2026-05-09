# Hints — Extract Hex Colors

Work through these hints one at a time.

---

## Hint 1 — What Is a Hex Digit?

Hexadecimal uses digits 0-9 plus letters a-f (or A-F). In a character class:

- Decimal digits: `[0-9]`
- Lowercase hex letters: `[a-f]`
- Uppercase hex letters: `[A-F]`
- All combined: `[0-9a-fA-F]`

Note: `x`, `y`, `z` are NOT hex digits. That's why `#xyz123` doesn't match.

---

## Hint 2 — Exact Repetition

The hex color needs **exactly 6** hex digits. The quantifier for "exactly n times" is `{n}`:

```
[0-9a-fA-F]{6}  — exactly 6 hex digits
```

Compare with:
- `{6,}` — 6 or MORE (too broad)
- `{1,6}` — 1 to 6 (too broad)
- `{6}` — exactly 6 ✓

---

## Hint 3 — Include the # Prefix

The full pattern starts with the literal `#`:

```
#[0-9a-fA-F]{6}
```

`#` is not a metacharacter in most positions in Java regex, so you don't need to escape it. The complete pattern as a Java string: `"#[0-9a-fA-F]{6}"`.

---

## Hint 4 — The find() Loop

Use the standard extraction loop:

```
Pattern p = Pattern.compile("#[0-9a-fA-F]{6}");
Matcher m = p.matcher(input);
List<String> colors = new ArrayList<>();
while (m.find()) {
    colors.add(m.group());
}
return colors;
```

`m.group()` returns the matched text including the `#`.

---

## Hint 5 — Optional: Case-Insensitive Variant

Instead of `[0-9a-fA-F]{6}`, you can write `[0-9a-f]{6}` with `Pattern.CASE_INSENSITIVE`:

```
Pattern p = Pattern.compile("#[0-9a-f]{6}", Pattern.CASE_INSENSITIVE);
```

Both approaches are correct for this problem. The explicit range is more common in production code because it's self-documenting and doesn't depend on flag state.
