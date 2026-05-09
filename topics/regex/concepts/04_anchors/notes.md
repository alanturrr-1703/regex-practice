# Anchors: Deep Notes
## Zero-Width Position Assertions, Multiline Mode, Word Boundaries, and the Absolute Anchors

---

## 1. What "Zero-Width" Actually Means

Most regex tokens consume characters. `\d` matches one character and advances the engine's position by one. A quantifier like `+` extends that to multiple characters consumed.

Anchors are different. They **consume nothing**. They assert a condition about the current position and either pass or fail. If they pass, the engine continues matching at the *same position*. If they fail, this path through the pattern is abandoned.

This has critical implications:
- Anchors can be placed anywhere in a pattern without consuming characters
- Multiple anchors can exist in a single pattern
- Anchors interact with `find()` in counterintuitive ways (explored below)
- Anchors are essentially free in terms of input consumption — they only cost a position check

The engine processes anchors as **state transitions in the NFA** that are conditioned on position metadata, not on the input character itself.

---

## 2. The `^` and `$` Anchors in Default Mode

### 2.1 `^` — Start of Input (Default)

In Java's `Pattern` with no special flags, `^` asserts: **"the current position is at index 0 of the input string."**

```java
Pattern p = Pattern.compile("^hello");
p.matcher("hello world").find();    // TRUE  — 'hello' starts at position 0
p.matcher("say hello").find();      // FALSE — 'hello' does not start at position 0
p.matcher("hello\nworld").find();   // TRUE  — default mode: ^ only matches absolute start
```

The third case is important: even though `"world"` is on a new line, `^` in default mode does NOT match the start of that line. It only matches position 0.

### 2.2 `$` — End of Input (Default)

In default mode, `$` asserts: **"the current position is at the end of the string, OR just before a `\n` that is the last character."**

```java
Pattern p = Pattern.compile("world$");
p.matcher("hello world").find();    // TRUE  — 'world' ends at string end
p.matcher("world here").find();     // FALSE — 'world' does not end at string end
p.matcher("hello world\n").find();  // TRUE  — $ matches before trailing \n
```

The trailing-newline behavior of `$` surprises many engineers. It exists because terminal output often ends with a newline, and patterns written for user-visible text should still match.

If you need to match at the absolute last character (never before a trailing newline), use `\z` instead.

### 2.3 `^` + `$` Together — Full-String Validation

The combination `^pattern$` with `matcher.matches()` is the canonical validation idiom:

```java
// These two are equivalent:
Pattern.compile("^\\d{5}$").matcher(input).find();
Pattern.compile("\\d{5}").matcher(input).matches();

// Prefer matches() for clarity, or use find() with anchors when you need both
// validation AND knowing WHERE in a multi-string context
```

`matcher.matches()` behaves as if the pattern is always wrapped in `^(?:...)$` — it must match the entire input. However, `matches()` cannot tell you the position of a match; it only returns true/false. When you need position information or are searching within a larger string, use `find()` with explicit anchors.

---

## 3. `Pattern.MULTILINE` — Redefining `^` and `$`

This is the most commonly misunderstood flag in Java regex.

### 3.1 What MULTILINE Does

`Pattern.MULTILINE` makes `^` match at the **start of each line** (after each `\n`) and `$` match at the **end of each line** (before each `\n` or at the end of the string).

```java
String text = "first line\nsecond line\nthird line";
Pattern p = Pattern.compile("^\\w+", Pattern.MULTILINE);
Matcher m = p.matcher(text);
while (m.find()) {
    System.out.println(m.group()); // "first", "second", "third"
}
```

Without `MULTILINE`:
```java
Pattern p = Pattern.compile("^\\w+");  // no flag
// Only matches "first" — ^ only matches position 0
```

### 3.2 How the Engine Sees Lines

Java defines a "line" for MULTILINE purposes as any sequence terminated by:
- `\n` (newline)
- `\r` (carriage return)
- `\r\n` (Windows CRLF)
- `\u0085` (Next Line)
- `\u2028` (Line Separator)
- `\u2029` (Paragraph Separator)

