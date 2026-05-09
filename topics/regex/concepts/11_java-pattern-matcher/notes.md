# Java Pattern & Matcher — Deep Notes

> Written for engineers who want to understand the runtime model, not just the API signatures.
> You will find engine internals, thread-safety reasoning, replacement string pitfalls,
> and interview traps with exact explanations of WHY they are traps.

---

## 1. The Two-Object Model: Why Pattern and Matcher Are Separate

Most beginners treat `Pattern` and `Matcher` as bureaucratic overhead. Why not a single `Regex` object with a `findAll(String)` method? The separation is deliberate and exposes a critical architectural decision.

**`Pattern` is the compiled NFA.** When you call `Pattern.compile("\\d+")`, Java:
1. Lexes the regex string into tokens (`\d`, `+`)
2. Parses those tokens into an Abstract Syntax Tree (AST)
3. Transforms the AST into an NFA graph (nodes = states, edges = transitions)
4. Optionally applies optimizations (e.g., anchoring at start to skip positions early)

This process allocates memory for the automaton structure, does character class analysis, and caches internal lookup tables. It is **not cheap**. Benchmarks typically show `Pattern.compile` takes 10–100× longer than a single match operation on short inputs.

The resulting `Pattern` object contains no mutable state — it describes the automaton structure but holds no "current position" or "last match". This makes it **intrinsically thread-safe**. You can share one `Pattern` across a million threads without synchronization.

**`Matcher` is the execution engine running on that automaton.** When you call `pattern.matcher(input)`, Java creates a new `Matcher` that holds:
- A reference to the `Pattern` (the automaton blueprint)
- The input `CharSequence`
- The current search position (`from`)
- The region boundaries (`regionStart`, `regionEnd`)
- The last match result: position of the overall match and all group captures
- Internal NFA stack state used during backtracking

All of this is **mutable state**. After each `find()` call, the position advances, the group capture state updates. A `Matcher` is deeply stateful and **NOT thread-safe**. Do not share Matchers across threads. Do not store them as static or instance fields.

### The Canonical Pattern

```
// CORRECT: Pattern is static final (compiled once, shared safely)
private static final Pattern DIGITS = Pattern.compile("\\d+");

public List<String> findDigits(String input) {
    // Matcher is created per call — never shared
    Matcher m = DIGITS.matcher(input);
    List<String> results = new ArrayList<>();
    while (m.find()) {
        results.add(m.group());
    }
    return results;
}
```

This is not just a "best practice" — it is the architecturally correct usage of the two-object model.

---

## 2. Pattern.compile() — Flags Deep Dive

### `Pattern.compile(String regex)`

Compiles with no flags. `^` and `$` match start/end of the entire input. `.` does not match `\n`. Matching is case-sensitive. Unicode support is partial (basic BMP characters).

### `Pattern.compile(String regex, int flags)`

Flags are OR-combined integer constants defined on `Pattern`. Here is every flag and what it actually does at the engine level:

#### `Pattern.CASE_INSENSITIVE` (flag: `i`)

Makes `[a-z]` also match `[A-Z]` and vice versa. In the NFA, every character transition for an ASCII letter is duplicated for its case counterpart. This is equivalent to adding the inline flag `(?i)` at the start of the pattern.

**Trap**: Only covers ASCII by default. To handle Unicode case folding (e.g., matching `ß` with `SS`), combine with `UNICODE_CASE`.

```java
Pattern.compile("hello", Pattern.CASE_INSENSITIVE); // matches "HELLO", "Hello", etc.
Pattern.compile("(?i)hello");                        // equivalent inline form
```

#### `Pattern.MULTILINE` (flag: `m`)

Changes the behavior of `^` and `$` anchors:
- Without MULTILINE: `^` matches only at the absolute start of the input; `$` matches only at the absolute end (or before a trailing `\n`).
- With MULTILINE: `^` matches at the start of **every line** (position 0, and every position after `\n`); `$` matches at the end of **every line** (every position before `\n`, and the end of the input).

This flag does NOT affect `.` — a common misconception. `.` still does not match `\n` unless `DOTALL` is also set.

