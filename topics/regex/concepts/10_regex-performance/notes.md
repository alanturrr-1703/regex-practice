# Deep Notes: Regex Performance & ReDoS Prevention

> Audience: engineers who want to understand why regex performance fails, how to diagnose
> it systematically, and what concrete techniques prevent production incidents.

---

## 1. Java's Regex Engine: NFA with Backtracking

### NFA vs DFA — The Fundamental Choice

Two families of regex engines exist:

**DFA (Deterministic Finite Automaton)**
- Compiled to a state machine where each character drives a single transition.
- Always O(N) time — guaranteed linear scan.
- Cannot support backreferences, lookaheads, or lookbehinds.
- Used by: `awk`, `grep -E` (GNU), RE2, Hyperscan.

**NFA (Nondeterministic Finite Automaton)**
- Simulates "all possible paths" via backtracking.
- Supports backreferences, lookaheads, possessive quantifiers.
- Worst case: exponential time.
- Used by: Java `java.util.regex`, PCRE, Python `re`, .NET, Perl, Ruby.

Java chose NFA because feature richness matters more than worst-case guarantees for most
use cases. The tradeoff is that you — the developer — must ensure your patterns do not
trigger exponential behavior.

### How the NFA Works

The NFA engine maintains a "thread" of execution with a current position in the input and
a current state in the pattern. When it hits a choice point (alternation `|` or quantifier
`*`/`+`/`?`), it pushes the alternative onto a **backtrack stack** and pursues one path.

If a path fails (reaches a point where no character matches), the engine **pops** the
stack and tries the alternative. This is backtracking.

```/dev/null/nfa-model.txt#L1-20
Pattern: a+b
Input:   "aaac"

Step 1: a+ greedily matches "aaa" (positions 0-2), cursor at 3
Step 2: try to match 'b' at position 3 — input is 'c' — FAIL
Step 3: BACKTRACK — a+ gives back one 'a', cursor at 2
Step 4: try to match 'b' at position 2 — input is 'a' — FAIL
Step 5: BACKTRACK — a+ gives back one 'a', cursor at 1
Step 6: try to match 'b' at position 1 — input is 'a' — FAIL
Step 7: BACKTRACK — a+ gives back one 'a', cursor at 0
Step 8: a+ has minimum 1, already used 1 — cannot give back more — overall FAIL

Total steps: O(N) for this case — linear backtracking
```

This linear backtracking is normal and acceptable. The problem arises with **exponential**
backtracking.

---

## 2. Catastrophic Backtracking — Root Cause Analysis

### The Structural Warning Sign

Catastrophic backtracking occurs when there is **ambiguity** in how the NFA can partition
the input. The canonical example:

```/dev/null/catastrophic.txt#L1-3
Pattern: (a+)+
Input:   "aaaaaaaaaaaa!"    (N 'a's followed by '!')
```

### Why This Is Exponential

`(a+)+` means "one or more repetitions of (one or more 'a')". For N 'a' characters, the
inner `a+` can match any non-zero partition:
- One group of N: `(aaaaaaaaaa)`
- Two groups: `(a)(aaaaaaaaa)`, `(aa)(aaaaaaaa)`, ..., `(aaaaaaaaa)(a)`
- Three groups: `(a)(a)(aaaaaaaa)`, ...
- And so on...

The number of ways to partition N 'a's into non-empty groups is 2^(N-1) (it equals the
number of binary strings of length N-1, choosing which positions are group boundaries).

When the '!' is encountered, no path matches, so the engine tries **all 2^(N-1) partitions**
before concluding failure. For N=30, that's 500 million paths. For N=40, it's over a trillion.

### The Mathematical Model

