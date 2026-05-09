# Simple Lexer

**Difficulty:** Hard  
**Concept:** Regex Basics  
**Concepts Tested:** Multiple patterns, ordered token matching, `Matcher.find()` loop as a scanner, `Matcher.region()`, building a token stream, enum-based token types

---

## Problem Statement

Implement a simple arithmetic expression lexer (tokenizer). Given a string representing an arithmetic expression, return a list of `Token` objects representing the tokens in the expression.

Recognize the following token types (in order of priority):

| Token Type | Pattern | Examples |
|-----------|---------|---------|
| `NUMBER` | One or more digits | `12`, `300`, `0` |
| `IDENTIFIER` | Letter, followed by letters/digits/underscores | `x`, `y_1`, `myVar` |
| `OPERATOR` | One of `+`, `-`, `*`, `/` | `+`, `*` |
| `LPAREN` | Literal `(` | `(` |
| `RPAREN` | Literal `)` | `)` |
| `WHITESPACE` | One or more whitespace characters | (skip — do not include in output) |

**Any character that doesn't match any token type should cause an `IllegalArgumentException` with a descriptive message.**

---

## Constraints

- Input is non-null but may be empty (empty → empty list).
- Whitespace tokens are consumed but NOT added to the result list.
- Tokens must be extracted left-to-right with no gaps (the lexer covers the entire input).
- `NUMBER` has higher priority than `IDENTIFIER` — a token starting with a digit is a number, not an identifier.
- `IDENTIFIER` starts with a letter only (not digit, not underscore at start — for simplicity).
- The `Token` class is a nested static class already provided in `Solution.java` — do not modify it.

---

## Input / Output Examples

**Example 1:** `"1 + 2"`
```
[NUMBER("1"), OPERATOR("+"), NUMBER("2")]
```

**Example 2:** `"(x)"`
```
[LPAREN("("), IDENTIFIER("x"), RPAREN(")")]
```

**Example 3:** `"100 * y_1"`
```
[NUMBER("100"), OPERATOR("*"), IDENTIFIER("y_1")]
```

**Example 4:** `"12 + 300 * (x - y_1)"`
```
[NUMBER("12"), OPERATOR("+"), NUMBER("300"), OPERATOR("*"),
 LPAREN("("), IDENTIFIER("x"), OPERATOR("-"), IDENTIFIER("y_1"), RPAREN(")")]
```

---

## Edge Cases

- **Empty string** → `[]` (empty list).
- **Only whitespace** `"   "` → `[]` (whitespace is consumed, nothing added).
- **Adjacent tokens** `"(x)"` → no whitespace between tokens; lexer must still work.
- **Multi-digit number** `"300"` → one `NUMBER` token, not three.
- **Multi-char identifier** `"y_1"` → one `IDENTIFIER` token (underscore and digit are valid continuation chars).
- **Invalid character** `"x!"` → should throw `IllegalArgumentException` when `!` is encountered.

---

## Expected Time Complexity

- **O(n)** where n = input length. Each character is visited a constant number of times.

---

## Real-World Relevance

This is the front end of every compiler, interpreter, and DSL parser. Examples:
- **Java compiler**: tokenizes source code into keywords, identifiers, literals, operators.
- **SQL parser**: tokenizes SQL into SELECT, FROM, WHERE, identifiers, literals.
- **JSON parser**: tokenizes into `{`, `}`, `[`, `]`, strings, numbers, `true`, `false`, `null`.
- **Calculator apps**: tokenize arithmetic expressions exactly like this problem.
- **Template engines**: tokenize templates into literal text, variable references, control flow.

The regex-based lexer you build here is the foundation of the "handwritten lexer" pattern used in production compilers for performance-critical code.

---

## Regex Thinking Process

A regex-based lexer works as follows:

1. **Compile multiple patterns**, one per token type. Order matters: NUMBER before IDENTIFIER (so `123abc` tokenizes as `NUMBER("123")` then `IDENTIFIER("abc")`, not one big token).

2. **Build a combined pattern** using alternation: `(NUMBER_PATTERN)|(IDENT_PATTERN)|(OP_PATTERN)|...`. The alternation is ordered left-to-right, so the engine tries NUMBER first.

3. **Loop with `Matcher.find()`**. After each match:
   - Determine which group captured (group 1 = NUMBER, group 2 = IDENT, etc.)
   - Create the appropriate `Token` and add it to the list (or skip for WHITESPACE)
   - Verify the match starts exactly where the previous match ended (no gaps)

4. **After the loop**, verify the entire input was consumed. If not, throw for the invalid character.

**Key insight**: Use `m.start()` and `m.end()` to track position and detect gaps.

---

## Common Mistakes

1. **Not checking for gaps**: if the combined pattern doesn't match at the current position, there's an invalid character. Don't silently skip it.
2. **Wrong group number**: when using alternation `(A)|(B)|(C)`, group 1 is for A, group 2 for B, group 3 for C. Check `m.group(n) != null` to find which alternative matched.
3. **Not anchoring the combined pattern to current position**: use `m.find()` starting from the last match end, or use `m.usePattern()` / multiple matchers.
4. **Greedy IDENTIFIER eating NUMBER tokens**: patterns must be ordered correctly, with NUMBER before IDENTIFIER in the alternation.
5. **Forgetting to skip WHITESPACE**: whitespace tokens must be recognized (to advance position) but not added to the result.

---

## Debugging Advice

- Print `m.group()`, `m.start()`, `m.end()` at each iteration to trace what's being matched.
- Print `lastEnd` vs `m.start()` to detect gaps (if `m.start() > lastEnd`, there's a gap containing invalid chars).
- Test with the simplest input first: `"1"`, then `"1+2"`, then add whitespace.
- Use `m.group(1)`, `m.group(2)`, etc. to confirm which alternative matched.
