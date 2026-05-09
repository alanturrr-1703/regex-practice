# Character Classes — Deep Notes

> This document goes beneath the surface: how character classes are compiled to NFA states,
> the Unicode model, intersection/subtraction operations, the POSIX vs Unicode category split,
> and every production pitfall you'll encounter writing real parsers.

---

## 1. How Character Classes Compile to NFA States

When the regex engine parses `[a-zA-Z0-9_]`, it doesn't build a branching NFA with one branch per character. Instead, it compiles this into a single **transition predicate**: a boolean function that tests whether the current character belongs to the set.

Internally, Java's `Pattern` compiler represents character classes as one of:
- A **bit vector** (for ASCII-only classes) — 128 bits, O(1) lookup.
- A **sorted array of character ranges** (for Unicode classes) — O(log n) binary search.
- A **composition** of predicates for complex classes with `&&`, `||`, `--`.

This matters for performance: a character class `[a-zA-Z0-9_]` performs a single O(1) (or O(log n)) test per character, not 62+ separate NFA state transitions. This is much faster than alternation `(a|b|c|...|z)`.

**Key performance insight:** Prefer `[abc]` over `(a|b|c)` whenever you're matching a set of single characters. The character class is always faster.

---

## 2. The Character Class Grammar in Java

Inside `[...]`, these have special meaning:

| Token | Meaning |
|-------|---------|
| `-` between two chars | Range operator |
| `^` at position 0 | Negation |
| `]` | Closes the class (escape with `\]` to include literal `]`) |
| `\` | Escape — applies to next char |
| `[` nested | Class intersection/union (Java extension) |
| `&&` | Intersection (Java extension, see Section 7) |

Everything else is literal inside `[...]`. This is different from OUTSIDE character classes, where many more characters are metacharacters.

---

## 3. Range Notation and Unicode Code Points

### How Ranges Work

`[a-z]` matches any single character `c` where `(int)c >= (int)'a' && (int)c <= (int)'z'`.

That is: ranges are based on **Unicode scalar values** (code points), not on any linguistic or alphabetical ordering. This has consequences:

```
[A-Z]   → code points 65-90    (A, B, ..., Z)
[a-z]   → code points 97-122   (a, b, ..., z)
[A-z]   → code points 65-122   (includes [\]^_` between Z and a!)
[0-9]   → code points 48-57    (0, 1, ..., 9)
```

The dangerous trap: `[A-z]` is syntactically valid but semantically wrong for "any letter". Always use `[A-Za-z]`.

### Code Point Verification

When debugging a range, print the code points:

```
System.out.printf("'A' = %d%n", (int)'A');  // 65
System.out.printf("'Z' = %d%n", (int)'Z');  // 90
System.out.printf("'[' = %d%n", (int)'[');  // 91
System.out.printf("'a' = %d%n", (int)'a');  // 97
```

Range `[A-z]` includes code points 91-96: `[`, `\`, `]`, `^`, `_`, `` ` ``.

### Ranges Must Be Ascending

`[z-a]` throws `PatternSyntaxException: Illegal character range near index 3`. The lower bound must have a code point less than or equal to the upper bound.

---

## 4. Negated Classes — Deep Semantics

`[^abc]` is NOT "a character that is a letter other than a, b, c." It is "ANY character — including digits, spaces, punctuation, Unicode letters, null bytes — EXCEPT a, b, and c."

This surprises developers who write `[^abc]` thinking they're still selecting from the "alphabet." They're selecting from the **entire Unicode space** minus the excluded characters.

Practical example — stripping HTML tags:

```
// This attempts to match "not a >". But [^>]+ also matches newlines!
<[^>]+>

// If you don't want to cross newlines (most common case):
<[^>\n]+>
```

### Double Negation

`[^\D]` means "NOT a non-digit" = "a digit" = same as `\d`. This is legal but confusing. Don't write double negations.

### Negation in Unicode Context

`[^\w]` in ASCII mode means "any character that is NOT [a-zA-Z0-9_]". It matches Unicode letters, emoji, Chinese characters, etc. This is often correct for "delimiter" detection in ASCII text but wrong for internationalized text where you want to exclude Unicode word characters too.

---

## 5. Enumerated Classes vs. Shorthand Classes

### Enumerated: `[aeiou]`

Explicitly lists acceptable characters. Perfectly clear, no ambiguity. The preferred form when the set is small and non-contiguous.

```
[aeiouAEIOU]   — all vowels, case-sensitive
```

Or with the case-insensitive flag:

```
Pattern.compile("[aeiou]", Pattern.CASE_INSENSITIVE)
```

With `CASE_INSENSITIVE`, `[aeiou]` also matches `A`, `E`, `I`, `O`, `U`. But with `UNICODE_CASE` flag too, it could also match accented vowels in some locales — be explicit about your requirements.

### Shorthand: `\d`, `\w`, `\s`

These are syntactic sugar for commonly needed classes. But they're not just abbreviations — their behavior changes based on flags:

**In default (ASCII) mode:**
- `\d` = `[0-9]`
- `\w` = `[a-zA-Z0-9_]`
- `\s` = `[ \t\n\r\f\x0B]`

**With `Pattern.UNICODE_CHARACTER_CLASS`:**
- `\d` = `\p{Nd}` (Unicode decimal digit: includes Arabic-Indic, Devanagari, etc.)
- `\w` = `[\p{Alpha}\p{gc=Mn}\p{gc=Me}\p{gc=Mc}\p{Digit}\p{gc=Pc}\p{IsJoin_Control}]`
- `\s` = `\p{IsWhite_Space}`

The `UNICODE_CHARACTER_CLASS` expansion of `\w` is intentionally broad. It includes **combining marks** (Mn, Me, Mc) and **connector punctuation** (Pc, which includes underscore and related chars in other scripts).

---

## 6. POSIX Character Classes in Java

Java supports POSIX-style class names inside `\p{...}` and `\P{...}` (negated):

| Class | POSIX Name | ASCII Equivalent | Notes |
|-------|-----------|-----------------|-------|
| `\p{Alpha}` | Alphabetic | `[a-zA-Z]` | ASCII letters only |
| `\p{Digit}` | Digit | `[0-9]` | ASCII digits only |
| `\p{Alnum}` | Alphanumeric | `[a-zA-Z0-9]` | ASCII letters and digits |
| `\p{Lower}` | Lowercase | `[a-z]` | ASCII lowercase |
| `\p{Upper}` | Uppercase | `[A-Z]` | ASCII uppercase |
| `\p{Space}` | Whitespace | `[ \t\n\r\f\x0B]` | ASCII whitespace |
| `\p{Punct}` | Punctuation | See docs | ASCII punctuation chars |
| `\p{Graph}` | Visible chars | ASCII 33-126 | Non-space, non-control |
| `\p{Print}` | Printable | ASCII 32-126 | Includes space |
| `\p{Cntrl}` | Control | ASCII 0-31, 127 | Control characters |

**Critical:** All POSIX classes in Java's default mode are ASCII-only. `\p{Alpha}` does NOT match `ö`, `ñ`, or `日`. For Unicode-aware matching, use Unicode categories instead.

---

## 7. Unicode Category Classes

Java supports the Unicode General Category system via `\p{Category}`:

### Letter Categories

| Class | Description | Examples |
|-------|-------------|---------|
| `\p{L}` | Any Unicode letter | a, ä, 日, α, Ñ |
| `\p{Lu}` | Uppercase letter | A, Ö, Α |
| `\p{Ll}` | Lowercase letter | a, ö, α |
| `\p{Lt}` | Titlecase letter | DŽ, Lj |
| `\p{Lm}` | Modifier letter | ʰ, ʲ |
| `\p{Lo}` | Other letter | 日, 한, ع |

### Number Categories

| Class | Description | Examples |
|-------|-------------|---------|
| `\p{N}` | Any Unicode number | 0, ١, ৩, Ⅷ |
| `\p{Nd}` | Decimal digit | 0-9, ٠-٩, ०-९ |
| `\p{Nl}` | Letter number | Ⅰ, Ⅱ, Ⅲ (Roman numerals as chars) |
| `\p{No}` | Other number | ½, ¼, ² |

### Punctuation and Symbol Categories

| Class | Description |
|-------|-------------|
| `\p{P}` | Any punctuation |
| `\p{S}` | Any symbol |
| `\p{Z}` | Any separator (space, line sep, para sep) |
| `\p{C}` | Any control/format/unassigned character |

### Unicode Script Classes

```
\p{IsLatin}    — Latin script characters
\p{IsGreek}    — Greek script
\p{IsHan}      — CJK (Chinese/Japanese/Korean) characters
\p{IsCyrillic} — Cyrillic script
```

These are available in Java via the `\p{Is...}` or `\p{script=...}` syntax (Java 7+).

---

## 8. Java's Unique Feature: Character Class Intersection and Subtraction

Java's regex engine supports **character class operations** not found in most other languages:

### Intersection with `&&`

`[class1&&class2]` — matches characters that are in BOTH classes:

```
[a-z&&[^aeiou]]   — lowercase consonants (letters a-z, minus vowels)
[\p{L}&&[a-z]]    — Unicode letters that are also ASCII lowercase
[0-9&&[^4-6]]     — digits, excluding 4, 5, 6
```

### Subtraction (special case of intersection with negation)

```
[a-z&&[^aeiou]]   — same as above: letters minus vowels
[\p{L}&&[^\p{Lu}]] — Unicode letters that are NOT uppercase (= lowercase + titlecase + ...)
```

### Union (simply concatenate inside `[...]`)

```
[a-z0-9]           — letters or digits (union)
[a-zA-Z\u0080-\u00FF]  — ASCII letters plus Latin Extended-A
```

### Nesting

Classes can be nested:

```
[[a-m][n-z]]  — same as [a-z] (union of two ranges)
[[a-z]&&[aeiou]]  — just vowels
```

This feature is Java-specific. Most other regex flavors don't support `&&` inside character classes.

---

## 9. Backtracking with Character Classes

Character classes themselves don't cause backtracking — they are atomic: they either match one character or they don't. Backtracking occurs at the **quantifier level**.

Consider `[a-z]+` applied to "hello world":

```
[a-z]+ is greedy. It matches "hello" (5 chars), stops at space.
Then find() continues from position 5.
Space doesn't match [a-z]. Engine tries position 6.
[a-z]+ matches "world" (5 chars).
Result: ["hello", "world"]
```

No backtracking needed here. The space character cleanly separates matches.

Now consider `[a-z]+` applied to "hello123world":

```
Position 0: [a-z]+ greedily matches "hello" (5 chars). Stops at '1'.
find() finds "hello".
Position 5: '1' doesn't match [a-z]. Tries 6,7,8 same. Position 9: 'w'.
[a-z]+ matches "world".
Result: ["hello", "world"]
```

Still no backtracking at the class level. The key: character classes are position-by-position matching, and quantifiers handle repetition.

When DOES backtracking happen with character classes? When the pattern has multiple parts:

```
Pattern: [a-z]+@[a-z]+
Input: "user@domain"
[a-z]+ greedily matches "user@domain". Then needs @. At end. Backtrack.
Gives back "domain". Still ends with "domain". Needs "@" at "n". No.
Continues backtracking until [a-z]+ has matched "user", then @ matches "@",
then [a-z]+ matches "domain". Match found.
```

---

## 10. Case-Insensitive Matching — The Full Story

`Pattern.CASE_INSENSITIVE` (or inline flag `(?i)`) makes the engine match letters regardless of case.

But "case" is not simple in Unicode:

### ASCII Case Folding
With `CASE_INSENSITIVE` alone, Java uses a simple ASCII folding:
- `[a-z]` also matches `[A-Z]` and vice versa.
- Does NOT fold Unicode-specific cases (ß → ss, etc.)

### Unicode Case Folding
With `CASE_INSENSITIVE | UNICODE_CASE`:
- `[a-z]` matches accented letters like `ä`, `ö`, `ü` and their uppercase counterparts `Ä`, `Ö`, `Ü` (when the case folding results in an ASCII lowercase letter... actually this is complex)
- Better to use `\p{L}` for "any Unicode letter" when you need full Unicode coverage

### Practical Rule
For international applications, use `Pattern.UNICODE_CASE | Pattern.CASE_INSENSITIVE` together, or switch to `\p{L}` for letter matching. Never assume `CASE_INSENSITIVE` alone handles all Unicode cases correctly.

---

## 11. Hex Digit Class — A Canonical Case Study

The hex digit class `[0-9a-fA-F]` is a perfect case study because:
1. It uses multiple ranges
2. It's case-sensitive within the class (both `a-f` and `A-F` needed)
3. It has an exact count with `{6}` for 6-digit hex colors
4. Alternatively, `[0-9a-f]` with `CASE_INSENSITIVE` achieves the same

```
// Verbose but explicit:
Pattern p = Pattern.compile("[0-9a-fA-F]{6}");

// Concise with flag:
Pattern p = Pattern.compile("[0-9a-f]{6}", Pattern.CASE_INSENSITIVE);
```

Both are correct. The first is more portable (works in systems where `CASE_INSENSITIVE` has Unicode implications). The second is more readable.

**Why `{6}` and not `{6,}`?** Exact repetition `{n}` means exactly n. `{n,}` means n or more. For hex colors, we want exactly 6 hex digits — not 7, not 8. A 6-digit hex color is precisely 24 bits of RGB.

But there's a subtlety: `#[0-9a-fA-F]{6}` used with `find()` on `"#AABBCC00"` will match `#AABBCC` (first 6 hex digits), leaving `00` unmatched. If you want to ensure the hex color ends at a word/token boundary, use a negative lookahead: `#[0-9a-fA-F]{6}(?![0-9a-fA-F])`.

---

## 12. Unicode Identifiers — A Hard Problem Solved Right

Java identifiers are defined by `Character.isJavaIdentifierStart(c)` and `Character.isJavaIdentifierPart(c)`. These methods use Unicode character properties, not just ASCII.

A Java identifier starts with: any Unicode letter, `_`, or `$` (but `$` is conventionally reserved for generated code).

For extracting generic Unicode identifiers in regex:

```
Pattern p = Pattern.compile(
    "(?<![_\\p{L}\\p{N}])[_\\p{L}][_\\p{L}\\p{N}]*",
    Pattern.UNICODE_CHARACTER_CLASS
);
```

Breaking this down:
- `(?<![_\\p{L}\\p{N}])` — negative lookbehind: not preceded by an identifier character. This ensures we don't start in the middle of an identifier.
- `[_\\p{L}]` — start: underscore or any Unicode letter.
- `[_\\p{L}\\p{N}]*` — continue: underscore, Unicode letter, or Unicode number.

Without the lookbehind, `find()` on `"123abc"` would find `"abc"` (starting at position 3, preceded by a digit). The lookbehind causes the match to fail when the preceding character is also part of an identifier.

### Why `\p{N}` and not `\d`?

`\d` in default mode = `[0-9]`. `\p{N}` = any Unicode numeric character, including non-ASCII digits. For identifier continuation, using `\p{N}` allows identifiers like `café1` where `1` is ASCII, but also potentially identifiers with other numeric characters. In strict Java-identifier mode, use `\p{Nd}` (decimal digits only).

---

## 13. Edge Cases in Character Classes

### Empty Class

`[]` is a `PatternSyntaxException` in Java. An empty character class is not allowed.

### Class Containing Only `]`

`[\]]` matches a literal `]`. The `\]` inside `[...]` escapes the bracket.

Alternatively: `[]]` — Java is lenient here (some implementations allow this, where `]` immediately after `[` is treated as literal). In Java, this might throw or might work depending on position. Safest: always escape: `[\]]`.

### Class Containing Only `-`

`[-]` matches a literal hyphen. `[a-]` matches `a` or a literal hyphen. `[-a]` matches a literal hyphen or `a`. Most positions work, but best practice: put hyphen at start or end.

### Class with `^` Not at Position 0

`[a^b]` matches `a`, `^`, or `b`. The caret is only special at position 0.

```
[^^]   — matches any char that is NOT a caret
[a^]   — matches 'a' or literal '^'
```

### Very Large Character Classes

A class like `[\u0000-\uFFFF]` matches any BMP character — effectively everything. This compiles to a "match any" NFA state in the engine's internal representation. It's equivalent to `.` with the `DOTALL` flag.

---

## 14. Performance Considerations for Character Classes

### Bit Vector Optimization (ASCII)

Java's regex engine internally uses a bit vector for character classes that cover only the ASCII range (code points 0-127). A 128-bit vector (16 bytes) fits in two longs. Membership testing is `O(1)`: shift the character's code point and check the bit.

Classes that stay in ASCII range:
- `[a-z]`, `[A-Z]`, `[0-9]`, `[a-zA-Z0-9_]`
- `[aeiou]`, `[!@#$%]`

### Binary Search (Unicode)

Once a character class includes characters above code point 127, the engine falls back to a sorted range array and binary search. For classes with few ranges, this is still very fast — O(log k) where k is the number of ranges.

### Case-Insensitive Unicode Classes

`Pattern.CASE_INSENSITIVE | Pattern.UNICODE_CASE` applied to `[a-z]` can expand the class to include many more Unicode characters due to case folding tables. This is notably slower because the engine must consult Unicode case fold mappings.

### Practical Advice

For ASCII-only data (log lines, HTTP headers, JSON keys), use ASCII character classes — they hit the bit vector fast path.

For internationalized data, use Unicode categories (`\p{L}`, `\p{N}`) with `UNICODE_CHARACTER_CLASS` — the engine is designed for this and handles it efficiently.

---

## 15. Malformed Character Classes — Errors and Recovery

`PatternSyntaxException` is thrown for:

| Invalid Pattern | Reason |
|----------------|--------|
| `[z-a]` | Range end before range start |
| `[` | Unclosed character class |
| `[a-\d]` | Range from literal char to class (can't mix) |
| `[\p{Invalid}]` | Unknown Unicode category |

Note: `[a-\d]` is an error because `\d` represents a class, not a single character. You can't create a range to/from a shorthand class. Use `[\da-f]` (union) instead of `[a-\d]` (invalid range).

Always wrap `Pattern.compile()` in a try-catch when compiling user-supplied patterns.

---

## 16. Interview Traps Specific to Character Classes

### Trap 1: "What does `[A-z]` match?"
Most say "all letters". Wrong — it matches code points 65-122, including `[`, `\`, `]`, `^`, `_`, and backtick. The correct "all ASCII letters" is `[A-Za-z]`.

### Trap 2: "How do you include a literal hyphen in a character class?"
Put it first or last: `[-a-z]` or `[a-z-]`. Or escape it: `[a\-z]`. If placed between two characters as in `[a-z]`, it's a range operator.

### Trap 3: "What's the difference between `\d` and `\p{Digit}`?"
In Java's default mode, both match `[0-9]`. But with `UNICODE_CHARACTER_CLASS`, `\d` expands to `\p{Nd}` (all Unicode decimal digits). `\p{Digit}` is always POSIX (ASCII-only in Java). So they differ under Unicode flags.

### Trap 4: "Can you negate a shorthand class inside `[...]`?"
`[^\d]` means "not a digit" = same as `\D`. Yes, you can negate shorthands inside negated classes. And `[\D\d]` means "digit or non-digit" = any character. Useful sometimes.

### Trap 5: "What does `[a-zA-Z&&[^aeiouAEIOU]]` match?"
ASCII consonants (letters that are not vowels). This uses Java's character class intersection. Most developers don't know about `&&` inside `[...]` — this is a Java-specific feature.

### Trap 6: "What does `\p{L}` NOT include?"
Digits, punctuation, symbols, spaces. It's strictly the Letter category. `\p{L}` does not include `1`, `!`, `@`, or space. If you want "word-like" chars including digits, use `\p{L}\p{N}` inside a class.

---

## 17. Production Engineering Concerns

### Validate User-Supplied Character Classes

If your application allows users to specify character classes (e.g., "only accept characters in this set"), validate the class expression before compiling:

```
public void validateCharClass(String charClass) {
    try {
        // Wrap in a minimal pattern to test compilation
        Pattern.compile("[" + charClass + "]");
    } catch (PatternSyntaxException e) {
        throw new IllegalArgumentException("Invalid character class: " + charClass, e);
    }
}
```

Better yet, whitelist what users can specify rather than compiling arbitrary expressions.

### Unicode Normalization Before Matching

Unicode has multiple representations of the same visual character. For example, `é` can be:
- U+00E9 (precomposed: single code point "LATIN SMALL LETTER E WITH ACUTE")
- U+0065 U+0301 (decomposed: `e` + combining acute accent)

`\p{L}` matches both representations, but `[é]` (enumerating the specific code point) only matches the precomposed form. Normalize input with `java.text.Normalizer` before matching when dealing with user-entered text:

```
String normalized = Normalizer.normalize(input, Normalizer.Form.NFC);
matcher = pattern.matcher(normalized);
```

### International Phone Number Digits

If you want to accept digits from any numeral system (for international phone fields), use `\p{Nd}` (Unicode decimal digits). If you want only ASCII digits for parsing into `int`, use `[0-9]`.

### Logging Unmatched Characters

In production parsers, log characters that don't match your expected character class. This catches encoding issues early — if your parser expects ASCII but receives UTF-8 encoded as Latin-1, you'll see unexpected bytes in the unmatched character log.

---

## Summary

| Topic | Key Takeaway |
|-------|-------------|
| Compilation | Character classes compile to bit vectors (ASCII) or range arrays (Unicode) — much faster than alternation |
| Range notation | Based on Unicode code points; `[A-z]` is a trap |
| Negation | `[^abc]` = ANY character except a, b, c (entire Unicode space) |
| Shorthand `\d`/`\w`/`\s` | ASCII by default; expand with `UNICODE_CHARACTER_CLASS` |
| POSIX classes | `\p{Alpha}` etc. — ASCII-only in Java |
| Unicode categories | `\p{L}`, `\p{N}` — all scripts; use for international data |
| Intersection | `[class1&&class2]` — Java-specific feature |
| Hyphen | Literal only at start/end or when escaped; range operator otherwise |
| Backtracking | Character classes are atomic — no backtracking at class level, only at quantifier level |
| Case-insensitive | `CASE_INSENSITIVE` alone is ASCII; add `UNICODE_CASE` for full Unicode case folding |
| Performance | ASCII classes → bit vector O(1); Unicode → binary search O(log k) |
| Production | Normalize Unicode input; validate user-supplied classes; use `\p{Nd}` vs `[0-9]` appropriately |
