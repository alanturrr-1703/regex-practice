# Protocol Extractor

**Difficulty:** Medium  
**Concept:** Alternation  
**Estimated Time:** 25–40 minutes

---

## Concepts Tested

- Alternation inside a capturing group: `(https?|ftp|sftp|ssh)`
- How group indices work when alternation is inside the group
- `group(1)` returns the specific branch that matched
- Non-capturing group `(?:...)` for the host portion
- Processing a list of strings with one pattern

---

## Problem Statement

Given a list of URLs, extract the **protocol** and **host** for each URL whose protocol is one of:
`http`, `https`, `ftp`, `sftp`, `ssh`

Return a `List<UrlParts>` with `protocol` and `host` fields. Skip URLs with unrecognized protocols.

---

## Method Signature

```java
public List<UrlParts> extractProtocols(List<String> urls)
```

Where `UrlParts` is a record with `String protocol` and `String host` fields.

---

## Constraints

- `urls` is never `null`; individual URL strings are never `null`
- The host is the part between `://` and the first `/` or end-of-string
- Unrecognized protocols (e.g., `mailto:`, `file://`, `jdbc:`) are silently skipped
- Order of results matches the order of URLs in input

---

## Input / Output Examples

| Input URLs | Output |
|---|---|
| `["http://example.com"]` | `[UrlParts("http","example.com")]` |
| `["https://secure.com"]` | `[UrlParts("https","secure.com")]` |
| `["ftp://files.net"]` | `[UrlParts("ftp","files.net")]` |
| `["mailto:user@host"]` | `[]` — unrecognized protocol |
| `["http://example.com", "https://secure.com", "ftp://files.net", "mailto:user@host"]` | 3 results |

---

## Edge Cases

- **Empty list**: return empty list
- **All unrecognized**: return empty list
- **URL with path**: `"https://example.com/path"` → host is `"example.com"` (stop at `/`)
- **`https?` matching both http and https**: `https?` is one NFA path; the `?` makes the `s` optional

---

## Time Complexity

- **O(n * m)** where n = number of URLs, m = average URL length

---

## Real-World Relevance

- **Web crawlers**: classify URLs by protocol before dispatching to the right fetcher
- **Firewall rule engines**: filter URLs by protocol/host combinations
- **Log analysis**: group access logs by protocol type

---

## Regex Thinking Process

1. **Pattern for the protocol**: We want to capture it, so use `(...)`:  
   `(https?|ftp|sftp|ssh)`  
   - `https?` matches both "http" and "https" (the `s` is optional)
   - `sftp` must be listed (it starts with 's', so it won't accidentally match "ftp")

2. **Fixed separator**: `://`

3. **Pattern for the host**: Capture everything from here up to the first `/` or whitespace:  
   `([^/\s]+)` — one or more chars that are not `/` or whitespace

4. **Full pattern**: `(https?|ftp|sftp|ssh)://([^/\s]+)`  
   Java string: `"(https?|ftp|sftp|ssh)://([^/\\s]+)"`

5. **Extraction**: `group(1)` = protocol, `group(2)` = host

6. **Key insight**: With alternation inside group 1, `group(1)` returns whichever branch matched: "http", "https", "ftp", "sftp", or "ssh". The alternation is transparent from the API perspective — you just call `group(1)` regardless.

---

## Common Mistakes

| Mistake | Why It's Wrong |
|---|---|
| Not grouping the protocol alternation | `https?|ftp://host` is `https?` OR `ftp://host` (wrong precedence) |
| Using `.*` for host | Greedy `.+` eats the whole URL including paths |
| `ftp` before `sftp` without `https?` style | If both start differently, order doesn't matter; `ftp` ≠ prefix of `sftp` |
| Calling `group()` instead of `group(1)` | `group()` = entire match `"https://example.com"`; `group(1)` = `"https"` |

---

## Debugging Advice

Print both groups to verify extraction:
```java
Pattern p = Pattern.compile("(https?|ftp|sftp|ssh)://([^/\\s]+)");
Matcher m = p.matcher("https://example.com/path");
m.find();
System.out.println("Protocol: " + m.group(1)); // "https"
System.out.println("Host:     " + m.group(2)); // "example.com"
System.out.println("Full:     " + m.group(0)); // "https://example.com"
```
