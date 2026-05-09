# Hints

## Hint 1 — The Core Question
What does `.` mean in a regex? It does NOT mean a literal dot. Think about what you need to do to force a regex to match a character literally instead of using its special meaning.

## Hint 2 — Escaping a Metacharacter
In regex, to match a metacharacter literally, you put a backslash before it. So to match a literal dot, the regex engine needs to see `\.`. What does that look like as a Java string?

## Hint 3 — Java Double Escaping
In Java string literals, a backslash itself must be escaped as `\\`. So to get one backslash into the string, you write `\\`. To get `\.` into the regex engine, you need to write `"\\."` in your Java source code. Try printing the pattern string to verify what the engine receives.

## Hint 4 — matches() vs find()
`String.matches(regex)` requires the ENTIRE string to match the regex. So `"hello.world".matches("\\.")` returns `false` because the whole string is not just a dot.  
You want `Matcher.find()` which searches for the pattern ANYWHERE in the string.

## Hint 5 — Putting It Together
```java
private static final Pattern DOT_PATTERN = Pattern.compile("\\.");

public List<String> filterContainingDot(List<String> inputs) {
    List<String> result = new ArrayList<>();
    for (String s : inputs) {
        if (s != null && DOT_PATTERN.matcher(s).find()) {
            result.add(s);
        }
    }
    return result;
}
```
Your task is to write the body yourself — don't just copy this. Understand each line.
