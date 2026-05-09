# Groups and Capturing — Deep Notes

> Read this like an engineer who needs to explain what the JVM is doing at every step.  
> These notes go beyond "how to use groups" into *why they work the way they do*.

---

## 1. The Capture Buffer: What the Engine Allocates at Compile Time

When you call `Pattern.compile("(\\d{4})-(\\d{2})-(\\d{2})")`, the regex engine doesn't just store a string. It compiles the pattern into a graph of NFA states. During this compilation phase, it performs a **group census**: it counts the number of opening parentheses in the pattern and allocates a **capture buffer** — an array of (start, end) index pairs.

For the pattern above, the engine allocates four slots:

```
slot[0]  → start/end of entire match  (group 0, always present)
slot[1]  → start/end of first  (...)   (group 1)
slot[2]  → start/end of second (...)   (group 2)
slot[3]  → start/end of third  (...)   (group 3)
```

Each slot stores two integers: the start offset (inclusive) and end offset (exclusive) into the input `CharSequence`. This is why `matcher.start(n)` and `matcher.end(n)` are O(1) — they're just array lookups.

**Key insight:** The buffer is allocated based on the *structure* of the pattern (how many `(` occur), not based on the input. If a group never participates in a match, its slot is set to `(-1, -1)`, causing `matcher.group(n)` to return `null` (not empty string — `null`).

**Non-capturing groups `(?:...)` are completely excluded from this census.** They don't get a slot, which is why they can't be referenced by index. This is their primary advantage when you only need grouping for structure or quantification.

---

## 2. Group Numbering: The Left-Paren Rule

The rule is simple but easy to misapply under pressure:

> **Groups are numbered 1, 2, 3, … in the order their OPENING parenthesis appears, left-to-right. Group 0 is always the entire match.**

Non-capturing groups `(?:...)` and lookaheads `(?=...)` do NOT consume a number.

### Example: Flat groups
```
Pattern:  (\d{4})-(\d{2})-(\d{2})
           ^1      ^2      ^3
```

### Example: Nested groups — the outer group comes FIRST
```
Pattern:  ((\d{4})-(\d{2}))
           ^1       ^2  ^3
           |--- outermost opening paren is first
```
Result for input "2024-01":
- `group(1)` = "2024-01"  (the outer group captures everything)
- `group(2)` = "2024"
- `group(3)` = "01"

### Example: Mixed capturing and non-capturing
```
Pattern:  (?:prefix-)(\w+)-(?:suffix-)(\d+)
                     ^1               ^2
```
The `(?:prefix-)` and `(?:suffix-)` groups don't count, so:
- `group(1)` = the `\w+` part
- `group(2)` = the `\d+` part

### Interview trap: counting groups in `(a)((?:b)(c))`
```
Position 0:  (a)        → group 1
Position 3:  ((?:b)(c)) → group 2  (outer opening paren at position 3)
Position 6:  (?:b)      → non-capturing, skipped
Position 10: (c)        → group 3
```
`groupCount()` returns 3.

---

## 3. Named Groups: `(?<name>pattern)`

Named groups are capturing groups with a human-readable label. They are allocated a slot in the capture buffer exactly like numbered groups — and they also get a slot in a name→index map that the `Matcher` maintains.

**Syntax:**
```
(?<year>\d{4})-(?<month>\d{2})-(?<day>\d{2})
```

**Access in Java:**
```java
matcher.group("year")    // returns the captured text
matcher.start("year")    // start offset
matcher.end("year")      // exclusive end offset
```

**Named groups still have numbers.** The named group `(?<year>...)` is still group 1 if it's the first opening paren. You can access it as either `matcher.group(1)` or `matcher.group("year")`. This is useful when mixing named and unnamed groups, though it's generally cleaner to use only one style.

**Name requirements in Java:**
- Must start with a letter (uppercase or lowercase)
- Can contain letters and digits (no hyphens, underscores, or spaces)
- Must be unique within the pattern (Java does not allow duplicate names, unlike some other engines)

