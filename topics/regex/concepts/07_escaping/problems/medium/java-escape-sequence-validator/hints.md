# Hints

## Hint 1 — Clarify What's in the Input
Before writing any regex, understand what characters are actually in the input. In your test code, `"\\n"` is a Java string containing two characters: backslash (char 92) and 'n' (char 110). That is the kind of input this validator processes. Print `charAt(0)` to confirm it's 92.

## Hint 2 — How to Match a Literal Backslash in Regex
In regex, `\\` matches one literal backslash. In a Java string, `\\` is one backslash character — so to get `\\` (two backslashes) into the regex engine, you write `"\\\\"` in Java source. This is the core escaping challenge of this problem.

## Hint 3 — Strategy: Find Invalid Escapes
The simplest approach: search for any backslash NOT followed by a valid escape character. If you find one, return false.

A backslash followed by a valid character looks like: `\\[ntr\\'\"0]` in regex.
The negated version (invalid): `\\[^ntr\\'\"0]`

Also handle: a backslash at the very end of the string with nothing following it.

## Hint 4 — Java String for the Invalid-Escape Pattern
To match a literal backslash in regex: `\\` → in Java string: `"\\\\"`
To then check for a character NOT in the valid set:
```java
// Regex:  \\[^ntr\\"'0]
// Java:   "\\\\[^ntr\\\\\"'0]"
```
Remember: inside a character class, `\\` is also needed to match a literal backslash.
And `"` inside a Java string needs `\"`.

For the end-of-string lone backslash: `\\$` in regex → `"\\\\$"` in Java.

Combined pattern using alternation:
```
"\\\\[^ntr\\\\\"'0]|\\\\$"
```

## Hint 5 — Putting It Together
```java
// Pattern matches an INVALID escape sequence
private static final Pattern INVALID_ESCAPE = Pattern.compile(
    "\\\\[^ntr\\\\\"'0]|\\\\$"
);

public boolean hasOnlyValidEscapes(String input) {
    // If we find any invalid escape, return false
    // If we find none, return true
    // ...implement this...
}
```
Use `INVALID_ESCAPE.matcher(input).find()` — if it finds a match, the input has invalid escapes.
