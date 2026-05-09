# Debugging Regex — Deep Notes

> Written from the perspective of a systems engineer who has diagnosed regex bugs in production.
> Every section answers the question: "Why does this happen, and how do I find it fast?"

---

## 1. The Systematic Debugging Framework: SIMPLIFY → VISUALIZE → ISOLATE → INSTRUMENT → VERIFY

When a regex doesn't work, most engineers immediately start modifying the pattern blindly, trying different quantifiers or escapes until something sticks. This is the worst possible approach — it masks the root cause and often introduces new bugs.

Use this framework instead:

### STEP 1: SIMPLIFY

Before adding more to the pattern, remove things. Strip the pattern down to its minimal components:

- Does the literal part match? (`Pattern.compile("hello").matcher(input).find()`)
- Does the character class match? (`\\d`, `\\w`, `[a-z]`)
- Does the quantifier scope the right range?

Starting simple and building up is always faster than debugging a complex pattern from scratch.

### STEP 2: VISUALIZE

Before running any code, trace through the pattern manually against a minimal input. Break the pattern into tokens and walk through each one:

```
Pattern: (\w+)=(\w+)
Input:   "name=Alice"

Token 1: (\w+) — greedily matches "name"
Token 2: =     — literal, matches "="
Token 3: (\w+) — greedily matches "Alice"
Result:  match! group(1)="name", group(2)="Alice"
```

This step catches 50% of bugs before you even run the code. The act of writing out the token trace forces you to notice mismatched escapes, wrong quantifiers, and missing groups.

### STEP 3: ISOLATE

Reduce the failing test case to the minimal input that reproduces the failure:

- "Works on 'abc' but fails on 'abc def'" → the space is relevant
- "Works on 3 words but fails on 20" → a backtracking issue
- "Works with MULTILINE but not without" → a flag issue

The minimal failing case tells you exactly what the bug is.

### STEP 4: INSTRUMENT

Add print statements to see what the matcher is actually doing:

```java
Matcher m = Pattern.compile(pattern).matcher(input);
while (m.find()) {
    System.out.printf("Match at [%d, %d): '%s'%n",
                      m.start(), m.end(), m.group());
    for (int i = 1; i <= m.groupCount(); i++) {
        System.out.printf("  group(%d) = '%s' at [%d, %d)%n",
                          i, m.group(i), m.start(i), m.end(i));
    }
}
```

`start()` and `end()` tell you WHERE the engine matched — not just what it captured. Seeing positions is often the key to understanding overcapture, missed matches, and anchor issues.

### STEP 5: VERIFY

Once you have a fix, write tests for:
1. The original failing case
2. The boundary cases around it
3. The known-working cases that must still work
4. An adversarial case (very long input, special characters, Unicode)

Never commit a regex fix without verifying all five categories.

---

## 2. How to Read a Regex as a Parser Engineer

Experienced engineers read regex the same way a compiler reads a grammar — as a sequence of productions, each consuming part of the input. Here is the mental model:

### Token Decomposition

Break the pattern at the top level first:

```
Pattern: ^(\d{4})-(\d{2})-(\d{2})$
Tokens:  ^  (\d{4})  -  (\d{2})  -  (\d{2})  $
```

Then for each token, identify:
1. What characters does it match?
2. How many times (quantifier)?
3. Is it a group? Does it capture?
4. Is it an anchor (no character consumption)?

### The "position cursor" model

Think of regex execution as a position cursor moving left-to-right through the input. Each token either:
- **Advances the cursor** (character matches): `\d`, `.`, `[a-z]`, literal `x`
- **Branches the cursor** (quantifiers, alternatives): the engine may need to try multiple paths
- **Asserts position** (anchors, lookaheads): does not move the cursor, only checks

This mental model makes it obvious why `^foo$` fails on `"foobar"` — the `$` anchor fails to assert "end of string" when there's still "bar" in the input after "foo".

### Greedy vs Lazy Quantifier Tracing

For `.*` on input `"<a>content</a>"`:

```
.*  greedily consumes: "<a>content</a>"  (entire string)
     Now nothing left for the rest of the pattern to match.
     Backtrack: give up one char → ">" . No improvement.
     Keep backtracking until the rest of the pattern can match.
```