**Pattern for extracting a name map (Java 8+):**
```java
Pattern p = Pattern.compile(
    "(?<ip>\\d+\\.\\d+\\.\\d+\\.\\d+).*?(?<port>\\d{2,5})"
);
Matcher m = p.matcher("Connected: 192.168.1.1:8080");
if (m.find()) {
    String ip   = m.group("ip");   // "192.168.1.1"
    String port = m.group("port"); // "8080"
}
```

**Production note:** Prefer named groups in long-lived code. `group(3)` breaks silently when someone inserts a new group earlier in the pattern; `group("method")` does not.

---

## 4. Non-Capturing Groups `(?:...)`: When and Why

Non-capturing groups exist for two reasons:

### 4.1 Grouping for structure (alternation scoping)

Without grouping:
```
Pattern:  cat|dog food   →  "cat"  OR  "dog food"
```

With non-capturing group:
```
Pattern:  (?:cat|dog) food  →  "cat food"  OR  "dog food"
```

The `(?:...)` creates a unit for the engine to operate on, without creating a numbered group.

### 4.2 Grouping for quantifiers without capturing

If you want to match "ha" repeated 3 times (`hahaha`) but don't care about capturing the repetition:
```
Pattern:  (?:ha){3}    → matches "hahaha", no group created
Pattern:  (ha){3}      → matches "hahaha", group(1) = "ha" (the LAST iteration)
```

**Important:** When a capturing group is inside a quantifier, `group(n)` returns the *last* value matched by that iteration of the quantifier, not all values. If you need all repetitions, you must use a loop, not one pattern.

### 4.3 Performance impact

Every capturing group adds a small overhead during matching because the engine must record start/end positions in the buffer every time the group is entered or exited (including during backtracking). For patterns applied millions of times per second (log processing, protocol parsers), replacing unnecessary `(...)` with `(?:...)` measurably reduces allocation pressure.

Benchmark rule of thumb: if you have more than 5-6 capturing groups and you only need 2-3, convert the rest to `(?:...)`.

---

## 5. Backreferences: `\1` Syntax and Engine Mechanics

A backreference `\1` in a pattern means: *match the same text that was captured by group 1 at this point in the match*.

**Critical distinction:** A backreference matches the same *text*, not the same *pattern*. If group 1 captured "hello", then `\1` will match only the literal string "hello" at the current position — not any other string that would have matched `(\w+)`.

### How the engine processes a backreference

When the engine reaches `\1` in the NFA:
1. It reads the current value stored in capture slot 1 (a substring of the input)
2. It attempts to match that exact string at the current position character by character
3. If it matches, it advances past those characters
4. If it fails, it backtracks like any other failed match

This means backreferences can fail even if the group itself matched — they require the captured text to repeat.

### Java syntax: double backslash is required

In a Java string literal, `\` must be escaped as `\\`. A backreference to group 1 is:
```java
// WRONG (won't compile or will misparse):
Pattern.compile("(\w+)\s+\1")

// CORRECT:
Pattern.compile("(\\w+)\\s+\\1")
```

The regex engine sees: `(\w+)\s+\1` — the `\1` is a backreference to group 1.

Note: `\1` through `\9` are backreferences. `\10` and above require `\10` notation and the pattern must actually have that many groups. To avoid ambiguity between backreference `\1` followed by literal `0`, use non-capturing group restructuring.

### Named backreferences

Java supports named backreferences: `\k<name>`:
```java
Pattern.compile("(?<word>\\w+)\\s+\\k<word>")
```
This is equivalent to a numbered backreference but more readable.

### Case-insensitive backreferences

With `Pattern.CASE_INSENSITIVE`, a backreference matches case-insensitively:
```java
Pattern p = Pattern.compile("(\\w+)\\s+\\1", Pattern.CASE_INSENSITIVE);
Matcher m = p.matcher("Hello hello");
m.find(); // true — "Hello" and "hello" are equal case-insensitively
m.group(1); // "Hello" (the first captured text, not the backreference text)
```

### Backreference to an unmatched group

If a backreference refers to an optional group that didn't participate in the match:
```java
Pattern p = Pattern.compile("(a)?\\s+\\1");
Matcher m = p.matcher("  "); // no 'a', group 1 didn't match
```
In most NFA engines (and in Java), a backreference to an unmatched group matches the empty string — *not* nothing. This can cause surprising matches. Test this case explicitly.

---

## 6. Nested Groups: Numbering by Structural Depth

Nested groups are groups whose entire span is contained within another group. The numbering rule — opening paren left-to-right — applies regardless of nesting.

### Example: Triple nesting
```
Pattern:  (a(b(c)d)e)
           1 2 3