This is broader than many engineers expect. If you're working with Unix-only line endings, `\n` is all you need to know. For cross-platform text, be aware that `\r\n` is treated as a single line terminator.

### 3.3 The Critical Rule: MULTILINE Does NOT Affect `\A`, `\Z`, `\z`

`\A`, `\Z`, and `\z` are **immune to MULTILINE**. They always refer to the absolute boundaries of the entire input string, never line boundaries. This is extremely useful when you want to:

- Use `^` and `$` for line-by-line matching (MULTILINE)
- AND still have a way to anchor to the entire string (use `\A`/`\z`)

```java
// Example: validate that a multiline block starts with a specific header
Pattern p = Pattern.compile("\\A\\[config\\](?m)\\n^\\w+=.*$", Pattern.MULTILINE);
// \A anchors to start of entire string
// (?m) enables MULTILINE for subsequent ^ and $
// The ^ inside the pattern now matches line starts
```

### 3.4 Setting MULTILINE via Inline Flag

You can embed the flag directly in the pattern without passing it to `compile()`:

```java
Pattern.compile("(?m)^\\w+")           // inline (?m) = MULTILINE
Pattern.compile("(?mi)^\\w+")          // MULTILINE + CASE_INSENSITIVE
Pattern.compile("(?s).*")              // (?s) = DOTALL
Pattern.compile("(?ms)^.+$")           // MULTILINE + DOTALL
```

The inline flag applies from the point it appears to the end of the pattern (or enclosing group). This allows fine-grained control within complex patterns.

---

## 4. Absolute Anchors: `\A`, `\Z`, and `\z`

These three anchors exist specifically to provide absolute-position guarantees regardless of flags.

### 4.1 `\A` — Absolute Start

`\A` always matches the very beginning of the input string. It is equivalent to `^` without `MULTILINE`, but it STAYS that way even when `MULTILINE` is active.

```java
// With MULTILINE, ^ matches line starts; \A still matches only string start
Pattern p = Pattern.compile("\\A\\d+", Pattern.MULTILINE);
p.matcher("123\n456").find();       // TRUE — matches "123" at absolute start
p.matcher("abc\n123").find();       // FALSE — "123" is not at absolute start
```

### 4.2 `\Z` — End of Input (with trailing newline tolerance)

`\Z` matches at the end of the string, OR just before a final `\n`. This is identical to `$` in non-MULTILINE mode.

Use `\Z` when:
- You need absolute end-of-string anchoring
- But you want to tolerate a single trailing newline (common in file input)

### 4.3 `\z` — Absolute End (strict)

`\z` (lowercase) matches ONLY at the absolute last position. No trailing newline tolerance.

```java
Pattern.compile("\\d+\\Z").matcher("123\n").find();   // TRUE
Pattern.compile("\\d+\\z").matcher("123\n").find();   // FALSE — \n is after digits
Pattern.compile("\\d+\\z").matcher("123").find();     // TRUE
```

**Best practice**: In security-critical validation, prefer `\A` and `\z` over `^` and `$`. They are unambiguous and not affected by any flags. A validator written with `\A...\z` will behave the same way whether someone accidentally adds `MULTILINE` to the compile call or not.

---

## 5. `\b` and `\B` — Word Boundary Mechanics

### 5.1 The Formal Definition of `\b`

A word boundary `\b` matches at a position where:
- The character to the left is a word character (`\w` = `[A-Za-z0-9_]`) AND the character to the right is a non-word character (or end of string)
- OR the character to the left is a non-word character (or start of string) AND the character to the right is a word character

In other words: `\b` matches at any **transition between `\w` and `\W`** (in either direction).

### 5.2 Visualizing `\b` Positions

