# Named Groups Log Parser

**Difficulty:** Medium  
**Concept:** Groups and Capturing  
**Estimated Time:** 30–45 minutes

---

## Concepts Tested

- Named capturing groups `(?<name>pattern)`
- `matcher.group("name")` — accessing captures by name
- Parsing a real-world structured log format with 6 named groups
- `Optional<T>` for parse-failures vs successes
- Non-capturing groups `(?:...)` for required structure without storing
- Escaping literal characters in patterns (brackets, quotes, slashes)

---

## Problem Statement

Parse a single Apache Combined Log Format line. The format is:

```
IP - - [DATE] "METHOD /path HTTP/1.1" STATUS BYTES
```

**Example input:**
```
127.0.0.1 - - [10/Oct/2024:13:55:36 +0000] "GET /index.html HTTP/1.1" 200 1234
```

Use **named capturing groups** to extract exactly these six fields:

| Name | Example | Notes |
|---|---|---|
| `ip` | `127.0.0.1` | The client IP address |
| `date` | `10/Oct/2024:13:55:36 +0000` | Full date/time inside brackets (without the brackets) |
| `method` | `GET` | HTTP method (POST, GET, etc.) |
| `path` | `/index.html` | Request path |
| `status` | `200` | HTTP status code |
| `bytes` | `1234` or `-` | Bytes sent; `-` means zero/unknown |

Return an `Optional<LogEntry>`. Return `Optional.empty()` for any line that doesn't match the format.

---

## Method Signature

```java
public Optional<LogEntry> parseLine(String line)
```

Where `LogEntry` is a record with `String` fields: `ip`, `date`, `method`, `path`, `status`, `bytes`.

---

## Constraints

- `line` is never `null`
- The format is strict: all 6 fields must match or return `Optional.empty()`
- `bytes` can be `\d+` or `-`
- The path may contain `/`, `.`, `?`, `=`, `&`, `-`, `_` etc. (use `[^\s"]+` or similar)
- Use `matcher.find()` or `matcher.matches()` — think carefully which is correct here

---

## Input / Output Examples

**Example 1 — Valid line:**
```
Input:  127.0.0.1 - - [10/Oct/2024:13:55:36 +0000] "GET /index.html HTTP/1.1" 200 1234
Output: Optional.of(LogEntry{ip="127.0.0.1", date="10/Oct/2024:13:55:36 +0000",
        method="GET", path="/index.html", status="200", bytes="1234"})
```

**Example 2 — 404 with `-` bytes:**
```
Input:  192.168.1.1 - - [10/Oct/2024:14:00:00 +0000] "POST /login HTTP/1.1" 404 -
Output: Optional.of(LogEntry{..., status="404", bytes="-"})
```

**Example 3 — Invalid line:**
```
Input:  not a log line at all
Output: Optional.empty()
```

**Example 4 — Empty string:**
```
Input:  ""
Output: Optional.empty()
```

---

## Edge Cases

- **Status 404, bytes `-`**: bytes field should accept `-` as a valid value
- **Path with query string** (`/search?q=hello`): your pattern for path must accommodate `?`, `=`, `&`
- **Non-matching line**: return `Optional.empty()` gracefully
- **Empty string**: return `Optional.empty()`

---

## Time Complexity

- **O(n)** per call — single pass NFA; O(1) group extraction from the capture buffer

---

## Real-World Relevance

Apache and nginx access logs are the backbone of web traffic analysis. Parsing them efficiently (millions of lines per day) is a classic systems task. Named groups make the parser self-documenting — future engineers reading `matcher.group("status")` understand exactly what's being extracted.

---

## Regex Thinking Process

Work field by field and compose:

1. **IP address**: `(?<ip>\d{1,3}(?:\.\d{1,3}){3})`  
   Or simpler: `(?<ip>\S+)` — an IP won't have spaces

2. **Fixed separator**: ` - - ` — literal characters, no group needed

3. **Date in brackets**: `\[(?<date>[^\]]+)\]`  
   `\[` and `\]` — brackets must be escaped because `[` starts a character class  
   `[^\]]+` — one or more chars that are NOT `]`

4. **Quoted request**: `"(?<method>\w+)\s+(?<path>[^\s"]+)\s+HTTP/[\d.]+"`  
   The outer `"..."` are literal quotes (escape if necessary based on pattern context)

5. **Status and bytes**: `(?<status>\d{3})\s+(?<bytes>\S+)`  
   `\S+` matches anything non-whitespace — handles both `1234` and `-`

6. **Anchor**: Should you use `matches()` (full-line) or `find()` (search)?  
   `find()` is safer if lines may have trailing content; `matches()` if you want strict full-line validation.

---

## Common Mistakes

| Mistake | Why It's Wrong |
|---|---|
| Forgetting to escape `[` and `]` | `[` starts a character class in regex; use `\[` and `\]` |
| Using `"` without considering context | Quote chars in the middle of a pattern are usually literal; they don't need escaping in Java regex |
| Path pattern too restrictive (`\w+`) | Paths contain `/`, `.`, `?`, `=`, `&` — use `[^\s"]+` |
| Using `group(5)` instead of `group("status")` | Numbered groups are error-prone; named groups are self-documenting |
| Returning null instead of Optional.empty() | Always return Optional.empty() for parse failures |

---

## Debugging Advice

If the match fails on a valid line, isolate field by field:
```java
// Test just the IP portion:
Pattern ipTest = Pattern.compile("(?<ip>\\S+)");
// Test just the date bracket portion:
Pattern dateTest = Pattern.compile("\\[(?<date>[^\\]]+)\\]");
// Test just the method+path portion:
Pattern reqTest = Pattern.compile("\"(?<method>\\w+)\\s+(?<path>[^\\s\"]+)");
```
Build up the full pattern piece by piece, confirming each segment works before combining.
