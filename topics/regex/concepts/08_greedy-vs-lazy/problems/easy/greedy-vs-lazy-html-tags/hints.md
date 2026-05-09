# Hints

## Hint 1 — Try the Greedy Version First
Before fixing anything, try the greedy version: `<b>(.*)</b>`. Test it on `"<b>one</b> and <b>two</b>"`. How many matches does it return? What is the content? This reveals the exact bug you need to fix.

## Hint 2 — The Fix Is One Character
The difference between the broken (greedy) and correct (lazy) pattern is exactly one character. The lazy quantifier is `.*?` — a `?` appended to the `*`. This one character changes the entire matching strategy.

## Hint 3 — Why Lazy Works Here
Lazy `.*?` tries to match as FEW characters as possible. When it encounters the closing `</b>`, it stops immediately. Greedy `.*` tries to match as MANY characters as possible, then backtracks — it ends up finding the LAST `</b>`, not the first.

## Hint 4 — Capture Group
The content you want is what's between the tags. Put `.*?` inside a capturing group `(.*?)`. Then use `matcher.group(1)` — not `matcher.group(0)` which returns the entire match including tags.

## Hint 5 — Loop Structure
```java
Pattern p = Pattern.compile("<b>(.*?)</b>");
Matcher m = p.matcher(html);
List<String> results = new ArrayList<>();
while (m.find()) {
    results.add(m.group(1));  // group 1 = content between tags
}
```
Implement this loop yourself. Understand each line before writing it.
