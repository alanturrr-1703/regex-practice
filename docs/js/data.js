// All 60 problem definitions for the Java Regex Practice sandbox.
// returnType: 'boolean' | 'int' | 'String' | 'List' | 'Optional' | 'Map' | 'ListList'
// For List expected values, items are joined with |
// For ListList expected values, rows joined with ; and cols with ,

const PROBLEMS = {

  // ──────────────────────────────────────────────────────────────────────────
  '01_basics': {
    label: '01 · Basics',
    description: 'Literal matching · Pattern vs Matcher · find() vs matches()',
    color: '#58a6ff',
    problems: [
      {
        id: 'detect-digit', difficulty: 'easy', title: 'Detect Digit',
        description: 'Return true if the string contains at least one digit character (0-9).\n\nKey trap: use Matcher.find() — not String.matches(), which requires the ENTIRE string to match.\n\nJava double-escaping: write "\\\\d" in source to get \\d in the regex engine.',
        returnType: 'boolean', method: 'containsDigit',
        starterCode: `public boolean containsDigit(String input) {
    // TODO: Pattern.compile("\\\\d") then matcher.find()
    throw new UnsupportedOperationException("TODO");
}`,
        tests: [
          { name: 'has digit',    args: '"hello123"', expected: 'true'  },
          { name: 'no digit',     args: '"hello"',    expected: 'false' },
          { name: 'empty',        args: '""',         expected: 'false' },
          { name: 'digit spaces', args: '"   9   "',  expected: 'true'  },
          { name: 'special only', args: '"!@#"',      expected: 'false' },
        ],
      },
      {
        id: 'match-literal-string', difficulty: 'easy', title: 'Match Literal String',
        description: 'Given a list of strings, return only those that contain the exact substring "ERROR" (case-sensitive).\n\nThis teaches: regex matches literals too. Use Matcher.find() for substring search.',
        returnType: 'List', method: 'filterErrors',
        starterCode: `public List<String> filterErrors(List<String> lines) {
    // TODO: Pattern.compile("ERROR"), then filter with matcher.find()
    throw new UnsupportedOperationException("TODO");
}`,
        tests: [
          { name: 'two errors',     args: 'Arrays.asList("ERROR: disk full","ok","CRITICAL ERROR")', expected: 'ERROR: disk full|CRITICAL ERROR' },
          { name: 'no errors',      args: 'Arrays.asList("info","debug")',                           expected: ''       },
          { name: 'case mismatch',  args: 'Arrays.asList("error: low","ERROR high")',                expected: 'ERROR high' },
          { name: 'empty list',     args: 'new ArrayList<>()',                                        expected: ''       },
        ],
      },
      {
        id: 'extract-words', difficulty: 'medium', title: 'Extract Words',
        description: 'Extract all sequences of purely alphabetic characters [a-zA-Z]+ from a string. Digits and underscores are NOT part of a word here.\n\nKey: use [a-zA-Z]+ not \\\\w+ (which includes digits and underscore).',
        returnType: 'List', method: 'extractWords',
        starterCode: `public List<String> extractWords(String input) {
    // TODO: Pattern("[a-zA-Z]+") + Matcher.find() loop
    throw new UnsupportedOperationException("TODO");
}`,
        tests: [
          { name: 'basic',        args: '"Hello, world! 123"', expected: 'Hello|world'     },
          { name: 'underscore',   args: '"foo_bar"',           expected: 'foo|bar'          },
          { name: 'only digits',  args: '"123 456"',           expected: ''                 },
          { name: 'mixed',        args: '"Java9Rocks"',        expected: 'Java|Rocks'       },
          { name: 'empty',        args: '""',                  expected: ''                 },
        ],
      },
      {
        id: 'validate-simple-email', difficulty: 'medium', title: 'Validate Simple Email',
        description: 'Return true if the string looks like a basic email: one or more non-@ chars, then @, then one or more non-@ chars, then a dot, then 2-6 letters.\n\nUse String.matches() — it validates the WHOLE string.',
        returnType: 'boolean', method: 'isValidEmail',
        starterCode: `public boolean isValidEmail(String email) {
    // TODO: pattern like [^@]+@[^@]+\\\\.[a-zA-Z]{2,6}
    throw new UnsupportedOperationException("TODO");
}`,
        tests: [
          { name: 'valid',        args: '"user@example.com"',   expected: 'true'  },
          { name: 'no @',         args: '"plainaddress"',       expected: 'false' },
          { name: 'no domain',    args: '"bad@"',               expected: 'false' },
          { name: 'short TLD',    args: '"x@y.c"',              expected: 'false' },
          { name: 'long TLD ok',  args: '"a@b.museum"',         expected: 'true'  },
        ],
      },
      {
        id: 'simple-lexer', difficulty: 'hard', title: 'Simple Lexer',
        description: 'Tokenize an arithmetic expression. Return tokens as TYPE:VALUE joined by |.\n\nToken types: NUMBER (\\\\d+), IDENTIFIER ([a-zA-Z][a-zA-Z0-9_]*), OPERATOR ([+\\\\-*/]), LPAREN, RPAREN. Skip whitespace.\n\nExample: "1 + x" → "NUMBER:1|OPERATOR:+|IDENTIFIER:x"',
        returnType: 'String', method: 'tokenize',
        starterCode: `public String tokenize(String expression) {
    // TODO: use alternation pattern to match token types in order
    // Collect TYPE:VALUE strings, join with |
    throw new UnsupportedOperationException("TODO");
}`,
        tests: [
          { name: 'basic',     args: '"1 + 2"',      expected: 'NUMBER:1|OPERATOR:+|NUMBER:2'                        },
          { name: 'parens',    args: '"(x)"',         expected: 'LPAREN:(|IDENTIFIER:x|RPAREN:)'                     },
          { name: 'complex',   args: '"a + b_1"',     expected: 'IDENTIFIER:a|OPERATOR:+|IDENTIFIER:b_1'             },
          { name: 'empty',     args: '""',            expected: ''                                                    },
        ],
      },
    ],
  },

  // ──────────────────────────────────────────────────────────────────────────
  '02_character-classes': {
    label: '02 · Character Classes',
    description: '[abc] · [^abc] · ranges · \\d \\w \\s · Unicode \\p{L}',
    color: '#3fb950',
    problems: [
      {
        id: 'match-vowels', difficulty: 'easy', title: 'Count Vowels',
        description: 'Count the number of vowels (a, e, i, o, u) in the string, case-insensitively.\n\nUse a character class [aeiouAEIOU] or [aeiou] with Pattern.CASE_INSENSITIVE.',
        returnType: 'int', method: 'countVowels',
        starterCode: `public int countVowels(String input) {
    // TODO: Pattern.compile("[aeiou]", Pattern.CASE_INSENSITIVE)
    throw new UnsupportedOperationException("TODO");
}`,
        tests: [
          { name: 'basic',      args: '"Hello World"', expected: '3' },
          { name: 'empty',      args: '""',            expected: '0' },
          { name: 'all vowels', args: '"AEIOU"',       expected: '5' },
          { name: 'no vowels',  args: '"rhythm"',      expected: '0' },
          { name: 'mixed',      args: '"Beautiful"',   expected: '5' },
        ],
      },
      {
        id: 'validate-alphanumeric', difficulty: 'easy', title: 'Validate Alphanumeric',
        description: 'Return true if the string contains ONLY alphanumeric characters (letters and digits), with length >= 1.\n\nKey: use matches() for full-string validation.',
        returnType: 'boolean', method: 'isAlphanumeric',
        starterCode: `public boolean isAlphanumeric(String input) {
    // TODO: [a-zA-Z0-9]+ with String.matches()
    throw new UnsupportedOperationException("TODO");
}`,
        tests: [
          { name: 'letters+digits', args: '"Hello123"', expected: 'true'  },
          { name: 'has space',      args: '"Hello 123"',expected: 'false' },
          { name: 'empty',          args: '""',          expected: 'false' },
          { name: 'special char',   args: '"abc!"',      expected: 'false' },
          { name: 'single char',    args: '"Z"',         expected: 'true'  },
        ],
      },
      {
        id: 'extract-hex-colors', difficulty: 'medium', title: 'Extract Hex Colors',
        description: 'Extract all CSS hex color codes from a string. Format: # followed by exactly 6 hexadecimal digits (0-9, a-f, A-F).\n\nDo NOT match 3-digit hex codes. Return the full token including #.',
        returnType: 'List', method: 'extractHexColors',
        starterCode: `public List<String> extractHexColors(String input) {
    // TODO: #[0-9a-fA-F]{6} with word boundary or lookahead to avoid 7+ chars
    throw new UnsupportedOperationException("TODO");
}`,
        tests: [
          { name: 'two colors',  args: '"color: #ff0000 and #ABCDEF"', expected: '#ff0000|#ABCDEF'  },
          { name: 'no colors',   args: '"no colors here"',             expected: ''                 },
          { name: 'invalid hex', args: '"#xyz123"',                    expected: ''                 },
          { name: 'in css',      args: '"background:#001122;"',        expected: '#001122'          },
        ],
      },
      {
        id: 'custom-character-range', difficulty: 'medium', title: 'Custom Character Range',
        description: 'Extract tokens consisting ONLY of lowercase letters a-m and digits 0-4. Tokens must be at least 1 character.\n\nAny character outside [a-m0-4] ends a token.',
        returnType: 'List', method: 'extractRestrictedTokens',
        starterCode: `public List<String> extractRestrictedTokens(String input) {
    // TODO: [a-m0-4]+ to match runs of allowed chars
    throw new UnsupportedOperationException("TODO");
}`,
        tests: [
          { name: 'mixed',       args: '"abc123xyz567"', expected: 'abc123'  },
          { name: 'partial',     args: '"hello"',        expected: 'hell'    },
          { name: 'none',        args: '"xyz999"',       expected: ''        },
          { name: 'all allowed', args: '"aaa444"',       expected: 'aaa444'  },
        ],
      },
      {
        id: 'unicode-identifier', difficulty: 'hard', title: 'Unicode Identifier',
        description: 'Extract all identifiers that may contain Unicode letters (\\\\p{L}) and Unicode digits (\\\\p{N}). An identifier starts with a Unicode letter or underscore, followed by more letters, digits, or underscores.\n\nUse \\\\p{L} for Unicode letter category.',
        returnType: 'List', method: 'extractIdentifiers',
        starterCode: `public List<String> extractIdentifiers(String input) {
    // TODO: [\\\\p{L}_][\\\\p{L}\\\\p{N}_]* with Pattern.UNICODE_CHARACTER_CLASS
    throw new UnsupportedOperationException("TODO");
}`,
        tests: [
          { name: 'ascii',    args: '"hello world"',    expected: 'hello|world'   },
          { name: 'unicode',  args: '"café wörld"',     expected: 'café|wörld'    },
          { name: 'japanese', args: '"日本語 english"',   expected: '日本語|english' },
          { name: 'digits start', args: '"123bad x1"', expected: 'x1'            },
        ],
      },
    ],
  },

  // ──────────────────────────────────────────────────────────────────────────
  '03_quantifiers': {
    label: '03 · Quantifiers',
    description: '* + ? {n,m} · greedy defaults · backtracking intro',
    color: '#d29922',
    problems: [
      {
        id: 'validate-digit-count', difficulty: 'easy', title: 'Exactly Five Digits',
        description: 'Return true if the string contains a run of EXACTLY 5 consecutive digits (not 4, not 6+).\n\nKey: \\\\d{5} alone matches any 5-digit substring inside a 6-digit run. You need negative lookbehind/lookahead to enforce exact boundary.',
        returnType: 'boolean', method: 'hasExactlyFiveDigits',
        starterCode: `public boolean hasExactlyFiveDigits(String input) {
    // TODO: (?<!\\\\d)\\\\d{5}(?!\\\\d)
    throw new UnsupportedOperationException("TODO");
}`,
        tests: [
          { name: '5 digits',  args: '"abc12345def"', expected: 'true'  },
          { name: '6 digits',  args: '"abc123456def"',expected: 'false' },
          { name: 'exact',     args: '"12345"',        expected: 'true'  },
          { name: '4 digits',  args: '"1234"',         expected: 'false' },
          { name: 'in zip',    args: '"zip 12345 ok"', expected: 'true'  },
        ],
      },
      {
        id: 'collapse-whitespace', difficulty: 'easy', title: 'Collapse Whitespace',
        description: 'Collapse any run of one or more whitespace characters into a single space, then trim leading/trailing whitespace.\n\nUse \\\\s+ with replaceAll, then trim().',
        returnType: 'String', method: 'collapseWhitespace',
        starterCode: `public String collapseWhitespace(String input) {
    // TODO: input.replaceAll("\\\\s+", " ").trim()
    throw new UnsupportedOperationException("TODO");
}`,
        tests: [
          { name: 'multi spaces',  args: '"hello   world"',  expected: 'hello world' },
          { name: 'leading/trail', args: '"  spaces  "',     expected: 'spaces'      },
          { name: 'tab',           args: '"a\\t\\tb"',       expected: 'a b'         },
          { name: 'empty',         args: '""',               expected: ''            },
          { name: 'no change',     args: '"no change"',      expected: 'no change'   },
        ],
      },
      {
        id: 'extract-version-numbers', difficulty: 'medium', title: 'Extract Version Numbers',
        description: 'Extract semantic version strings (major.minor.patch) from text. Each group is 1-3 digits. Must NOT be followed by a dot and more digits (so 1.2.3.4 is excluded).',
        returnType: 'List', method: 'extractVersions',
        starterCode: `public List<String> extractVersions(String input) {
    // TODO: \\\\d{1,3}\\\\.\\\\d{1,3}\\\\.\\\\d{1,3} with boundary checks
    throw new UnsupportedOperationException("TODO");
}`,
        tests: [
          { name: 'two versions', args: '"ver 1.2.3 and 10.0.5"',  expected: '1.2.3|10.0.5' },
          { name: 'with prefix',  args: '"v2.0.0-beta"',            expected: '2.0.0'        },
          { name: 'none',         args: '"no version"',             expected: ''             },
          { name: 'four groups',  args: '"1.2.3.4"',               expected: ''             },
        ],
      },
      {
        id: 'parse-repeated-tokens', difficulty: 'medium', title: 'Parse Repeated Tokens',
        description: 'Extract all uppercase-letter-only fields (1-8 chars) from a pipe-separated string. Ignore lowercase fields and fields longer than 8 chars.',
        returnType: 'List', method: 'parseFields',
        starterCode: `public List<String> parseFields(String input) {
    // TODO: (?<![A-Z])[A-Z]{1,8}(?![A-Z]) or similar boundary approach
    throw new UnsupportedOperationException("TODO");
}`,
        tests: [
          { name: 'basic',     args: '"AAA|BBB|CCC"',        expected: 'AAA|BBB|CCC' },
          { name: 'pipes',     args: '"|HELLO|"',             expected: 'HELLO'       },
          { name: 'too long',  args: '"TOOLONGFIELD|OK"',     expected: 'OK'          },
          { name: 'lowercase', args: '"aaa|BBB"',             expected: 'BBB'         },
          { name: 'empty',     args: '""',                    expected: ''            },
        ],
      },
      {
        id: 'catastrophic-backtracking-debug', difficulty: 'hard', title: 'Fix Catastrophic Backtracking',
        description: 'Implement matchesPattern(input) — returns true if the string is one or more \'a\' characters followed by \'b\'.\n\nThe BROKEN pattern (a+)+b causes exponential backtracking on inputs like "aaaaaac". Fix it to run in O(n) time.',
        returnType: 'boolean', method: 'matchesPattern',
        starterCode: `public boolean matchesPattern(String input) {
    // BROKEN — DO NOT USE: Pattern.compile("(a+)+b")
    // This hangs on "aaaaaaaaaaaaaaac" (exponential backtracking)
    //
    // TODO: Rewrite as a simple linear pattern: a+b
    throw new UnsupportedOperationException("TODO");
}`,
        tests: [
          { name: 'one a',     args: '"ab"',                  expected: 'true'  },
          { name: 'many a',    args: '"aaab"',                expected: 'true'  },
          { name: 'just b',    args: '"b"',                   expected: 'false' },
          { name: 'no b',      args: '"aaa"',                 expected: 'false' },
          { name: 'adversarial',args: '"aaaaaaaaaaaaaaaaac"', expected: 'false' },
        ],
      },
    ],
  },

  // ──────────────────────────────────────────────────────────────────────────
  '04_anchors': {
    label: '04 · Anchors',
    description: '^ $ \\b \\A \\Z · MULTILINE mode',
    color: '#f78166',
    problems: [
      {
        id: 'validate-starts-with-http', difficulty: 'easy', title: 'Starts With HTTP',
        description: 'Return true if the string starts with "http://" or "https://".\n\nUse ^ anchor. Note: ^ without MULTILINE only matches the start of the entire string.',
        returnType: 'boolean', method: 'startsWithHttp',
        starterCode: `public boolean startsWithHttp(String input) {
    // TODO: ^https?:// with matcher.find() or matches()
    throw new UnsupportedOperationException("TODO");
}`,
        tests: [
          { name: 'http',        args: '"http://example.com"',  expected: 'true'  },
          { name: 'https',       args: '"https://example.com"', expected: 'true'  },
          { name: 'ftp',         args: '"ftp://example.com"',   expected: 'false' },
          { name: 'leading space',args: '" http://x.com"',      expected: 'false' },
          { name: 'empty',       args: '""',                    expected: 'false' },
        ],
      },
      {
        id: 'validate-ends-with-semicolon', difficulty: 'easy', title: 'Ends With Semicolon',
        description: 'Return true if the string ends with a semicolon, optionally followed by whitespace.\n\nUse ;\\\\s*$ anchor.',
        returnType: 'boolean', method: 'endsWithSemicolon',
        starterCode: `public boolean endsWithSemicolon(String input) {
    // TODO: ;\\\\s*$  — the $ anchors to end of string
    throw new UnsupportedOperationException("TODO");
}`,
        tests: [
          { name: 'exact',          args: '"int x = 5;"',     expected: 'true'  },
          { name: 'trailing space', args: '"int x = 5;  "',   expected: 'true'  },
          { name: 'no semicolon',   args: '"int x = 5"',      expected: 'false' },
          { name: 'empty',          args: '""',               expected: 'false' },
          { name: 'just semi',      args: '";"',              expected: 'true'  },
        ],
      },
      {
        id: 'multiline-section-parser', difficulty: 'medium', title: 'Multiline Section Parser',
        description: 'Extract lines that START with ">>" and return the text after ">>" (trimmed).\n\nKey trap: without Pattern.MULTILINE, ^ only matches the start of the entire string.',
        returnType: 'List', method: 'extractSections',
        starterCode: `public List<String> extractSections(String text) {
    // TODO: Pattern.compile("^>>(.+)$", Pattern.MULTILINE)
    // group(1).trim() for the content
    throw new UnsupportedOperationException("TODO");
}`,
        tests: [
          { name: 'two sections', args: '">> title\\ncontent\\n>> another"', expected: 'title|another' },
          { name: 'none',         args: '"no sections\\nhere"',              expected: ''              },
          { name: 'one section',  args: '">> only one"',                     expected: 'only one'      },
        ],
      },
      {
        id: 'word-boundary-extractor', difficulty: 'medium', title: 'Word Boundary Count',
        description: 'Count how many times the word "log" appears as a STANDALONE word (not as part of "logger", "catalog", "dialog").\n\nUse \\\\blog\\\\b word boundaries.',
        returnType: 'int', method: 'countWordLog',
        starterCode: `public int countWordLog(String input) {
    // TODO: \\\\blog\\\\b — counts the exact word "log" only
    throw new UnsupportedOperationException("TODO");
}`,
        tests: [
          { name: 'one',      args: '"log the event"',        expected: '1' },
          { name: 'two',      args: '"log and log again"',    expected: '2' },
          { name: 'none',     args: '"catalog logger"',       expected: '0' },
          { name: 'punct',    args: '"log."',                 expected: '1' },
          { name: 'empty',    args: '""',                     expected: '0' },
        ],
      },
      {
        id: 'log-line-anchor-parser', difficulty: 'hard', title: 'Log Line Anchor Parser',
        description: 'Given a multiline log string, extract and return the FIRST matched log level (DEBUG/INFO/WARN/ERROR). Lines follow the format: [LEVEL] YYYY-MM-DD HH:MM:SS - message.\n\nReturn "NONE" if no valid line is found.',
        returnType: 'String', method: 'parseFirstLevel',
        starterCode: `public String parseFirstLevel(String logText) {
    // TODO: Pattern.compile("^\\\\[(DEBUG|INFO|WARN|ERROR)\\\\].*$", Pattern.MULTILINE)
    // return group(1) of first match, or "NONE"
    throw new UnsupportedOperationException("TODO");
}`,
        tests: [
          { name: 'ERROR first', args: '"[ERROR] 2024-01-01 10:00:00 - msg"', expected: 'ERROR' },
          { name: 'INFO first',  args: '"[INFO] 2024-01-01 10:00:00 - ok"',   expected: 'INFO'  },
          { name: 'none',        args: '"no log lines here"',                  expected: 'NONE'  },
          { name: 'multiline',   args: '"other\\n[WARN] 2024-01-01 10:00:00 - low"', expected: 'WARN' },
        ],
      },
    ],
  },

  // ──────────────────────────────────────────────────────────────────────────
  '05_groups-and-capturing': {
    label: '05 · Groups & Capturing',
    description: '() (?:) · named groups · backreferences',
    color: '#bc8cff',
    problems: [
      {
        id: 'extract-date-parts', difficulty: 'easy', title: 'Extract Date Parts',
        description: 'Find all YYYY-MM-DD dates in a string and return them as "YYYY/MM/DD" (reformatted with slashes).\n\nUse three capture groups: (\\\\d{4})-(\\\\d{2})-(\\\\d{2}) and rebuild with group(1)+"/"+group(2)+"/"+group(3).',
        returnType: 'List', method: 'extractDates',
        starterCode: `public List<String> extractDates(String input) {
    // TODO: (\\\\d{4})-(\\\\d{2})-(\\\\d{2})
    // rebuild as year/month/day using groups 1, 2, 3
    throw new UnsupportedOperationException("TODO");
}`,
        tests: [
          { name: 'two dates', args: '"Event 2024-01-15 and 2023-12-31"', expected: '2024/01/15|2023/12/31' },
          { name: 'none',      args: '"no dates"',                         expected: ''                     },
          { name: 'one',       args: '"Date: 1999-06-01."',               expected: '1999/06/01'           },
        ],
      },
      {
        id: 'capture-first-word', difficulty: 'easy', title: 'Capture First Word',
        description: 'Return the first \\\\w+ token in the string as an Optional<String>. Return Optional.empty() if the string has no word characters.\n\nNote: group(0) = entire match. group(1) = first capture group.',
        returnType: 'Optional', method: 'captureFirstWord',
        starterCode: `public Optional<String> captureFirstWord(String input) {
    // TODO: Pattern("(\\\\w+)"), find(), return Optional.of(group(1)) or Optional.empty()
    throw new UnsupportedOperationException("TODO");
}`,
        tests: [
          { name: 'normal',   args: '"hello world"',  expected: 'hello'    },
          { name: 'spaces',   args: '"  spaces"',     expected: 'spaces'   },
          { name: 'empty',    args: '""',             expected: 'EMPTY'    },
          { name: 'no word',  args: '"!@#$"',         expected: 'EMPTY'    },
          { name: 'digit',    args: '"123 num"',      expected: '123'      },
        ],
      },
      {
        id: 'named-groups-log-parser', difficulty: 'medium', title: 'Named Groups: Log Parser',
        description: 'Parse an Apache access log line and extract just the IP address using a named capturing group (?<ip>...).\n\nFormat: IP - - [DATE] "METHOD /path HTTP/1.1" STATUS BYTES\n\nReturn "NONE" if line does not match.',
        returnType: 'String', method: 'parseIp',
        starterCode: `public String parseIp(String line) {
    // TODO: (?<ip>\\\\S+) at the start, then use matcher.group("ip")
    throw new UnsupportedOperationException("TODO");
}`,
        tests: [
          { name: 'valid line', args: '"127.0.0.1 - - [10/Oct/2024:13:55:36 +0000] \\"GET /index.html HTTP/1.1\\" 200 1234"', expected: '127.0.0.1' },
          { name: 'another ip', args: '"10.0.0.1 - - [01/Jan/2024:00:00:00 +0000] \\"POST /api HTTP/1.1\\" 201 100"',          expected: '10.0.0.1'  },
          { name: 'no match',   args: '"invalid log line"',                                                                    expected: 'NONE'      },
        ],
      },
      {
        id: 'backreference-duplicate-word', difficulty: 'medium', title: 'Duplicate Word Finder',
        description: 'Find consecutive duplicate words (e.g., "the the") using a backreference \\\\1. Return each duplicated word once (case-insensitive).',
        returnType: 'List', method: 'findDuplicateWords',
        starterCode: `public List<String> findDuplicateWords(String input) {
    // TODO: \\\\b(\\\\w+)\\\\s+\\\\1\\\\b with CASE_INSENSITIVE
    throw new UnsupportedOperationException("TODO");
}`,
        tests: [
          { name: 'one dup',   args: '"the the cat sat"',  expected: 'the'      },
          { name: 'two dups',  args: '"one one two two"',  expected: 'one|two'  },
          { name: 'none',      args: '"no duplicates"',    expected: ''         },
          { name: 'case',      args: '"Hello hello world"',expected: 'Hello'    },
        ],
      },
      {
        id: 'nested-groups-csv-parser', difficulty: 'hard', title: 'CSV Line Parser',
        description: 'Parse a single CSV line where fields may be double-quoted. Quoted fields may contain commas. Return field values without surrounding quotes.\n\nExample: one,"two,comma",three → ["one","two,comma","three"]',
        returnType: 'List', method: 'parseCsvLine',
        starterCode: `public List<String> parseCsvLine(String line) {
    // TODO: alternate between "([^"]*)" and ([^,]*)
    // Use matcher.group(1) != null to detect quoted case
    throw new UnsupportedOperationException("TODO");
}`,
        tests: [
          { name: 'basic',    args: '"a,b,c"',                    expected: 'a|b|c'                  },
          { name: 'quoted',   args: '"one,\\"two,comma\\",three"', expected: 'one|two,comma|three'    },
          { name: 'empty mid',args: '"a,,b"',                     expected: 'a||b'                   },
          { name: 'all quoted',args: '"\\"x\\",\\"y\\""',         expected: 'x|y'                    },
        ],
      },
    ],
  },

  // ──────────────────────────────────────────────────────────────────────────
  '06_alternation': {
    label: '06 · Alternation',
    description: '| · ordering matters · NFA branching',
    color: '#f0883e',
    problems: [
      {
        id: 'match-log-levels', difficulty: 'easy', title: 'Match Log Levels',
        description: 'Return true if the string contains any of DEBUG|INFO|WARN|ERROR|FATAL as a WHOLE word (not as a substring of longer words).\n\nUse \\\\b word boundaries around the alternation group.',
        returnType: 'boolean', method: 'containsLogLevel',
        starterCode: `public boolean containsLogLevel(String input) {
    // TODO: \\\\b(DEBUG|INFO|WARN|ERROR|FATAL)\\\\b
    throw new UnsupportedOperationException("TODO");
}`,
        tests: [
          { name: 'DEBUG',       args: '"DEBUG: starting"',      expected: 'true'  },
          { name: 'substring',   args: '"INFORMATION: long"',    expected: 'false' },
          { name: 'ERROR',       args: '"An ERROR occurred"',    expected: 'true'  },
          { name: 'lowercase',   args: '"debugging"',            expected: 'false' },
          { name: 'empty',       args: '""',                     expected: 'false' },
        ],
      },
      {
        id: 'validate-file-extension', difficulty: 'easy', title: 'Validate File Extension',
        description: 'Return true if the filename ends with .java, .py, .js, .ts, or .go (case-insensitive).\n\nThe dot must be a literal dot (escape it!).',
        returnType: 'boolean', method: 'isSourceFile',
        starterCode: `public boolean isSourceFile(String filename) {
    // TODO: (?i).*\\\\.(java|py|js|ts|go)$
    throw new UnsupportedOperationException("TODO");
}`,
        tests: [
          { name: 'java',     args: '"Main.java"',       expected: 'true'  },
          { name: 'py',       args: '"script.py"',       expected: 'true'  },
          { name: 'uppercase',args: '"SCRIPT.PY"',       expected: 'true'  },
          { name: 'png',      args: '"image.png"',       expected: 'false' },
          { name: 'ts',       args: '"App.ts"',          expected: 'true'  },
        ],
      },
      {
        id: 'multi-format-date-parser', difficulty: 'medium', title: 'Multi-Format Date Parser',
        description: 'Extract dates in ANY of 3 formats from text:\n1. YYYY-MM-DD\n2. MM/DD/YYYY\n3. DD Mon YYYY (Mon = 3-letter abbreviation)\n\nReturn raw matched strings. Order alternation: most specific first.',
        returnType: 'List', method: 'extractDates',
        starterCode: `public List<String> extractDates(String input) {
    // TODO: (?:\\\\d{4}-\\\\d{2}-\\\\d{2}|\\\\d{2}/\\\\d{2}/\\\\d{4}|\\\\d{2} [A-Z][a-z]{2} \\\\d{4})
    throw new UnsupportedOperationException("TODO");
}`,
        tests: [
          { name: 'ISO',       args: '"Born 2024-01-15 here"',  expected: '2024-01-15'      },
          { name: 'US format', args: '"Date: 01/15/2024"',      expected: '01/15/2024'      },
          { name: 'long',      args: '"15 Jan 2024 event"',     expected: '15 Jan 2024'     },
          { name: 'none',      args: '"no dates"',              expected: ''                },
        ],
      },
      {
        id: 'protocol-extractor', difficulty: 'medium', title: 'Protocol Extractor',
        description: 'Given a list of URLs, return just the protocol (http/https/ftp/sftp/ssh) for each recognized URL. Skip unrecognized protocols.\n\nOrdering matters: "https" must come before "http" in alternation or https will match as http.',
        returnType: 'List', method: 'extractProtocols',
        starterCode: `public List<String> extractProtocols(List<String> urls) {
    // TODO: ^(https?|s?ftp|ssh):// — note https before http in alternation
    throw new UnsupportedOperationException("TODO");
}`,
        tests: [
          { name: 'http',    args: 'Arrays.asList("http://example.com")',                    expected: 'http'       },
          { name: 'https',   args: 'Arrays.asList("https://secure.com")',                   expected: 'https'      },
          { name: 'ftp',     args: 'Arrays.asList("ftp://files.net/path")',                 expected: 'ftp'        },
          { name: 'skipped', args: 'Arrays.asList("mailto:user@host","http://x.com")',      expected: 'http'       },
          { name: 'mixed',   args: 'Arrays.asList("ssh://server","https://web.com")',       expected: 'ssh|https'  },
        ],
      },
      {
        id: 'alternation-order-bug', difficulty: 'hard', title: 'Alternation Order Bug',
        description: 'Extract currency amounts ($N.NN or $N, also €) from text. The BROKEN pattern has \\\\d+ before \\\\d+\\\\.\\\\d{2}, so "$10.50" incorrectly matches only "$10".\n\nFix: put the more specific pattern FIRST in alternation.',
        returnType: 'List', method: 'extractAmounts',
        starterCode: `public List<String> extractAmounts(String input) {
    // BROKEN: "[$€](\\\\d+|\\\\d+\\\\.\\\\d{2})"  — \\d+ matches before decimal can
    // FIXED:  "[$€](\\\\d+\\\\.\\\\d{2}|\\\\d+)"  — decimal tried first
    throw new UnsupportedOperationException("TODO");
}`,
        tests: [
          { name: 'decimal',     args: '"$10.50"',             expected: '$10.50'       },
          { name: 'integer',     args: '"$10"',                expected: '$10'          },
          { name: 'both',        args: '"$10.50 and $10"',     expected: '$10.50|$10'   },
          { name: 'euro',        args: '"€20.99 total"',       expected: '€20.99'       },
          { name: 'none',        args: '"no money"',           expected: ''             },
        ],
      },
    ],
  },

  // ──────────────────────────────────────────────────────────────────────────
  '07_escaping': {
    label: '07 · Escaping',
    description: 'Two-layer escaping · \\\\\\\\ in Java · Pattern.quote()',
    color: '#79c0ff',
    problems: [
      {
        id: 'match-literal-dot', difficulty: 'easy', title: 'Match Literal Dot',
        description: 'Filter a list of strings, returning only those that contain a literal dot character.\n\nKey trap: "." in regex matches ANY character. Use "\\\\\\\\." (Java) to match a literal dot.',
        returnType: 'List', method: 'filterContainingDot',
        starterCode: `public List<String> filterContainingDot(List<String> inputs) {
    // TODO: Pattern.compile("\\\\.") — two backslashes in Java source
    // Use matcher.find() not matches()
    throw new UnsupportedOperationException("TODO");
}`,
        tests: [
          { name: 'two dots',  args: 'Arrays.asList("hello.world","helloX","3.14","nodot")', expected: 'hello.world|3.14' },
          { name: 'just dot',  args: 'Arrays.asList(".")',                                    expected: '.'               },
          { name: 'none',      args: 'Arrays.asList("","abc","   ")',                         expected: ''                },
        ],
      },
      {
        id: 'count-special-regex-chars', difficulty: 'easy', title: 'Count Regex Metacharacters',
        description: 'Count occurrences of the 14 regex metacharacters: . * + ? ^ $ { } [ ] | ( ) \\\\\n\nBuild a character class inside [...] that contains all 14, properly escaped.',
        returnType: 'int', method: 'countSpecialChars',
        starterCode: `public int countSpecialChars(String input) {
    // TODO: [\\]\\[.*+?^\${}|()\\\\ ] inside a character class
    // Watch the escaping: \\\\ inside [] needs \\\\\\\\ in Java source
    throw new UnsupportedOperationException("TODO");
}`,
        tests: [
          { name: 'dot+star',    args: '"a.b*c"',  expected: '2' },
          { name: 'none',        args: '"hello"',  expected: '0' },
          { name: 'parens+$',    args: '"$(test)"',expected: '3' },
          { name: 'empty',       args: '""',       expected: '0' },
          { name: 'backslash',   args: '"\\\\"',   expected: '1' },
        ],
      },
      {
        id: 'extract-ip-addresses', difficulty: 'medium', title: 'Extract IP Addresses',
        description: 'Extract IPv4 addresses from text. Format: four 1-3 digit groups separated by LITERAL dots.\n\nKey: escape the dots as \\\\\\\\. in Java. Also exclude 5-group chains like 1.2.3.4.5.',
        returnType: 'List', method: 'extractIpAddresses',
        starterCode: `public List<String> extractIpAddresses(String input) {
    // TODO: (?<!\\\\d)\\\\d{1,3}\\\\.\\\\d{1,3}\\\\.\\\\d{1,3}\\\\.\\\\d{1,3}(?!\\\\.\\\\d)
    throw new UnsupportedOperationException("TODO");
}`,
        tests: [
          { name: 'two IPs',   args: '"server 192.168.1.1 and 10.0.0.1"', expected: '192.168.1.1|10.0.0.1' },
          { name: 'none',      args: '"no ips here"',                      expected: ''                     },
          { name: 'loopback',  args: '"127.0.0.1"',                        expected: '127.0.0.1'            },
          { name: 'five groups',args: '"1.2.3.4.5"',                       expected: ''                     },
        ],
      },
      {
        id: 'java-escape-sequence-validator', difficulty: 'medium', title: 'Java Escape Validator',
        description: 'Given a string (the content of a Java string literal), return true if all backslash sequences are valid Java escapes: \\\\n \\\\t \\\\r \\\\\\\\ \\\\" \\\\\' \\\\0\n\nA lone backslash at end is invalid. Any \\\\x for unknown x is invalid.',
        returnType: 'boolean', method: 'hasOnlyValidEscapes',
        starterCode: `public boolean hasOnlyValidEscapes(String input) {
    // TODO: find any \\\\ NOT followed by [ntr\\\\"'0] → invalid
    // Pattern: \\\\\\\\[^ntr\\\\\\\\"'0]|\\\\\\\\$
    throw new UnsupportedOperationException("TODO");
}`,
        tests: [
          { name: 'no backslash',  args: '"hello"',       expected: 'true'  },
          { name: 'valid \\n',     args: '"line1\\\\nline2"', expected: 'true'  },
          { name: 'invalid \\x',   args: '"bad\\\\xesc"',    expected: 'false' },
          { name: 'lone at end',   args: '"trailing\\\\"',   expected: 'false' },
          { name: 'empty',         args: '""',            expected: 'true'  },
        ],
      },
      {
        id: 'string-literal-extractor', difficulty: 'hard', title: 'String Literal Extractor',
        description: 'Extract content of all double-quoted string literals from Java source code, handling escaped quotes \\\\" inside strings.\n\nThe canonical pattern: \\"((?:[^\\"\\\\\\\\]|\\\\\\\\.)*)\\"  — normal chars OR escape sequences.',
        returnType: 'List', method: 'extractStringLiterals',
        starterCode: `public List<String> extractStringLiterals(String javaCode) {
    // TODO: "((?:[^"\\\\\\\\]|\\\\\\\\.)*)"
    // In Java: "\\"((?:[^\\"\\\\\\\\\\\\\\\\]|\\\\\\\\\\\\\\\\.)*)\\"" — count carefully!
    // Use group(1) for the content
    throw new UnsupportedOperationException("TODO");
}`,
        tests: [
          { name: 'simple',   args: '"String s = \\"hello\\";"',       expected: 'hello'          },
          { name: 'two',      args: '"String a = \\"one\\"; String b = \\"two\\";"', expected: 'one|two' },
          { name: 'empty',    args: '"String s = \\"\\";"',             expected: ''               },
          { name: 'no strings', args: '"int x = 42;"',                 expected: ''               },
        ],
      },
    ],
  },

  // ──────────────────────────────────────────────────────────────────────────
  '08_greedy-vs-lazy': {
    label: '08 · Greedy vs Lazy',
    description: '.* vs .*? vs possessive *+ · negated class [^x]*',
    color: '#56d364',
    problems: [
      {
        id: 'greedy-vs-lazy-html-tags', difficulty: 'easy', title: 'Greedy vs Lazy HTML Tags',
        description: 'Extract the content of each <b>...</b> pair. Greedy .* spans from the first <b> to the LAST </b>. Lazy .*? stops at the FIRST </b>.\n\nReturn one entry per pair.',
        returnType: 'List', method: 'extractBoldContents',
        starterCode: `public List<String> extractBoldContents(String html) {
    // BROKEN: Pattern.compile("<b>(.*)</b>")  — greedy, returns one big match
    // TODO:   Pattern.compile("<b>(.*?)</b>") — lazy, one per pair
    throw new UnsupportedOperationException("TODO");
}`,
        tests: [
          { name: 'one pair',   args: '"<b>hello</b>"',                      expected: 'hello'      },
          { name: 'two pairs',  args: '"<b>one</b> and <b>two</b>"',         expected: 'one|two'    },
          { name: 'none',       args: '"no bold"',                            expected: ''           },
          { name: 'empty bold', args: '"<b></b>"',                            expected: ''           },
          { name: 'three',      args: '"<b>a</b><b>b</b><b>c</b>"',          expected: 'a|b|c'     },
        ],
      },
      {
        id: 'extract-bracketed-values', difficulty: 'easy', title: 'Extract Bracketed Values',
        description: 'Extract content between [...] pairs using lazy matching.\n\nGreedy \\\\[.*\\\\] matches from first [ to LAST ]. Lazy \\\\[.*?\\\\] matches each pair independently.',
        returnType: 'List', method: 'extractBracketedValues',
        starterCode: `public List<String> extractBracketedValues(String input) {
    // BROKEN: Pattern.compile("\\\\[.*\\\\]")  — greedy
    // TODO:   Pattern.compile("\\\\[(.*?)\\\\]") — lazy
    throw new UnsupportedOperationException("TODO");
}`,
        tests: [
          { name: 'two',    args: '"[123][456]"',         expected: '123|456'  },
          { name: 'one',    args: '"[hello]"',            expected: 'hello'    },
          { name: 'none',   args: '"no brackets"',        expected: ''         },
          { name: 'three',  args: '"[a][b][c]"',          expected: 'a|b|c'   },
          { name: 'empty',  args: '"[]"',                 expected: ''         },
        ],
      },
      {
        id: 'extract-quoted-strings', difficulty: 'medium', title: 'Extract Quoted Strings',
        description: 'Extract content of single-quoted strings. Two methods:\n1. extractSimpleQuoted: lazy \'.*?\' (no escaped quotes)\n2. extractRobustQuoted: handles escaped single-quotes \\\'  inside\n\nImplement BOTH. Sandbox tests the simple version.',
        returnType: 'List', method: 'extractSimpleQuoted',
        starterCode: `public List<String> extractSimpleQuoted(String input) {
    // TODO: '(.*?)' — lazy, stops at first closing quote
    throw new UnsupportedOperationException("TODO");
}

public List<String> extractRobustQuoted(String input) {
    // TODO: '((?:[^'\\\\\\\\]|\\\\\\\\.)*)'  — handles \\\\' inside
    throw new UnsupportedOperationException("TODO");
}`,
        tests: [
          { name: 'two strings', args: '"say \'hello\' then \'bye\'"', expected: 'hello|bye' },
          { name: 'none',        args: '"no quotes"',                   expected: ''          },
          { name: 'empty',       args: "''"  ,                          expected: ''          },
          { name: 'one',         args: '"\'only one\'"',                expected: 'only one'  },
        ],
      },
      {
        id: 'multiline-comment-extractor', difficulty: 'medium', title: 'Multiline Comment Extractor',
        description: 'Extract content of /* ... */ block comments, which may span multiple lines.\n\nTwo keys:\n1. Lazy .*? — stops at first */ (not last)\n2. Pattern.DOTALL — allows . to match newlines',
        returnType: 'List', method: 'extractBlockComments',
        starterCode: `public List<String> extractBlockComments(String code) {
    // TODO: Pattern.compile("/\\\\*(.*?)\\\\*/", Pattern.DOTALL)
    // group(1) = raw content including spaces
    throw new UnsupportedOperationException("TODO");
}`,
        tests: [
          { name: 'single',    args: '"code /* comment */ more"',          expected: ' comment '        },
          { name: 'two',       args: '"/* first */ code /* second */"',    expected: ' first | second ' },
          { name: 'multiline', args: '"/* line1\\nline2 */"',              expected: ' line1\\nline2 '  },
          { name: 'none',      args: '"no comments"',                      expected: ''                 },
        ],
      },
      {
        id: 'csv-field-tokenizer', difficulty: 'hard', title: 'CSV Field Tokenizer',
        description: 'Parse multi-line CSV into List<List<String>>. Quoted fields may contain commas.\n\nKey insight: neither greedy nor lazy is right — use a PRECISE character class:\n• Quoted: \\"([^\\"]*)\\"  • Unquoted: ([^,\\\\n]*)',
        returnType: 'ListList', method: 'parseCsv',
        starterCode: `public List<List<String>> parseCsv(String text) {
    // TODO: Pattern.compile("\\"([^\\"]*)\\"|([^,\\\\n]*)")
    // group(1) = quoted content, group(2) = unquoted content
    // Split on \\n for rows first, then scan each row
    throw new UnsupportedOperationException("TODO");
}`,
        tests: [
          { name: 'simple',   args: '"a,b,c"',                          expected: 'a,b,c'              },
          { name: 'quoted',   args: '"\\"quoted,field\\",plain"',       expected: 'quoted,field,plain' },
          { name: 'empty mid',args: '"a,,b"',                           expected: 'a,,b'               },
          { name: 'multirow', args: '"r1a,r1b\\nr2a,r2b"',             expected: 'r1a,r1b;r2a,r2b'    },
        ],
      },
    ],
  },

  // ──────────────────────────────────────────────────────────────────────────
  '09_lookahead-lookbehind': {
    label: '09 · Lookahead & Lookbehind',
    description: '(?=) (?!) (?<=) (?<!) · zero-width assertions',
    color: '#ff7b72',
    problems: [
      {
        id: 'password-strength-validator', difficulty: 'easy', title: 'Password Strength Validator',
        description: 'Return true if password satisfies ALL of:\n• At least 8 characters\n• Has uppercase letter\n• Has lowercase letter\n• Has digit\n• Has special character (!@#$%^&*)\n\nUse chained positive lookaheads from ^ anchor.',
        returnType: 'boolean', method: 'isStrongPassword',
        starterCode: `public boolean isStrongPassword(String password) {
    // TODO: ^(?=.*[A-Z])(?=.*[a-z])(?=.*\\\\d)(?=.*[!@#$%^&*]).{8,}$
    throw new UnsupportedOperationException("TODO");
}`,
        tests: [
          { name: 'valid',       args: '"Secure1!"',    expected: 'true'  },
          { name: 'no upper',    args: '"secure1!"',    expected: 'false' },
          { name: 'no lower',    args: '"SECURE1!"',    expected: 'false' },
          { name: 'no digit',    args: '"SecurePass!"', expected: 'false' },
          { name: 'too short',   args: '"Sec1!"',       expected: 'false' },
          { name: 'valid long',  args: '"ValidPass1@"', expected: 'true'  },
        ],
      },
      {
        id: 'find-word-not-followed-by', difficulty: 'easy', title: 'Negative Lookahead',
        description: 'Count occurrences of "file" that are NOT immediately followed by "name" or "path".\n\nUse negative lookahead: file(?!name|path).',
        returnType: 'int', method: 'countStandaloneFile',
        starterCode: `public int countStandaloneFile(String input) {
    // TODO: file(?!name|path) with \\\\b boundaries
    throw new UnsupportedOperationException("TODO");
}`,
        tests: [
          { name: 'one match',  args: '"read file from filepath"', expected: '1' },
          { name: 'no match',   args: '"filename and filepath"',   expected: '0' },
          { name: 'three',      args: '"file file file"',          expected: '3' },
          { name: 'none',       args: '"no match"',                expected: '0' },
        ],
      },
      {
        id: 'extract-prices', difficulty: 'medium', title: 'Extract Prices (Lookbehind)',
        description: 'Extract numeric amounts that are preceded by $ or €. Use positive lookbehind (?<=\\\\$|€) — Java requires fixed-length lookbehind alternation of equal-length alternatives.\n\nReturn just the number, not the currency symbol.',
        returnType: 'List', method: 'extractAmounts',
        starterCode: `public List<String> extractAmounts(String input) {
    // TODO: (?<=\\\\$|€)[\\\\d.]+ — lookbehind then number
    throw new UnsupportedOperationException("TODO");
}`,
        tests: [
          { name: 'two currencies', args: '"total: $19.99 and €25.00"', expected: '19.99|25.00' },
          { name: 'none',           args: '"no currency"',              expected: ''             },
          { name: '$100',           args: '"$100 off"',                 expected: '100'          },
          { name: 'space gap',      args: '"$ 50"',                     expected: ''             },
        ],
      },
      {
        id: 'log-severity-extractor', difficulty: 'medium', title: 'Log Severity Extractor',
        description: 'Extract the message text that follows [WARN] or [ERROR] markers in a multiline log string.\n\nReturn just the message part (after "] "), not the marker itself.',
        returnType: 'List', method: 'extractWarningsAndErrors',
        starterCode: `public List<String> extractWarningsAndErrors(String logText) {
    // TODO: Pattern.compile("^\\\\[(?:WARN|ERROR)\\\\] (.+)$", Pattern.MULTILINE)
    // group(1) = the message
    throw new UnsupportedOperationException("TODO");
}`,
        tests: [
          { name: 'warn+error',  args: '"[ERROR] disk full\\n[INFO] ok\\n[WARN] low memory"', expected: 'disk full|low memory' },
          { name: 'info only',   args: '"[INFO] nothing wrong"',                               expected: ''                    },
          { name: 'empty',       args: '""',                                                    expected: ''                    },
        ],
      },
      {
        id: 'overlapping-pattern-finder', difficulty: 'hard', title: 'Overlapping Pattern Count',
        description: 'Count overlapping occurrences of a pattern string within input.\n\nExample: "abababa" contains "aba" 3 times (at positions 0, 2, 4).\n\nTrick: use lookahead (?=(pattern)) — zero-width so the engine doesn\'t advance past the match.',
        returnType: 'int', method: 'countOverlapping',
        starterCode: `public int countOverlapping(String input, String pattern) {
    // TODO: "(?=(" + Pattern.quote(pattern) + "))"
    // Each lookahead match = one occurrence; use Matcher.find() loop
    throw new UnsupportedOperationException("TODO");
}`,
        tests: [
          { name: 'aba in abababa', args: '"abababa", "aba"', expected: '3' },
          { name: 'aa in aaaa',    args: '"aaaa", "aa"',     expected: '3' },
          { name: 'no match',      args: '"hello", "xyz"',   expected: '0' },
          { name: 'll in hello',   args: '"hello", "ll"',    expected: '1' },
        ],
      },
    ],
  },

  // ──────────────────────────────────────────────────────────────────────────
  '10_regex-performance': {
    label: '10 · Regex Performance',
    description: 'ReDoS · Pattern caching · atomic groups · possessive',
    color: '#e3b341',
    problems: [
      {
        id: 'pattern-cache-refactor', difficulty: 'easy', title: 'Pattern Cache Refactor',
        description: 'Filter a list keeping only phone numbers matching \\\\d{3}-\\\\d{4}.\n\nBROKEN: calls String.matches() in a loop (recompiles the pattern every call).\nFIX: declare a static final Pattern field and use it with a Matcher.',
        returnType: 'List', method: 'filterPhoneNumbers',
        starterCode: `// BROKEN: compiles pattern on every call
// for (String s : inputs) { if (s.matches("\\\\d{3}-\\\\d{4}")) ... }

public List<String> filterPhoneNumbers(List<String> inputs) {
    // TODO: declare static final Pattern PHONE = Pattern.compile("\\\\d{3}-\\\\d{4}");
    // then use PHONE.matcher(s).matches() inside the loop
    throw new UnsupportedOperationException("TODO");
}`,
        tests: [
          { name: 'two valid',   args: 'Arrays.asList("123-4567","abc-defg","999-0000","12-345")', expected: '123-4567|999-0000' },
          { name: 'empty list',  args: 'new ArrayList<>()',                                         expected: ''                 },
          { name: 'none valid',  args: 'Arrays.asList("bad","1234567")',                            expected: ''                 },
        ],
      },
      {
        id: 'identify-catastrophic-pattern', difficulty: 'easy', title: 'Identify Catastrophic Pattern',
        description: 'Return false if the regex pattern is potentially catastrophically backtracking (nested quantifiers on overlapping sets: (a+)+, (.+)+, ([a-z]+)+).\n\nDetect by running against an adversarial string with a timeout.',
        returnType: 'boolean', method: 'isSafePattern',
        starterCode: `public boolean isSafePattern(String regex) {
    // Strategy: try to match "aaaaaaaaaaaaaaaaaac" (many a's + non-matching char)
    // with a 200ms timeout. If it times out → catastrophic → return false.
    // If it completes → safe → return true.
    throw new UnsupportedOperationException("TODO");
}`,
        tests: [
          { name: 'nested quant',  args: '"(a+)+"',    expected: 'false' },
          { name: 'dotplus',       args: '"(.+)+"',    expected: 'false' },
          { name: 'simple',        args: '"\\\\d+"',   expected: 'true'  },
          { name: 'char class',    args: '"[a-z]+"',   expected: 'true'  },
        ],
      },
      {
        id: 'optimize-email-validator', difficulty: 'medium', title: 'Optimize Email Validator',
        description: 'Validate email addresses efficiently. A BROKEN slow pattern has overlapping quantifiers in the local part.\n\nFix to use flat, non-backtracking character classes.',
        returnType: 'boolean', method: 'isValidEmail',
        starterCode: `public boolean isValidEmail(String email) {
    // BROKEN (slow): "^([a-z]+[a-z0-9]*)*@([a-z0-9]+\\\\.)+[a-z]{2,6}$"
    // The ([a-z]+[a-z0-9]*)* local part is catastrophic on invalid input.
    //
    // TODO: rewrite with flat classes: [a-zA-Z0-9._%+\\\\-]+@[a-zA-Z0-9.\\\\-]+\\\\.[a-zA-Z]{2,6}
    throw new UnsupportedOperationException("TODO");
}`,
        tests: [
          { name: 'valid',     args: '"user@example.com"',  expected: 'true'  },
          { name: 'no @',      args: '"bad-email"',         expected: 'false' },
          { name: 'no domain', args: '"@nodomain"',         expected: 'false' },
          { name: 'valid sub', args: '"a@b.co.uk"',         expected: 'true'  },
        ],
      },
      {
        id: 'possessive-quantifier-usage', difficulty: 'medium', title: 'Possessive Quantifiers',
        description: 'Parse a log line "HH:MM:SS LEVEL message" using possessive quantifiers (++) to prevent unnecessary backtracking.\n\nReturn "timestamp=HH:MM:SS level=LEVEL" or "INVALID".',
        returnType: 'String', method: 'tokenizeFirst',
        starterCode: `public String tokenizeFirst(String line) {
    // TODO: ([\\\\d:]++) ([A-Z]++) (.++) — possessive prevents backtrack
    // group(1)=timestamp, group(2)=level, group(3)=message
    throw new UnsupportedOperationException("TODO");
}`,
        tests: [
          { name: 'valid',   args: '"14:23:01 ERROR disk full"',   expected: 'timestamp=14:23:01 level=ERROR'   },
          { name: 'warn',    args: '"09:00:00 WARN low memory"',   expected: 'timestamp=09:00:00 level=WARN'    },
          { name: 'invalid', args: '"not a log line"',             expected: 'INVALID'                          },
          { name: 'empty',   args: '""',                           expected: 'INVALID'                          },
        ],
      },
      {
        id: 'redos-safe-log-processor', difficulty: 'hard', title: 'ReDoS-Safe Log Processor',
        description: 'Process log lines of format "TIMESTAMP LEVEL message". Return "level:message" for each valid line.\n\nA BROKEN pattern using (.+)+ causes ReDoS. Fix to use possessive quantifiers or flat character classes.\n\nMust complete 100 adversarial lines in under 500ms.',
        returnType: 'List', method: 'processLogs',
        starterCode: `public List<String> processLogs(List<String> lines) {
    // BROKEN: "^((.+) )+(.+)$" — catastrophic on adversarial input
    // TODO: "^(\\\\S++) (\\\\S++) (.+)$" — possessive quantifiers, no overlap
    throw new UnsupportedOperationException("TODO");
}`,
        tests: [
          { name: 'two lines', args: 'Arrays.asList("2024-01-01 ERROR disk full","2024-01-01 INFO started")', expected: 'ERROR:disk full|INFO:started' },
          { name: 'invalid',   args: 'Arrays.asList("bad")',                                                   expected: ''                           },
          { name: 'empty',     args: 'new ArrayList<>()',                                                       expected: ''                           },
        ],
      },
    ],
  },

  // ──────────────────────────────────────────────────────────────────────────
  '11_java-pattern-matcher': {
    label: '11 · Java Pattern & Matcher',
    description: 'Full API · appendReplacement · split · flags',
    color: '#a5d6ff',
    problems: [
      {
        id: 'find-all-matches', difficulty: 'easy', title: 'Find All Matches',
        description: 'Given an input string and a regex pattern string, return ALL non-overlapping matches using the Matcher.find() loop.\n\nThis is the core Pattern/Matcher usage pattern.',
        returnType: 'List', method: 'findAll',
        starterCode: `public List<String> findAll(String input, String regex) {
    // TODO: Pattern.compile(regex).matcher(input)
    // while (m.find()) results.add(m.group())
    throw new UnsupportedOperationException("TODO");
}`,
        tests: [
          { name: 'digits',      args: '"one 1 two 2 three 3", "\\\\d+"',     expected: '1|2|3'      },
          { name: 'no match',    args: '"hello", "xyz"',                       expected: ''            },
          { name: 'words',       args: '"abc123def456", "[a-z]+"',             expected: 'abc|def'    },
          { name: 'empty input', args: '"", "\\\\w+"',                         expected: ''            },
        ],
      },
      {
        id: 'extract-key-value-pairs', difficulty: 'easy', title: 'Extract Key-Value Pairs',
        description: 'Parse key=value pairs from a string (space-separated) into a Map<String,String>.\n\nUse capture groups: (\\\\w+)=(\\\\w+) — group(1)=key, group(2)=value.',
        returnType: 'Map', method: 'extractKeyValuePairs',
        starterCode: `public Map<String, String> extractKeyValuePairs(String input) {
    // TODO: Pattern("(\\\\w+)=(\\\\w+)") + Matcher.find() loop
    // map.put(m.group(1), m.group(2))
    throw new UnsupportedOperationException("TODO");
}`,
        tests: [
          { name: 'two pairs',  args: '"name=Alice age=30"', expected: 'age=30|name=Alice' },
          { name: 'empty',      args: '""',                  expected: ''                  },
          { name: 'no pairs',   args: '"no pairs here"',     expected: ''                  },
          { name: 'three',      args: '"x=1 y=2 z=3"',      expected: 'x=1|y=2|z=3'      },
        ],
      },
      {
        id: 'split-keeping-delimiter', difficulty: 'medium', title: 'Split Keeping Delimiter',
        description: 'Split on semicolons but keep the semicolon attached to the preceding token.\n\n"a;b;c" → ["a;", "b;", "c"]\n\nTip: use lookbehind split Pattern.compile("(?<=;)").split(input), or manual Matcher approach.',
        returnType: 'List', method: 'splitKeepingDelimiter',
        starterCode: `public List<String> splitKeepingDelimiter(String input) {
    // TODO: split on (?<=;) — lookbehind keeps the ; with the left token
    // Or manually with Matcher + lastEnd tracking
    throw new UnsupportedOperationException("TODO");
}`,
        tests: [
          { name: 'three',    args: '"a;b;c"',      expected: 'a;|b;|c'   },
          { name: 'no semi',  args: '"abc"',         expected: 'abc'       },
          { name: 'trailing', args: '"trailing;"',   expected: 'trailing;' },
          { name: 'empty',    args: '""',            expected: ''          },
        ],
      },
      {
        id: 'replace-with-custom-logic', difficulty: 'medium', title: 'Double All Numbers',
        description: 'Replace every integer in a string by doubling its value.\n\n"I have 3 cats and 10 dogs" → "I have 6 cats and 20 dogs"\n\nRequires Matcher.appendReplacement(StringBuilder, String) + appendTail().',
        returnType: 'String', method: 'doubleAllNumbers',
        starterCode: `public String doubleAllNumbers(String input) {
    // TODO: Pattern("\\\\d+"), then while m.find():
    //   int val = Integer.parseInt(m.group());
    //   m.appendReplacement(sb, String.valueOf(val * 2));
    // m.appendTail(sb); return sb.toString()
    throw new UnsupportedOperationException("TODO");
}`,
        tests: [
          { name: 'one number',  args: '"I have 3 cats"',      expected: 'I have 6 cats'       },
          { name: 'zero',        args: '"0 items"',             expected: '0 items'             },
          { name: 'two numbers', args: '"100 and 200"',         expected: '200 and 400'         },
          { name: 'no numbers',  args: '"no numbers"',          expected: 'no numbers'          },
          { name: 'mixed',       args: '"5 and 50"',            expected: '10 and 100'          },
        ],
      },
      {
        id: 'streaming-line-processor', difficulty: 'hard', title: 'Multi-Pattern Line Transformer',
        description: 'Process each line in a list: mask all email addresses with [EMAIL] and all IPv4 addresses with [IP].\n\nReturn transformed lines. Use static Pattern fields for both patterns.',
        returnType: 'List', method: 'processLines',
        starterCode: `public List<String> processLines(List<String> lines) {
    // TODO: two static final Patterns: one for email, one for IPv4
    // For each line: replaceAll email → [EMAIL], then IP → [IP]
    throw new UnsupportedOperationException("TODO");
}`,
        tests: [
          { name: 'email',       args: 'Arrays.asList("send to user@example.com now")',               expected: 'send to [EMAIL] now'          },
          { name: 'ip',          args: 'Arrays.asList("server at 192.168.1.1 ok")',                   expected: 'server at [IP] ok'            },
          { name: 'both',        args: 'Arrays.asList("user@x.com at 10.0.0.1")',                     expected: '[EMAIL] at [IP]'              },
          { name: 'unchanged',   args: 'Arrays.asList("nothing to mask")',                            expected: 'nothing to mask'              },
          { name: 'empty list',  args: 'new ArrayList<>()',                                            expected: ''                             },
        ],
      },
    ],
  },

  // ──────────────────────────────────────────────────────────────────────────
  '12_debugging-regex': {
    label: '12 · Debugging Regex',
    description: 'Systematic diagnosis · greedy bugs · wrong flags · ReDoS',
    color: '#ffa657',
    problems: [
      {
        id: 'fix-greedy-overcapture', difficulty: 'easy', title: 'Fix Greedy Overcapture',
        description: 'A broken method extracts <title>...</title> content using greedy <title>(.*)</title>. On two tags, it returns one result spanning both.\n\nFix: use lazy <title>(.*?)</title>.',
        returnType: 'List', method: 'extractTitles',
        starterCode: `public List<String> extractTitles(String html) {
    // BROKEN: Pattern.compile("<title>(.*)</title>")  — greedy, one result for two tags
    // TODO:   Pattern.compile("<title>(.*?)</title>") — lazy, one per tag
    throw new UnsupportedOperationException("TODO");
}`,
        tests: [
          { name: 'one title',  args: '"<title>Hello</title>"',                         expected: 'Hello'    },
          { name: 'two titles', args: '"<title>A</title><title>B</title>"',             expected: 'A|B'      },
          { name: 'none',       args: '"no titles"',                                    expected: ''         },
          { name: 'empty',      args: '"<title></title>"',                              expected: ''         },
        ],
      },
      {
        id: 'fix-matches-vs-find', difficulty: 'easy', title: 'Fix matches() vs find()',
        description: 'A broken method checks if a string CONTAINS a 5-digit ZIP code anywhere. Bug: uses input.matches("\\\\d{5}") which requires the ENTIRE string to be 5 digits.\n\nFix: use Pattern + Matcher.find() with boundary checks.',
        returnType: 'boolean', method: 'containsZipCode',
        starterCode: `public boolean containsZipCode(String input) {
    // BROKEN: return input.matches("\\\\d{5}");  — full-string match only
    // TODO: Pattern("(?<!\\\\d)\\\\d{5}(?!\\\\d)").matcher(input).find()
    throw new UnsupportedOperationException("TODO");
}`,
        tests: [
          { name: 'in text',    args: '"ZIP: 90210 is CA"', expected: 'true'  },
          { name: 'exact',      args: '"90210"',            expected: 'true'  },
          { name: 'too short',  args: '"9021"',             expected: 'false' },
          { name: 'six digits', args: '"902109"',           expected: 'false' },
          { name: 'empty',      args: '""',                 expected: 'false' },
        ],
      },
      {
        id: 'debug-wrong-group-index', difficulty: 'medium', title: 'Debug Wrong Group Index',
        description: 'A broken method extracts the YEAR from YYYY-MM-DD. Bug: uses matcher.group(0) which returns the ENTIRE match, not just the year.\n\nFix: use matcher.group(1) for the first capture group.',
        returnType: 'Optional', method: 'extractYear',
        starterCode: `public Optional<String> extractYear(String input) {
    // BROKEN: return Optional.of(matcher.group(0)); // group(0) = full match "2024-01-15"
    // TODO:   return Optional.of(matcher.group(1)); // group(1) = "2024"
    throw new UnsupportedOperationException("TODO");
}`,
        tests: [
          { name: 'valid date',  args: '"2024-01-15"',            expected: '2024'  },
          { name: 'no date',     args: '"no date"',               expected: 'EMPTY' },
          { name: 'in text',     args: '"event 1999-06-15 done"', expected: '1999'  },
          { name: 'empty',       args: '""',                      expected: 'EMPTY' },
        ],
      },
      {
        id: 'debug-missing-multiline-flag', difficulty: 'medium', title: 'Debug Missing MULTILINE Flag',
        description: 'A broken method finds comment lines starting with "#" in multiline text. Bug: Pattern.compile("^#.*") without MULTILINE — ^ only matches the start of the entire string.\n\nFix: add Pattern.MULTILINE flag.',
        returnType: 'List', method: 'extractCommentLines',
        starterCode: `public List<String> extractCommentLines(String text) {
    // BROKEN: Pattern.compile("^#.*")  — no MULTILINE, ^ = start of whole string
    // TODO:   Pattern.compile("^#.*", Pattern.MULTILINE)
    throw new UnsupportedOperationException("TODO");
}`,
        tests: [
          { name: 'two comments', args: '"# comment\\ncode line\\n# another"', expected: '# comment|# another' },
          { name: 'no comments',  args: '"no comments\\nhere"',                 expected: ''                    },
          { name: 'one',          args: '"# only one"',                         expected: '# only one'          },
          { name: 'empty',        args: '""',                                    expected: ''                    },
        ],
      },
      {
        id: 'debug-catastrophic-log-parser', difficulty: 'hard', title: 'Debug Catastrophic Log Parser',
        description: 'A broken log parser uses (\\\\w+\\\\s*)+ which is catastrophically backtracking. It hangs on lines without a colon.\n\nFix: rewrite to "^([^:]+):\\\\s*(.*)$" — no nested quantifiers.\n\nReturn "label:message" for each valid line.',
        returnType: 'List', method: 'parseLogs',
        starterCode: `public List<String> parseLogs(List<String> lines) {
    // BROKEN: Pattern.compile("^((\\\\w+\\\\s*)+):\\\\s*(.*)$")
    // Hangs on: "word word word word word word word word!"  (no colon)
    //
    // TODO: Pattern.compile("^([^:]+):\\\\s*(.*)$")  — flat, O(n), safe
    throw new UnsupportedOperationException("TODO");
}`,
        tests: [
          { name: 'system msg',     args: 'Arrays.asList("SYSTEM: disk full")',                 expected: 'SYSTEM:disk full'     },
          { name: 'error msg',      args: 'Arrays.asList("ERROR: connection timeout")',         expected: 'ERROR:connection timeout' },
          { name: 'no colon',       args: 'Arrays.asList("word word word word word word!")',    expected: ''                     },
          { name: 'empty message',  args: 'Arrays.asList("LABEL: ")',                           expected: 'LABEL:'               },
          { name: 'empty list',     args: 'new ArrayList<>()',                                   expected: ''                     },
        ],
      },
    ],
  },

};
