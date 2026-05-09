# Alternation — Deep Notes

> Read this as a systems engineer debugging why a regex "almost works."  
> Alternation looks trivial (`a|b`) but controls every branching decision the NFA makes.

---

## 1. How the NFA Processes Alternation

To understand alternation, you must first understand the execution model: Java's regex engine is an **NFA** (Nondeterministic Finite Automaton), not a DFA. The key property of an NFA is that it explores multiple possible paths through the pattern and reports the *first successful path* according to the rules of the implementation.

For alternation `a|b`, the NFA compiles to roughly:

```
         ┌──→ [match a] ──→ ACCEPT
START ───┤
         └──→ [match b] ──→ ACCEPT
```

When the engine reaches this fork:
1. It saves its current position (a "backtrack point") on an internal stack
2. It tries the **left** branch first
3. If the left branch succeeds, it **immediately** reports a match — the right branch is **never tried**
4. If the left branch fails at any point, it restores the saved position and tries the **right** branch
5. If both branches fail, the overall match fails at this position

This is **Thompson NFA simulation** at a conceptual level. The "first match wins" property of the left branch is the most important thing to understand about alternation ordering.

### The engine's commitment

When the NFA takes a branch and that branch succeeds, the engine commits to it for that match attempt. This is different from a DFA (which would always find the "maximal" match). The NFA finds the *first defined match*, which is the leftmost alternative that succeeds from the current position.

---

## 2. Ordering Sensitivity: Why `cat|catalog` is Usually Wrong

The canonical example:

```
Pattern:  cat|catalog
Input:    "catalog"

Step 1: Engine at position 0 of "catalog"
Step 2: Try left branch "cat"
  - c matches c ✓
  - a matches a ✓
  - t matches t ✓
  - Branch succeeded! Match = "cat" (positions 0-2)
Step 3: Right branch "catalog" is NEVER tried for this position
Result: match = "cat" in "catalog", leaving "alog" unmatched
```

**The fix: put more specific (longer) alternatives first:**
```
Pattern:  catalog|cat
Input:    "catalog"

Step 1: Try "catalog"
  - c,a,t,a,l,o,g all match ✓
  - Match = "catalog" ✓
```

### When ordering DOESN'T matter

If the alternatives cannot both match at the same position, ordering is irrelevant:
```
Pattern:  \d+|\w+
Input:    "abc"

"abc" at position 0:
  - Try \d+ → 'a' is not a digit → fails
  - Try \w+ → 'a' is a word char → succeeds
```
Here ordering doesn't matter because `\d+` can never match where `\w+` matches (for non-digit input) and vice versa for pure-digit input. But for mixed input like "123abc", `\d+` matches "123" and never tries `\w+`, so you'd get just "123".

### The real rule: ordering matters whenever alternatives can match AT THE SAME POSITION

If the left alternative can succeed at position P, the right alternative will never be tried at P, even if the right alternative would produce a "longer" or "better" match.

---

## 3. Precedence Rules: The Low Precedence of `|`

The `|` operator has the **lowest precedence** of all regex operators. This surprises many programmers who expect it to work like addition (`+`) in arithmetic, where surrounding context binds tighter.

**Precedence from highest to lowest (in Java regex):**
1. Escape sequences and character class atoms: `\d`, `[abc]`
2. Quantifiers: `*`, `+`, `?`, `{n,m}`
3. Concatenation (implicit): `ab` means "a then b"
4. Alternation: `|`

### Consequence: `abc|def` is `(abc)|(def)`, not `ab(c|d)ef`

```
Pattern:  http|ftp://host
Parsed:   "http"  OR  "ftp://host"

This is almost certainly wrong if you wanted (http|ftp)://host.

Fix:      (?:http|ftp)://host
```

```
Pattern:  cat|catalog food
Parsed:   "cat"  OR  "catalog food"
Spaces don't bind tighter than |.
```