For `.*?` on the same input:

```
.*? lazily consumes: "" (nothing)
     Try to match rest of pattern. Can it? Try giving up 0 chars: check.
     If not, consume 1 char and retry.
     Consumes minimum needed for the whole pattern to match.
```

Writing out the greedy vs lazy trace manually exposes overcapture bugs immediately.

---

## 3. Using matcher.start() and matcher.end() for Diagnosis

Position information is your most powerful debugging tool. These two methods tell you exactly where the engine matched — and more importantly, WHERE IT STARTED AND ENDED.

### The Diagnostic Print Loop

```java
String pattern = "your_pattern";
String input   = "your input string";

Matcher m = Pattern.compile(pattern).matcher(input);
int matchCount = 0;
while (m.find()) {
    matchCount++;
    System.out.printf("[Match %d]  start=%d  end=%d  text='%s'%n",
                      matchCount, m.start(), m.end(), m.group());
    for (int i = 1; i <= m.groupCount(); i++) {
        System.out.printf("  group(%d): start=%d  end=%d  value=%s%n",
                          i, m.start(i), m.end(i),
                          m.group(i) == null ? "<null>" : "'" + m.group(i) + "'");
    }
}
System.out.println("Total matches: " + matchCount);
```

Run this before you change anything. The output tells you:
- Whether the engine is matching at all
- Whether it's matching at the right position
- Whether groups are capturing the right substrings
- Whether a match is "bigger" than expected (overcapture)

### Common Position Diagnosis Patterns

**Overcapture discovered by position:**
```
Pattern: <title>.*</title>
Input:   "<title>A</title><title>B</title>"
Match 1: start=0  end=32  text='<title>A</title><title>B</title>'
```
The match spans the entire string — the `.*` greedily consumed everything including the first `</title>`.

**Missed match discovered by position:**
```
Pattern: ^\d+$
Input:   "line1\n42\nline3"
Expected: match "42"
Actual:  0 matches
```
Without `MULTILINE`, `^` and `$` only match at the absolute start/end of the input. "42" is in the middle of the string.

**Wrong group discovered by index:**
```
Pattern: (\d{4})-(\d{2})-(\d{2})
Input:   "2024-01-15"
group(0) = "2024-01-15"   (WHOLE MATCH — often the mistake)
group(1) = "2024"         (first group — the year)
group(2) = "01"           (second group — the month)
group(3) = "15"           (third group — the day)
```

---

## 4. Bug Category 1: Wrong Method — matches() vs find()

**This is the single most common Java regex bug.**

### The Bug

```java
// Trying to check if a string CONTAINS a 5-digit ZIP code
String input = "ZIP: 90210 is in California";
boolean result = input.matches("\\d{5}"); // returns FALSE — but should be TRUE!
```

Why it fails: `String.matches(regex)` (and `Matcher.matches()`) requires the **entire string** to match. The string `"ZIP: 90210 is in California"` is not just 5 digits, so it returns false.

### The Fix

```java
boolean result = Pattern.compile("\\d{5}").matcher(input).find(); // TRUE
```

`find()` searches for any substring matching the pattern. It doesn't require the entire input to be consumed.

### The Rule of Thumb

- **Validating**: use `matches()` — "Is this entire string a valid email/ZIP/phone?"
- **Searching**: use `find()` — "Does this string contain an email/ZIP/phone anywhere?"

### Boundary Case: 6-digit Number Should Not Match as ZIP

```java
String input = "902109"; // 6 digits — NOT a valid standalone ZIP
Pattern.compile("\\d{5}").matcher(input).find(); // TRUE — matches "90210" inside "902109"!
```

To prevent this, use word boundaries or lookaheads:
```java
Pattern.compile("(?<!\\d)\\d{5}(?!\\d)").matcher("902109").find(); // FALSE — correct
```

The lookbehind `(?<!\d)` asserts "no digit before this", and the lookahead `(?!\d)` asserts "no digit after this".

---

## 5. Bug Category 2: Greedy Overcapture

**Greedy quantifiers consume as much as possible.** This causes patterns to match far more than intended.

### The Classic HTML Tag Bug

