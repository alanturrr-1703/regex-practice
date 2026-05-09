# Hints — Custom Character Range

Work through these hints one at a time.

---

## Hint 1 — What Code Points Define the Ranges?

Before writing the regex, understand the boundaries:

- `a` has code point 97, `m` has code point 109. Range `[a-m]` = characters 97–109.
- `0` has code point 48, `4` has code point 52. Range `[0-4]` = characters 48–52.

Verify: `(int)'m'` = 109, `(int)'n'` = 110. So `n` is NOT in `[a-m]`.

This is the core of the exercise — you're creating a character class with two non-adjacent ranges.

---

## Hint 2 — Combining Two Ranges in One Class

A character class can contain multiple ranges. Just concatenate them inside `[...]`:

```
[a-m0-4]   — matches any char that is in a-m OR in 0-4
```

There's no separator needed between ranges — just put them next to each other inside the brackets.

---

## Hint 3 — Maximal Tokens with `+`

You want the longest contiguous sequences of allowed characters. Use `+` (one or more):

```
[a-m0-4]+   — one or more characters from the allowed set
```

Without `+`, you'd get individual characters. With `+`, you get maximal runs.

---

## Hint 4 — The find() Loop

Use the standard extraction loop:

```
Pattern p = Pattern.compile("[a-m0-4]+");
Matcher m = p.matcher(input);
List<String> tokens = new ArrayList<>();
while (m.find()) {
    tokens.add(m.group());
}
return tokens;
```

Characters outside `[a-m0-4]` simply don't match, which ends the current token and starts a gap. `find()` skips the gap and looks for the next token.

---

## Hint 5 — Boundary Testing

Before you submit, manually trace through:

- `"hello"`: h=104 ∈ [97-109], e=101 ∈, l=108 ∈, l=108 ∈, o=111 ∉ (111 > 109). Result: `["hell"]`.
- `"aaa444"`: a ∈, a ∈, a ∈, 4=52 ∈ [48-52], 4 ∈, 4 ∈. Result: `["aaa444"]`.
- `"xyz999"`: x=120 ∉ [97-109], y=121 ∉, z=122 ∉, 9=57 ∉ [48-52]. Result: `[]`.

If your results match, your implementation is correct.