```
Input:   "  hello, world! "
         0123456789...

Position 2:  between ' ' (\W) and 'h' (\w) → \b MATCHES
Position 7:  between 'o' (\w) and ',' (\W) → \b MATCHES
Position 9:  between ' ' (\W) and 'w' (\w) → \b MATCHES
Position 14: between 'd' (\w) and '!' (\W) → \b MATCHES
```

So `\b` matches 4 positions in `"  hello, world! "`. The pattern `\bhello\b` would match only at positions 2-7, not elsewhere.

### 5.3 Edge Cases for `\b`

**At string start**: If the string starts with a word character, position 0 is a boundary (before it is conceptually non-word/start).

```java
Pattern.compile("\\bcat\\b").matcher("cat").find();     // TRUE
Pattern.compile("\\bcat\\b").matcher("cats").find();    // FALSE — 's' after 'cat' is \w
Pattern.compile("\\bcat\\b").matcher("the cat.").find(); // TRUE — '.' after 'cat' is \W
```

**Underscore is a word character**: `_` is part of `\w`, so `\b` does NOT create a boundary between a letter and an underscore.

```java
Pattern.compile("\\blog\\b").matcher("log_file").find();  // FALSE — underscore is \w
Pattern.compile("\\blog\\b").matcher("log-file").find();  // TRUE — hyphen is \W
```

**Digits are word characters**: `\b` does not create a boundary between letters and digits.

```java
Pattern.compile("\\bvar\\b").matcher("var1").find();  // FALSE — '1' is \w
Pattern.compile("\\bvar\\b").matcher("var=").find();  // TRUE — '=' is \W
```

### 5.4 `\B` — Non-Word Boundary

`\B` is the complement of `\b`. It matches at every position where `\b` does NOT match — i.e., where both sides are word characters, or both sides are non-word characters.

```java
// Find 'log' when it appears INSIDE a larger word (not standalone)
Pattern.compile("\\Blog\\B").matcher("logger").find();   // TRUE
Pattern.compile("\\Blog\\B").matcher("log").find();      // FALSE
Pattern.compile("\\Blog\\B").matcher("log.").find();     // FALSE — '.' is \W
```

`\B` is less commonly used, but useful for "find this substring only within words" scenarios.

### 5.5 Word Boundaries vs. Lookahead/Lookbehind

`\b` is convenient but limited to the `\w`/`\W` definition. For more control, use lookahead/lookbehind:

```java
// Find "log" not followed by any lowercase letter (broader than \b)
"log(?![a-z])"

// Find "log" not preceded by any letter or digit (strict \W boundary)
"(?<![a-zA-Z0-9])log"
```

Use `\b` for quick word isolation. Use lookahead/lookbehind when your definition of "boundary" doesn't match the `\w`/`\W` split.

---

## 6. How Anchors Interact with `find()` in Loops

This is a subtle but important behavior. When you use `find()` in a loop to find all occurrences, anchors constrain which positions the engine will attempt.

### 6.1 `^` with MULTILINE in `find()` Loops

```java
String text = "foo bar\nbaz qux\nfoo again";
Pattern p = Pattern.compile("(?m)^foo");
Matcher m = p.matcher(text);
while (m.find()) {
    System.out.println("Found at: " + m.start());
    // Prints: 0, 16 — only positions where 'foo' starts a line
}
```

Each call to `find()` searches from the last match's end position, but the `^` anchor filters to only positions that are at line starts. So even if `find()` could see more `foo` occurrences in the middle of lines, `^` prevents them from matching.

### 6.2 `\b` in `find()` Loops

```java
Pattern p = Pattern.compile("\\bword\\b");
Matcher m = p.matcher("word in a word-for-word situation");
int count = 0;
while (m.find()) {
    count++;
    // "word" at pos 0 (boundary: start, then ' ')
    // "word" at pos 10 (boundary: ' ', then '-') -- YES, '-' is non-word
    // "word-for-word": "word" followed by '-' matches, so second "word" matches
    // Actually: "word" at index 0, "word" at index 10, "word" at index 18
    // "word" before "situation" at 18 is preceded by '-' (\W) and followed by space
}
```

