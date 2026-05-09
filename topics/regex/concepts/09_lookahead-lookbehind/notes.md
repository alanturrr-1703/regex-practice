# Deep Notes: Lookahead & Lookbehind Assertions

> Audience: engineers who understand basic regex and want to reason about how the engine
> actually executes these constructs, where they help, and where they hurt.

---

## 1. The Foundational Idea: Zero-Width Assertions

Every ordinary regex token — a literal character, `\d`, `[a-z]`, `.` — **consumes** input.
After a successful match, the engine's cursor (current position in the string) advances past
the matched characters.

A **zero-width assertion** is different: it tests a condition at the current position but
**does not advance the cursor**. Think of it as a side-channel check: "Before I commit to
consuming the next character, is a certain condition true at this exact spot?"

The canonical zero-width assertions you already know:

| Token | Name           | What it tests                                |
|-------|----------------|----------------------------------------------|
| `^`   | Start anchor   | Current position is start of input (or line) |
| `$`   | End anchor     | Current position is end of input (or line)   |
| `\b`  | Word boundary  | Position is between `\w` and `\W`            |

Lookaheads and lookbehinds are **user-defined** zero-width assertions — you specify the
condition yourself.

---

## 2. Positive Lookahead `(?=X)`

### Syntax

```/dev/null/syntax.txt#L1-1
(?=X)
```

### Semantics

> "Succeed if pattern X matches starting at the current position. Do not consume anything."

The engine records the current cursor position, attempts to match X, then **resets the cursor
to the saved position** regardless of whether X matched. If X matches → the assertion passes
and the outer match continues. If X fails → the assertion fails (triggers backtracking in the
outer expression).

### Worked Example

Pattern: `\w+(?=\s*=)`  
Input: `"x = 5; y = 10;"`

- Engine tries `\w+` at position 0 → matches `x`
- Lookahead `(?=\s*=)` is tested: can `\s*=` match at position 1? Yes (` =`). Pass.
- Cursor is still at position 1 (after `x`). `\w+` reports `"x"`.
- Next call: engine advances past the space, finds `=`, then `5`, etc.

The equals sign is **not** included in the match. That's the entire point.

### Common Use: Password Validation

```/dev/null/password.regex#L1-1
^(?=.*[A-Z])(?=.*[a-z])(?=.*\d)(?=.*[!@#$%^&*]).{8,}$
```

Decomposed:

```/dev/null/decomposed.txt#L1-9
^                    anchor at start
(?=.*[A-Z])          lookahead: somewhere ahead is an uppercase letter
(?=.*[a-z])          lookahead: somewhere ahead is a lowercase letter
(?=.*\d)             lookahead: somewhere ahead is a digit
(?=.*[!@#$%^&*])     lookahead: somewhere ahead is a special char
.{8,}                actually consume: at least 8 of any character
$                    anchor at end
```

Each lookahead rewinds to `^` before testing, so each one independently scans the entire
string. The `.*` is the "anywhere in the string" wildcard. The `.{8,}` at the end is the
only part that actually consumes — it enforces minimum length.

**Key insight**: You can stack lookaheads because they all test from the same position and
none of them advance the cursor. This is how you express "AND" constraints.

---

## 3. Negative Lookahead `(?!X)`

### Syntax

```/dev/null/syntax.txt#L1-1
(?!X)
```

### Semantics

> "Succeed if pattern X does NOT match at the current position."

Mechanically identical to `(?=X)`, but the pass/fail logic is inverted: the assertion
succeeds when the inner pattern **fails** to match.

### Worked Example

Pattern: `file(?!name|path)`  
Input: `"read file from filepath"`

Walk-through:
1. Position 5: `file` matches. Test `(?!name|path)` — can `name` or `path` match at position 9? No (next chars are ` f`). Assertion passes → match `"file"` at pos 5.
2. Position 18: `file` matches (inside `filepath`). Test `(?!name|path)` — can `path` match at position 22? Yes. Assertion fails → no match here.

Result: one match — the standalone `"file"`.

### Common Use: Exclude Keywords

```/dev/null/example.txt#L1-1
\bint(?!erface|ernal)\b   -- match "int" but not "interface" or "internal"
```

---

## 4. Positive Lookbehind `(?<=X)`