```java
// BROKEN: trying to extract each title separately
Pattern broken = Pattern.compile("<title>(.*)</title>");
Matcher m = broken.matcher("<title>First</title><title>Second</title>");
if (m.find()) {
    System.out.println(m.group(1)); // "First</title><title>Second" — WRONG!
}
```

Why: `.*` is greedy. It matches `First</title><title>Second` — everything from after the first `<title>` to just before the LAST `</title>`.

### The Lazy Fix

```java
Pattern fixed = Pattern.compile("<title>(.*?)</title>");
```

`.*?` is the lazy version: it matches the MINIMUM number of characters needed for the overall pattern to succeed.

### The Negated Character Class Fix (Often Better)

```java
Pattern fixed = Pattern.compile("<title>([^<]*)</title>");
```

`[^<]*` matches "any character except `<`" — structurally prevents the match from crossing a tag boundary. This is often more explicit and slightly more efficient than lazy quantifiers because it removes backtracking entirely.

### When to Use Lazy vs Negated Class

- **Lazy `.*?`**: when the delimiter is a multi-character sequence (`</title>`) and you can't express it as a simple character class. Simple to write.
- **Negated class `[^x]*`**: when the delimiter is a single character. More performant (no backtracking), more explicit about intent. Always prefer this when it's applicable.

---

## 6. Bug Category 3: Missing Flags

### Missing MULTILINE

**Symptom**: `^pattern` or `pattern$` only matches on the first or last line of a multi-line string.

```java
// BROKEN: finds no comment lines except if the entire input starts with #
Pattern broken = Pattern.compile("^#.*");
Matcher m = broken.matcher("code\n# comment\nmore code");
// m.find() returns FALSE!
```

**Fix**:
```java
Pattern fixed = Pattern.compile("^#.*", Pattern.MULTILINE);
// or inline: Pattern.compile("(?m)^#.*");
```

**Mental model**: Without `MULTILINE`, `^` is an absolute anchor to position 0 in the input string. With `MULTILINE`, `^` is a relative anchor to any position after a `\n` (or position 0).

### Missing DOTALL

**Symptom**: A pattern with `.` fails on inputs that contain newlines.

```java
// BROKEN: trying to match a multi-line JSON value
Pattern broken = Pattern.compile("\"description\":\\s*\"(.*)\"");
String input = "\"description\": \"This spans\nmultiple lines\"";
// m.find() returns FALSE — . doesn't match \n
```

**Fix**:
```java
Pattern fixed = Pattern.compile("\"description\":\\s*\"(.*?)\"", Pattern.DOTALL);
```

### MULTILINE + DOTALL Interaction

They are orthogonal — setting one does not imply the other. A common mistake is thinking `DOTALL` makes `^` match line starts. It doesn't. `MULTILINE` and `DOTALL` affect completely different parts of the engine.

```
MULTILINE: changes ^ and $ behavior
DOTALL:    changes . behavior
Neither:   affects \w, \d, \s (unless UNICODE_CHARACTER_CLASS is set)
```

### Missing CASE_INSENSITIVE

```java
// BROKEN: trying to match both "Error" and "ERROR"
Pattern broken = Pattern.compile("error");
// Does not match "Error" or "ERROR"

Pattern fixed = Pattern.compile("error", Pattern.CASE_INSENSITIVE);
// Matches "error", "Error", "ERROR", "eRrOr"
```

---

## 7. Bug Category 4: Wrong Group Index

**Group(0) is the entire match. Group(1) is the first captured group.**

This off-by-one is the most common group-related bug.

```java
// BROKEN: trying to extract the year from a date
Matcher m = Pattern.compile("(\\d{4})-(\\d{2})-(\\d{2})").matcher("2024-01-15");
if (m.find()) {
    return m.group(0); // Returns "2024-01-15" — the WHOLE MATCH, not the year!
}
```

**Fix**: Use `group(1)` for the first capturing group:

```java
return m.group(1); // Returns "2024" — the year
```

### Debugging Group Indices

Count opening parentheses from left to right:

```
Pattern: (\d{4})-((\d{2})-(\d{2}))
         ^       ^^      ^
         1       23      4

group(0) = entire match
group(1) = (\d{4})         = year
group(2) = ((\d{2})-(\d{2})) = month-day combined
group(3) = (\d{2}) first   = month
group(4) = (\d{2}) second  = day
```

