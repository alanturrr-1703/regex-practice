# Hints — ReDoS-Safe Log Processor

---

## Hint 1 — Parse in Segments

Think of the format as: `TIMESTAMP LEVEL [OPTIONAL_CONTEXT] MESSAGE`

Start with the mandatory parts:
```/dev/null/hint1.txt#L1-2
^(\d{4}-\d{2}-\d{2}) ([A-Z]++) 
```
Note the `++` on `[A-Z]` — severity letters are disjoint from the space that follows.

---

## Hint 2 — Safe Optional Context

The context is `["anything"]` — optionally present. Use `[^"]*+` instead of `.*?`:
```/dev/null/hint2.txt#L1-3
(?:\["([^"]*+)"\] )?
// Outer (?:...)? — non-capturing, optional group
// \[" — literal ["
// ([^"]*+) — captured content: non-quote chars, possessively (KEY: no backtracking)
// "\] — literal "]
// Space after ] — separator before message
```

---

## Hint 3 — Message Field

The message is everything remaining. Use `.++` (possessive) or `(.+)` — it's at the end:
```/dev/null/hint3.txt#L1-1
(.++)$
```

---

## Hint 4 — Full Safe Pattern

```/dev/null/hint4.txt#L1-3
^(\d{4}-\d{2}-\d{2}) ([A-Z]++) (?:\["([^"]*+)"\] )?(.++)$
// Group 1: timestamp
// Group 2: level
// Group 3: context (may be null if optional group didn't match)
// Group 4: message
```

---

## Hint 5 — Performance Test Pattern

The test creates 1000 lines including adversarial ones like:
`"2024-01-15 ERROR [\"aaaaaaaaaaaaaaaaaaa"` (unclosed quote).

Your `[^"]*+` will fail immediately on this (the `[^"]*+` eats all non-quotes, then
`"` doesn't match the next char `a` isn't a `"`, wait — at some point hits end of string
or the `a` after `[`, at which point the whole optional group fails cleanly → message
takes over. With possessive, no retry of the optional group's internals.