### Full pattern scope
The `|` applies to the ENTIRE surrounding scope:
- In the top-level pattern: the entire left vs the entire right
- Inside a group `(a|bc|def)`: scope is limited to inside the group
- This is how you control precedence — use groups to limit the scope of `|`

### Example: grouping controls scope
```
Pattern:  (?:pre|post)fix     → "prefix" or "postfix"
Pattern:  pre|postfix         → "pre"    or "postfix"
Pattern:  pre|post(?:fix)?    → "pre"    or "post"    or "postfix"
```

---

## 4. Alternation Inside Character Classes: A Common Confusion

Character classes `[...]` look like alternation but are entirely different:

```
[cat]   → ONE character: c, a, or t
cat     → the three-character WORD "cat"
c|a|t   → alternation: the word "c", or the word "a", or the word "t"
          (functionally same as [cat] for single-char alternatives but much slower)
```

**Inside a character class, the `|` character is LITERAL, not alternation:**
```
[yes|no]   → matches one of: y, e, s, |, n, o
             NOT "yes" or "no"
(?:yes|no) → alternation: "yes" or "no" (correct way)
```

This is a very common mistake when someone writes `[DEBUG|INFO|WARN]` thinking they're matching log levels. This matches one character that is D, E, B, U, G, I, N, F, O, W, A, or |.

The correct pattern: `(?:DEBUG|INFO|WARN|ERROR|FATAL)`.

### When to use `[...]` vs `|`

| Use case | Correct syntax |
|---|---|
| Single-character alternatives | `[aeiou]`, `[0-9A-Fa-f]` |
| Multi-character word alternatives | `cat\|dog\|bird` or `(?:cat\|dog\|bird)` |
| Single char from large set | `[a-zA-Z]`, `\w`, `\d` |
| Negation | `[^aeiou]` (no alternation equivalent) |

Character classes are implemented as a **bitmap** or **interval tree** in the engine — O(1) lookup for any character. Alternation is O(k) NFA branches. For single-character alternatives, always prefer `[...]`.

---

## 5. Performance: NFA Branching and Catastrophic Backtracking

### How alternation creates exponential paths

Each `|` creates a fork in the NFA. With `k` alternatives, the engine may need to explore up to `k` paths at each position. With `n` positions in the input and `k` alternatives, worst case is O(k^n) — exponential in input length.

### The danger: shared prefixes with backtracking

Consider this pattern attempting to validate an email-like token:
```
Pattern:  \w+@\w+|\w+@\w+\.\w+
Input:    "user@example.com"

Try left branch: \w+@\w+
  - \w+ matches "user", @ matches @, \w+ matches "example"
  - Left branch succeeds! Match = "user@example"
  - ".com" is left unmatched
```

Even worse: patterns like `(a+)+b` with input "aaaaaaaaaaaac" cause exponential backtracking because each level of the nested quantifier can try multiple split points. Alternation combined with quantifiers is the classic ReDoS recipe.

### Factoring shared prefixes

If many alternatives share a common prefix, move the prefix outside the alternation:

```
SLOW:    color|colour|colored|colouring
         ^^^^^
         Shared prefix "col" is re-matched for each alternative

FAST:    col(?:or|our|ored|ouring)
         ^^^
         "col" matched once, then branch
```

More aggressive factoring:
```
SLOW:    color|colour
FAST:    colou?r        (the 'u' is optional — same semantics, one pass)
```

### Atomic groups for alternation without backtracking (Java 17+)

In Java 17+, atomic groups `(?>...)` prevent the engine from reconsidering a choice:
```java
Pattern p = Pattern.compile("(?>cat|catalog)");
// Once "cat" is matched, the engine will NEVER backtrack into the group
// to try "catalog" — even if the rest of the pattern fails
// This eliminates backtracking overhead at the cost of potentially missing matches
```

Use atomic groups when you know the alternation is deterministic (only one alternative can possibly succeed at any given position).

---

