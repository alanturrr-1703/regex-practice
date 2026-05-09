# Regex Basics — Deep Notes

> These notes are written for someone who wants to understand the *why*, not just the *what*.
> If you are looking for a quick cheat sheet, read the README instead.
> Here we go deep: engine mechanics, backtracking, Java-specific traps, production patterns.

---

## 1. What IS a Regex Engine? (The Mental Model You Actually Need)

Most tutorials treat regex as magic syntax. It isn't. A regex is a **specification of a finite automaton**. When you call `Pattern.compile("\\d+")`, Java parses the pattern string and constructs an internal graph — a **Nondeterministic Finite Automaton (NFA)**. When you call `matcher.find()`, Java walks that NFA over the input string.

There are two families of regex engines:

### DFA (Deterministic Finite Automaton)
- Runs in **O(n)** time always, where n = input length.
- Does not support backreferences, lookahead, or possessive quantifiers.
- Used by: `awk`, `grep` (in some implementations), `RE2` (Google's library).
- Trades feature richness for guaranteed linear time.

### NFA (Nondeterministic Finite Automaton)  
- Supports the full feature set: backreferences, lookaheads, lookbehinds, possessive quantifiers.
- Runtime: **O(n)** in the best case, **O(2^n)** in catastrophic backtracking cases.
- Used by: **Java** (`java.util.regex`), Perl, Python, Ruby, JavaScript, .NET, PHP (PCRE).

**Java uses an NFA engine.** This has huge consequences:

1. The ORDER of alternatives in `(a|ab)` matters — the engine tries left-to-right and takes the first match.
2. Greedy quantifiers consume as much as possible, then backtrack if the rest of the pattern fails.
3. Catastrophic backtracking is a real attack vector (ReDoS — Regex Denial of Service).

Understanding this is not theoretical — it changes how you write every non-trivial pattern.

---

## 2. The NFA Execution Model in Detail

Let's trace the NFA executing `ab+c` against the string `"abbbc"`:

```
Pattern: a b+ c
Input:   a b b b c
         ^         (position 0)
```

**Step 1:** The engine tries to match `a` at position 0. `'a'` == `'a'`. OK, advance.

**Step 2:** Try to match `b+` starting at position 1. `b+` is greedy — it consumes as many `b`s as possible. It consumes `b`, `b`, `b` (positions 1, 2, 3). Now at position 4.

**Step 3:** Try to match `c` at position 4. `'c'` == `'c'`. OK. Pattern exhausted. Match found: `"abbbc"` from pos 0 to 4.

Now let's trace a **backtracking** case: `ab+c` against `"abbd"`:

**Step 1:** `a` matches pos 0. Advance.
**Step 2:** `b+` greedily consumes `b`, `b` (pos 1-2). Now at pos 3.
**Step 3:** Try `c` at pos 3. `'d'` != `'c'`. **FAIL.**
**Step 4 (backtrack):** `b+` gives back one character. Now at pos 2.
**Step 5:** Try `c` at pos 2. `'b'` != `'c'`. **FAIL.**
**Step 6 (backtrack):** `b+` gives back another character. Now at pos 1. But `b+` requires at least 1 match, and it already consumed only 1 `b`. Can't give more back. `b+` fails entirely.
**Step 7:** The whole pattern fails to match at pos 0.
**Step 8:** Since `find()` was called, the engine advances to pos 1 and tries again. `'b'` != `'a'`. Fail.
... continues until end of string. Result: no match.

This backtracking dance is happening for every quantifier, every alternative. In well-written patterns, it's fast. In pathological cases, it explodes.

---

## 3. Literal Matching — It's Not as Simple as You Think

### The Metacharacter Problem

The following characters are metacharacters in Java's regex syntax — they have special meaning and **do not match themselves**:

```
. * + ? [ ] ( ) { } ^ $ | \
```

If you need to match a literal `.`, you write `\\.` in a Java string (the `\\` becomes a single `\` passed to the engine, and `\.` tells the engine to match a literal dot).

This is the most common source of subtle bugs. Consider validating an IP address segment with `.` instead of `\\.`:

```
Pattern "1.2.3.4" would match "1X2Y3Z4" because . means "any char"!
```

### Pattern.quote() — The Right Way to Match Literals

When you have a user-provided literal to match, **never** build a regex by string concatenation. Use `Pattern.quote()`:

```
String userInput = "price: $5.00";
Pattern p = Pattern.compile(Pattern.quote(userInput));
```

`Pattern.quote(s)` wraps the string in `\Q...\E`, which tells the engine to treat everything inside as literal characters, including metacharacters.

### \Q...\E in Patterns

You can inline literal blocks anywhere in a pattern:

```
\Q(.*)\E  matches the literal string "(.*)" not a capturing group with quantifier
```

This is invaluable when building patterns programmatically from user input.

---

## 4. Shorthand Character Classes — The Internal Definitions

Java defines these shorthands in **two modes**:

### ASCII Mode (default)

| Shorthand | Equivalent | Matches |
|-----------|-----------|---------|
| `\d` | `[0-9]` | ASCII digits only |
| `\D` | `[^0-9]` | NOT an ASCII digit |
| `\w` | `[a-zA-Z0-9_]` | ASCII word chars |
| `\W` | `[^a-zA-Z0-9_]` | NOT an ASCII word char |
| `\s` | `[ \t\n\r\f\x0B]` | ASCII whitespace |
| `\S` | `[^ \t\n\r\f\x0B]` | NOT ASCII whitespace |

### Unicode Mode (with `Pattern.UNICODE_CHARACTER_CLASS` flag)

| Shorthand | Matches |
|-----------|---------|
| `\d` | Any Unicode decimal digit (`\p{Nd}`) |
| `\w` | Any Unicode letter, digit, or connector punctuation |
| `\s` | Any Unicode whitespace character |

**Critical insight:** In default mode, `\d` does NOT match Arabic-Indic digits (٠١٢٣٤٥٦٧٨٩), Persian digits, etc. In Unicode mode, it does. This matters for international applications.

### The `\w` Trap

`\w` matches underscore. This surprises people doing "word" extraction:

```
Pattern.compile("\\w+") on "foo_bar" → one match: "foo_bar"
Pattern.compile("[a-zA-Z]+") on "foo_bar" → two matches: "foo", "bar"
```

If you want pure alphabetic words, use `[a-zA-Z]+` or `\p{Alpha}+`.

---

## 5. Java's Double-Escaping: A Deep Dive

Java has **two layers** of string interpretation before the regex engine sees your pattern:

```
Source code: "\\d+"
    Java string layer: processes \\ → single backslash
    Result passed to regex engine: \d+
    Regex engine: \d means "digit class", + means "one or more"
```

The confusion matrix:

| What you type | Java string value | Regex engine sees | What it matches |
|--------------|-------------------|-------------------|-----------------|
| `"\d"` | Compiler error (`\d` is not a Java escape) | N/A | N/A |
| `"\\d"` | `\d` | `\d` | A digit |
| `"\\\\d"` | `\\d` | `\\d` | A literal backslash followed by 'd' |
| `"\\."` | `\.` | `\.` | A literal dot |
| `"."` | `.` | `.` | Any character (except newline) |

### Java 13+ Text Blocks Help

```
String pattern = """
    \\d{3}-\\d{4}
    """.strip();
// Still needs double escaping, but multi-line patterns are more readable
```

### Tip: Always Check with `pattern.pattern()`

After compiling, print the raw pattern string to see what the engine received:

```
Pattern p = Pattern.compile("\\d{3}-\\d{3}-\\d{4}");
System.out.println(p.pattern());  // prints: \d{3}-\d{3}-\d{4}
```

---

## 6. The Three Matching APIs — In Depth

Java's `Pattern`/`Matcher` API offers three matching modes. They are **not interchangeable**.

### `Matcher.matches()` — Full-String Match

Requires the pattern to match the **entire** input string. Equivalent to anchoring with `^...$`.

```
Pattern p = Pattern.compile("\\d+");
Matcher m = p.matcher("123");
m.matches();  // → true  (entire string is digits)

m = p.matcher("123abc");
m.matches();  // → false (entire string is not only digits)
```

**Common mistake:** Calling `matches()` after `find()` on the same `Matcher`. `Matcher` is stateful! Calling `find()` advances the internal position; calling `matches()` on a used `Matcher` gives wrong results. Always call `matcher.reset()` between uses, or create a fresh `Matcher`.

### `Matcher.find()` — Substring Search

Searches for the **next** occurrence of the pattern anywhere in the string. Advances position on each call. This is the **most commonly needed** method but also the most commonly misused.

```
Pattern p = Pattern.compile("\\d+");
Matcher m = p.matcher("abc123def456");
while (m.find()) {
    System.out.println(m.group());  // prints "123", then "456"
}
```

The `find(int start)` overload resets position to `start` before searching:

```
m.find(0);  // resets to beginning
```

### `Matcher.lookingAt()` — Prefix Match

Matches from the **beginning** of the string but doesn't require matching the entire string. Like `matches()` with an implicit `$` removed.

```
Pattern p = Pattern.compile("\\d+");
Matcher m = p.matcher("123abc");
m.lookingAt();  // → true (starts with digits, doesn't have to end with them)
```

This is useful for parsing: try to consume a token at the current position.

### `String.matches(regex)` — Convenience, But Dangerous in Loops

`"abc".matches("\\w+")` is syntactic sugar for `Pattern.matches("\\w+", "abc")`. It:
1. Compiles the pattern (not cached — a new `Pattern` object every call!)
2. Creates a `Matcher`
3. Calls `matches()` (full-string)

**Never call `String.matches()` in a loop over large datasets.** It recompiles the pattern on every invocation. Use a static final `Pattern` field instead.

---

## 7. Pattern Compilation and the Immutability Contract

`Pattern` objects are **immutable and thread-safe**. Declare them as `static final` constants:

```
// Good — compiled once at class load time
private static final Pattern ERROR_PATTERN = Pattern.compile("ERROR");

// Bad — compiled on every method call
public boolean hasError(String line) {
    return Pattern.compile("ERROR").matcher(line).find();
}
```

`Matcher` objects are **NOT thread-safe**. Never share a `Matcher` across threads. Create a new `Matcher` from the thread-safe `Pattern` on each thread:

```
// Thread-safe usage
public boolean hasError(String line) {
    return ERROR_PATTERN.matcher(line).find();  // new Matcher each time, that's fine
}
```

---

## 8. Greedy, Reluctant, and Possessive Quantifiers

Understanding quantifier behavior is key to understanding both correctness and performance.

### Greedy (default)
Consume as much as possible, then backtrack to let the rest of the pattern succeed.

```
Pattern: a.*b
Input: "aXXb and aYYb"
Greedy .* consumes "XXb and aYY", then the engine tries to match final 'b'
Input at that point is just "b". Match found: "aXXb and aYYb" (one big match)
```

### Reluctant (add `?`)
Consume as little as possible, expanding only if the rest of the pattern requires it.

```
Pattern: a.*?b
Input: "aXXb and aYYb"
Reluctant .*? initially consumes nothing, then expands one char at a time
First match: "aXXb"
Second match: "aYYb"
```

### Possessive (add `+`)
Consume as much as possible, **never give back**. No backtracking.

```
Pattern: a.*+b
Input: "aXXb"
Possessive .*+ consumes "XXb", then needs to match 'b'. Nothing left. FAIL. No backtrack.
```

Possessive quantifiers can prevent backtracking loops — use them in performance-critical patterns when you know the structure of the input.

---

## 9. Backtracking and Catastrophic Complexity

### The Catastrophic Pattern

The classic ReDoS pattern is something like `(a+)+` matched against a string of `a`s followed by a character that fails the overall pattern:

```
Pattern: (a+)+$
Input: "aaaaaaaaaaaaaaaX"
```

The engine tries exponentially many ways to partition the `a`s between the outer `+` repetitions. For n `a`s followed by a non-matching character, this is O(2^n).

### Safe Patterns for the Same Semantics

Use atomic groups `(?>...)` or possessive quantifiers to prevent pathological backtracking:

```
// Catastrophic
(a+)+$

// Safe — atomic group prevents the outer + from repartitioning
(?>a+)+$

// Also safe — possessive quantifier
a++$
```

Java supports possessive quantifiers (`++`, `*+`, `?+`) and atomic groups `(?>...)` since Java 1.4.

---

## 10. The `Matcher.find()` Loop Pattern

This is the most important code pattern in this entire concept:

```
Pattern p = Pattern.compile(SOME_PATTERN);
Matcher m = p.matcher(inputString);
List<String> results = new ArrayList<>();
while (m.find()) {
    results.add(m.group());
}
```

Key mechanics:
- `m.find()` returns `true` if another match exists from the current position.
- `m.group()` returns the text of the most recent match (group 0 = full match).
- `m.start()` / `m.end()` return the indices of the match.
- The loop terminates when `find()` returns `false` (no more matches).

**After calling `find()`, do NOT call `matches()` on the same Matcher without reset.**

### Capture Groups in the Loop

```
Pattern p = Pattern.compile("(\\w+)=(\\d+)");
Matcher m = p.matcher("x=10 y=20 z=30");
while (m.find()) {
    String key = m.group(1);   // first capture group
    String val = m.group(2);   // second capture group
}
```

Group 0 is always the entire match. Groups 1, 2, ... are capturing parentheses left to right.

---

## 11. Edge Cases and Worked Examples

### Empty Pattern Matches Everywhere

```
Pattern.compile("").matcher("abc").find()  // → true
// match at pos 0, length 0. Then find() advances. Match at pos 1. etc.
// Be careful — empty matches appear between every character.
```

### Zero-Width Matches in Loops

When a pattern can match zero characters, the `find()` loop can produce infinite matches unless the engine advances automatically. Java's `Matcher` advances by one character after a zero-length match to prevent infinite loops. Be aware of this when matching optional-length patterns.

### Matching Newlines

```
String text = "line1\nline2";
Pattern.compile(".*").matcher(text).find();  // matches "line1" only
Pattern.compile("(?s).*").matcher(text).find();  // matches "line1\nline2"
```

### `^` and `$` with MULTILINE

By default:
- `^` matches start of entire input
- `$` matches end of entire input

With `Pattern.MULTILINE`:
- `^` matches start of each line (after `\n`)
- `$` matches end of each line (before `\n`)

---

## 12. Malformed Input Handling

`Pattern.compile()` throws `PatternSyntaxException` (unchecked, extends `IllegalArgumentException`) if the pattern is syntactically invalid.

**Always validate patterns from external sources:**

```
public Pattern safeCompile(String userPattern) {
    try {
        return Pattern.compile(userPattern);
    } catch (PatternSyntaxException e) {
        // log the error, return a safe default, or rethrow as a domain exception
        throw new IllegalArgumentException("Invalid regex: " + e.getDescription(), e);
    }
}
```

### Handling `null` Input

`Matcher.matcher(null)` throws `NullPointerException`. Always null-check before matching:

```
public boolean safeFind(Pattern p, String input) {
    if (input == null) return false;
    return p.matcher(input).find();
}
```

---

## 13. Debugging Workflow

### Step 1: Isolate the Pattern

Test the pattern on a minimal failing input in a unit test or in [regex101.com](https://regex101.com) with "Java" flavor selected.

### Step 2: Check Your Escaping

Print the compiled pattern:

```
System.out.println(Pattern.compile("\\d+\\.\\d+").pattern());
// Output: \d+\.\d+  ← this is what the engine sees
```

### Step 3: Trace Matching Step by Step

For a Matcher `m` after `find()`:
- `m.start()` — where did the match start?
- `m.end()` — where does it end (exclusive)?
- `m.group()` — what was matched?
- `m.groupCount()` — how many capture groups?
- `m.group(n)` — what did group n capture?

### Step 4: Check the API You're Using

The #1 debugging question: "Am I using `matches()` when I should be using `find()`?"

- `matches()` → need the **entire** string to match? → use for validation
- `find()` → searching for a **substring**? → use for extraction, filtering

### Step 5: Enable Verbose Mode

Java supports the `(?x)` inline flag (or `Pattern.COMMENTS`) which ignores whitespace and allows `#` comments inside the pattern:

```
Pattern p = Pattern.compile("""
    (?x)
    \\d{3}    # area code
    [-.]      # separator
    \\d{3}    # exchange
    [-.]      # separator
    \\d{4}    # number
    """.strip());
```

---

## 14. Interview Traps Specific to This Concept

### Trap 1: "Does `"abc".matches("abc")` return true?"
Yes — `matches()` does a full-string match, and `"abc"` is the full string. Trick: does `"abcd".matches("abc")`? **No** — because `matches()` anchors both ends.

### Trap 2: "What does `\w` match?"
Most candidates say "word characters" and stop. The full answer: `[a-zA-Z0-9_]` in ASCII mode. The underscore is included. Unicode letters are NOT included in default mode.

### Trap 3: "Is `Pattern` thread-safe?"
**Yes.** `Matcher` is **not.** Know the difference. `Pattern.matcher()` creates a new `Matcher` — that's the thread-safe usage pattern.

### Trap 4: "What's wrong with `String.matches()` in a hot loop?"
It recompiles the pattern on every call. For a method called millions of times per second, this is a measurable performance regression. Store the `Pattern` as a static final.

### Trap 5: "What does `.*` match?"
Everything up to (but not including) a newline. If you want to cross newlines, use `[\s\S]*` or `(?s).*`. Candidates who say "everything" are missing the newline caveat.

### Trap 6: "What's the difference between `+` and `*`?"
`+` requires at least one match. `*` allows zero matches. A pattern with `*` can match the empty string. Knowing when that matters (e.g., splitting, replacement) is key.

---

## 15. Production Engineering Concerns

### Compile Patterns Once
Define patterns as `private static final Pattern` at the class level. Pattern compilation involves parsing, NFA construction, and potentially optimization — it's orders of magnitude more expensive than creating a `Matcher`.

### Guard Against ReDoS
Any pattern with nested quantifiers on overlapping character sets is a potential ReDoS vulnerability. Examples:
- `(a+)+` — catastrophic
- `(\w+\s)+` — potentially catastrophic for large inputs
- `(a|aa)+` — catastrophic

For user-supplied patterns in web applications, consider:
1. Setting a **timeout** via `Matcher` in a `FutureTask` with a timeout
2. Using a safe regex engine like `RE2/J` (Google's Java port)
3. Validating the pattern complexity before using it

### Named Groups for Readability

```
Pattern p = Pattern.compile("(?<year>\\d{4})-(?<month>\\d{2})-(?<day>\\d{2})");
Matcher m = p.matcher("2024-01-15");
if (m.find()) {
    String year  = m.group("year");   // more readable than m.group(1)
    String month = m.group("month");
    String day   = m.group("day");
}
```

### Character Encoding Awareness

Java `String` is UTF-16. `Matcher` operates on `char` sequences. Characters outside the Basic Multilingual Plane (code points > U+FFFF) are represented as **surrogate pairs** — two `char` values. Regex patterns may need special handling for such characters.

```
// Emoji are outside BMP — this might not work as expected
Pattern.compile(".").matcher("emoji").find()  // . matches one CHAR, not one code point
```

For full Unicode-aware matching of supplementary characters, use `\X` (not standard in Java) or `Pattern.UNICODE_CHARACTER_CLASS` with `\p{...}` categories.

### Logging and Observability

In production parsers, log:
- The input that caused a pattern to fail (at DEBUG level)
- The time taken for matching if it might be slow
- Pattern compilation errors with the full `PatternSyntaxException` message

### Testing Strategy for Regex Code

- Test the happy path (exact expected match)
- Test boundary cases (exactly minimum/maximum length)
- Test adversarial input (all metacharacters, very long strings, null, empty)
- Test strings that **almost** match but fail (one character off)
- For validators, test both `true` and `false` expected returns with near-misses

---

## Summary

| Concept | Key Takeaway |
|---------|-------------|
| NFA engine | Java regex backtracks; order matters; can be exponential |
| Literal matching | Metacharacters must be escaped; use `Pattern.quote()` for user input |
| Shorthands | `\d` = `[0-9]` (ASCII); `\w` = `[a-zA-Z0-9_]` (includes underscore!) |
| Double-escaping | Java string `"\\d"` → engine sees `\d` |
| `matches()` | Full-string; use for validation |
| `find()` | Substring search; use for extraction |
| `lookingAt()` | Prefix match; use for streaming/incremental parsing |
| `Pattern` | Thread-safe; immutable; compile once as `static final` |
| `Matcher` | Not thread-safe; create per thread from the shared `Pattern` |
| Greedy | Default; consumes max, backtracks |
| Reluctant | `?` suffix on quantifier; consumes min, expands |
| Possessive | `+` suffix on quantifier; consumes max, no backtrack |
| ReDoS | Nested quantifiers on overlapping classes = danger |