### Use Named Groups to Avoid Index Bugs

```java
Pattern p = Pattern.compile("(?<year>\\d{4})-(?<month>\\d{2})-(?<day>\\d{2})");
// No need to count parentheses — intent is explicit
m.group("year");  // "2024"
m.group("month"); // "01"
m.group("day");   // "15"
```

---

## 8. Bug Category 5: Catastrophic Backtracking (ReDoS)

**This is not just a correctness bug — it is a security vulnerability.**

### What Causes Catastrophic Backtracking

Catastrophic backtracking occurs when a pattern has **ambiguous decompositions** of the input, and the NFA engine tries all possible decompositions before failing.

**The archetypal catastrophic pattern**: `(a+)+`

On input `"aaaa!"`:
- The engine must decide how to group the `a` characters across the outer and inner `+` repetitions
- `(a+)` matching `"aaaa"` at once is one option
- `(a+)` matching `"aaa"` then `(a+)` matching `"a"` is another
- `(a+)` matching `"aa"` then `(a+)` matching `"aa"` is another
- `(a+)` matching `"aa"` then `(a+)(a+)` matching `"a"` and `"a"` separately... 
- Number of decompositions is exponential in the length of input before the `!`

For n `a`s, the engine performs O(2^n) work before concluding there's no match.

### The Production Log Parser Example

```java
// BROKEN — catastrophic on adversarial input
Pattern broken = Pattern.compile("^((\\w+\\s*)+):\\s*(.*?)$");
```

Here `(\\w+\\s*)+` on input `"word word word word word word!"` (no colon at end):
- `(\w+\s*)` can match `"word "` as one group, or `"word"` + `""` (empty space portion)
- With `\s*` (zero or more spaces), there are multiple ways to split `"word "` into `\w+` and `\s*`
- The engine tries all possible decompositions before failing
- With 10+ words, this takes seconds or minutes

### Safe Rewrite Strategies

**Strategy 1: Eliminate the ambiguity structurally**

Instead of `(\w+\s*)+` where both `\w+` and `\s*` can be empty in different combinations, use:
```
\w+(\s+\w+)*
```
Here `\s+` requires at least one space, so `"\w+"` and `"\s+"` cannot overlap. The decomposition is unique.

**Strategy 2: Use a negated character class**

Instead of matching the "label" as a series of words, match everything that's NOT the delimiter:
```
[^:]+
```
`[^:]+` matches any character except `:`, consuming the entire label in one linear pass with no backtracking.

**Strategy 3: Possessive quantifiers (Java supports these)**

Java supports possessive quantifiers with `++`, `*+`, `?+` — they never give back matched characters (no backtracking):
```
((\w+\s*)*+)  →  ((\w++\s*+)*+)
```
`\w++` matches one or more word characters and never releases them. If the match fails, the engine fails immediately without backtracking. This eliminates catastrophic cases.

**Strategy 4: Atomic groups**

Java also supports atomic groups `(?>...)` which similarly prevent backtracking within the group:
```
(?>(\w+\s*)+)
```
The atomic group commits to its first successful decomposition and never retries.

### Testing for Catastrophic Backtracking

```java
@Test
void testDoesNotCatastrophicallyBacktrack() {
    String adversarial = "word ".repeat(15) + "!"; // many words, no colon at end
    assertTimeout(Duration.ofMillis(200), () -> {
        solution.parseLogs(List.of(adversarial));
    });
}
```

If this test times out, the pattern is catastrophic. If it completes quickly, it's safe.

---

## 9. Tools for Debugging Regex

### regex101.com

The most important external tool. Key features:
- **FLAVOR**: select "Java 8" for accurate Java behavior
- **EXPLAIN mode**: click "Regex Debugger" to see step-by-step NFA execution including backtracking steps
- **Match information**: hover over each match to see group captures and positions
- **Quick reference**: right panel explains syntax while you type
- **Catastrophic detection**: the debugger shows an explosion in steps for catastrophic patterns

Workflow: paste your pattern, paste your input, look at the explanation. The "backtracking steps" count tells you immediately if your pattern is catastrophic.

### IntelliJ IDEA Regex Tester