### Syntax

```/dev/null/syntax.txt#L1-1
(?<=X)
```

### Semantics

> "Succeed if pattern X matches immediately before the current position."

The engine temporarily scans **backwards** from the current position to see if X could have
produced the characters immediately preceding. Crucially, the engine needs to know **how far
to look back**, which is why X must be fixed-length in Java 8–13.

### Worked Example

Pattern: `(?<=\$)\d+(\.\d{2})?`  
Input: `"pay $19.99 now"`

- Engine tries to match at each position.
- At position 5 (the `1` after `$`): lookbehind checks position 4 — is there a `$`? Yes. Pass.
- `\d+(\.\d{2})?` then consumes `19.99` starting at position 5.
- The `$` is not part of the match.

### The "Fixed-Length" Restriction (Java 8–13)

The restriction stems from how the engine implements lookbehind: it must step backwards by
exactly the length of X to find the starting position for the backwards match. If X can
match strings of different lengths, the engine doesn't know how far to step back.

**Allowed in Java 8:**
```/dev/null/ok.txt#L1-5
(?<=abc)          -- fixed: always 3 chars
(?<=\$|€)         -- alternation of same-length alternatives is allowed
                  -- NOTE: $ is 1 byte, € is also 1 codepoint — same length
(?<=\d{4})        -- exactly 4 digits — fixed
(?<=[A-Z]{3})     -- exactly 3 uppercase — fixed
```

**NOT allowed in Java 8 (PatternSyntaxException):**
```/dev/null/bad.txt#L1-4
(?<=\w+)          -- variable length
(?<=.*)           -- variable length
(?<=a{1,3})       -- variable (could be 1, 2, or 3 chars)
```

**Allowed in Java 14+ (bounded variable-length):**
```/dev/null/java14.txt#L1-3
(?<=\w{1,10})     -- bounded: between 1 and 10 chars
(?<=a{1,3})       -- now legal
```

### Workaround for Variable-Length Lookbehind (Java 8)

Use a **capturing group** instead. Instead of:
```/dev/null/workaround.txt#L1-1
(?<=\[WARN\] |(?<=\[ERROR\] ))\w+
```

Write:
```/dev/null/workaround2.txt#L1-1
\[(?:WARN|ERROR)\] (\S+)
```

Then extract group 1. It's less "pure" but more readable, performs better, and works on all
Java versions.

---

## 5. Negative Lookbehind `(?<!X)`

### Syntax

```/dev/null/syntax.txt#L1-1
(?<!X)
```

### Semantics

> "Succeed if pattern X does NOT match immediately before the current position."

Same fixed-length restriction as positive lookbehind.

### Worked Example

Pattern: `(?<!\$)\d+`  
Input: `"item 42, price $100"`

- Position 5 (`4` in `42`): check if `$` precedes it. Char at pos 4 is ` `. No `$`. Assertion passes → match `42`.
- Position 16 (`1` in `100`): check if `$` precedes it. Char at pos 15 is `$`. Assertion fails → no match.

Result: only `"42"` matches, not `"100"`.

---

## 6. Engine Internals: How the NFA Implements These

Java's `java.util.regex` uses a **nondeterministic finite automaton (NFA)** with backtracking,
similar to Perl-style engines.

### Lookahead Execution Model

```/dev/null/pseudocode.txt#L1-12
// Engine state machine, simplified
function matchLookahead(pattern, savedPos):
    cursor = savedPos
    result = attemptMatch(pattern, cursor)   // run inner NFA
    cursor = savedPos                         // ALWAYS reset cursor
    return result.matched                     // return boolean only
```

The inner pattern runs its own NFA starting at `savedPos`. The match result is used only as
a boolean — no characters are consumed by the outer engine.

**Cost**: the inner NFA runs fully. Complex patterns inside lookaheads can be expensive.

### Lookbehind Execution Model

```/dev/null/pseudocode2.txt#L1-15
// Conceptual model — actual implementation varies
function matchLookbehind(pattern, savedPos):
    length = computeFixedLength(pattern)      // must know this at compile time
    if savedPos < length:
        return false                           // not enough chars before us
    startPos = savedPos - length
    result = attemptMatch(pattern, startPos)   // match forward from calculated start
    // cursor is never changed
    return result.matched && result.endPos == savedPos
```

