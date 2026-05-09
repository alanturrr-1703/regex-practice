# Hints — Simple Lexer

Work through these hints one at a time. Only reveal the next hint if you are genuinely stuck.

---

## Hint 1 — The Combined Pattern Approach

Instead of running multiple separate patterns, build ONE combined pattern using alternation (`|`) between groups. Each alternative is one token type:

```
(NUMBER_PATTERN)|(IDENTIFIER_PATTERN)|(OPERATOR_PATTERN)|(LPAREN)|(RPAREN)|(WHITESPACE)
```

The regex engine tries alternatives left-to-right. The ORDER matters:
- NUMBER must come before IDENTIFIER so `1abc` starts with a NUMBER, not an IDENTIFIER.
- WHITESPACE is last (or in any position) because it's just consumed.

After a match, check which group captured to know the token type.

---

## Hint 2 — Writing Each Sub-Pattern

Think about what each token looks like:

- **NUMBER**: one or more digits → `\d+`  (Java string: `"\\d+"`)
- **IDENTIFIER**: starts with a letter, continues with letters, digits, or underscores → `[a-zA-Z][a-zA-Z0-9_]*`
- **OPERATOR**: one of `+`, `-`, `*`, `/` → `[+\-*/]` (hyphen must be escaped or placed correctly)
- **LPAREN**: literal `(` → `\(` (Java string: `"\\("`)
- **RPAREN**: literal `)` → `\)` (Java string: `"\\)"`)
- **WHITESPACE**: one or more whitespace chars → `\s+`

Combine them into one alternation string, wrapping each in a capturing group.

---

## Hint 3 — Detecting Which Group Matched

When a `Matcher` uses alternation `(A)|(B)|(C)`, after a successful `find()`:
- `m.group(1)` is non-null if alternative A matched
- `m.group(2)` is non-null if alternative B matched
- `m.group(3)` is non-null if alternative C matched

Check them in order using `if (m.group(1) != null)` to determine the token type. The matched text is always `m.group()` (the full match = group 0).

---

## Hint 4 — Detecting Gaps (Invalid Characters)

After each successful `find()`, check if the match started exactly where the last match ended:

```
int pos = 0;
while (m.find()) {
    if (m.start() != pos) {
        throw new IllegalArgumentException("Unexpected character at position " + pos
            + ": '" + expression.charAt(pos) + "'");
    }
    pos = m.end();
    // ... process token
}
if (pos != expression.length()) {
    throw new IllegalArgumentException("Unexpected character at position " + pos);
}
```

If `m.start() > pos`, there are characters between `pos` and `m.start()` that no pattern matched — those are invalid.

---

## Hint 5 — Skeleton of the Full Approach

Here is the overall structure (fill in the blanks):

```
private static final Pattern LEXER = Pattern.compile(
    "(\\d+)"               +  // group 1: NUMBER
    "|([a-zA-Z][a-zA-Z0-9_]*)" +  // group 2: IDENTIFIER
    "|([+\\-*/])"          +  // group 3: OPERATOR
    "|(\\()"               +  // group 4: LPAREN
    "|(\\))"               +  // group 5: RPAREN
    "|(\\s+)"                  // group 6: WHITESPACE (skip)
);

public List<Token> tokenize(String expression) {
    List<Token> tokens = new ArrayList<>();
    Matcher m = LEXER.matcher(expression);
    int pos = 0;
    while (m.find()) {
        if (m.start() != pos) {
            throw new IllegalArgumentException(...);
        }
        pos = m.end();
        if (m.group(1) != null)      tokens.add(new Token(TokenType.NUMBER, m.group()));
        else if (m.group(2) != null) tokens.add(new Token(TokenType.IDENTIFIER, m.group()));
        else if (m.group(3) != null) tokens.add(new Token(TokenType.OPERATOR, m.group()));
        else if (m.group(4) != null) tokens.add(new Token(TokenType.LPAREN, m.group()));
        else if (m.group(5) != null) tokens.add(new Token(TokenType.RPAREN, m.group()));
        // group 6 = WHITESPACE — skip (don't add to list)
    }
    if (pos != expression.length()) {
        throw new IllegalArgumentException(...);
    }
    return tokens;
}
```
