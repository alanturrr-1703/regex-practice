package com.example;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.concurrent.ExecutionException;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

/**
 * Identify Catastrophic Pattern
 *
 * <p>Your task: implement {@link #isSafePattern(String)} that returns {@code true} if
 * the pattern completes quickly on adversarial input, {@code false} if it times out
 * (indicating likely catastrophic backtracking).
 *
 * <p>TODO: Return false for null input or PatternSyntaxException.
 *
 * <p>TODO: Build adversarial input: {@code "a".repeat(25) + "!"}.
 *
 * <p>TODO: Run {@code pattern.matcher(adversarial).matches()} inside an ExecutorService.
 *
 * <p>TODO: Use {@code future.get(100, TimeUnit.MILLISECONDS)} — if TimeoutException
 *         is thrown, cancel the future, shut down the executor, and return false.
 *
 * <p>TODO: Always call {@code exec.shutdownNow()} in a finally block to avoid thread leaks.
 */
public class Solution {

    /**
     * Returns true if the regex is unlikely to cause catastrophic backtracking,
     * false if it times out (or is null/invalid).
     *
     * <p>Uses adversarial input {@code "aaaaaaaaaaaaaaaaaaaaaaaaa!"} (25 a's + '!')
     * and a 100ms timeout to detect exponential backtracking patterns.
     *
     * <p>Examples:
     * <ul>
     *   <li>"(a+)+" → false (catastrophic: nested quantifier)</li>
     *   <li>"\\d+" → true (safe: single quantifier)</li>
     *   <li>"(.+)+" → false (catastrophic)</li>
     *   <li>"[a-z]+" → true (safe)</li>
     *   <li>null → false</li>
     * </ul>
     *
     * @param regex the regex pattern string to test; may be null
     * @return true if pattern completes within timeout on adversarial input
     */
    public boolean isSafePattern(String regex) {
        throw new UnsupportedOperationException("TODO");
    }
}
