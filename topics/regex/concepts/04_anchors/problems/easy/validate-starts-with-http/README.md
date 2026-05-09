# Problem: Validate Starts With HTTP

**Difficulty**: Easy  
**Concept**: Anchors  
**Skills Tested**: `^` anchor at string start, `?` optional character, `find()` with anchors

---

## Problem Statement

Given a string, return `true` if it starts with `"http://"` or `"https://"`. Use the `^` anchor in your regex to enforce "start of string."

---

## Constraints

- Input may be `null` — return `false`
- Input may be empty — return `false`
- Match is case-sensitive: `"HTTP://"` should return `false`
- Leading whitespace makes the test fail: `" http://..."` → `false`

---

## Input / Output Examples

| Input | Output | Reason |
|---|---|---|
| `"http://example.com"` | `true` | Starts with `http://` |
| `"https://example.com"` | `true` | Starts with `https://` |
| `"ftp://example.com"` | `false` | Different scheme |
| `" http://example.com"` | `false` | Leading space — not at position 0 |
| `""` | `false` | Empty string |
| `"see http://x.com"` | `false` | `http://` not at start |
| `"HTTP://example.com"` | `false` | Case-sensitive mismatch |

---

## Edge Cases

- `"https://"` alone (no host) → `true` (still starts with the scheme)
- `"http"` (no `://`) → `false`
- `null` → `false`

---

## Expected Time Complexity

O(1) with the `^` anchor — the engine only attempts the match at position 0.

---

## Real-World Relevance

URL scheme validation is the first step in any URL parser or security check:
- **Content Security Policy (CSP)**: block non-HTTPS resources
- **Link validators**: ensure outbound links use secure schemes
- **API gateway routing**: route HTTP vs HTTPS traffic differently
- **Input sanitization**: reject any URL not using HTTP/HTTPS

---

## Regex Thinking Process

**Step 1**: You need to match either `"http://"` or `"https://"`. The only difference is the optional `'s'`. Use `?` after `s`: `https?://` matches both `"http://"` and `"https://"`.

**Step 2**: You need this to be at the START of the string. Add `^`: `^https?://`

**Step 3**: Use `find()` with the pattern. Since `^` anchors to position 0, `find()` will only succeed if the string begins with the scheme.

**Alternative**: Use `matcher.lookingAt()` (matches from the beginning) without `^`.

---

## Common Mistakes

1. **Forgetting `^`**: Without it, `"see http://x.com"` returns `true` because `find()` finds `"http://"` at position 4.
2. **Using `matches()`**: `matches()` requires the ENTIRE string to match. `"http://example.com"` would NOT match `^https?://` with `matches()` because there's content after `://`.
3. **Missing the `://` part**: `https?` alone matches `"http"` and `"https"` but not the `"://"` suffix.
4. **Not escaping `/` in Java**: `/` does NOT need escaping in Java regex. No `\/` needed.

---

## Debugging Advice

- Test `"see http://x.com"` — if it returns `true`, you forgot `^`
- Test `"https://x.com"` — if it returns `false`, your `s?` is wrong
- Test `"HTTP://x.com"` — should be `false` (case-sensitive)
