# Hints — Replace With Custom Logic

Work through these hints in order.

---

## Hint 1

`matcher.replaceAll("someFixedString")` cannot help here — the replacement is different for each match (it's the doubled value). You need to run Java code for each match to compute the replacement.

The API that enables per-match custom logic is `appendReplacement(StringBuffer, String)` combined with `appendTail(StringBuffer)`.

---

## Hint 2

The `appendReplacement` loop structure:

```java
StringBuffer sb = new StringBuffer();
while (m.find()) {
    // compute replacementString here
    m.appendReplacement(sb, replacementString);
}
m.appendTail(sb);
return sb.toString();
```

`appendReplacement` automatically handles the text between matches (the "gaps" — `" cats and "` in `"I have 3 cats and 10 dogs"`). You don't need to track positions manually.

---

## Hint 3

Inside the loop, you have access to `m.group()` which gives you the current matched number as a String. To double it:
1. Parse it: `long value = Long.parseLong(m.group())`
2. Double it: `long doubled = value * 2`
3. Convert back to string: `String.valueOf(doubled)`

Use `Long.parseLong` (not `Integer.parseInt`) to safely handle large numbers.

---

## Hint 4

What happens when there are no numbers? After zero `find()` calls, `appendTail(sb)` appends the entire original input unchanged. So `"no numbers"` → `"no numbers"` is handled automatically — no special case needed.

---

## Hint 5 (Reveal)

```java
Pattern p = Pattern.compile("\\d+");
Matcher m = p.matcher(input);
StringBuffer sb = new StringBuffer();
while (m.find()) {
    long value = Long.parseLong(m.group());
    m.appendReplacement(sb, String.valueOf(value * 2));
}
m.appendTail(sb);
return sb.toString();
```

The pattern `\\d+` matches one or more consecutive digits. Each match is extracted, doubled, and replaced. The `appendTail` call ensures any trailing non-digit text is included in the output.
