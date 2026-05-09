# Hints — Validate Simple Email

Work through these hints one at a time. Only reveal the next hint if you are genuinely stuck.

---

## Hint 1 — Use matches(), Not find()

This is a **validation** problem — you need the entire string to look like an email. This means full-string matching, not substring search.

In Java, `String.matches(regex)` is equivalent to anchoring the pattern with `^` and `$` at both ends. It returns `true` only if the entire string matches.

Ask yourself: what method enforces that the WHOLE string matches the pattern?

---

## Hint 2 — Break the Email Into Parts

Don't try to write the whole pattern at once. Write it in parts:

1. What matches the part before `@`? (hint: "any character that is not `@`", one or more)
2. What matches the `@` sign?
3. What matches the domain name? (same idea as part 1)
4. What matches the literal dot? (hint: `.` in regex means "any char" — you need to escape it)
5. What matches the TLD? (2-6 letters only)

Write each part as a regex fragment, then concatenate them.

---

## Hint 3 — Character Classes for Local and Domain

"Any character that is not `@`" is a **negated character class**: `[^@]`.

But you also want to exclude whitespace (spaces in an email address are invalid). Extend the negation: `[^@\\s]` — not `@` and not whitespace.

Add `+` to require at least one such character.

For the literal dot before the TLD, remember: `.` matches ANY character. To match a literal dot, you escape it: `\\.` (Java string `"\\."`) → engine sees `\.`.

---

## Hint 4 — The TLD Part

The TLD (like `com`, `io`, `museum`) must be:
- Only ASCII letters: `[a-zA-Z]`
- 2 to 6 characters long: `{2,6}`

Combined: `[a-zA-Z]{2,6}`

This is an exact-count quantifier. `{2,6}` means "at least 2, at most 6". For `String.matches()`, this is anchored — so `"toolong"` (7 chars) won't match `[a-zA-Z]{2,6}` because the engine is forced to match the entire remaining string.

---

## Hint 5 — Assembling the Pattern

Combine all parts:

```
[^@\s]+   @   [^@\s]+   \.   [a-zA-Z]{2,6}
```

As a Java string (with double-escaping):
```
"[^@\\s]+@[^@\\s]+\\.[a-zA-Z]{2,6}"
```

Use this with `String.matches()`:
```
return email.matches("[^@\\s]+@[^@\\s]+\\.[a-zA-Z]{2,6}");
```

Or compile it as a `static final Pattern` and call `matcher.matches()`:
```
private static final Pattern EMAIL = Pattern.compile("[^@\\s]+@[^@\\s]+\\.[a-zA-Z]{2,6}");

public boolean isValidEmail(String email) {
    return EMAIL.matcher(email).matches();
}
```