```java
Pattern p = Pattern.compile("^#.*", Pattern.MULTILINE);
// Now finds comment lines (starting with #) anywhere in a multi-line string
```

#### `Pattern.DOTALL` (flag: `s`)

Makes `.` match **any** character, including `\n`. Without this flag, `.` matches everything except line terminators (`\n`, `\r`, `\r\n`, etc.).

```java
Pattern p = Pattern.compile("<div>.*?</div>", Pattern.DOTALL);
// Now matches multi-line div blocks
```

**Combining MULTILINE + DOTALL**: This is the combination needed for "match across lines while still treating anchors line-by-line". They are orthogonal flags.

#### `Pattern.UNICODE_CASE` (flag: `u`)

When combined with `CASE_INSENSITIVE`, extends case folding to the full Unicode character set. Without this, `\w` only covers ASCII word characters. With `UNICODE_CHARACTER_CLASS` (below), this becomes even more comprehensive.

#### `Pattern.UNICODE_CHARACTER_CLASS` (flag: `U`)

Changes the behavior of predefined character classes (`\w`, `\d`, `\s`, `\W`, `\D`, `\S`) and POSIX classes to conform to Unicode definitions:
- `\w` becomes equivalent to `[\p{Alpha}\p{gc=Mn}\p{gc=Me}\p{gc=Mc}\p{Digit}\p{gc=Pc}]`
- `\d` matches any Unicode decimal digit (not just `[0-9]`)
- `\s` matches any Unicode whitespace

Use this when processing international text. Note: it also implicitly enables `UNICODE_CASE`.

#### `Pattern.COMMENTS` (flag: `x`)

Whitespace in the pattern is ignored, and `#` starts a comment to end of line. Allows writing documented, multi-line patterns:

```java
Pattern p = Pattern.compile(
    "(\\d{4})   # year \n" +
    "-(\\d{2})  # month \n" +
    "-(\\d{2})  # day",
    Pattern.COMMENTS
);
```

A literal space or `#` must be escaped with `\\ ` or `\\#` when this flag is active.

#### `Pattern.LITERAL` (flag: N/A via inline)

Treats the entire pattern string as a literal string — all metacharacters lose their special meaning. Equivalent to wrapping the pattern in `Pattern.quote(...)`. Useful when matching user-supplied strings that might contain regex metacharacters.

```java
String userInput = "2+2=4";
Pattern p = Pattern.compile(userInput, Pattern.LITERAL); // matches literally
```

#### `Pattern.CANON_EQ`

Enables canonical Unicode equivalence. Two characters that are canonically equivalent under Unicode (e.g., `é` as a single codepoint vs `e` + combining accent) are treated as equal. Rarely needed but critical for correct Unicode text processing.

### Combining Flags

```java
int flags = Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL;
Pattern p = Pattern.compile("^start.*?end$", flags);
```

### Inline Flags

Any flag can be embedded in the pattern with `(?flags)` syntax — this is useful when you want the flag to apply to only part of the pattern:

```java
"(?i)hello(?-i)World"  // case-insensitive for "hello", case-sensitive for "World"
"(?ims)"               // set CASE_INSENSITIVE + MULTILINE + DOTALL
"(?-m)"                // clear MULTILINE
```

---

## 3. Matcher — The Complete API Reference

### Obtaining a Matcher

```java
Pattern pattern = Pattern.compile("\\w+");
Matcher m = pattern.matcher("hello world");  // input as String (implements CharSequence)
Matcher m = pattern.matcher(new StringBuilder("hello")); // any CharSequence works
```

### The Three Match Methods: matches(), find(), lookingAt()

These are the three distinct modes for applying a `Matcher`:

#### `matcher.matches()`

Attempts to match the **entire** input sequence against the pattern. Returns `true` only if the whole input string is consumed by the match.

```
Pattern: \d+
Input:   "123"   → matches() = true
Input:   "123x"  → matches() = false  (trailing 'x' not consumed)
Input:   "x123"  → matches() = false  (leading 'x' not consumed)
```

Use `matches()` for **validation**: "Is this entire string a valid ZIP code / phone number / UUID?"

