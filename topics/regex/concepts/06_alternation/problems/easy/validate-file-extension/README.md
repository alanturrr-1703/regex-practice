# Validate File Extension

**Difficulty:** Easy  
**Concept:** Alternation  
**Estimated Time:** 10–20 minutes

---

## Concepts Tested

- Alternation anchored to end-of-string with `$`
- Case-insensitive matching with `Pattern.CASE_INSENSITIVE` or `(?i)` inline flag
- Escaping the literal dot: `\.` (dot is a metacharacter that matches any char)
- Non-capturing group `(?:...)` to scope the alternation without creating a capture slot
- `matcher.find()` vs the full-match convenience method

---

## Problem Statement

Given a filename string, return `true` if it ends with one of the recognized source file extensions:

`.java`, `.py`, `.js`, `.ts`, `.go`

The match is **case-insensitive**: `"SCRIPT.PY"` should return `true`.

The extension must match the **complete** extension — `"file.javascript"` should return `false` because `.javascript` is not `.js`.

---

## Method Signature

```java
public boolean isSourceFile(String filename)
```

---

## Constraints

- `filename` is never `null`
- Extension comparison is case-insensitive
- The filename must actually end with the extension (anchor to `$`)
- `"file.javascript"` → `false` (`javascript` ≠ `js`, even though `js` is a prefix)

---

## Input / Output Examples

| Input | Output | Reason |
|---|---|---|
| `"Main.java"` | `true` | `.java` extension |
| `"script.py"` | `true` | `.py` extension |
| `"SCRIPT.PY"` | `true` | Case-insensitive |
| `"image.png"` | `false` | `.png` not in the list |
| `"file.javascript"` | `false` | `.javascript` ≠ `.js` |
| `"app.ts"` | `true` | `.ts` extension |
| `"mod.go"` | `true` | `.go` extension |
| `"noextension"` | `false` | No dot extension |
| `"file.JS"` | `true` | Case-insensitive |
| `"Main.JAVA"` | `true` | Case-insensitive |

---

## Edge Cases

- **`"file.javascript"`**: `\.js` matches "js" but it's followed by "avascript" which is more than the `$` anchor allows. The `$` anchor requires the extension to be at the very end of the string.
- **No extension**: `"noextension"` — the pattern `\.(java|py|...)$` requires a dot; no dot → no match.
- **All uppercase**: `"MAIN.JAVA"` — case-insensitive flag handles this.
- **Empty string**: no dot → `false`.
- **Just a dot**: `"."` — the alternation `java|py|...` won't match empty string after the dot.

---

## Time Complexity

- **O(n)** but essentially O(extension_length) in practice since we anchor to `$`

---

## Real-World Relevance

- **Build tools**: filter source files from a directory listing
- **CI pipelines**: run linters only on changed source files
- **File upload validators**: reject non-source files
- **IDE plugins**: determine language mode from filename

---

## Regex Thinking Process

1. **What are the valid extensions?** `.java`, `.py`, `.js`, `.ts`, `.go`

2. **How to match "ends with extension"?** Anchor to `$` and match the dot + extension:  
   `\.(java|py|js|ts|go)$`

3. **Critical: escape the dot!** In regex, `.` matches ANY character. A bare `."java"` would also match `Xjava`. Use `\.` to match a literal dot.  
   In Java string: `"\\."`.

4. **Why does `"file.javascript"` not match?**  
   Pattern: `\.(java|py|js|ts|go)$`  
   For input `"file.javascript"`:
   - Engine scans to the `.` in `.javascript`
   - Tries `java` → `j,a,v,a` match "java", but then there's `s` not `$` → fails
   - Tries `py` → `j` ≠ `p` → fails
   - Tries `js` → `j,s` match but then `a` remains before `$` → fails  
   (Because `"javascript"` after the dot is `j,a,v,a,s,c,r,i,p,t` — `js` only matches the first two chars but `$` requires end-of-string immediately after)
   - All alternatives fail → `false` ✓

5. **Case-insensitive**: Use `Pattern.CASE_INSENSITIVE` or the inline flag `(?i)`.

---

## Common Mistakes

| Mistake | Why It's Wrong |
|---|---|
| Using `.` instead of `\.` | `.` matches any character, not just dot |
| Missing `$` anchor | `"file.javascript"` would match `.js` without the anchor |
| Using `matches()` with an un-anchored pattern | `matches()` is implicitly anchored to the full string |
| Using `[java\|py\|js]` | Character class, not alternation |
| Forgetting case-insensitive flag | `"SCRIPT.PY"` won't match without the flag |

---

## Debugging Advice

Test the dot-escape and anchoring:
```java
// Without anchoring:
Pattern p1 = Pattern.compile("\\.(?:java|py|js|ts|go)", Pattern.CASE_INSENSITIVE);
p1.matcher("file.javascript").find(); // TRUE (wrong!) — matches ".js" inside ".javascript"

// With anchoring:
Pattern p2 = Pattern.compile("\\.(?:java|py|js|ts|go)$", Pattern.CASE_INSENSITIVE);
p2.matcher("file.javascript").find(); // false (correct!)
```

The `$` makes all the difference for the "prefix collision" case.
