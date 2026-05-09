# Greedy vs Lazy — Deep Notes

---

## 1. The Engine's Quantifier Algorithm

To understand greedy vs lazy, you must understand how the NFA regex engine executes a quantifier.

### Greedy Algorithm

When the engine encounters a greedy quantifier like `.*`, it:
1. **Consumes as much as possible** — reads forward until it can't match anymore (or hits end of string)
2. **Attempts the rest of the pattern** — tries to match what comes after the quantifier
3. **Gives back one character** if the rest of the pattern fails — this is called **backtracking**
4. **Repeats** until the rest of the pattern succeeds or there is nothing left to give back

**Trace example**: pattern `a.*c` on input `"aXbXc"`

```
Position 0: 'a' matches 'a' ✓
Now at .*: greedily consume everything → eats "XbXc" (4 chars)
Position 5: at end of string, try to match 'c' → fail (nothing left)
BACKTRACK: give back one char. Position 4: try to match 'c' → matches 'c' ✓
MATCH: "aXbXc"
```

### Lazy (Reluctant) Algorithm

When the engine encounters a lazy quantifier like `.*?`, it:
1. **Consumes as little as possible** — starts by matching zero characters
2. **Attempts the rest of the pattern** — tries to match what comes after the quantifier
3. **Takes one more character** if the rest of the pattern fails
4. **Repeats** until the rest of the pattern succeeds or no more input is available

**Trace example**: pattern `a.*?c` on input `"aXbXc"`

```
Position 0: 'a' matches 'a' ✓
Now at .*?: lazily consume 0 chars
Position 1: try to match 'c' → matches 'X'? No. EXPAND.
Take one char 'X'. Position 2: try to match 'c' → 'b'? No. EXPAND.
Take one char 'b'. Position 3: try to match 'c' → 'X'? No. EXPAND.
Take one char 'X'. Position 4: try to match 'c' → 'c'? YES ✓
MATCH: "aXbXc"
```

In this case, greedy and lazy produce the same result. The difference appears when there are MULTIPLE possible end positions.

---

## 2. When Greedy and Lazy Give DIFFERENT Results

**Pattern**: `<b>.*</b>` vs `<b>.*?</b>` on `"<b>one</b> text <b>two</b>"`

**Greedy `<b>.*</b>`**:
```
Match '<b>' ✓
Greedy .* eats: "one</b> text <b>two</b>"
Try to match '</b>' — at end of string, fail.
Backtrack: give back chars one at a time...
Eventually: .* has "one</b> text <b>two" and '</b>' matches the last </b>
MATCH: "<b>one</b> text <b>two</b>"  ← the entire string as ONE match
```

**Lazy `<b>.*?</b>`**:
```
Match '<b>' ✓
Lazy .*? starts with 0 chars.
Try '</b>' at position 3 → 'o'? No.
Take 'o'. Try '</b>' → 'n'? No.
Take 'n'. Try '</b>' → 'e'? No.
Take 'e'. Try '</b>' → '<'? No... wait, '</b>' is 4 chars.
Eventually: .*? has "one" and '</b>' matches.
FIRST MATCH: "<b>one</b>"
Continue from after the match...
Next find(): matches "<b>two</b>"
RESULT: two matches — "<b>one</b>" and "<b>two</b>"
```

**This is the essential difference**: greedy consumes the gap between the first opening and LAST closing. Lazy stops at the FIRST closing.

---

## 3. Possessive Quantifiers (Java-specific)

Java supports a third mode that most other regex flavors lack: **possessive**. Add `+` after the quantifier:

| Quantifier | Mode | Behavior |
|---|---|---|
| `*` | Greedy | Match max, backtrack if needed |
| `*?` | Lazy | Match min, expand if needed |
| `*+` | Possessive | Match max, NEVER backtrack |

Possessive means: "I take as much as I can, and I NEVER give any of it back — not even one character."

```java
// Greedy: "a.*c" on "abcXc"
// .* eats "bcXc", backtracks to give 'c' back → matches "abcXc"
Pattern.compile("a.*c").matcher("abcXc").find(); // true

// Possessive: "a.*+c" on "abcXc"
// .*+ eats "bcXc", never gives back → can't match 'c' → FAIL
Pattern.compile("a.*+c").matcher("abcXc").find(); // false!
```

Possessive is useful for **performance**: when you know the quantified part and the rest of the pattern are non-overlapping (share no characters), possessive eliminates unnecessary backtracking.

---

## 4. Atomic Groups — The Alternative to Possessive

An atomic group `(?>...)` is equivalent to possessive for a sub-expression:

```java
// These are equivalent:
Pattern.compile("\\d++");          // possessive
Pattern.compile("(?>\\d+)");       // atomic group
```

Once an atomic group (or possessive quantifier) succeeds, the engine commits to that match and **never revisits** the characters it consumed.

Java 17+ supports atomic groups natively. Java 8–16 supports them as well — they've been in Java regex since Java 1.4.

---

## 5. The Key Insight: Lazy ≠ "Shortest Match"

This is a crucial point that many developers misunderstand:

**Lazy does NOT find the globally shortest match. It finds the LEFTMOST match, trying minimum first.**

Example: pattern `a+?` on `"aaaa"`
- Lazy tries to match 1 'a' (the minimum for `+`)
- At position 0: matches "a" ← leftmost, minimum length
- `find()` again: at position 1: matches "a"
- etc.

So you get 4 separate matches of "a" — not one match of the shortest run. The minimum is applied per-find-attempt, not globally.