Under the hood, `matches()` is equivalent to wrapping the pattern in `^(?:pattern)$` and calling `find()`, except it operates on the already-compiled internal automaton more directly.

#### `matcher.find()`

Scans the input from the **current position** forward, looking for the next substring that matches the pattern. Returns `true` if found, advances the internal position. Successive calls walk through all non-overlapping matches.

```
Pattern: \d+
Input:   "abc123def456"
1st find() → true,  group() = "123" (positions 3-5)
2nd find() → true,  group() = "456" (positions 9-11)
3rd find() → false (no more matches)
```

Use `find()` for **search and extraction**: "Find all occurrences of this pattern in a longer text."

#### `matcher.find(int start)`

Resets the matcher and starts scanning from position `start`. This is NOT the same as just advancing to position `start` — it fully resets the match state first. Useful for re-scanning from a specific offset.

```java
m.find(5); // reset + find from position 5 onward
```

#### `matcher.lookingAt()`

Like `find()`, but only attempts to match at the **beginning** of the input (position 0). It does NOT require the entire input to match (unlike `matches()`). Returns `true` if the start of the input matches the pattern.

```
Pattern: \d+
Input:   "123abc"   → lookingAt() = true  (starts with digits)
Input:   "abc123"   → lookingAt() = false (doesn't start with digits)
Input:   "123"      → lookingAt() = true  (also works without trailing chars)
```

`lookingAt()` is the least-used of the three but useful for parsers that consume input token by token from left to right.

### Comparison Table

| Method       | Requires full match? | Scans from? | Advances position? |
|---|---|---|---|
| `matches()`  | YES                  | Always pos 0 | No (resets on next call) |
| `find()`     | NO                   | Current pos  | YES                |
| `lookingAt()`| NO                   | Always pos 0 | No (resets on next call) |

---

## 4. Capturing Groups: group(), group(int), group(String)

### Group Numbering

Groups are numbered by the position of their **opening parenthesis**, left-to-right, starting at 1. Group 0 is always the entire match.

```
Pattern: ((\d{4})-(\d{2})-(\d{2}))
Groups:   1       2        3        4
          ^^^^^^^^ ^^^^^^^ ^^^^^^^^
          outer    year    month     day — no, re-count:

Actually:
(          = group 1 opens
  (\d{4})  = group 2
  -
  (\d{2})  = group 3
  -
  (\d{2})  = group 4
)          = group 1 closes
```

So for input "2024-01-15":
- `group(0)` = `"2024-01-15"` (entire match)
- `group(1)` = `"2024-01-15"` (outer group)
- `group(2)` = `"2024"` (year)
- `group(3)` = `"01"` (month)
- `group(4)` = `"15"` (day)

### Non-Capturing Groups

`(?:...)` creates a group for grouping/quantification purposes only, without consuming a group number. Use these whenever you don't need to capture the group's content — it makes the pattern more readable and avoids off-by-one errors in group indices.

```java
Pattern.compile("(?:foo|bar)(\\d+)");
// group(1) is the digits — the alternation group is non-capturing
```

### Named Groups

Java supports named capturing groups with `(?<name>...)` syntax (since Java 7):

```java
Pattern p = Pattern.compile("(?<year>\\d{4})-(?<month>\\d{2})-(?<day>\\d{2})");
Matcher m = p.matcher("2024-01-15");
if (m.find()) {
    String year  = m.group("year");   // "2024"
    String month = m.group("month");  // "01"
    String day   = m.group("day");    // "15"
}
```

Named groups make patterns self-documenting and immune to index shifts when you add groups later.

### null-Returning Groups

`group(n)` returns `null` (NOT an empty string, NOT an exception) when the group did NOT participate in the current match. This happens with optional groups:

```java
Pattern p = Pattern.compile("(\\d+)(px|em)?");
Matcher m = p.matcher("42");
m.find();
m.group(1); // "42"
m.group(2); // null — the (px|em)? group did not match
```

Always check for null before using group values! `NullPointerException` from unchecked group results is a common production bug.

### `matcher.groupCount()`

