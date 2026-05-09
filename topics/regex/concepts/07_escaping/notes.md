# Escaping — Deep Notes

---

## 1. The Two-Layer Escaping Problem

When you write a regex in Java, your pattern string travels through **two independent interpreters** before it does any matching work. Understanding this pipeline is the foundation of everything in this concept.

```
Your Java source code
       |
       v
[Java String Literal Parser]   -- Layer 1
       |
       v
The regex pattern string (in memory)
       |
       v
[Java Regex Engine / NFA Compiler]  -- Layer 2
       |
       v
The compiled NFA, ready to match
```

Both layers independently recognize escape sequences, and both layers consume backslashes.

---

## 2. Layer 1: Java String Literal Escaping

The Java compiler processes your string literal before your code ever runs. These are the Java string escape sequences:

| Java Source | Character in Memory | Meaning |
|---|---|---|
| `\\` | `\` | One literal backslash |
| `\"` | `"` | Double quote |
| `\'` | `'` | Single quote |
| `\n` | newline (0x0A) | Line feed |
| `\t` | tab (0x09) | Horizontal tab |
| `\r` | carriage return (0x0D) | CR |
| `\0` | null (0x00) | Null character |
| `\uXXXX` | Unicode char | Unicode escape |

Any other `\X` sequence that is not in this list will cause a **Java compile error**. So `"\d"` does not mean the regex digit class — it is a compile error because `\d` is not a valid Java string escape.

This is the first thing to check when you get `error: illegal escape character`.

---

## 3. Layer 2: Regex Engine Escaping

The regex engine has its own escape system that operates on the string it receives (after Java's string parser has already run). Regex escape sequences the Java engine recognizes:

| Regex Engine Receives | Matches |
|---|---|
| `\.` | literal dot |
| `\*` | literal asterisk |
| `\+` | literal plus |
| `\?` | literal question mark |
| `\^` | literal caret |
| `\$` | literal dollar sign |
| `\{` | literal open brace |
| `\}` | literal close brace |
| `\[` | literal open bracket |
| `\]` | literal close bracket |
| `\|` | literal pipe |
| `\(` | literal open paren |
| `\)` | literal close paren |
| `\\` | literal backslash |
| `\d` | digit class `[0-9]` |
| `\D` | non-digit |
| `\w` | word character `[a-zA-Z0-9_]` |
| `\W` | non-word character |
| `\s` | whitespace |
| `\S` | non-whitespace |
| `\b` | word boundary (zero-width) |
| `\B` | non-word-boundary |
| `\n` | newline character |
| `\t` | tab character |

---

## 4. The Composition Table — The Most Important Reference

This is the table you must internalize. It shows what you type in Java source, what the regex engine receives, and what it matches:

| You want to match | Regex engine needs | Java string literal |
|---|---|---|
| Any character | `.` | `"."` |
| A literal dot | `\.` | `"\\."` |
| A digit | `\d` | `"\\d"` |
| A literal backslash | `\\` | `"\\\\"` |
| A literal dollar sign | `\$` | `"\\$"` |
| A literal open paren | `\(` | `"\\("` |
| A digit, then dot, then digit | `\d\.\d` | `"\\d\\.\\d"` |
| The word "end" at end of string | `end$` | `"end$"` ($ needs no Java escape) |
| A literal dollar at string end | `\$$` | `"\\$$"` |
| Two literal backslashes | `\\\\` | `"\\\\\\\\"` (8 chars in source) |

The backslash-backslash case is the most extreme: to match **two** literal backslashes, you write **eight** backslash characters in your Java source code. This is where developers usually stop and reach for `Pattern.quote()`.

---

## 5. Which Characters Are Metacharacters?

Outside a character class `[...]`, these 14 characters have special regex meaning and must be escaped to match literally:

```
. * + ? ^ $ { } [ ] | ( ) \
```

Inside a character class `[...]`, the rules change:
- Most metacharacters **lose** their special meaning: `.` matches a literal dot, `(` matches a literal paren.
- These four characters **retain** special meaning inside `[...]`:
  - `\` — still the escape character
  - `^` — negates the class only when it is the **first** character
  - `-` — creates a range only when it is **between** two characters
  - `]` — closes the character class

So inside `[...]`:
- `[.]` matches a literal dot (`.` loses its "any char" meaning)
- `[^abc]` matches anything NOT a, b, or c
- `[a-z]` matches a through z (range)
- `[-az]` or `[az-]` matches literal hyphen, a, or z (no range — hyphen at start or end)
- `[a\-z]` also matches literal hyphen — escaped

---

## 6. `Pattern.quote(String s)` — Your Safety Net

When you have a string that should be matched **literally** (every character treated as a plain character, nothing special), use `Pattern.quote()`:

```java
String userSearchTerm = "2+2=4 (basic math)";
// WRONG — parentheses open a group, + is a quantifier, etc.
// Pattern.compile(userSearchTerm); // throws PatternSyntaxException or matches wrong things

// RIGHT
String safePattern = Pattern.quote(userSearchTerm);
// safePattern is now: \Q2+2=4 (basic math)\E
Pattern p = Pattern.compile(safePattern);
```

`Pattern.quote()` wraps the string in `\Q` and `\E`. Between `\Q` and `\E`, the regex engine treats every character literally. Even backslashes and other metacharacters have no special meaning.

**The one edge case**: if the input string itself contains `\E`, `Pattern.quote()` handles it by splitting around it: `\Q...\E\\E\Q...\E`.

**Rule**: any time you build a regex pattern from user-supplied or externally-loaded data, use `Pattern.quote()` on the literal portions. This is a **security requirement**, not just a convenience.

---

## 7. `Matcher.quoteReplacement(String s)` — Replacement Escaping

The replacement string in `matcher.replaceAll(replacement)` has its OWN special characters: `$` (group reference) and `\` (escape in replacement). This is a **third** escaping layer that many developers miss.

```java
String input = "price: 100";
Matcher m = Pattern.compile("\\d+").matcher(input);

// WRONG — "$10" means "capture group 10 reference"
// m.replaceAll("$10"); // probably throws IndexOutOfBoundsException

// RIGHT — for a literal dollar sign
m.replaceAll(Matcher.quoteReplacement("$10"));
// result: "price: $10"
```

So the full pipeline for a replacement operation is:
1. Java string literal escaping (e.g., `"\\\\"` → `\\`)
2. Replacement string interpretation (`\\` → literal `\`, `$1` → group 1 value)
3. Use `Matcher.quoteReplacement()` when your replacement contains literal `$` or `\`

---

## 8. Java Text Blocks (Java 15+) — Do They Help?

Java 15 introduced text blocks (multi-line strings delimited by `"""`). You might hope they eliminate Layer 1 escaping for regex. They do NOT:

```java
// Text block — still needs double backslash for regex
String pattern = """
        \\d{4}-\\d{2}-\\d{2}
        """.strip();
// pattern = \d{4}-\d{2}-\d{2}  ✓ correct
```

Text blocks do allow literal newlines, and they suppress the common `\n` string escaping, but **backslashes for regex still need to be doubled**. Text blocks help readability for very long patterns with many `|` alternatives, but they do not solve the double-backslash problem.

---

## 9. Engine Behavior: What Happens With an Unknown Escape?

If the regex engine receives `\q` (a backslash followed by a character that is not a recognized escape), the behavior depends on the implementation:
- Java's regex engine: throws `PatternSyntaxException` for unknown escape sequences
- Exception message: `"Illegal/unsupported escape sequence near index N"`

This is different from Java string literal parsing, which also throws a compile error for unknown escapes like `"\q"`. Both layers enforce valid escapes.

---

## 10. The `\Q...\E` Literal Block

You can embed a literal block directly inside a larger regex using `\Q...\E`:

```java
// Match the literal string "1+1" followed by "=2"
Pattern p = Pattern.compile("\\Q1+1\\E=2");
// Matches: "1+1=2" 
// Does NOT match: "1 1=2" (+ is literal, not a quantifier)
```

This is useful when you have a literal prefix or suffix combined with actual regex:

```java
// Match the literal domain "api.example.com" followed by any path
Pattern p = Pattern.compile("\\Qapi.example.com\\E/[\\w/]+");
```

---

## 11. Escaping Inside Character Classes — Detailed Rules

The `-` (hyphen) inside `[...]` is the subtlest case:

```
[a-z]   → range: a through z (26 chars)
[az]    → just a and z
[a-]    → a and literal hyphen (hyphen at end = literal)
[-z]    → literal hyphen and z (hyphen at start = literal)
[a\-z]  → a, literal hyphen, z (explicitly escaped)
```

The `]` inside a character class:
```
[]]     → matches literal ]
[^]]    → matches anything that is NOT ]
```

The `^` inside a character class:
```
[^abc]  → NOT a, b, or c (negation — ^ must be at start)
[a^bc]  → matches a, ^, b, or c (^ not at start = literal)
```

---

## 12. Debugging Workflow for Escaping Bugs

**Step 1: Identify which layer is failing**

- `javac` error `illegal escape character` → Layer 1 (Java string). Add a backslash.
- `PatternSyntaxException` at runtime → Layer 2 (regex engine). Check your backslash counts.
- Pattern compiles, wrong matches → metacharacter not escaped. Check for unescaped `.`, `*`, etc.

**Step 2: Print the pattern string**

Always print the pattern string (the Java String value, not the source literal) before compiling:
```java
String pat = "\\d{3}\\.\\d{4}";
System.out.println(pat);  // should print: \d{3}\.\d{4}
```
Reading this printed output makes the escaping problem obvious.

**Step 3: Count backslashes systematically**

- In Java source: `\\` = one backslash in memory
- In memory: `\` = regex escape prefix
- So: `\\\\` in source = `\\` in memory = one literal backslash matched

**Step 4: Test with `Pattern.quote()` first**

If you're unsure about escaping, use `Pattern.quote()` on your literal parts first to get the match working. Then incrementally convert to a hand-written pattern.

---

## 13. Interview Traps

**"What does the Java string `"."` match as a regex?"**
Answer: ANY character. The dot is a metacharacter. To match a literal dot, you need `"\\."`.

**"What is wrong with `input.matches("3.14")`?"**
Answer: `.` matches any character, so "3X14" would also match. Use `"3\\.14"` for a literal dot.

**"What does `Pattern.compile("\\\\")` match?"**
Answer: Java string `"\\\\"` → memory string `\\` → regex engine interprets `\\` as one literal backslash. So it matches a string containing one `\` character.

**"When would you use `Pattern.quote()`?"**
Answer: Any time the pattern (or part of the pattern) comes from outside the code — user input, config files, database, etc. Never use raw external strings as regex without quoting.

**"Why can't you use `String.replaceAll()` with a literal `$` in the replacement?"**
Answer: The replacement string interprets `$N` as a back-reference to capture group N. Use `Matcher.quoteReplacement("$5.00")` to get a literal `$5.00`.

---

## 14. Production Engineering Concerns

**Concern 1: User-supplied patterns without sanitization**
```java
// DANGEROUS
String userPattern = request.getParameter("search");
Pattern p = Pattern.compile(userPattern); // could be "[invalid" → crash
                                           // could be ".*" → matches everything
```
Fix:
```java
// Option A: Quote it entirely (user searches for literals only)
Pattern p = Pattern.compile(Pattern.quote(userPattern));

// Option B: Validate the pattern first
try {
    Pattern p = Pattern.compile(userPattern);
} catch (PatternSyntaxException e) {
    // Return error to user
}
```

**Concern 2: Replacement strings with dynamic content**
```java
String replacement = getReplacementFromConfig(); // might contain "$1" or "\"
// DANGEROUS:
result = matcher.replaceAll(replacement); // $1 is a group reference!
// SAFE:
result = matcher.replaceAll(Matcher.quoteReplacement(replacement));
```

**Concern 3: Build patterns programmatically**
When building patterns from components, escape each literal piece:
```java
String domain = "api.example.com"; // literal
String pattern = Pattern.quote(domain) + "/[\\w/]+"; // regex part hand-written
Pattern p = Pattern.compile(pattern);
```

---

## 15. Metacharacter Reference Card

```
Outside [...]:    . * + ? ^ $ { } [ ] | ( ) \  ← must escape for literal
Inside [...]:     \ ^ (at start) - (in middle) ]  ← must escape for literal
In replacement:   $ \  ← must escape or use Matcher.quoteReplacement()
```

Quick Java string rule: to write one regex backslash, type two Java backslashes.