## 6. Alternation with Quantifiers

Alternation inside a quantified group behaves in potentially surprising ways:

### `(a|b)*` vs `[ab]*`

These are semantically equivalent but the NFA behavior differs:
- `[ab]*`: single state machine transition, O(n) matching
- `(a|b)*`: two NFA branches per character, potentially O(2^n) in pathological cases

For single-character alternatives inside a repetition, always use character classes.

### The "last value" trap with quantified capturing groups

```java
Pattern p = Pattern.compile("(\\d+,)+");
Matcher m = p.matcher("1,2,3,");
m.find();
m.group(1); // "3,"  ← only the LAST repetition is stored
```

If you need all repetitions, restructure:
```java
// Option 1: capture the whole thing, then split
Pattern p = Pattern.compile("((?:\\d+,)+)");
// group(1) = "1,2,3," → split by ","

// Option 2: iterate find() over the inner pattern
Pattern inner = Pattern.compile("\\d+");
```

### Alternation with `?` (zero-or-one)

`(?:a|b)?` means: optionally match "a" or "b".
The engine tries: match "a" → if fails, try "b" → if fails, match empty string (since the whole thing is optional).

This can produce surprising matches:
```
Pattern:  (?:yes|no)?thing
Input:    "nothing"

Position 0: try "yes" → 'n' ≠ 'y' → fail
            try "no"  → 'n'='n', 'o'='o' → success!
            then "thing" → t,h,i,n,g match ✓
Result: "nothimg" → match! (no + thing)
```

---

## 7. Alternation with Anchors

### At the top level: `^a|b$`

**Parsed as:** `(^a)|(b$)` — NOT `^(a|b)$`!

```
Pattern:  ^DEBUG|ERROR$
Parsed:   (^DEBUG) OR (ERROR$)
Matches:  "DEBUG anything"  OR  "anything ERROR"
          NOT: only "DEBUG" or "ERROR" as the whole string
```

**The fix:**
```
Pattern:  ^(?:DEBUG|ERROR)$
```

### Inside a group: anchors apply to their scope

Anchors inside a group apply relative to the group's start/end position if the group is used with certain flags, but in standard Java regex, `^` and `$` always refer to the start/end of the entire input (or line in `MULTILINE` mode). This does not change inside alternation groups.

### `MULTILINE` flag interaction

With `Pattern.MULTILINE`, `^` matches at the start of each line and `$` matches at the end of each line:
```java
Pattern p = Pattern.compile("^(?:ERROR|WARN)", Pattern.MULTILINE);
// Matches lines that start with ERROR or WARN
```

Without `MULTILINE`, `^` only matches the very beginning of the entire input.

---

## 8. Java-Specific Alternation Notes

### No possessive alternation