The key insight: `\b` is zero-width, so consecutive finds can have adjacent `\b` assertions.

---

## 7. Java API: Using Flags

### 7.1 Compile-time Flags

```java
// Single flag
Pattern p = Pattern.compile("^\\w+", Pattern.MULTILINE);

// Multiple flags combined with bitwise OR
Pattern p = Pattern.compile("^\\w+", Pattern.MULTILINE | Pattern.CASE_INSENSITIVE);
```

### 7.2 Inline Flags in Pattern

```java
// Equivalent to Pattern.MULTILINE
Pattern p = Pattern.compile("(?m)^\\w+");

// Flag table:
// (?i) = CASE_INSENSITIVE
// (?m) = MULTILINE
// (?s) = DOTALL (. matches \n)
// (?x) = COMMENTS (whitespace and # comments in pattern ignored)
// (?u) = UNICODE_CASE
// (?d) = UNIX_LINES (only \n recognized as line terminator)
```

### 7.3 Flags Can Be Turned Off

```java
// Enable CASE_INSENSITIVE for just part of the pattern
"(?i)hello(?-i)WORLD"
// Matches "hElLo" followed by literally "WORLD" (case-sensitive after (?-i))
```

### 7.4 `Pattern.UNIX_LINES` — A Hidden Gem

By default, Java's MULTILINE mode treats `\r`, `\r\n`, and Unicode line separators as line terminators. If you're processing Unix-only files and want ONLY `\n` to count:

```java
Pattern p = Pattern.compile("^\\w+", Pattern.MULTILINE | Pattern.UNIX_LINES);
```

---

## 8. The `find()` vs `matches()` vs `lookingAt()` API

Java's `Matcher` provides three matching methods. Understanding when to use each is essential.

### `matches()`
- Attempts to match the **entire input** against the pattern
- Equivalent to `^pattern$` — implicitly anchors to full string
- Returns boolean
- Does NOT advance position (no state for `find()` loops)

### `find()`
- Attempts to find the **next occurrence** of the pattern in the input
- Starts from the end of the previous match (or from 0 for the first call)
- Does NOT implicitly anchor — matches anywhere in the remaining input
- Returns boolean; use `group()`, `start()`, `end()` to access the match

### `lookingAt()`
- Attempts to match the pattern at the **beginning of the input**
- Like `matches()` but does not require the entire input to match
- Equivalent to using `^` with `find()` but without MULTILINE

```java
Matcher m = Pattern.compile("\\d+").matcher("123abc");
m.matches();     // FALSE — "123abc" is not all digits
m.lookingAt();   // TRUE  — the INPUT STARTS WITH digits (resets position to 0)
m.find();        // TRUE  — finds "123" anywhere
m.group();       // "123"
```

---

## 9. Practical Patterns: Combining Anchors

### 9.1 Parse Lines Starting with a Prefix (MULTILINE)

```java
// Extract everything after "> " at the start of each line
Pattern p = Pattern.compile("(?m)^>> ?(.+)$");
Matcher m = p.matcher(text);
while (m.find()) {
    System.out.println(m.group(1).trim());
}
```

### 9.2 Validate Entire String Format

```java
// UUID validation — must be entire string
Pattern UUID = Pattern.compile(
    "\\A[0-9a-f]{8}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{12}\\z",
    Pattern.CASE_INSENSITIVE
);
boolean valid = UUID.matcher(input).matches();
```

### 9.3 Log Line Parser with Multiple Anchors

```java
// MULTILINE: ^ matches each line start
// Named groups for extraction
Pattern LOG = Pattern.compile(
    "(?m)^\\[(DEBUG|INFO|WARN|ERROR)\\] (\\d{4}-\\d{2}-\\d{2}) (\\d{2}:\\d{2}:\\d{2}) - (.+)$"
);
```

---

## 10. The `\A`, `\Z`, `\z` Decision Tree

