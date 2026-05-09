# Hints

## Hint 1 — Greedy Brackets
First, think about what `\[.*\]` does on `"[123][456]"`. It matches from the FIRST `[` to the LAST `]` — so you get one match containing `123][456`. That's the greedy problem you need to fix.

## Hint 2 — Make It Lazy
Append `?` to the `*` quantifier: `\[.*?\]`. This tells the engine: "match as few characters as possible before you find the closing `]`." Now it stops at the FIRST `]` it encounters.

## Hint 3 — Escaping the Brackets
In regex, `[` and `]` have special meaning (character class). Outside a character class, `\[` and `\]` match literal brackets. In Java string: `"\\["` and `"\\]"`.

## Hint 4 — Capture Group for the Content
Use a capture group around `.*?` to extract just the content (without brackets):
Pattern: `\[(.*?)\]`
Java string: `"\\[(.*?)\\]"`
Then use `matcher.group(1)` to get the content.

## Hint 5 — The Full Loop
Use `Matcher.find()` in a while loop. Each iteration gives you one `[...]` block. Extract `group(1)` for the inner content.
