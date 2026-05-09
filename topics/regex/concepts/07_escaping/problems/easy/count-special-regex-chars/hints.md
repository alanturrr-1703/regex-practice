# Hints

## Hint 1 — The Goal
You need one regex that matches any single character that is a regex metacharacter. A character class `[...]` is perfect for "match any one of these characters." Your job is to include all 14 metacharacters: `. * + ? ^ $ { } [ ] | ( ) \`

## Hint 2 — Inside a Character Class
Most metacharacters LOSE their special meaning inside `[...]`. So `[.*+?]` matches a literal dot, asterisk, plus, or question mark — no special meaning. The tricky ones are: `\` (still an escape), `^` (negates if first), `-` (creates range if between two chars), `]` (closes the class).

## Hint 3 — Handling the Tricky Four
- Place `]` first right after `[` or escape it as `\]`
- Place `-` at the very start or very end of the class (or escape it as `\-`)
- Place `^` anywhere but the first position (or escape it as `\^`)
- Escape `\` as `\\` in the regex (and as `\\\\` in the Java string)

A valid regex character class containing all 14: `[][.*+?^${}|()\\-]`

## Hint 4 — Java String Translation
Every `\` in the regex pattern becomes `\\` in the Java string. So:
- Regex `\\` (matches one backslash) → Java string `"\\\\"`
- Regex `\]` (literal close bracket) → Java string `"\\]"`
- Full class in Java: `"[][.*+?^${}|()\\\\-]"` or similar

Try printing `pattern.pattern()` to verify what the engine sees.

## Hint 5 — Counting Matches
Once you have a working character class, use `Matcher.find()` in a while loop and increment a counter each time it matches. One match = one metacharacter found.
