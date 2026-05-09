# Hints — Unicode Identifier

Work through these hints one at a time.

---

## Hint 1 — Unicode Letter and Number Classes

ASCII character classes like `[a-zA-Z]` only match the 52 ASCII letters. For Unicode, you need **Unicode property classes**:

- `\p{L}` — matches ANY Unicode letter from any script (Latin, Greek, CJK, Arabic, etc.)
- `\p{N}` — matches ANY Unicode number

In Java strings: `"\\p{L}"` and `"\\p{N}"`.

Test: does `\p{L}` match `'日'`? Yes — CJK characters are in Unicode category `Lo` (Letter, Other), which is part of `\p{L}`.

---

## Hint 2 — The Identifier Structure

A valid identifier:
1. **Starts with**: underscore `_` OR a Unicode letter `\p{L}`
2. **Continues with**: zero or more of: `_`, `\p{L}`, or `\p{N}`

As a character class pattern:
- Start: `[_\\p{L}]`
- Continue: `[_\\p{L}\\p{N}]*`

Combined (Java string): `"[_\\p{L}][_\\p{L}\\p{N}]*"`

Note: the first character requires `[_\p{L}]` — NOT `\p{N}`. Identifiers cannot start with a digit.

---

## Hint 3 — The Lookbehind Problem

Without additional constraints, `find()` on `"123bad"` finds `"bad"` at position 3. But this is wrong — `"bad"` immediately follows digits, making it a continuation of a mixed token, not an identifier start.

The fix: a **negative lookbehind** that says "the character before this position is NOT an identifier character":

```
(?<![_\p{L}\p{N}])
```

This zero-width assertion fails if the preceding character is `_`, `\p{L}`, or `\p{N}`. At the start of the string, there is no preceding character, so the lookbehind succeeds.

---

## Hint 4 — The Full Pattern

```
(?<![_\p{L}\p{N}])[_\p{L}][_\p{L}\p{N}]*
```

As a Java string: `"(?<![_\\p{L}\\p{N}])[_\\p{L}][_\\p{L}\\p{N}]*"`

Compile with `Pattern.UNICODE_CHARACTER_CLASS` flag:

```
Pattern p = Pattern.compile(
    "(?<![_\\p{L}\\p{N}])[_\\p{L}][_\\p{L}\\p{N}]*",
    Pattern.UNICODE_CHARACTER_CLASS
);
```

---

## Hint 5 — The find() Loop

Use the standard extraction loop. The `UNICODE_CHARACTER_CLASS` flag is applied at compile time and doesn't need to be referenced again:

```
private static final Pattern IDENTIFIER = Pattern.compile(
    "(?<![_\\p{L}\\p{N}])[_\\p{L}][_\\p{L}\\p{N}]*",
    Pattern.UNICODE_CHARACTER_CLASS
);

public List<String> extractIdentifiers(String input) {
    List<String> identifiers = new ArrayList<>();
    Matcher m = IDENTIFIER.matcher(input);
    while (m.find()) {
        identifiers.add(m.group());
    }
    return identifiers;
}
```

Trace through `"x1 y2 123bad"`:
- Position 0: `x` is `\p{L}`. No preceding char → lookbehind passes. Matches `x1`. ✓
- Position 3: `y` is `\p{L}`. Preceded by space → lookbehind passes. Matches `y2`. ✓
- Position 6: `1` — not `[_\p{L}]`. No match.
- Position 7: `2` — no match.
- Position 8: `3` — no match.
- Position 9: `b` is `\p{L}`. Preceded by `3` which is `\p{N}` → lookbehind **fails**. No match for `"bad"`. ✓