Returns the number of capturing groups in the pattern (does not include group 0). Use this to iterate all groups programmatically.

---

## 5. Position Tracking: start(), end()

After a successful `find()` or `matches()`, you can interrogate where the match occurred in the input:

- `matcher.start()` — index of the first character of the match (inclusive)
- `matcher.end()` — index just PAST the last character of the match (exclusive)

These follow Java's standard `[start, end)` convention, matching how `String.substring(start, end)` works:

```java
String input = "price: $42.50";
Matcher m = Pattern.compile("\\$[\\d.]+").matcher(input);
if (m.find()) {
    System.out.println(m.start()); // 7
    System.out.println(m.end());   // 13
    System.out.println(input.substring(m.start(), m.end())); // "$42.50"
    // equivalent to m.group()
}
```

For groups, `start(int)` and `end(int)` give the positions of the group's match within the input — critical for computing offsets when doing text replacement with position awareness.

### Using start() and end() for Manual Splitting

The standard use case for position tracking beyond simple extraction:

```java
Matcher m = Pattern.compile("delimiter").matcher(input);
int lastEnd = 0;
List<String> parts = new ArrayList<>();
while (m.find()) {
    parts.add(input.substring(lastEnd, m.start())); // text BEFORE delimiter
    lastEnd = m.end();                               // skip past the delimiter
}
parts.add(input.substring(lastEnd)); // remaining text after last delimiter
```

This gives you full control that `Pattern.split()` does not — you can decide whether to include the delimiter in the parts, keep it attached to the preceding or following token, or discard it entirely.

---

## 6. Matcher State Management: reset() and find(int)

### `matcher.reset()`

Resets the matcher to the beginning of the **same input** without creating a new `Matcher` object. Clears the current match, position, and group state. Use this to re-scan the same input from the start.

```java
Matcher m = pattern.matcher("abc 123 def 456");
// First pass
while (m.find()) { /* first pass processing */ }
// Re-scan from start without creating new Matcher
m.reset();
while (m.find()) { /* second pass processing */ }
```

### `matcher.reset(CharSequence newInput)`

Resets to the beginning AND changes the input string. The pattern stays the same. This is the Matcher reuse pattern for processing many strings with the same pattern:

```java
Matcher m = PATTERN.matcher(""); // initial empty input
for (String line : lines) {
    m.reset(line);              // reuse matcher with new input
    while (m.find()) {
        process(m.group());
    }
}
```

This avoids the object creation overhead of calling `pattern.matcher(line)` in a tight loop. In high-throughput parsing, this matters.

### `matcher.find(int start)`

Resets the matcher AND starts scanning from `start`. Note this is a full reset — it clears the previous match state:

```java
m.find(10); // start scanning from position 10
```

### Matcher Region: regionStart(), regionEnd(), region()

You can restrict the region of the input that the matcher considers:

```java
m.region(5, 20); // only consider characters at indices [5, 20)
```

Anchors `^` and `$` respect the region boundaries when `m.useTransparentBounds(false)` (default). Useful for tokenizers that process segments of a larger buffer.

---

## 7. Replacement API

### `matcher.replaceAll(String replacement)`

Replaces every match with the literal replacement string. This is equivalent to the core behavior of `String.replaceAll(regex, replacement)` but on a pre-compiled pattern:

```java
String result = Pattern.compile("\\bfoo\\b")
                       .matcher(input)
                       .replaceAll("bar");
```

