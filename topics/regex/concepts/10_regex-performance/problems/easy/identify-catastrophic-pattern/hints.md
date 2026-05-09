# Hints — Identify Catastrophic Pattern

---

## Hint 1 — Adversarial Input

Catastrophic patterns fail on input consisting of many repeating characters followed by
a character that cannot match. Use:
```/dev/null/hint1.txt#L1-2
String adversarial = "a".repeat(25) + "!";
```
The '!' ensures no match is possible, forcing the engine to exhaust all backtrack paths.

---

## Hint 2 — ExecutorService with Timeout

Run the regex match in a separate thread to enforce a time budget:
```/dev/null/hint2.txt#L1-12
ExecutorService exec = Executors.newSingleThreadExecutor();
Future<?> future = exec.submit(() -> {
    pattern.matcher(adversarial).matches();
});
try {
    future.get(100, TimeUnit.MILLISECONDS);
    return true;   // completed in time -> safe
} catch (TimeoutException e) {
    future.cancel(true);
    return false;  // timed out -> likely catastrophic
} finally {
    exec.shutdownNow();
}
```

---

## Hint 3 — Handle PatternSyntaxException

Wrap `Pattern.compile(regex)` in a try-catch:
```/dev/null/hint3.txt#L1-4
try {
    Pattern p = Pattern.compile(regex);
} catch (PatternSyntaxException e) {
    return false;  // invalid regex → not safe
}
```

---

## Hint 4 — Null Guard

Check `if (regex == null) return false;` before trying to compile.

---

## Hint 5 — Checked vs Unchecked Exceptions

`Future.get()` throws `ExecutionException` (wraps exceptions from the task) and
`InterruptedException`. Catch these alongside `TimeoutException`:
```/dev/null/hint5.txt#L1-3
} catch (TimeoutException e) {
    return false;
} catch (ExecutionException | InterruptedException e) {
    return false;  // unexpected error — treat as unsafe
}
```
