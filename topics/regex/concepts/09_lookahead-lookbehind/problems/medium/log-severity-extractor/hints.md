# Hints — Log Severity Extractor

---

## Hint 1 — Escape the Brackets

Square brackets in regex define character classes. To match a literal `[`, escape it: `\[`.
So `[ERROR]` in regex is `\[ERROR\]`. In a Java string: `"\\[ERROR\\]"`.

---

## Hint 2 — Non-Capturing Group for Alternation

To match `WARN` or `ERROR` without capturing that group, use `(?:WARN|ERROR)`:
```/dev/null/hint2.txt#L1-1
\[(?:WARN|ERROR)\]
```
This matches `[WARN]` or `[ERROR]` as a literal token.

---

## Hint 3 — Capturing the Message

Wrap the message part in a capturing group `(...)`:
```/dev/null/hint3.txt#L1-2
\[(?:WARN|ERROR)\] (.+)
// group(1) = the message text
```
The `.` does not match `\n` by default, so `(.+)` stops at the end of the line. No need
for `MULTILINE` flag just for this — `find()` advances through the string naturally.

---

## Hint 4 — Why Not Lookbehind Here?

A lookbehind `(?<=\[WARN\] |\[ERROR\] )` has alternatives of lengths 7 and 8 — this
is variable-length and will throw `PatternSyntaxException` in Java 8-13.
The capturing group approach avoids this entirely and is more readable.

---

## Hint 5 — Collecting Results

```/dev/null/hint5.txt#L1-8
List<String> results = new ArrayList<>();
Matcher m = PATTERN.matcher(logText);
while (m.find()) {
    results.add(m.group(1));  // group(1) is the message after "[WARN] " or "[ERROR] "
}
return results;
```