**Replacement string special characters:**
- `$0` — inserts the entire match
- `$1`, `$2`, ... — inserts the captured group
- `$<name>` — inserts the named group
- `\\` — inserts a literal `\`
- `$$` — inserts a literal `$`... wait, no: in replacement strings, `$` followed by a digit/name is interpolation; to get a literal `$`, use `\$` — but in a Java string, `\$` is `\\$`... the escaping is confusing.

### `Matcher.quoteReplacement(String s)`

This static method escapes a string so that it can be used safely as a replacement string without `$` or `\` being interpreted as special. Always use this when inserting user-provided content into a replacement:

```java
String userValue = "$100.00";
String safeReplacement = Matcher.quoteReplacement(userValue); // "\\$100.00"
result = m.replaceAll(safeReplacement);
```

Forgetting this causes `IllegalArgumentException` or silent data corruption when user data contains `$` followed by digits.

### `matcher.replaceFirst(String replacement)`

Replaces only the first match. Everything else is identical to `replaceAll`.

### `appendReplacement(StringBuffer sb, String replacement)` + `appendTail(StringBuffer sb)`

This is the **power API** — it allows you to run arbitrary Java code logic to compute the replacement for each match. The workflow:

1. Call `m.find()` to find the next match
2. Call `m.appendReplacement(sb, computedReplacement)` — this appends everything between the previous match and this match (the "gap" text) to `sb`, then appends `computedReplacement`
3. After the loop, call `m.appendTail(sb)` — this appends the text after the last match

```java
Pattern p = Pattern.compile("\\d+");
Matcher m = p.matcher("I have 3 cats and 10 dogs");
StringBuffer sb = new StringBuffer();
while (m.find()) {
    int value = Integer.parseInt(m.group());
    m.appendReplacement(sb, String.valueOf(value * 2));
}
m.appendTail(sb);
System.out.println(sb.toString()); // "I have 6 cats and 20 dogs"
```

The key insight: `appendReplacement` handles the "gap between matches" automatically. You never need to track `lastEnd` yourself.

**Java 9+**: `appendReplacement` and `appendTail` also accept `StringBuilder` (in addition to `StringBuffer`), avoiding synchronization overhead from `StringBuffer`.

### Java 9+ `replaceAll(Function<MatchResult, String>)` 

Since Java 9, `Matcher.replaceAll` and `replaceFirst` accept a `Function<MatchResult, String>` — a lambda that computes the replacement:

```java
String result = Pattern.compile("\\d+")
    .matcher("price: 42 items: 7")
    .replaceAll(mr -> String.valueOf(Integer.parseInt(mr.group()) * 2));
// result: "price: 84 items: 14"
```

This is cleaner than the `appendReplacement` loop for straightforward transformations.

---

## 8. Pattern.split() — Nuances and Limitations

### Basic split

```java
String[] parts = Pattern.compile("\\s+").split("one  two   three");
// ["one", "two", "three"]
```

### The Limit Parameter

`Pattern.split(CharSequence input, int limit)` controls how many elements the result has:
- `limit > 0`: at most `limit` elements; the last element contains all remaining input (including the delimiter)
- `limit = 0` (default): remove trailing empty strings
- `limit < 0`: no limit AND trailing empty strings are kept

```java
Pattern.compile(",").split("a,b,c,,", 0);   // ["a", "b", "c"]     — trailing empty removed
Pattern.compile(",").split("a,b,c,,", -1);  // ["a", "b", "c", "", ""] — trailing empty kept
Pattern.compile(",").split("a,b,c,,", 3);   // ["a", "b", "c,,"]   — stopped at 3 pieces
```

### Why Pattern.split() Is Sometimes Wrong

`Pattern.split()` cannot keep the delimiter in the results. If you need the delimiter attached to a token (before or after), you must use manual `Matcher.find()` with `start()` and `end()` tracking. This is a fundamental limitation of the split abstraction.

---

## 9. Pattern.matches() — The Static Convenience Method

```java
boolean isValid = Pattern.matches("\\d{5}", "90210");
```

This is a convenience method that compiles the pattern AND runs `matches()` in one call. It is functionally equivalent to:

```java
Pattern.compile("\\d{5}").matcher("90210").matches();
```

**Performance warning**: `Pattern.matches()` compiles the pattern on every call. Never call this in a loop or high-frequency code path. It exists only for one-off validation in low-traffic code.

---

## 10. Java 9+: Matcher.results() — Stream API

Since Java 9, `Matcher` exposes a `results()` method returning `Stream<MatchResult>`. This enables fully functional, lazy processing of matches:

```java
List<String> words = Pattern.compile("\\w+")
    .matcher("hello world foo")
    .results()
    .map(MatchResult::group)
    .collect(Collectors.toList());