Java does not support possessive quantifiers on alternation directly (you can't write `(a|b)*+`). The workaround is to use an atomic group: `(?>a|b)*` which prevents backtracking into the group once it has committed.

Possessive quantifiers (`*+`, `++`, `?+`) DO work in Java, but only on atoms and groups, not on alternation directly.

### The `COMMENTS` flag and alternation

With `Pattern.COMMENTS` (`(?x)` inline flag), whitespace is ignored and `#` starts comments. This is extremely useful for documenting complex alternation:

```java
Pattern p = Pattern.compile(
    "(?x)                     # enable comments\n" +
    "  (?:https?|ftp|sftp)    # protocol alternatives\n" +
    "  ://                    # separator\n" +
    "  ([^/\\s]+)             # host\n"
);
```

Note that inside character classes `[...]`, whitespace and `#` are still literal even in `COMMENTS` mode.

### String literal encoding of alternation in Java

The `|` character needs no escaping in Java regex strings. However, if you're building patterns programmatically with string concatenation, be careful about operator precedence:

```java
// WRONG — joins as one big alternation:
String p = prefix + "|" + suffix;
// This is fine IF you want "prefix" or "suffix"

// WRONG if you want (prefix)(middle)(suffix):
String p = prefix + "|" + middle + "|" + suffix;
// Now you have three alternatives: prefix, middle, suffix
// Fix:
String p = "(?:" + prefix + ")" + middle + "(?:" + suffix + ")";
// But this changes semantics completely — rethink the design
```

---

## 9. Factoring Techniques for Production Patterns

### Longest-first ordering (the golden rule)

When alternatives can overlap at their starting characters, put longer/more-specific alternatives first:

```
WRONG:  Jan|January|Feb|February
        "January" → matches "Jan", stops

CORRECT: January|Jan|February|Feb
         "January" → tries "January" first, matches fully

BETTER:  Jan(?:uary)?|Feb(?:ruary)?
         Factored — no ordering issue since Jan? covers both
```

### Trie-based factoring for many alternatives

If you have many alternatives like `apple|apricot|avocado|banana|blueberry`, factor by first character:

```
(?:a(?:pple|pricot|vocado)|b(?:anana|lueberry))
```

This reduces the number of NFA states and backtracking points. For very large word lists, consider converting to a DFA (using a library like dk.brics.automaton) rather than using regex at all.

### The `color|colour` pattern: optional letters vs alternation

```
color|colour    → 2 alternatives, each tried independently
colou?r         → 1 pattern, single NFA path with an optional node
```

Both match the same strings but the second is strictly more efficient.

---

## 10. Debugging Alternation Issues

### Symptom: Pattern matches partial text
Most common cause: ordering bug — shorter alternative consumed input before longer one had a chance.

**Diagnosis steps:**
1. Test each alternative in isolation on the problematic input
2. Identify which alternative is matching when you don't want it to
3. Reorder so the more specific alternative comes first
4. Or use anchors to prevent partial matches: `\b(cat|catalog)\b`

### Symptom: Pattern matches completely wrong text
Check precedence: is the `|` scoped where you think it is?
```java
// Debug: print what the pattern actually compiles to
Pattern p = Pattern.compile(yourPattern);
System.out.println(p.pattern()); // prints the pattern string
// Then manually trace which part the | applies to
```

### Symptom: Performance degradation with large input
Alternation + backtracking symptom. Profile with:
```java
long start = System.nanoTime();
matcher.find();
long elapsed = System.nanoTime() - start;
// If elapsed is unexpectedly large, you have catastrophic backtracking
```

Fix: add anchors, reduce alternatives, use atomic groups, or replace the regex with a state machine.

### The isolation technique
When debugging complex alternation, break it down:
```java
// Instead of debugging this monster:
String p = "(?:abc|def|ghi|jkl)(?:mno|pqr)";

// Test each part separately:
String p1 = "(?:abc|def|ghi|jkl)";
String p2 = "(?:mno|pqr)";
// Does p1 match what you expect?
// Does p2 match what you expect?
// Does p1 + p2 together work?
```

---

## 11. Interview Traps

### Trap 1: "What does `cat|catalog` match in 'catalog'?"
Expected wrong answer: "catalog".  
Correct answer: "cat" — the left alternative succeeds and the engine commits. The full word is never tried.

### Trap 2: "Does `(?:a|b)` create a capturing group?"
Expected wrong answer: "yes, it's group 1".  
Correct answer: No — `(?:...)` is non-capturing. `groupCount()` returns 0. The `?:` prefix suppresses the slot.

### Trap 3: "What is the difference between `[cat]` and `cat`?"
Expected wrong answer: "they're equivalent".  
Correct answer: `[cat]` matches a single character that is c, a, or t. `cat` matches the three-character string "cat".

### Trap 4: "Will `^error|warning$` match a line containing only 'error'?"
Expected answer: "yes" (in MULTILINE mode, `^error` matches).  
But the pattern is parsed as `(^error)|(warning$)` — so it matches lines STARTING with "error" OR lines ENDING with "warning". It does NOT match only "error" or only "warning" as complete lines. For complete-line matching: `^(?:error|warning)$`.

### Trap 5: "Does alternation order matter if all alternatives are different lengths?"
Answer: Yes, if they share a common prefix or if a shorter one can match at the same position where a longer one should. Length alone doesn't determine ordering; what matters is whether the shorter one could match before the longer one at the same offset.

### Trap 6: "How do you match the literal pipe character `|`?"
Answer: Escape it as `\|` in the regex, which in Java is `"\\|"`. Inside a character class `[|]`, no escaping is needed (but it's still good practice).

### Trap 7: "`(a|b)*` vs `[ab]*` — which is faster?"
Answer: `[ab]*` is significantly faster for single-character alternatives because it uses a character class (bitmap lookup) instead of NFA branching. `(a|b)*` creates 2 NFA states per character; `[ab]*` creates 1.

---

## 12. Production Concerns

### Validate alternation patterns before deployment

Test boundary cases:
1. Does each alternative match something it should? Test with canonical examples.
2. Does each alternative *not* match something it shouldn't? Test with near-misses.
3. With complex ordering-sensitive alternatives, test the ambiguous cases explicitly.

### Alternation in validators: anchor everything

Validators should use `matches()` or anchored patterns:
```java
// Validates that the ENTIRE string is one of the log levels:
private static final Pattern LOG_LEVEL =
    Pattern.compile("DEBUG|INFO|WARN|ERROR|FATAL");
// WRONG: "DEBUG: message" would return true (find() semantics with matches())
// Actually matches() anchors automatically, but the intent is clearer with ^...$

private static final Pattern LOG_LEVEL_SAFE =
    Pattern.compile("^(?:DEBUG|INFO|WARN|ERROR|FATAL)$");
```

### Compile-time vs runtime alternation

If you're building alternation patterns dynamically (e.g., from a configuration list of keywords), be careful about:
1. Escaping each keyword with `Pattern.quote()` before inserting into alternation
2. Ordering — sort by length descending if keywords can be prefixes of each other
3. Deduplication — duplicate alternatives waste engine time

```java
List<String> keywords = List.of("cat", "catalog", "dog");
String pattern = keywords.stream()
    .sorted(Comparator.comparingInt(String::length).reversed()) // longest first
    .map(Pattern::quote)
    .collect(Collectors.joining("|"));
Pattern p = Pattern.compile(pattern);
```

---

## 13. Quick Reference

| Concept | Syntax | Behavior |
|---|---|---|
| Basic alternation | `a\|b` | Match "a" or "b" |
| Left-first semantics | `cat\|catalog` | Tries "cat" first; "catalog" if "cat" fails |
| Grouped alternation | `(?:a\|b)c` | "(a or b) then c" |
| Capturing alternation | `(a\|b)c` | "(a or b) then c", captures which branch |
| Anchored alternation | `^(?:a\|b)$` | Whole string must be "a" or "b" |
| Factored prefix | `co(?:lor\|lour)` | Avoids re-trying "co" for each alternative |
| Character class | `[abc]` | Single char: a, b, or c — NOT alternation |
| Literal pipe | `\\|` | Matches the `\|` character literally |
| Atomic group | `(?>a\|b)` | No backtracking into the group (Java 17+) |
| Case insensitive | `(?i)` or flag | All alternatives match case-insensitively |

---

## Further Reading

- [Java Pattern.compile Javadoc](https://docs.oracle.com/en/java/docs/api/java.base/java/util/regex/Pattern.html) — complete flag and syntax reference
- Friedl, *Mastering Regular Expressions* (3rd ed.) — Chapter 4: "The Mechanics of Expression Processing"
- OWASP ReDoS: [https://owasp.org/www-community/attacks/ReDoS](https://owasp.org/www-community/attacks/ReDoS) — production security implications
- [regex101.com](https://regex101.com/) — step-by-step debugger shows NFA branching choices visually