Another example: `".*?"` on `'he said "hello" and "bye"'`
- At position 9 (first `"`): `.*?` lazily matches `hello`, stops at the next `"`
- `find()` again: at position 16 (next `"`): matches `bye`
- Result: `["hello", "bye"]` — correct behavior for parsing quoted strings

---

## 6. When to Use Greedy, Lazy, and Possessive

| Situation | Best Choice | Reason |
|---|---|---|
| Consume everything to end of token | Greedy (default) | No backtracking needed |
| Match between two different delimiters | Lazy `.*?` | Stop at FIRST closing delimiter |
| Match between same delimiter, no escaping | Negated class `[^"]*` | Faster, no backtracking, more correct |
| Match between same delimiter, WITH escaping | `(?:[^"\\]|\\.)*` | Handles `\"` inside |
| Known non-overlapping token (e.g., digits) | Possessive `\d++` | No backtracking, maximum performance |
| Unknown content length, DOTALL needed | Lazy `.*?` with DOTALL | Multiline content between delimiters |

---

## 7. Negated Character Class vs Lazy — The Competition

For matching between two identical delimiters (like quotes), a negated character class is almost always superior to lazy:

| Pattern | Input | How it works |
|---|---|---|
| `".*?"` | `"hello"` | Lazy: tries 0 chars, then 1, then 2... until `"` found |
| `"[^"]*"` | `"hello"` | Negated class: consumes chars that are NOT `"`, stops at `"` |

The negated class is:
- **Faster**: no backtracking at all — `[^"]*` consumes chars that are NOT `"`, then the engine encounters a `"` and the pattern succeeds
- **More semantically correct**: it explicitly says "no quotes inside" rather than "match as few chars as possible"
- **More debuggable**: the behavior is obvious from reading the pattern

**The rule**: if your content cannot contain the closing delimiter, use a negated class. If it can (like escaped quotes), use the `(?:[^"\\]|\\.)*` pattern.

---

## 8. DOTALL and Multiline Lazy Matching

By default, `.` does NOT match newline characters. So `/*.*?*/` would not match a multiline comment:

```java
String text = "/* first line\nsecond line */";
Pattern lazy = Pattern.compile("/\\*.*?\\*/");
lazy.matcher(text).find(); // false — . doesn't match \n
```

Fix: add `Pattern.DOTALL`:
```java
Pattern correct = Pattern.compile("/\\*.*?\\*/", Pattern.DOTALL);
correct.matcher(text).find(); // true
```

With DOTALL, `.` matches ANY character including newline. Lazy `.*?` then correctly finds the shortest span between `/*` and `*/`.

---

## 9. Performance: Why Negated Class Beats Lazy

Consider `".*?"` on `"hello"` (including the surrounding quotes in the input):

Lazy execution:
1. Match `"` ✓
2. Try 0 chars. Check if next char is `"`. It's `h`. Fail. Take `h`.
3. Try again. Check if next char is `"`. It's `e`. Fail. Take `e`.
4. ... (n steps for n characters in the content)
5. Check if next char is `"`. It IS. MATCH.

That's O(n) backtracking attempts — each character causes a fail-and-expand cycle. In practice this is still O(n) but with higher constant factor than a negated class.

For negated class `"[^"]*"`:
1. Match `"` ✓  
2. Consume chars that are NOT `"` (greedy, no backtracking needed — the class doesn't match `"` at all)
3. `[^"]*` consumes `hello` (5 chars), then sees `"` which doesn't match `[^"]`, so stops.
4. Match `"` ✓

Zero backtracking. Faster. More semantically clear.

---

## 10. Catastrophic Backtracking Introduction

This concept is the gateway to understanding **catastrophic backtracking** (fully covered in `regex-performance/`). The key warning sign:

**If you combine lazy or greedy quantifiers with quantifiers on an outer group, you may be creating an exponential backtracking trap.**

Bad pattern: `(.*?)*` — this nests a lazy quantifier inside an outer group with a quantifier. The engine can split the input in exponentially many ways.

The fix is always to use a more precise character class that doesn't require backtracking.

---

## 11. Debugging Workflow

1. **Identify the boundaries** — what starts and ends the region you want to capture?
2. **Identify the content** — can the content contain the closing delimiter? Can it contain backslashes? Can it span lines?
3. **Choose the mode**:
   - Content can't contain closing delimiter → negated class `[^delimiter]*`
   - Content can span lines, simple case → lazy `.*?` with DOTALL
   - Content can contain escaped delimiter → `(?:[^delimiter\\]|\\.)*`
4. **Test boundary behavior** — specifically test with multiple matches in one string to ensure the pattern doesn't "leak" between matches.
5. **Print positions**: use `matcher.start()` and `matcher.end()` to see WHERE the engine matched.

---

## 12. Interview Traps

**"What does `<.*>` match on `<a>hello</a>`?"**
Answer: the entire string `<a>hello</a>` — greedy `.* ` spans from first `<` to last `>`.

**"What does `<.*?>` match?"**
Answer: just `<a>` — lazy stops at the first `>`.

**"What does `<[^>]*>` match?"**
Answer: also just `<a>` — and it's faster than the lazy version.

**"What is a possessive quantifier? When would you use it?"**
Answer: a quantifier that never backtracks (e.g., `*+`). Use when you know the quantified part is non-overlapping with the rest of the pattern — gives a performance boost and can prevent catastrophic backtracking.

**"Is lazy always slower than greedy?"**
Answer: No. For content that is shorter than the full available input, lazy often does fewer operations. For content that fills most of the available space, greedy (which starts from the end) may succeed faster.
