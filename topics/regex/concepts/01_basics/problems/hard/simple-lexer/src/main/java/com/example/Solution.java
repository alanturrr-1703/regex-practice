package com.example;

import java.util.List;
import java.util.ArrayList;
import java.util.Objects;
import java.util.regex.Pattern;
import java.util.regex.Matcher;

/**
 * Problem: Implement a simple arithmetic expression lexer (tokenizer).
 *
 * <p>Given an arithmetic expression string, produce a list of {@link Token} objects.
 * Whitespace tokens are consumed but NOT included in the output list.
 * Any unrecognized character should cause an {@link IllegalArgumentException}.
 *
 * <p>Token types (in priority order):
 * <ul>
 *   <li>{@code NUMBER} — one or more digits ({@code \d+})</li>
 *   <li>{@code IDENTIFIER} — letter followed by letters/digits/underscores
 *       ({@code [a-zA-Z][a-zA-Z0-9_]*})</li>
 *   <li>{@code OPERATOR} — one of {@code +}, {@code -}, {@code *}, {@code /}</li>
 *   <li>{@code LPAREN} — literal {@code (}</li>
 *   <li>{@code RPAREN} — literal {@code )}</li>
 *   <li>WHITESPACE — consumed but not returned</li>
 * </ul>
 *
 * <p>Key concepts:
 * <ul>
 *   <li>Combined alternation pattern: {@code (A)|(B)|(C)|...} with capturing groups</li>
 *   <li>Detecting which group matched: {@code m.group(n) != null}</li>
 *   <li>Gap detection: {@code m.start() != lastEnd} indicates an invalid character</li>
 *   <li>Position tracking: {@code m.start()} and {@code m.end()}</li>
 * </ul>
 *
 * <p><b>TODO:</b>
 * <ol>
 *   <li>Build a combined {@code Pattern} with one capturing group per token type.</li>
 *   <li>Loop with {@code Matcher.find()} and check which group matched.</li>
 *   <li>Track position ({@code pos}) and detect gaps to throw on invalid chars.</li>
 *   <li>Skip WHITESPACE tokens; add all others to the result list.</li>
 * </ol>
 *
 * <p>Example:
 * <pre>
 *   tokenize("1 + 2")         → [NUMBER("1"), OPERATOR("+"), NUMBER("2")]
 *   tokenize("(x)")           → [LPAREN("("), IDENTIFIER("x"), RPAREN(")")]
 *   tokenize("100 * y_1")     → [NUMBER("100"), OPERATOR("*"), IDENTIFIER("y_1")]
 * </pre>
 */
public class Solution {

    // -------------------------------------------------------------------------
    // Token infrastructure — DO NOT MODIFY
    // -------------------------------------------------------------------------

    /** The type of a lexer token. */
    public enum TokenType {
        NUMBER, IDENTIFIER, OPERATOR, LPAREN, RPAREN
    }

    /**
     * Represents a single token produced by the lexer.
     * Immutable value object. Equality is based on both {@code type} and {@code value}.
     */
    public static class Token {
        /** The syntactic category of this token. */
        public final TokenType type;
        /** The exact text from the input that this token represents. */
        public final String value;

        /**
         * Creates a new Token.
         *
         * @param type  the token type (non-null)
         * @param value the matched text (non-null)
         */
        public Token(TokenType type, String value) {
            this.type = Objects.requireNonNull(type, "type must not be null");
            this.value = Objects.requireNonNull(value, "value must not be null");
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (!(o instanceof Token)) return false;
            Token that = (Token) o;
            return type == that.type && value.equals(that.value);
        }

        @Override
        public int hashCode() {
            return Objects.hash(type, value);
        }

        @Override
        public String toString() {
            return type + "(\"" + value + "\")";
        }
    }

    // -------------------------------------------------------------------------
    // TODO: implement below
    // -------------------------------------------------------------------------

    /**
     * Tokenizes the given arithmetic expression into a list of {@link Token} objects.
     *
     * <p>Whitespace is consumed but not returned. Invalid characters cause
     * {@link IllegalArgumentException} with a message indicating the position and character.
     *
     * @param expression a non-null string representing an arithmetic expression
     * @return a list of tokens in left-to-right order; never null; may be empty
     * @throws IllegalArgumentException      if the expression contains unrecognized characters
     * @throws UnsupportedOperationException until this method is implemented
     */
    public List<Token> tokenize(String expression) {
        throw new UnsupportedOperationException("TODO: implement tokenize");
    }
}
