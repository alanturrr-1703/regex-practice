# Hints: Extract Version Numbers

---

## Hint 1 (Gentle)

A semantic version has the form `X.Y.Z` where each of X, Y, Z is a number. Think about which quantifier limits each component to 1–3 digits, and which metacharacter matches a literal dot.

---

## Hint 2

The base pattern for one version component is `\d{1,3}` — 1 to 3 digits. A literal dot in regex is `\.` (backslash-dot). So the basic version pattern is:

`\d{1,3}\.\d{1,3}\.\d{1,3}`

In Java string literals, each `\` must be doubled: `"\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}"`

Try this pattern first. What happens with `"1.2.3.4"`?

---

## Hint 3

The issue with `"1.2.3.4"` is that the engine matches `"1.2.3"` successfully — it doesn't look ahead to see the `.4`. You need a **negative lookahead** at the end of the pattern to reject matches followed by a dot-and-digit:

`(?!\.\d)` — fails if the next characters are a dot followed by a digit.

Also add `(?!\d)` to prevent matching `"1.2.3456"` (patch part followed by more digits).

---

## Hint 4

You also need a **negative lookbehind** at the start to ensure the version doesn't start mid-number:

`(?<!\d)` — fails if the character before the match is a digit.

This prevents `"1234.5.6"` from being matched as `"234.5.6"` or `"34.5.6"`.

Put together:
`(?<!\d)\d{1,3}\.\d{1,3}\.\d{1,3}(?!\.\d)(?!\d)`

---

## Hint 5 (Near Solution)

Use a `while (matcher.find())` loop to collect all matches:

```
List<String> results = new ArrayList<>();
Matcher m = PATTERN.matcher(input);
while (m.find()) {
    results.add(m.group());
}
return results;
```

Return an empty list (not null) when there are no matches.
Handle null input at the start: `if (input == null) return Collections.emptyList();`