```
Q: Do you want to match at the start of the entire input (never a line)?
   → Use \A

Q: Do you want to match at the end of the entire input, tolerating trailing \n?
   → Use \Z  (or $  in non-MULTILINE mode)

Q: Do you want to match at the absolute last character, no tolerance?
   → Use \z

Q: Do you want to match at the start/end of EACH LINE in multi-line input?
   → Use ^ and $ with Pattern.MULTILINE (or inline (?m))

Q: Do you want to isolate a whole word?
   → Use \b on both sides: \bword\b
   → But watch for underscores and digits being word chars!
```

---

## 11. Performance Implications of Anchors

Anchors can dramatically **improve** regex performance by reducing the number of positions the engine attempts.

### 11.1 Anchors Eliminate Positions

Without anchors, `find()` attempts to start a match at every position in the string. With `^`, it only attempts at position 0 (or at line starts with MULTILINE). This can make the difference between O(n) and O(1) attempts.

```java
// These are O(n) for find() — must check every position
Pattern.compile("\\d{5}").matcher(longString).find();

// This is O(1) for find() — only checks position 0
Pattern.compile("^\\d{5}").matcher(longString).find();
```

### 11.2 `\b` is Cheap

`\b` only does a two-character comparison at each position. It is very fast. Using `\b` instead of a lookahead/lookbehind pair is faster for simple word-isolation needs.

### 11.3 `\A` is the Fastest Start Anchor

`\A` tells the engine to attempt the match exactly once, at position 0. Combined with `\z` at the end, the engine either matches the whole string in one attempt or fails immediately. This is the fastest possible anchoring for full-string validation.

---

## 12. Interview Traps and Production Concerns

### Trap 1: "MULTILINE and DOTALL are the same"

No. `DOTALL` makes `.` match newlines. `MULTILINE` changes `^`/`$` behavior. You can use them independently or together. A common mistake: wanting `.` to match newlines AND wanting line-by-line `^`/`$` — that requires BOTH flags.

### Trap 2: "`$` matches the end of the string"

Technically, `$` matches the end of the string **or** just before a trailing `\n`. Use `\z` if you truly mean "absolute last position."

### Trap 3: "`\b` creates a word boundary around any character"

Only around `\w` characters. `\bfoo\b` in `"foo-bar"` — `foo` is bounded by start-of-string and `-`. In `"foo_bar"` — `\b` does NOT match between `foo` and `_` because underscore is `\w`.

### Trap 4: "I can use anchors with `String.matches()`"

`String.matches(regex)` already anchors to the full string. Adding `^` and `$` is redundant but harmless. However, `String.matches()` is slow (re-compiles the pattern). Use a pre-compiled `Pattern` with `matcher.matches()` in production.

### Production Concern: MULTILINE with User Input

If you process user-submitted multiline text with MULTILINE mode, be aware that users can inject newlines to create "line starts." A pattern like `(?m)^admin` would match `"user\nadmin"`. Always sanitize or escape user input before using it in multiline patterns.

### Production Concern: Thread Safety

`Pattern` is thread-safe and should be stored as `static final`. `Matcher` is NOT thread-safe. Never share a `Matcher` instance between threads. Create a new `Matcher` for each thread via `pattern.matcher(input)`.

---

## 13. Summary: The Anchor Mental Model

Think of anchors as **gates** the engine must pass through to continue matching:

- `^` / `\A`: "Gate: am I at the beginning?"
- `$` / `\Z` / `\z`: "Gate: am I at the end?"
- `\b`: "Gate: am I at a word/non-word transition?"
- `\B`: "Gate: am I NOT at a word/non-word transition?"

None of these gates move you forward in the input. They simply allow or deny the current matching path.

The flag `MULTILINE` changes what "beginning" and "end" mean for `^` and `$`. The anchors `\A` and `\z` are absolute and never change.

When debugging anchor problems: always ask "at what position does the engine see my anchor?" Print `matcher.start()` to find out. And always distinguish between "start of line" (MULTILINE `^`) and "start of string" (`\A` or non-MULTILINE `^`).