// ["hello", "world", "foo"]
```

`MatchResult` is an interface with `group()`, `group(int)`, `start()`, `start(int)`, `end()`, `end(int)`, and `groupCount()`. `Matcher` itself implements `MatchResult`, so you get the same API.

The stream is lazy — matches are computed on demand as the terminal operation consumes them. This is memory-efficient for large inputs.

```java
// Find the first 5 phone numbers in a huge document
Pattern.compile("\\b\\d{3}-\\d{4}\\b")
    .matcher(hugeDocument)
    .results()
    .limit(5)
    .map(MatchResult::group)
    .forEach(System.out::println);
```

---

## 11. Exception Handling: PatternSyntaxException

`Pattern.compile()` throws `PatternSyntaxException` (a `RuntimeException`) if the pattern string is not valid regex syntax. Always catch this when compiling user-supplied patterns:

```java
try {
    Pattern p = Pattern.compile(userPattern);
    // use p
} catch (PatternSyntaxException e) {
    System.err.println("Invalid pattern: " + e.getMessage());
    System.err.println("Near index " + e.getIndex() + ": " + e.getDescription());
}
```

`PatternSyntaxException` provides:
- `getMessage()` — full formatted message
- `getDescription()` — short description of the syntax error
- `getIndex()` — approximate index in the pattern string where the error was detected
- `getPattern()` — the offending pattern string

Never use bare `Exception` catches for this — `PatternSyntaxException` is the specific type you care about.

---

## 12. Thread Safety: Patterns for Production Code

### The Golden Rules

1. **`Pattern` objects are immutable and thread-safe.** Store them as `static final` fields at the class level. They are shared across all threads with zero synchronization.

2. **`Matcher` objects are stateful and NOT thread-safe.** Never store them as static or instance fields. Create one per method call with `pattern.matcher(input)`, OR reuse one per-thread via `ThreadLocal`.

### Production Pattern 1: Static Final Pattern

```java
public class EmailValidator {
    private static final Pattern EMAIL =
        Pattern.compile("[a-zA-Z0-9._%+\\-]+@[a-zA-Z0-9.\\-]+\\.[a-zA-Z]{2,}");

    public boolean isValid(String email) {
        return EMAIL.matcher(email).matches();
    }
}
```

### Production Pattern 2: ThreadLocal Matcher

For extremely high throughput where even `pattern.matcher(input)` object creation matters:

```java
private static final Pattern PATTERN = Pattern.compile("\\d+");
private static final ThreadLocal<Matcher> MATCHER =
    ThreadLocal.withInitial(() -> PATTERN.matcher(""));

public List<String> findDigits(String input) {
    Matcher m = MATCHER.get();
    m.reset(input);  // reuse with new input
    List<String> results = new ArrayList<>();
    while (m.find()) results.add(m.group());
    return results;
}
```

This is micro-optimization territory — profile before adopting. The simpler Pattern 1 is correct for most use cases.

### Anti-Pattern: Compiling Inside a Loop

```java
// WRONG — compiles the pattern thousands of times
for (String line : lines) {
    if (Pattern.compile("\\d+").matcher(line).find()) { ... }
}