```
For input "abcde":
- `group(0)` = "abcde"
- `group(1)` = "abcde" (outermost group)
- `group(2)` = "bcde"  (middle group)  — wait, let me re-check
```
(a(b(c)d)e)
 ^1         → captures "abcde" if input is "abcde"
   ^2       → captures "bcd"
     ^3     → captures "c"
```
Actually: the outer group `(...)` spans positions 0-4 of the pattern metacharacters. Let's trace for input "abcde":
- Group 1: `(a(b(c)d)e)` → matches "abcde"
- Group 2: `(b(c)d)` → matches "bcd"
- Group 3: `(c)` → matches "c"

The takeaway: outer groups contain more text than inner groups because they wrap more of the pattern.

### Practical usage: date with time

```java
Pattern dt = Pattern.compile(
    "((?<year>\\d{4})-(?<month>\\d{2})-(?<day>\\d{2}))" +
    "(?:T(?<hour>\\d{2}):(?<min>\\d{2}))?"
);
//   ^1 ^2              ^3              ^4               ^5     ^6
```
Groups: 1=full date, 2=year, 3=month, 4=day, 5=hour, 6=minute.
Group 5 and 6 may be null if the "T..." portion is absent.

---

## 7. Unmatched Optional Groups: Returning `null`

When a group is syntactically present in the pattern but its branch was not taken during a successful match, `matcher.group(n)` returns **`null`** — not empty string, not zero-length match, **`null`**.

### Case 1: Optional group `(pattern)?`
```java
Pattern p = Pattern.compile("(\\w+)(?:-(\\w+))?");
Matcher m = p.matcher("hello");
m.find();
m.group(1); // "hello"
m.group(2); // null  ← the optional group didn't participate
```

### Case 2: Alternation with unequal groups
```java
Pattern p = Pattern.compile("(\\d+)|(\\w+)");
Matcher m = p.matcher("abc");
m.find(); // matches "abc" via second alternative
m.group(1); // null  ← first alternative wasn't taken
m.group(2); // "abc"
```

### The defensive check
```java
String val = matcher.group(2);
if (val != null) {
    // safe to use val
}

// Or using Optional in Java 9+:
Optional<String> opt = Optional.ofNullable(matcher.group(2));
```

**Interview trap:** "What does `matcher.group(n)` return when the group matches an empty string?"  
Answer: `""` (empty string). This is different from `null`, which means the group *didn't participate* at all. This distinction matters when checking for optional groups.

---

## 8. Java API Deep Dive

### `matcher.groupCount()`
Returns the number of capturing groups in the pattern. Does NOT include group 0. A pattern with `(a)(b)` returns 2. A pattern with `(?:a)(b)` returns 1.

```java
Pattern p = Pattern.compile("(\\w+)(?:-)(\\d+)");
p.matcher("any").groupCount(); // 2
```

### `matcher.group()` and `matcher.group(0)`
Equivalent — both return the entire matched text. Use `matcher.group()` for clarity.

### `matcher.start(n)` and `matcher.end(n)`
Return the start (inclusive) and end (exclusive) offsets of group `n` in the input string.

```java
Matcher m = Pattern.compile("(\\d+)").matcher("age: 42 years");
m.find();
m.start(1); // 5  (offset of '4' in input)
m.end(1);   // 7  (offset after '2')
m.group(1); // "42"

// Verify: input.substring(m.start(1), m.end(1)).equals(m.group(1)) → true
```

### `matcher.start()` and `matcher.end()`
Shorthand for `start(0)` and `end(0)` — start/end of the entire match.

### `matcher.replaceAll(String replacement)` with group references
In replacement strings, `$1`, `$2`, etc., reference captured groups:
```java
String result = Pattern.compile("(\\d{4})-(\\d{2})-(\\d{2})")
    .matcher("Born: 1990-07-04")
    .replaceAll("$3/$2/$1");
