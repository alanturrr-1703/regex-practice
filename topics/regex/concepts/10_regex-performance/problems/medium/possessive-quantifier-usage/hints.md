# Hints — Possessive Quantifier Usage

---

## Hint 1 — Possessive Quantifier Syntax

A possessive quantifier is formed by appending `+` to a regular quantifier:
```/dev/null/hint1.txt#L1-4
+   greedy (default)
++  possessive (never gives back)
*+  possessive zero-or-more
?+  possessive zero-or-one
```
Example: `[\d:]++` — match one or more digit-or-colon characters, possessively.

---

## Hint 2 — Pattern Structure

The log line format is: `TIMESTAMP SEVERITY MESSAGE`
```/dev/null/hint2.txt#L1-2
([\d:]++) ([A-Z]++) (.*)
```
Each group is separated by a literal space. The message part uses `.*` (may be empty).

---

## Hint 3 — Full Pattern

```/dev/null/hint3.txt#L1-1
^([\d:]++) ([A-Z]++) (.*)$
```
Use `Pattern.DOTALL` only if you expect multi-line messages. For single-line, default
behavior (`.` doesn't match `\n`) is appropriate.

---

## Hint 4 — Extracting Groups

```/dev/null/hint4.txt#L1-7
Matcher m = PATTERN.matcher(line);
if (m.matches()) {
    String timestamp = m.group(1);
    String severity  = m.group(2);
    String message   = m.group(3);
    return Optional.of(new TokenizedLine(timestamp, severity, message));
}
return Optional.empty();
```

---

## Hint 5 — The TokenizedLine Class

Define `TokenizedLine` as a simple class with three `String` fields (or a Java 16+
`record`). For Java 8+ compatibility, use a class with a constructor and getters.