// CORRECT — compile once, reuse
Pattern p = Pattern.compile("\\d+");
for (String line : lines) {
    if (p.matcher(line).find()) { ... }
}
```

---

## 13. Pattern.quote() — Escaping Literal Strings for Use as Patterns

When you want to search for a string that may contain regex metacharacters, use `Pattern.quote()`:

```java
String literal = "2+2=4"; // the '+' is a metacharacter
Pattern safe = Pattern.compile(Pattern.quote(literal));
// equivalent to Pattern.compile("\\Q2+2=4\\E");
```

`Pattern.quote(s)` wraps `s` in `\Q...\E` — the Java regex "quotation" escape that treats everything between them as literals.

---

## 14. Summary: Interview Traps and Their Exact Answers

### Trap 1: "Is Matcher thread-safe?"

**Answer**: No. `Matcher` is stateful — it tracks the current scan position, last match bounds, and captured groups. Sharing a `Matcher` across threads without synchronization causes race conditions. `Pattern` is thread-safe; `Matcher` is not.

### Trap 2: "What does `group(0)` return?"

**Answer**: `group(0)` returns the entire matched substring — equivalent to `group()` with no argument. Group 0 is special: it always represents the full match. The first capturing group `(...)` in the pattern is group 1.

### Trap 3: "What is the difference between `matches()` and `find()`?"

**Answer**: `matches()` requires the **entire input** to match the pattern from start to end — like having implicit `^` and `$` around the pattern. `find()` searches for the pattern **anywhere** in the input as a substring. For validation use `matches()`; for search/extraction use `find()`.

### Trap 4: "What does `group(n)` return if the group didn't participate?"

**Answer**: `null`. NOT an empty string, NOT an exception (unless no match attempt has been made yet, in which case `IllegalStateException`). Optional groups `(...)?` that don't match return `null` from `group(n)`. You must null-check group results before using them.

### Trap 5: "What is the difference between `Pattern.MULTILINE` and `Pattern.DOTALL`?"

**Answer**: They are orthogonal and affect different things. `MULTILINE` changes `^` and `$` to match at line boundaries instead of only at string start/end. `DOTALL` changes `.` to match any character including `\n`. You often need both together when parsing multi-line blocks.

### Trap 6: "What does `appendReplacement` / `appendTail` do?"

**Answer**: They implement the manual replacement loop. `appendReplacement(sb, repl)` appends everything from the end of the previous match to the start of the current match (the "gap"), then appends the replacement string. `appendTail(sb)` appends everything after the last match. Together they allow per-match custom replacement logic impossible with `replaceAll(String)`.

### Trap 7: "Why should you use `Matcher.quoteReplacement()` in replacement strings?"

**Answer**: In replacement strings, `$n` expands to the n-th captured group, and `\` is an escape character. If you're inserting a computed or user-provided string as a replacement, any `$` or `\` in that string will be misinterpreted. `Matcher.quoteReplacement(s)` escapes these characters so the string is treated literally.

### Trap 8: "When does `PatternSyntaxException` get thrown?"

**Answer**: At compile time — when `Pattern.compile()` is called with an invalid pattern. Not during matching. Always surround `Pattern.compile()` with user-supplied patterns in a try-catch for `PatternSyntaxException`.

---

## 15. Real-World Usage Patterns

### Log Parsing

```java
private static final Pattern LOG_LINE =
    Pattern.compile("^(\\d{4}-\\d{2}-\\d{2}T\\d{2}:\\d{2}:\\d{2}) (\\w+) (.+)$",
                    Pattern.MULTILINE);

public void parseLog(String logContent) {
    Matcher m = LOG_LINE.matcher(logContent);
    while (m.find()) {
        String timestamp = m.group(1);
        String level     = m.group(2);
        String message   = m.group(3);
        // process each line
    }
}
```

### Data Masking Pipeline

```java
private static final Pattern EMAIL =
    Pattern.compile("[a-zA-Z0-9._%+\\-]+@[a-zA-Z0-9.\\-]+\\.[a-zA-Z]{2,}");
private static final Pattern CREDIT_CARD =
    Pattern.compile("\\b\\d{4}[\\s-]\\d{4}[\\s-]\\d{4}[\\s-]\\d{4}\\b");

public String maskSensitiveData(String input) {
    String masked = EMAIL.matcher(input).replaceAll("[EMAIL]");
    masked = CREDIT_CARD.matcher(masked).replaceAll("[CARD]");
    return masked;
}
```

### Config Parser with Named Groups

```java
private static final Pattern CONFIG_LINE =
    Pattern.compile("^(?<key>[\\w.]+)\\s*=\\s*(?<value>.+?)\\s*$",
                    Pattern.MULTILINE);

public Map<String, String> parseConfig(String config) {
    Map<String, String> map = new LinkedHashMap<>();
    Matcher m = CONFIG_LINE.matcher(config);
    while (m.find()) {
        map.put(m.group("key"), m.group("value"));
    }
    return map;
}
```

---

*Next: Work through the problems in `problems/` to practice each part of this API. Start with `find-all-matches` and build up.*
