# Hints — Protocol Extractor

Work through these one at a time. Stop as soon as you feel unblocked.

---

## Hint 1 — Alternation inside a capturing group

Normally you use `(?:a|b)` to group alternation without capturing. But in this problem, you WANT to capture which protocol matched. So use `(a|b)` — a capturing group with alternation inside it:

```
(https?|ftp|sftp|ssh)
```

`group(1)` will return whichever branch matched: "http", "https", "ftp", "sftp", or "ssh".

---

## Hint 2 — `https?` covers both http and https

`https?` means "http" followed by an optional "s". This matches both "http" and "https" as a single alternative. You don't need `http|https` — that's two alternatives. `https?` is one.

---

## Hint 3 — Host pattern

The host is everything between `://` and the first `/` or whitespace:

```
([^/\s]+)
```

In Java: `"([^/\\s]+)"`

For `"https://example.com/path"`, this captures `"example.com"` and stops at the `/`.

---

## Hint 4 — Full pattern

```java
private static final Pattern URL_PATTERN =
    Pattern.compile("(https?|ftp|sftp|ssh)://([^/\\s]+)");
```

- `group(1)` = protocol
- `group(2)` = host

---

## Hint 5 — Processing a list

Iterate each URL, apply `find()`, and if it matches, add a `UrlParts`:

```java
public List<UrlParts> extractProtocols(List<String> urls) {
    List<UrlParts> results = new ArrayList<>();
    for (String url : urls) {
        Matcher m = URL_PATTERN.matcher(url);
        if (m.find()) {
            results.add(new UrlParts(m.group(1), m.group(2)));
        }
        // If no match: silently skip
    }
    return results;
}
```