// "Born: 04/07/1990"
```

Use `$0` for the entire match. Named groups: `${name}`.

### `matcher.appendReplacement()` and `matcher.appendTail()`
For building replacement strings programmatically (e.g., conditionally transforming captures):
```java
StringBuffer sb = new StringBuffer();
Matcher m = Pattern.compile("(\\w+)").matcher("hello world");
while (m.find()) {
    m.appendReplacement(sb, m.group(1).toUpperCase());
}
m.appendTail(sb);
// sb.toString() = "HELLO WORLD"
```

### `matcher.reset()`
Resets the matcher to the beginning of the input without recompiling. Useful when re-using a matcher:
```java
Matcher m = p.matcher("first input");
m.find();
m.reset("second input"); // reset to new input
m.find();
```

### Thread safety
`Matcher` is **not thread-safe**. `Pattern` is thread-safe and immutable. The canonical pattern is:
```java
private static final Pattern PATTERN = Pattern.compile("...");  // shared
// Per-call:
Matcher m = PATTERN.matcher(input);  // new Matcher each time
```

---

## 9. Performance Considerations

### Overhead of capturing groups
Every time the engine enters or exits a capturing group (including during backtracking), it writes to the capture buffer. In a hot loop with complex patterns, this adds up:

- **Measured overhead:** ~5-15% slower with 6 capturing groups vs 6 non-capturing groups in a tight loop (input-dependent)
- **Worst case:** Complex patterns with many optional groups that the engine enters and exits during backtracking

### When to use `(?:...)`
- Alternation scoping: `(?:yes|no)` — you don't need to capture which branch matched
- Quantified groups: `(?:ha)+` — you don't need the last repetition
- Grouping for precedence: `(?:abc)?` — making abc optional as a unit

### Pattern caching
`Pattern.compile()` is expensive (compiles to NFA, builds the capture buffer map). Always cache compiled patterns as static finals:

```java
// BAD — recompiles on every call:
public List<String> extract(String s) {
    Matcher m = Pattern.compile("(\\d+)").matcher(s);
    ...
}