In any string literal containing a regex, use the "Check RegExp" balloon (Alt+Enter or hover):
- Opens an inline tester where you type test inputs
- Highlights matched portions directly in the editor
- Shows group captures

This is the fastest workflow for patterns you're developing — no need to leave the IDE.

### Java REPL (jshell)

For quick one-off testing without a full class:

```
$ jshell
jshell> import java.util.regex.*
jshell> var m = Pattern.compile("\\d+").matcher("abc123def456")
jshell> while(m.find()) System.out.println(m.start() + " " + m.group())
3 123
9 456
```

### grep (for initial pattern development)

For simple patterns without Java-specific features, `grep -P` (Perl-compatible regex on macOS/Linux) gives fast feedback on whether a pattern matches a file's lines before you write any Java.

---

## 10. Reducing the Test Case

When a complex pattern fails on complex input, the first step is reduction:

**Reduction process:**
1. Take the failing input
2. Remove parts of it until the bug disappears — the removed part is NOT the cause
3. The smallest input that still fails is your minimal failing case
4. Debug against that minimal case — it's much clearer

**Example**: Pattern `^(\w+\s*)+:` fails on `"error loading config: failed"`. Is it the `:` position? The spaces? The specific words?
- Test with `"a b c: done"` — still fails? Good, simpler.
- Test with `"a b: done"` — still fails? Simpler still.
- Test with `"a: done"` — passes? So 2+ words before `:` triggers the bug.
- Now you know it's related to multiple words, and you have a 2-word minimal case.

---

## 11. The "Works on My Machine" Problem

### Platform Line Endings

Windows uses `\r\n` (carriage return + newline). Unix/Linux/macOS use `\n`. If your input comes from a Windows file, each line ends with `\r\n`.

```java
// Pattern: ^#.*$  (MULTILINE)
// Input from Windows file: "# comment\r\ncode"
// Match result: "# comment\r"  — the \r is included in .*
```

The `\r` at the end of each line is NOT consumed by `$`. It ends up in your captured group. Fix:

```java
Pattern.compile("^#[^\r\n]*", Pattern.MULTILINE); // stop before \r or \n
// or strip \r before processing:
input.replace("\r\n", "\n");
```

### Unicode and Encoding

A string loaded from a UTF-8 file through an ASCII-decoded stream will have garbled characters. Regex patterns expecting `\w` (ASCII word chars) will fail to match accented characters. Always specify encoding explicitly when reading files:

```java
Files.readString(path, StandardCharsets.UTF_8);
```

### Java Version Differences

`\b` word boundary behavior changed slightly in some Java versions for Unicode. `\d` in Java does NOT match Unicode digits by default (unlike Python). The `UNICODE_CHARACTER_CLASS` flag is needed for Unicode-aware `\d`, `\w`, `\s`. If code works in Python's regex but not Java, check this.

---

## 12. Writing Regex Unit Tests: The Anti-Regression Framework

### Test the Boundary, Not Just the Middle

For a pattern like `(?<!\d)\d{5}(?!\d)` (5-digit ZIP, not adjacent to other digits):

```
Test: "90210"          → TRUE  (exact match)
Test: "ZIP: 90210"     → TRUE  (surrounded by non-digits)
Test: "90210-1234"     → FALSE — wait, actually true? No: the - is not a digit, so the lookahead (?!\d) sees '-' which is fine. Actually TRUE.
```

Wait, let me be more precise:
```
Test: "90210"          → TRUE
Test: "902109"         → FALSE (6 consecutive digits — 5-digit sub is adjacent to a digit)
Test: "ZIP 90210 CA"   → TRUE
Test: "ZIP 9021 CA"    → FALSE (only 4 digits)
Test: "ZIP 902100 CA"  → FALSE (6 digits, adjacent)
Test: ""               → FALSE (empty)
```

Always test: exact match, too-short, too-long, embedded-in-longer, empty string, leading/trailing spaces.

### Test Error Categories, Not Just Happy Paths

For each regex bug category, write a test that would fail if that bug existed:

```java
// Test for matches() vs find() bug:
// If implementation uses matches(), this would fail
@Test
void testFindsZipEmbeddedInText() {
    assertTrue(solution.containsZipCode("My ZIP is 90210 here"));
}

// Test for greedy overcapture bug:
// If implementation uses .*, this would fail (returns only one match)
@Test
void testExtractsMultipleTitles() {
    List<String> titles = solution.extractTitles("<title>A</title><title>B</title>");
    assertEquals(2, titles.size());
}
```

