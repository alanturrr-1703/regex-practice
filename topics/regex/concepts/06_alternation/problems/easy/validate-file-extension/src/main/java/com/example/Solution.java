package com.example;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Solution for the <b>validate-file-extension</b> problem.
 *
 * <p>Your task: implement {@link #isSourceFile(String)} to return {@code true} if
 * the filename ends with one of the recognized source file extensions.</p>
 *
 * <h2>Valid extensions</h2>
 * <p>{@code .java}, {@code .py}, {@code .js}, {@code .ts}, {@code .go} — case-insensitive.</p>
 *
 * <h2>Key concepts</h2>
 * <ul>
 *   <li><b>Escape the dot:</b> {@code \.} — bare {@code .} matches any character</li>
 *   <li><b>End anchor:</b> {@code $} — extension must be at the very end of the filename</li>
 *   <li><b>Non-capturing group:</b> {@code (?:...)} — scope the alternation without a capture slot</li>
 *   <li><b>Case-insensitive flag:</b> {@code Pattern.CASE_INSENSITIVE} — handles "SCRIPT.PY"</li>
 * </ul>
 *
 * <h2>Pattern</h2>
 * <pre>
 *   \.(java|py|js|ts|go)$
 * </pre>
 * Java string: {@code "\\.(?:java|py|js|ts|go)$"} with {@code Pattern.CASE_INSENSITIVE}
 *
 * <h2>Why "file.javascript" returns false</h2>
 * <p>The {@code $} anchor requires end-of-string immediately after the extension.
 * After matching {@code js} in {@code .javascript}, the remaining {@code avascript}
 * prevents the {@code $} from matching.</p>
 *
 * <h2>TODOs</h2>
 * <ol>
 *   <li>Declare a {@code private static final Pattern} with the anchored alternation
 *       and {@code CASE_INSENSITIVE} flag.</li>
 *   <li>Return {@code matcher.find()} — the {@code $} anchor ensures the extension
 *       is at the end even with {@code find()}.</li>
 * </ol>
 */
public class Solution {

    /**
     * Returns {@code true} if {@code filename} ends with one of the recognized
     * source file extensions: {@code .java}, {@code .py}, {@code .js}, {@code .ts},
     * or {@code .go}. Comparison is case-insensitive.
     *
     * @param filename the filename to check; never {@code null}
     * @return {@code true} if the filename has a recognized source extension
     * @throws UnsupportedOperationException until the method is implemented
     */
    public boolean isSourceFile(String filename) {
        throw new UnsupportedOperationException("TODO");
    }
}
