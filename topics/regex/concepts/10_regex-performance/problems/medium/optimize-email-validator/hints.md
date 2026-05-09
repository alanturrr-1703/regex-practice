# Hints — Optimize Email Validator

---

## Hint 1 — Bound Input Length First

Before running any regex, reject inputs that are too long:
```/dev/null/hint1.txt#L1-3
if (email == null || email.length() > 254) {
    return false;
}
```
This alone prevents ReDoS even if your pattern isn't perfect.

---

## Hint 2 — Safe Local Part Pattern

The slow pattern's `([a-z]+[a-z0-9]*)*` is catastrophic. Replace it with a single
quantifier over a flat character class:
```/dev/null/hint2.txt#L1-2
[a-zA-Z0-9._%+\\-]+
```
One quantifier, one character class, no nesting, no ambiguity.

---

## Hint 3 — Safe Domain Pattern

For the domain `example.com` or `sub.example.co.uk`:
```/dev/null/hint3.txt#L1-2
[a-zA-Z0-9\\-]+(?:\\.[a-zA-Z0-9\\-]+)*\\.[a-zA-Z]{2,}
```
Each `[a-zA-Z0-9\\-]+` label is separated by a literal `.` — no overlapping character
sets, no ambiguity.

---

## Hint 4 — Full Safe Pattern

```/dev/null/hint4.txt#L1-3
^[a-zA-Z0-9._%+\\-]+@[a-zA-Z0-9\\-]+(?:\\.[a-zA-Z0-9\\-]+)*\\.[a-zA-Z]{2,}$
```
Flat, unambiguous, O(N).

---

## Hint 5 — Static Field and matches()

```/dev/null/hint5.txt#L1-3
private static final Pattern EMAIL_PATTERN = Pattern.compile(
    "^[a-zA-Z0-9._%+\\-]+@[a-zA-Z0-9\\-]+(?:\\.[a-zA-Z0-9\\-]+)*\\.[a-zA-Z]{2,}$"
);
```
Use `EMAIL_PATTERN.matcher(email).matches()` — `matches()` is anchored to the full string.