The key requirement: `computeFixedLength(pattern)` must succeed deterministically. Variable-
length patterns make this function undefined → hence the restriction.

For **alternation** like `(?<=\$|€)`, both alternatives must have the same length (both are
1 Unicode code point). Java checks this at compile time and allows it.

---

## 7. Nested Lookaheads

Lookaheads can be nested: `(?=(?!abc)...)`. This is legal but often a sign that a different
approach would be clearer. The outer assertion saves position, the inner assertion runs (also
saving/restoring position), and the combined result propagates outward.

A practical nested case:

```/dev/null/nested.txt#L1-2
(?=.*\d)(?=.*(?!123)\d{3})
-- "must have a digit, and that digit must not be part of the sequence 123"
```

Generally: **avoid nesting beyond one level** in production code. The cognitive overhead
outweighs the brevity.

---

## 8. The Overlapping Match Trick

Standard regex matching is non-overlapping: after `find()` returns a match, the engine
advances the cursor past the end of the match. This means `"abababa".find("aba")` returns
matches at positions 0 and 4 (skipping position 2).

### The Pattern

Wrap your target in a lookahead inside a capturing group:

```/dev/null/overlap.txt#L1-2
(?=(aba))
```

Because the lookahead is zero-width, every call to `find()` advances the cursor by only 1
(the minimum advance to prevent infinite loops), and the target text is captured in group 1
without being consumed.

```/dev/null/overlap-java.txt#L1-12
Pattern p = Pattern.compile("(?=(aba))");
Matcher m = p.matcher("abababa");
int count = 0;
while (m.find()) {
    count++;                   // group(1) = "aba" each time
}
// count = 3  (positions 0, 2, 4)
```

### Why This Works

The outer `(?=...)` matches at every position without consuming. `find()` must advance at
least 1 character after a zero-width match to prevent infinite loops (this is part of the
Java Matcher contract). So:
- Position 0: lookahead succeeds (chars 0-2 are "aba") → count++, advance to pos 1
- Position 1: lookahead fails (chars 1-3 are "bab") → advance to pos 2
- Position 2: lookahead succeeds (chars 2-4 are "aba") → count++, advance to pos 3
- Position 3: lookahead fails → advance to pos 4
- Position 4: lookahead succeeds (chars 4-6 are "aba") → count++, advance to pos 5
- Positions 5, 6: fail → done

Total: 3 overlapping matches. This technique is used in bioinformatics sequence scanning,
text analysis, and tokenizer testing.

---

## 9. Performance Considerations

### Lookahead Cost

Every `(?=...)` runs a complete sub-match. The pattern `^(?=.*[A-Z])` — for a string of
length N — will potentially scan all N characters in the `.*` before finding (or not finding)
an uppercase letter. Stack 5 such lookaheads and you have 5 linear scans.

For password validation this is perfectly fine (passwords are short). But **do not** use
`(?=.*)` inside a loop over millions of records — prefer a single-pass parser or a compiled
character classification.

### The `.*` Inside a Lookahead

`^(?=.*X)` is O(N) in the worst case (X not present). If you have 5 independent lookaheads
of this form, worst-case input triggers 5 × O(N) scans. Still linear overall, but consider
whether a single-pass approach is cleaner.

### Lookbehind Cost

Lookbehind is generally cheap for fixed-length patterns — the engine computes the start
position arithmetically (no backtracking needed for fixed-width). The check is O(length-of-X).

Variable-length lookbehind (Java 14+) may require trying multiple starting positions — read
the release notes for your JDK's specific implementation.

---

## 10. When NOT to Use Lookahead/Lookbehind

Lookaheads are elegant but they're not always the right tool:

| Situation | Use lookahead | Use capturing group |
|-----------|:---:|:---:|
| Extract text adjacent to a symbol | ✅ | — |
| Multi-constraint validation | ✅ | — |
| Extract the symbol AND the text | — | ✅ |
| Complex extraction with variable-length prefix | — | ✅ |
| Readability is paramount | — | ✅ |

Rule of thumb: if you need to capture what the assertion is testing, use a group. If you
only need to **condition** on it, use an assertion.

