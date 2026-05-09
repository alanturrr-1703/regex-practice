# Hints

## Hint 1 — The Core Escaping Issue
An IPv4 address like "192.168.1.1" has dots as separators. In regex, `.` matches ANY character. So `\d{1,3}.\d{1,3}.\d{1,3}.\d{1,3}` would match "1X2Y3Z4". You need the dots to be literal. How do you make a dot literal in regex?

## Hint 2 — Escaping the Dot
In regex, `\.` means literal dot. In a Java string, `\.` requires two backslashes: `"\\."`. So your four-octet pattern in Java string form starts as:
`"\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}"`
Print the compiled pattern to verify: it should show `\d{1,3}\.\d{1,3}\.\d{1,3}\.\d{1,3}`.

## Hint 3 — The Boundary Problem
The pattern above would match the first four octets of "1.2.3.4.5" — leaving ".5" unmatched. You need to ensure the match is NOT followed by a dot and more digits. Add a negative lookahead at the end: `(?!\\.\\d)` — "not followed by a dot and a digit."

Similarly, add a check at the start to ensure you don't start in the middle of a longer number. Use `(?<!\\d)` — "not preceded by a digit."

## Hint 4 — Assembling the Full Pattern
```
(?<!\d)\d{1,3}\.\d{1,3}\.\d{1,3}\.\d{1,3}(?!\.\d)
```
In Java string: `"(?<!\\d)\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}(?!\\.\\d)"`

## Hint 5 — Collecting Results
Use `Matcher.find()` in a while loop. Each time `find()` returns true, call `matcher.group()` to get the matched IP address and add it to your result list.