// GOOD — compiled once:
private static final Pattern DIGITS = Pattern.compile("(\\d+)");
public List<String> extract(String s) {
    Matcher m = DIGITS.matcher(s);
    ...
}
```

### String.matches() and String.replaceAll() hidden costs
Both methods call `Pattern.compile()` internally on every invocation. Never use them in a hot path. Even for one-shot usage, `Pattern.compile(...).matcher(s).matches()` is not worse and makes the compile cost visible.

---

## 10. Debugging Capturing Group Issues

### Symptom: `group(n)` returns `null` unexpectedly
Diagnose:
1. Print `matcher.groupCount()` to confirm the pattern has that many groups
2. Check if the group is inside an optional `(...)?` that didn't participate
3. Check for alternation — if the group is in one branch and the other branch matched
4. Verify the match succeeded (always check `find()` or `matches()` return value before calling `group()`)

### Symptom: Wrong text captured
Diagnose:
1. Are you sure which group number you want? Print all groups from 0 to `groupCount()`
2. Did you accidentally add a group earlier in the pattern, shifting all indices?
3. Is there a quantifier on the group? If so, remember only the LAST repetition is captured

### Debugging template
```java
Pattern p = Pattern.compile(YOUR_PATTERN);
Matcher m = p.matcher(YOUR_INPUT);
System.out.println("Groups: " + p.groupCount());
while (m.find()) {
    System.out.println("Full match: [" + m.group(0) + "]");
    for (int i = 1; i <= p.groupCount(); i++) {
        System.out.println("  group(" + i + ") = [" + m.group(i) + "]  " +
                           "at " + m.start(i) + "-" + m.end(i));
    }
}
```

### Common mistake: calling `group()` before `find()`
```java
Matcher m = p.matcher(input);
String s = m.group(1); // IllegalStateException: No match found
// You must call m.find() or m.matches() first!
```

---

## 11. Interview Traps and Counterintuitive Behavior

### Trap 1: "What is group 0?"
Expected wrong answer: "there is no group 0" or "null".  
Correct answer: Group 0 is always the entire matched text. `matcher.group(0) == matcher.group()`.

### Trap 2: "What does `group(n)` return for an unmatched optional group?"
Expected wrong answer: `""` (empty string).  
Correct answer: `null`. The group participated in the pattern structure but the engine took a path that didn't go through it.

### Trap 3: "In `(ha){3}`, what is group 1 after matching 'hahaha'?"
Expected wrong answer: "hahaha".  
Correct answer: "ha" — the LAST value the group matched. Each repetition overwrites the capture slot.

### Trap 4: "How many groups does `(?:a)(b)(?:c)(d)` have?"
Expected wrong answer: 4.  
Correct answer: 2. The `(?:...)` groups don't count.

### Trap 5: "Can a backreference reference a group that comes after it in the pattern?"
Answer: No — forward references are not supported in Java regex (and most NFA engines). The referenced group must appear before the backreference.

### Trap 6: "Are named groups also numbered?"
Answer: Yes. `(?<year>\d{4})` if it's the first opening paren is also accessible as `group(1)`. Named and numbered access are equivalent.

### Trap 7: "What happens if you use `\1` in Java without double-backslash?"
Answer: `\1` in a Java string literal is an octal escape (`\001`, the SOH control character). The pattern engine would then try to match that control character, not a backreference. The correct Java string for a backreference to group 1 is `"\\1"`.

---

## 12. Production Concerns

### Robustness: always null-check optional groups
In production code parsing external data, optional groups will be null. Defensive code:
```java
String bytes = matcher.group("bytes");
long byteCount = bytes != null ? Long.parseLong(bytes) : -1L;
```

### Regex injection: never build patterns from user input without sanitization
```java
// DANGEROUS:
Pattern.compile("prefix-" + userInput + "-suffix")

// SAFE:
Pattern.compile("prefix-" + Pattern.quote(userInput) + "-suffix")
```
`Pattern.quote(s)` wraps the string in `\Q...\E`, treating it as a literal.

### Group reuse in `replaceAll`: `$` syntax in replacement strings
In the replacement string argument, `$1` and `${name}` reference groups. But literal `$` and `\` in replacement strings must be escaped:
```java
matcher.replaceAll("\\$1") // replaces with literal $1 (escaped)
matcher.replaceAll("$1")   // replaces with the content of group 1
```
Use `Matcher.quoteReplacement(literal)` for safe literal replacements:
```java
matcher.replaceAll(Matcher.quoteReplacement(userProvidedReplacement));
```

---

## 13. Quick Reference

| Syntax | Name | What it does |
|---|---|---|
| `(pattern)` | Capturing group | Matches and stores text; numbered L→R by `(` |
| `(?:pattern)` | Non-capturing group | Groups without storing; no index assigned |
| `(?<name>pattern)` | Named capturing group | Accessible via `matcher.group("name")` |
| `\1`, `\2` … | Numbered backreference | Matches same text as group N |
| `\k<name>` | Named backreference | Matches same text as named group |
| `matcher.group()` | Whole-match accessor | Equivalent to `matcher.group(0)` |
| `matcher.group(n)` | Numbered accessor | Returns captured text or `null` |
| `matcher.group("name")` | Named accessor | Returns captured text or `null` |
| `matcher.groupCount()` | Group count | Number of capturing groups (excludes group 0) |
| `matcher.start(n)` | Start offset | Inclusive start of group n in input |
| `matcher.end(n)` | End offset | Exclusive end of group n in input |
| `$1`, `${name}` | Replacement reference | In `replaceAll()` replacement strings |

---

## Further Reading

- [java.util.regex.Pattern Javadoc](https://docs.oracle.com/en/java/docs/api/java.base/java/util/regex/Pattern.html) — definitive reference for all Java-specific behavior
- Friedl, *Mastering Regular Expressions* (3rd ed.) — Chapter 4 covers capture mechanics in detail
- [regex101.com](https://regex101.com/) — set flavor to "Java 8" for interactive group debugging