---

## 11. Alternation Inside Lookbehind — The Interview Trap

**Question**: "Can you use alternation inside a lookbehind?"

**Answer in Java 8+**: Yes, BUT all alternatives must produce strings of the same length.

```/dev/null/alt-lookbehind.txt#L1-6
(?<=cat|dog)     -- both 3 chars: ALLOWED in Java 8
(?<=cat|dogs)    -- 3 vs 4 chars:  ERROR in Java 8, allowed in Java 14+
(?<=\$|€)        -- both 1 Unicode code point: ALLOWED
(?<=USD|EUR)     -- both 3 chars: ALLOWED
(?<=\d+|[A-Z])   -- variable: ERROR in Java 8
```

The `\$|€` case is subtle: `$` is U+0024 (1 byte in UTF-16), `€` is U+20AC (also 1
code point in UTF-16). In Java, strings are UTF-16, so both are 1 `char` wide → equal length
→ allowed. If you used a supplementary plane character (4 bytes in UTF-8, 2 chars in UTF-16),
the length in terms of `char` units would be 2 — and you'd need to match that.

---

## 12. Real-World Use Cases

### 12.1 Price Extraction

Problem: extract numeric amounts from a receipt without capturing the currency symbol.

```/dev/null/price.regex#L1-2
(?<=[$€£¥])\d{1,3}(?:,\d{3})*(?:\.\d{2})?
```

Lookbehind asserts the currency symbol is there; the main pattern captures just the number.

### 12.2 Split Without Losing the Delimiter

`"hello world".split("\\s+")` discards spaces. To split on a boundary but keep the delimiter
in one of the parts:

```/dev/null/split.txt#L1-3
// Split BEFORE uppercase: "camelCaseString" → ["camel", "Case", "String"]
"camelCaseString".split("(?=[A-Z])")
// The lookahead is zero-width, so the split point is before the uppercase letter,
// which stays attached to the following token.
```

### 12.3 Context-Sensitive Redaction

Replace SSNs only when preceded by the word "SSN:" (case-insensitive):

```/dev/null/ssn.txt#L1-2
(?i)(?<=ssn:\s)\d{3}-\d{2}-\d{4}
// Replace match with "***-**-****"
```

### 12.4 HTML Tag Content (Simple Cases)

Extract text between specific tags without including the tags:

```/dev/null/html.txt#L1-2
(?<=<b>).*?(?=</b>)
// Lookbehind asserts <b> precedes; lookahead asserts </b> follows
```

Note: Do not parse real HTML with regex. This works only for simple, well-formed snippets.

---

## 13. Summary Reference Card

```/dev/null/summary.txt#L1-30
ZERO-WIDTH ASSERTIONS
─────────────────────────────────────────────────────────────
Positive lookahead   (?=X)   -- X must match ahead (no consume)
Negative lookahead   (?!X)   -- X must NOT match ahead
Positive lookbehind  (?<=X)  -- X must match behind (fixed-length in Java 8)
Negative lookbehind  (?<!X)  -- X must NOT match behind

JAVA VERSION NOTES
─────────────────────────────────────────────────────────────
Java 8-13: lookbehind X must have fixed, deterministic length
Java 14+ : bounded variable-length lookbehind allowed (e.g. \w{1,10})
Lookahead: no length restriction in any version

ENGINE MODEL
─────────────────────────────────────────────────────────────
Lookahead:   save pos → try inner NFA → reset pos → return bool
Lookbehind:  compute startPos = currentPos - len(X)
             → try inner NFA from startPos → return bool

OVERLAPPING MATCH TRICK
─────────────────────────────────────────────────────────────
(?=(target))   zero-width outer + capturing inner
Matcher.find() advances 1 after zero-width match → detects overlaps

PERFORMANCE
─────────────────────────────────────────────────────────────
(?=.*X)        O(N) scan per lookahead — avoid in tight loops
(?<=FIXED)     O(len) — very cheap
Nested lookaheads — legal, but prefer flat when possible

WHEN TO USE GROUPS INSTEAD
─────────────────────────────────────────────────────────────
If you need to capture what's in the assertion → use a group
If you need variable-length lookbehind on Java 8 → use a group
If readability matters more than elegance → use a group
```
