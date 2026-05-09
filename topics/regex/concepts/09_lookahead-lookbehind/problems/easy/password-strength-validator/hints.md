# Hints — Password Strength Validator

Work through these in order. Only read the next hint if you are truly stuck.

---

## Hint 1 — The Pattern Structure

A single regex can enforce "must contain X" using a **positive lookahead** anchored at `^`:

```/dev/null/hint1.txt#L1-2
^(?=.*[A-Z])        -- from start, skip anything (.*), then require an uppercase
```

The `.*` is greedy but that's fine here — we only care about existence, not position.
Replicate this structure for each of your four character-type constraints.

---

## Hint 2 — Stacking Lookaheads

Lookaheads are **zero-width** — they don't consume characters. You can stack multiple
lookaheads at the same anchor position `^`:

```/dev/null/hint2.txt#L1-3
^(?=.*[A-Z])(?=.*[a-z])(?=.*\d)
```

All three assertions test from position `^` independently. After all pass, the cursor
is still at position 0.

---

## Hint 3 — Special Characters Need Care

The special character set `!@#$%^&*` inside a character class `[...]` needs some
attention:
- `^` at the start of `[...]` means negation — put it anywhere else.
- `$` is literal inside `[...]` — no escaping needed.
- `*` is literal inside `[...]` — no escaping needed.

Safe form: `[!@#$%^&*]` where `^` is not first.

---

## Hint 4 — The Length Constraint

The final consuming part of the pattern enforces minimum length. After all lookaheads
pass (cursor still at `^`):

```/dev/null/hint4.txt#L1-2
.{8,}$     -- match 8 or more of any character up to end of string
```

Combined with `^`, this ensures the entire string is at least 8 characters.

---

## Hint 5 — Full Pattern Skeleton

```/dev/null/hint5.txt#L1-3
^(?=.*[A-Z])(?=.*[a-z])(?=.*\d)(?=.*[SPECIALS]).{8,}$
```

Replace `[SPECIALS]` with the actual character class for `!@#$%^&*`.
Use `Pattern.compile(...)` with `Matcher.matches()` (not `find()`).
