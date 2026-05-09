# Hints — Pattern Cache Refactor

---

## Hint 1 — The Core Problem

`String.matches(regex)` compiles the pattern every time it is called. It is equivalent to:
```/dev/null/hint1.txt#L1-2
Pattern.compile(regex).matcher(this).matches()
```
This means 1 `Pattern.compile()` call per string — very wasteful in a loop.

---

## Hint 2 — Static Pattern Field

Compile the pattern ONCE at class load time:
```/dev/null/hint2.txt#L1-2
private static final Pattern PHONE_PATTERN = Pattern.compile("\\d{3}-\\d{4}");
```
`static final` ensures it is:
- Created exactly once (when the class is loaded by the JVM)
- Thread-safe (immutable after construction)
- Shared across all instances

---

## Hint 3 — Using the Pattern

Replace `s.matches("\\d{3}-\\d{4}")` with:
```/dev/null/hint3.txt#L1-1
PHONE_PATTERN.matcher(s).matches()
```
`Pattern.matcher(s)` creates a `Matcher` (cheap). `.matches()` tests the full string.

---

## Hint 4 — Return Type

Return `List<String>`. Build it with `new ArrayList<>()` and `.add()` inside your loop.

---

## Hint 5 — Null Handling

Guard at the start of the method:
```/dev/null/hint5.txt#L1-3
if (inputs == null) return Collections.emptyList();
```
If the list might contain null strings, also check: `if (s != null && PHONE_PATTERN.matcher(s).matches())`.
