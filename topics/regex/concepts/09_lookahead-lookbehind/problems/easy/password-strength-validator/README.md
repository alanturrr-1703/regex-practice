# Password Strength Validator

**Difficulty**: Easy
**Concept**: Positive Lookahead `(?=...)` — multiple chained assertions

---

## Problem Statement

Implement `isStrongPassword(String password)` that returns `true` if and only if the
password satisfies **all five** of the following rules simultaneously:

1. At least **8 characters** long
2. Contains at least one **uppercase letter** (`A-Z`)
3. Contains at least one **lowercase letter** (`a-z`)
4. Contains at least one **digit** (`0-9`)
5. Contains at least one **special character** from the set: `!@#$%^&*`

You must express all constraints in **a single regex pattern** using multiple chained
positive lookaheads anchored at `^`.

---

## Constraints

- Input may be `null` — return `false` for null input.
- Input will not contain newline characters.
- The entire string must satisfy the rules — partial matching is not acceptable.
- You may not call `isStrongPassword` recursively or check constraints procedurally —
  a single regex pass is the intent of this exercise.

---

## Input / Output Examples

| Input | Expected | Reason |
|-------|----------|--------|
| `"Secure1!"` | `true` | Meets all 5 criteria |
| `"ValidPass1@"` | `true` | Meets all 5 criteria |
| `"secure1!"` | `false` | No uppercase letter |
| `"SECURE1!"` | `false` | No lowercase letter |
| `"SecurePass!"` | `false` | No digit |
| `"Sec1!"` | `false` | Too short (5 chars) |
| `"Secure123"` | `false` | No special character |
| `""` | `false` | Empty string |
| `null` | `false` | Null input |

---

## Edge Cases

- All constraints satisfied except exactly one — each should individually fail.
- Boundary length: exactly 8 characters (`"Secure1!"` → true, `"Secur1!"` → false).
- Special characters at start, middle, or end of string.
- Password consisting entirely of one character type (e.g., all digits).

---

## Time Complexity

O(N) per call where N is the password length. Each lookahead `(?=.*X)` scans at most
N characters. With 5 lookaheads plus one consumption pass, this is 6 × O(N) = O(N).

---

## Real-World Relevance

Password strength validation appears in every authentication system. The regex approach
allows declarative specification of policy in a single string that can be stored in
configuration or a database (e.g., "password policy for tenant X requires pattern Y").
Chained lookaheads are the standard idiom.

---

## Regex Thinking Process

Step 1: I need to check that `[A-Z]` appears *somewhere* in the string.
        "Somewhere in the string" = `.*` before the character.
        Assertion (zero-width): `(?=.*[A-Z])`

Step 2: Same for lowercase, digit, special char.

Step 3: These checks must ALL be true. Stack them at `^`:
        `^(?=.*[A-Z])(?=.*[a-z])(?=.*\d)(?=.*[!@#$%^&*])`
        Each one rewinds to `^` and independently scans the whole string.

Step 4: The length check. `.{8,}` consumes 8+ characters from `^` to `$`:
        `^(?=.*[A-Z])(?=.*[a-z])(?=.*\d)(?=.*[!@#$%^&*]).{8,}$`

---

## Common Mistakes

- Forgetting the `$` anchor — pattern matches a prefix of the string rather than the whole thing.
- Forgetting the `.*` before the character class in the lookahead — `(?=[A-Z])` only checks
  the FIRST character.
- Escaping `$` inside a character class: `[$%]` is fine; `\$` is also fine.
- Omitting the `^` anchor — lookaheads float to every position.

---

## Debugging Advice

Test each lookahead in isolation first:
```/dev/null/debug.txt#L1-5
^(?=.*[A-Z]).{8,}$   -- passes only if uppercase present AND 8+ chars
^(?=.*[a-z]).{8,}$   -- etc.
```
Then stack them one by one until you find which constraint is failing for a given input.