### Parameterized Tests for Regex

```java
@ParameterizedTest
@CsvSource({
    "90210,           true",
    "902109,          false",
    "ZIP: 90210 CA,   true",
    "'',              false",
    "12345,           true"
})
void testZipCode(String input, boolean expected) {
    assertEquals(expected, solution.containsZipCode(input));
}
```

Parameterized tests make it trivial to add new test cases without duplicating setup code.

---

## 13. Production Debugging: Patterns and Practices

### Wrap Pattern.compile in User-Facing APIs

```java
public class RegexValidator {
    private final Pattern pattern;

    public RegexValidator(String regex) {
        try {
            this.pattern = Pattern.compile(regex);
        } catch (PatternSyntaxException e) {
            throw new IllegalArgumentException(
                "Invalid regex pattern '" + regex + "': " + e.getMessage(), e);
        }
    }
}
```

Never let `PatternSyntaxException` propagate to end users as an uncaught exception.

### Log Match Positions in Production Parsers

```java
private static final Logger log = LoggerFactory.getLogger(Parser.class);

while (m.find()) {
    log.debug("Pattern '{}' matched at [{},{}): '{}'",
              PATTERN.pattern(), m.start(), m.end(), m.group());
    // process match
}
```

Match positions in logs let you diagnose production parsing failures against real data without reproducing locally.

### Validate Regex Patterns at Startup

If your application loads regex patterns from config files, validate all of them at startup:

```java
@PostConstruct
public void validatePatterns() {
    for (Map.Entry<String, String> entry : patternConfig.entrySet()) {
        try {
            Pattern.compile(entry.getValue());
        } catch (PatternSyntaxException e) {
            throw new BeanInitializationException(
                "Invalid regex pattern for key '" + entry.getKey() + "': " + e.getMessage());
        }
    }
}
```

Fail fast at startup rather than failing silently at runtime when a particular input triggers the bad pattern.

### Add Timeout Protection for External Patterns

When executing user-supplied patterns (e.g., a search feature), add timeout protection:

```java
ExecutorService exec = Executors.newSingleThreadExecutor();
Future<List<String>> future = exec.submit(() -> {
    Matcher m = userPattern.matcher(input);
    List<String> results = new ArrayList<>();
    while (m.find()) results.add(m.group());
    return results;
});
try {
    return future.get(500, TimeUnit.MILLISECONDS);
} catch (TimeoutException e) {
    future.cancel(true);
    throw new RuntimeException("Pattern execution timed out — possible ReDoS");
}
```

---

## 14. Interview Debugging Questions and Their Answers

### "This pattern doesn't match — why?"

**Script**: Check in order:
1. Is it `matches()` vs `find()`? (Most common)
2. Are the escapes correct? (`\d` in pattern must be `\\d` in Java string)
3. Is a flag missing? (MULTILINE for `^`/`$`, DOTALL for `.`)
4. Is the group index correct? (group(0) vs group(1))
5. Is there greedy overcapture?

### "The pattern works on simple inputs but hangs on complex ones"

**Answer**: Catastrophic backtracking. Look for nested quantifiers on overlapping character classes: `(\w+\s*)+`, `(a*b*)+`, `(.+)+`. Rewrite using possessive quantifiers, atomic groups, or a non-backtracking equivalent.

### "The pattern extracts the wrong substring"

**Answer**: Either wrong group index (usually off by one — check where the opening parentheses are) or greedy overcapture (replace `.*` with `.*?` or `[^delimiter]*`).

### "The pattern works in Python but not Java"

**Common causes**:
- Python's `re.match()` is like Java's `lookingAt()`, not `matches()`
- Python `re.search()` is like Java's `find()`
- Python `re` does not need double-escaping (uses raw strings `r"\d+"`), Java does need `\\d`
- Python's `\d` matches Unicode digits; Java's does not (without `UNICODE_CHARACTER_CLASS`)

---

*Work through the problems starting with `fix-greedy-overcapture`. Each builds on the debugging skills introduced here.*