Let T(n) be the number of states the engine explores for `(a+)+` on "aaa...a!" (n a's):

```/dev/null/recurrence.txt#L1-8
T(1) = 1    (just tries (a) -> fails at '!')
T(2) = 3    (tries (aa), (a)(a) -> fails both)
T(3) = 7    (tries (aaa), (a)(aa), (aa)(a), (a)(a)(a))
T(n) = 2*T(n-1) + 1  =>  T(n) = 2^n - 1

This is O(2^n) — catastrophically exponential.
```

### Other Catastrophic Structures

```/dev/null/catastrophic-structures.txt#L1-12
(a+)+          -- classic nested quantifier
(.+)+          -- same, with any character
(a*)+          -- same, zero-length inner makes it worse
(a|a)+         -- alternation where both branches match the same set
(a+|b+)+       -- overlapping alternation with quantifiers
([a-z]+)+      -- nested quantifier on a character class
^(a*)*$        -- anchored catastrophic (even worse — must exhaust all paths)
(a+b?)+        -- overlapping: b? means inner can match a-only or ab
```

The key structural indicator is: **a quantifier applied to a group that itself contains
a quantifier, where the inner and outer character sets overlap**.

### When It Becomes ReDoS

If user input controls what goes into the regex engine, an attacker can craft input of
the form:

```/dev/null/redos-input.txt#L1-5
// Pattern: ^([a-z]+[a-z0-9]*)*@[a-z]+\.[a-z]{2,}$  (catastrophic local part)
// Safe input:    "user@example.com"          -> fast
// Attack input:  "aaaaaaaaaaaaaaaaaaaaa@"    -> hangs for seconds/minutes
//                                               (no @ but many a's before it)
```

Famous real-world ReDoS incidents:
- **Cloudflare outage (July 2019)**: a WAF regex `(?:(?:\"|'|\]|\}|\\|\d|(?:nan|infinity|true|false|null|undefined|symbol|math)|\`|\-|[+\-])` triggered catastrophic backtracking on certain HTTP headers, taking down Cloudflare globally for ~27 minutes.
- **Stack Overflow (July 2016)**: a poorly-written regex in their markdown parser caused a cascading failure.
- **npm (March 2016)**: a ReDoS in the `ms` package (via `ua-parser-js`) affected millions of projects.

---

## 3. Detection Strategies

### Strategy 1: Structural Analysis

Look for:
1. A quantifier (`*`, `+`, `{n,}`) applied to a group.
2. The group itself contains a quantifier.
3. The inner and outer patterns can match the same characters.

```/dev/null/detect-structural.txt#L1-10
SAFE:    \d+[A-Z]+      -- no nesting of quantifiers
SAFE:    (?:abc)+       -- inner is a literal, no inner quantifier
SAFE:    (?:[a-z]+)[0-9]+ -- inner group quantifier, BUT separated by non-overlapping outer
DANGER:  ([a-z]+)+      -- nested, same charset
DANGER:  (\w+\s*)+      -- nested, overlapping (\s is subset of \w? No, but whitespace can appear in \w...actually \w is [a-zA-Z0-9_] and \s is whitespace — non-overlapping but the repetition creates ambiguity at boundaries)
DANGER:  (a*b)+         -- the a* can match zero chars, creating cycles
```

### Strategy 2: Adversarial Testing with Timeout

For programmatic detection, test the pattern against a known-bad input with a time limit:

```/dev/null/adversarial.java#L1-20
public static boolean isSafePattern(String regex) {
    try {
        Pattern p = Pattern.compile(regex);
        // Adversarial input: many repeated chars + a char that doesn't match
        String adversarial = "a".repeat(25) + "!";
        ExecutorService exec = Executors.newSingleThreadExecutor();
        Future<Boolean> future = exec.submit(() -> {
            p.matcher(adversarial).matches();
            return true;
        });
        try {
            future.get(100, TimeUnit.MILLISECONDS);  // 100ms budget
            return true;   // completed quickly -> likely safe
        } catch (TimeoutException e) {
            future.cancel(true);
            return false;  // timed out -> likely catastrophic
        } finally {
            exec.shutdownNow();
        }
    } catch (PatternSyntaxException e) {
        return false;  // invalid pattern
    }
}
```

This is not a proof of safety — it's a heuristic. A pattern might be safe on `"aaa...a!"`
but catastrophic on a different adversarial input. Static analysis tools (like `vuln-regex-detector`)
give more comprehensive answers.

### Strategy 3: Use a Static Analysis Tool

Tools like `safe-regex` (Node.js) and Java's own FindBugs/SpotBugs with regex plugins, or
the OWASP RegexSecurity Scanner, can statically detect polynomial and exponential patterns.
Consider integrating these into your CI pipeline.

---

## 4. Fix Strategies

### Fix 1: Possessive Quantifiers

A **possessive quantifier** matches as much as possible and **never gives it back**. It
disables backtracking for that quantifier entirely.

```/dev/null/possessive.txt#L1-10
Greedy:     a+     -- matches 1+, will give chars back if rest fails
Lazy:       a+?    -- matches 1, will accept more if rest fails
Possessive: a++    -- matches 1+, NEVER gives chars back

Similarly:
*+   zero-or-more possessive
?+   zero-or-one possessive
{n,m}+  bounded possessive
```

When is possessive safe? When the characters matched by the quantifier **cannot** be
needed by what follows. This is the case when the character sets are **disjoint**:

```/dev/null/possessive-safe.txt#L1-10
SAFE with possessive:
\d++[A-Z]     -- digits then uppercase; digits can't be uppercase
[^"]*+"       -- non-quote chars then quote; sets are disjoint
[A-Z]++-\d+   -- uppercase then hyphen then digit; all disjoint

UNSAFE — do NOT use possessive:
[a-z]++[a-z]  -- possessive grabs ALL lowercase, leaving none for the last [a-z]
               -- this will NEVER match "abc" because ++ ate all three letters
```

Possessive quantifiers are Java-specific (not in all engines). In PCRE they work the same.

### Fix 2: Atomic Groups `(?>...)`

An **atomic group** makes the entire group possessive — once the group matches something,
it will not backtrack into the group to try shorter alternatives:

```/dev/null/atomic.txt#L1-6
(?>a+)b     -- matches one or more 'a', then exactly 'b'
             -- if 'b' fails, the engine does NOT retry with fewer 'a's
             -- equivalent to a++b

// Rewriting the catastrophic pattern:
(?>([a-z]+))+  -- outer quantifier has no overlap with inner due to atomicity
```

Atomic groups are available in Java 8+ via `(?>...)` syntax.

### Fix 3: Restructure to Eliminate Ambiguity

The most robust fix: **redesign the pattern so there is only one way to match the input**.

```/dev/null/restructure.txt#L1-12
// BEFORE (catastrophic):
^([a-z]+[a-z0-9]*)*@...
// Why: inner ([a-z]+[a-z0-9]*) can match any [a-z0-9]+ string multiple ways:
// "abc123" could be (abc)(123) or (a)(bc123) or (abc123) etc.

// AFTER (safe):
^[a-z][a-z0-9]*(?:\.[a-z0-9]+)*@...
// Why: no nested quantifiers; each part matches a distinct set
//      or is separated by a literal character (.)
```

The restructuring principle: **use literals as anchors between quantified segments**.
A literal like `.` or `@` or `-` that the quantified patterns cannot match acts as a
fence that prevents the engine from splitting the match across segments.

### Fix 4: Bound Your Inputs

Before running regex on user input, enforce length limits:

```/dev/null/bound-input.java#L1-8
public boolean isValidEmail(String email) {
    if (email == null || email.length() > 254) {  // RFC 5321 max
        return false;
    }
    return EMAIL_PATTERN.matcher(email).matches();
}
```

Even a catastrophic pattern on a bounded input is safe — 2^30 might be terrible but
2^254 is the concern. Keep inputs short (passwords < 72 chars, emails < 254, etc.).

---

## 5. Pattern Compilation Cost

### What `Pattern.compile()` Does

`Pattern.compile(regex)` performs:
1. Lexical analysis of the regex string (tokenization)
2. Parsing into an AST
3. Optimization passes (e.g., literal prefix extraction, character class merging)
4. NFA construction (state machine with epsilon transitions)
5. Optional DFA subset construction (for certain simple patterns)

This is a non-trivial computation — benchmarks show it takes 1-100 microseconds depending
on pattern complexity. That's 1000x slower than a simple `char` comparison.

### The `String.matches()` Trap

```/dev/null/string-matches-trap.java#L1-10
// This is what String.matches(regex) does internally:
public boolean matches(String regex) {
    return Pattern.matches(regex, this);
}

// And Pattern.matches:
public static boolean matches(String regex, CharSequence input) {
    Pattern p = Pattern.compile(regex);   // RECOMPILES EVERY TIME
    Matcher m = p.matcher(input);
    return m.matches();
}
```

If you call `line.matches("\\d+")` in a loop over 10,000 lines, you compile the pattern
10,000 times. The same applies to `String.replaceAll()` and `String.split()`.

### The Correct Pattern

```/dev/null/correct-pattern.java#L1-12
public class PhoneValidator {
    // Compiled ONCE at class load time
    private static final Pattern PHONE_PATTERN =
        Pattern.compile("\\d{3}-\\d{4}");

    public boolean isValid(String s) {
        return PHONE_PATTERN.matcher(s).matches();
    }

    // Even better for high throughput: reuse Matcher via reset()
    // (Matcher is NOT thread-safe — don't share across threads)
}
```

### Reusing `Matcher` Objects

`Matcher.reset(CharSequence)` resets the matcher for a new input without allocating a new
`Matcher` object. Useful in single-threaded tight loops:

```/dev/null/matcher-reuse.java#L1-10
Matcher m = PATTERN.matcher("");   // dummy input — will be reset
for (String line : lines) {
    m.reset(line);
    if (m.matches()) {
        process(line);
    }
}
```

Note: `Matcher` is **not thread-safe**. Use a `ThreadLocal<Matcher>` or just call
`Pattern.matcher()` per-call in multithreaded contexts (the allocation is cheap compared
to `compile()`).

---

## 6. Pattern Flags and Performance

### CASE_INSENSITIVE

`Pattern.CASE_INSENSITIVE` adds overhead: every character comparison must now consider both
the upper and lower case variant. On ASCII-only patterns with `Pattern.CASE_INSENSITIVE |
Pattern.UNICODE_CASE`, the overhead is even higher because Unicode case folding involves
consulting lookup tables.

**Recommendation**: if you know your input is ASCII, use `(?i)` inline sparingly, or
pre-normalize your strings before matching (e.g., `input.toLowerCase(Locale.ROOT)`).

### MULTILINE vs DOTALL

- `MULTILINE`: `^` and `$` match at line boundaries (not just input boundaries). Adds
  minimal overhead.
- `DOTALL`: `.` matches `\n`. Adds minimal overhead but changes semantics significantly
  — be explicit about whether you want to cross line boundaries.

### UNICODE_CHARACTER_CLASS

`Pattern.UNICODE_CHARACTER_CLASS` makes `\d`, `\w`, `\s` Unicode-aware. This is much slower
than the ASCII defaults because the Unicode tables are large. Use only when you actually
need to match Unicode digits/words/spaces.

---

## 7. Benchmarking Regex with JMH

For production-critical regex, measure before optimizing. The Java Microbenchmark Harness
(JMH) is the standard tool:

```/dev/null/jmh-benchmark.java#L1-30
@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.MICROSECONDS)
@Warmup(iterations = 5, time = 1, timeUnit = TimeUnit.SECONDS)
@Measurement(iterations = 10, time = 1, timeUnit = TimeUnit.SECONDS)
@Fork(2)
public class RegexBenchmark {

    private static final Pattern FAST = Pattern.compile("[a-z0-9._%+\\-]+@[a-z0-9.\\-]+\\.[a-z]{2,}");
    private static final String EMAIL = "user.name+tag@example.co.uk";

    @Benchmark
    public boolean fastPattern() {
        return FAST.matcher(EMAIL).matches();
    }

    @Benchmark
    public boolean slowPattern() {
        // Recompiles every time -- the baseline "bad" measurement
        return EMAIL.matches("[a-z0-9._%+\\-]+@[a-z0-9.\\-]+\\.[a-z]{2,}");
    }
}
```

Typical findings:
- `FAST` (static Pattern): ~0.5 microseconds
- `slowPattern` (recompile): ~15-50 microseconds
- Catastrophic on adversarial input: seconds to never-terminates

---

## 8. The Production-Safe Regex Checklist

Before deploying any regex that will process untrusted input:

```/dev/null/checklist.txt#L1-20
[ ] 1. Pattern is compiled as a static final field — not inside a method/loop
[ ] 2. No nested quantifiers on overlapping character sets
       (check for (X+)+ or (X*|Y+)+ or similar structures)
[ ] 3. Input length is bounded before regex is applied
[ ] 4. Adversarial test: run "aaaa...a!" (30+ chars) and verify it completes fast
[ ] 5. If processing user-supplied input: consider running match in a separate
       thread with a timeout (ExecutorService + Future.get(timeout))
[ ] 6. Character classes in adjacent quantified groups are disjoint
       OR possessive quantifiers / atomic groups are used
[ ] 7. Pattern has been reviewed by a second engineer if it processes
       security-sensitive data (auth, email, SSN, credit cards)
[ ] 8. Unit tests include adversarial inputs, not just valid ones
```

---

## 9. Deep Dive: Possessive Quantifiers vs Atomic Groups

Both eliminate backtracking, but they operate at different granularities:

```/dev/null/possessive-vs-atomic.txt#L1-20
Possessive quantifier: applied per-repetition
  a++b    -- each 'a' consumed is never given back; single quantifier

Atomic group: applied to the whole group once it matches
  (?>a+)b  -- group matches as much as possible; group result is all-or-nothing
            -- if 'b' fails, engine does NOT retry with fewer 'a's from the group

These are semantically equivalent for simple cases:
  a++b  ==  (?>a+)b

But they differ for complex groups:
  (?>ab|a)c   -- tries 'ab' first; if 'c' fails, does NOT backtrack to try just 'a'
  (?:ab|a)++c -- different semantics (possessive applies per repetition of the group)
```

For the log parser problem: prefer atomic groups when the group has alternation, possessive
quantifiers when applying to a single character class.

---

## 10. Summary Reference Card

```/dev/null/perf-summary.txt#L1-40
JAVA REGEX ENGINE
─────────────────────────────────────────────────────────────
Type:    NFA with backtracking (like PCRE, Perl, Python)
Time:    O(N) best case, O(2^N) worst case
Flexibility: supports lookahead, lookbehind, backreferences

CATASTROPHIC BACKTRACKING
─────────────────────────────────────────────────────────────
Root cause: ambiguity in how NFA partitions input
Warning signs: (X+)+  (.+)+  (a*b?)+  nested quantifiers
               where inner and outer patterns overlap
Attack vector: ReDoS — crafted string of length N triggers O(2^N) work

FIX STRATEGIES (in order of preference)
─────────────────────────────────────────────────────────────
1. Restructure: eliminate ambiguity with literal anchors
2. Possessive quantifiers: a++  [a-z]++  \d*+
3. Atomic groups: (?>a+)  (?>email-local-part)
4. Bound input: validate length before matching
5. Timeout: run match in ExecutorService with get(timeout, MILLISECONDS)

COMPILATION COST
─────────────────────────────────────────────────────────────
Pattern.compile()    1-100 microseconds — DO ONCE, statically
String.matches()     recompiles every call — NEVER in hot path
String.replaceAll()  recompiles every call — NEVER in hot path
String.split()       recompiles every call — NEVER in hot path

CORRECT PATTERN
─────────────────────────────────────────────────────────────
private static final Pattern P = Pattern.compile("...");
// Per call:
P.matcher(input).matches()
// High-throughput single-threaded:
Matcher m = P.matcher(""); m.reset(input); m.matches()
// Multithreaded: Pattern.matcher() per call is fine (Pattern IS thread-safe)

FLAGS
─────────────────────────────────────────────────────────────
CASE_INSENSITIVE   moderate overhead; use toLowerCase() for ASCII
UNICODE_CASE       high overhead; only when needed
UNICODE_CHARACTER_CLASS  high overhead; only for true Unicode matching
```
