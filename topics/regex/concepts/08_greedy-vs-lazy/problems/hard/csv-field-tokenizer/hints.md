# Hints

## Hint 1 — Don't Split on Commas
Your first instinct: `line.split(",")`. This FAILS on `"quoted,field",plain` — you get 3 parts instead of 2. The comma inside the quoted field gets treated as a separator. You need a smarter approach.

## Hint 2 — Match Fields as Tokens
Instead of splitting, scan through each line and match field TOKENS. A field is either:
- A quoted field: `"[^"]*"` — a `"`, then any non-`"` chars, then `"`
- An unquoted field: `[^,\n]*` — any chars that are NOT comma or newline

Use alternation: `"([^"]*)"` or `([^,\n]*)` with a `find()` loop.

## Hint 3 — The Combined Pattern
The pattern for one field token (using alternation):
```
"([^"]*)"   — quoted: content in group 1
|           — OR
([^,\n]*)   — unquoted: content in group 2
```
In Java string: `"\"([^\"]*)\"|([^,\\n]*)"`

After each match, check: did group 1 match (quoted) or group 2 match (unquoted)?

## Hint 4 — Handling the Separator
After each field token, the `find()` loop positions just AFTER the field. The next character should be a `,` (separator) or end of line. You need to advance PAST the comma somehow. One approach: include an optional comma in the pattern and discard it, or advance `matcher.end()` manually.

A cleaner approach: split the input on `\n` to get rows, then for each row, run the token-finding loop.

## Hint 5 — Group Checking
```java
while (matcher.find()) {
    String quoted = matcher.group(1);   // null if unquoted alternative matched
    String unquoted = matcher.group(2); // null if quoted alternative matched
    String field = (quoted != null) ? quoted : unquoted;
    rowFields.add(field);
    // advance past the comma if present at matcher.end()
}
```
Remember: `matcher.group(n)` returns `null` if that group did not participate in the match.
