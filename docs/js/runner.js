/**
 * runner.js — Piston API harness generator and executor
 *
 * Piston is a free, open-source code execution engine.
 * No API key required. CORS enabled for browser requests.
 * Docs: https://github.com/engineer-man/piston
 */

const PISTON_URL = 'https://emkc.org/api/v2/piston/execute';

// ── Convert a method return value to a comparable String ─────────────────────
function toStringExpr(returnType, call, customToString) {
  switch (returnType) {
    case 'boolean':
    case 'int':
      return `String.valueOf(${call})`;
    case 'String':
      return `(${call} == null ? "null" : ${call})`;
    case 'List':
      return `(${call} == null ? "null" : String.join("|", ${call}))`;
    case 'Optional':
      return `${call}.map(Object::toString).orElse("EMPTY")`;
    case 'Map':
      // Sort keys → "k1=v1|k2=v2"
      return `${call}.entrySet().stream().sorted(java.util.Map.Entry.comparingByKey()).map(e -> e.getKey()+"="+e.getValue()).collect(java.util.stream.Collectors.joining("|"))`;
    case 'ListList':
      return `(${call} == null ? "null" : ${call}.stream().map(r -> String.join(",", r)).collect(java.util.stream.Collectors.joining(";")))`;
    case 'custom':
      return `(java.util.function.Supplier<String>)(() -> { var _r = ${call}; return ${customToString.replace(/result/g, '_r')}; }).get()`;
    default:
      return `String.valueOf(${call})`;
  }
}

// ── Build the full runnable Java source ──────────────────────────────────────
function buildHarness(problem, userCode) {
  const testLines = problem.tests.map(t => {
    const call   = `sol.${problem.method}(${t.args})`;
    const actual = toStringExpr(problem.returnType, call, problem.customToString);
    return `        check(${JSON.stringify(t.name)}, ${JSON.stringify(t.expected)}, ${actual});`;
  }).join('\n');

  // Indent user code so it fits inside the Solution class
  const indented = userCode
    .split('\n')
    .map(l => '    ' + l)
    .join('\n');

  return `import java.util.*;
import java.util.regex.*;
import java.util.stream.*;
import java.util.Optional;

public class Main {
    static int _p = 0, _f = 0;

    static void check(String name, String expected, String actual) {
        String e = expected == null ? "null" : expected.trim();
        String a = actual   == null ? "null" : actual.trim();
        if (e.equals(a)) {
            System.out.println("PASS: " + name);
            _p++;
        } else {
            System.out.println("FAIL: " + name);
            System.out.println("  expected: " + e);
            System.out.println("  got:      " + a);
            _f++;
        }
    }

    public static void main(String[] args) {
        Solution sol = new Solution();
        try {
${testLines}
        } catch (UnsupportedOperationException e) {
            System.out.println("NOT_IMPLEMENTED");
            return;
        } catch (Exception e) {
            System.out.println("EXCEPTION: " + e.getClass().getSimpleName() + ": " + e.getMessage());
        }
        System.out.println("SUMMARY: " + _p + "/" + (_p + _f) + " passed");
    }
}

class Solution {
${indented}
}
`;
}

// ── Send to Piston and return { output, error } ───────────────────────────────
async function runInSandbox(problem, userCode) {
  const javaSource = buildHarness(problem, userCode);

  const payload = {
    language: 'java',
    version:  '15.0.2',
    files:    [{ name: 'Main.java', content: javaSource }],
    run_timeout:     8000,
    compile_timeout: 15000,
    compile_memory_limit: -1,
    run_memory_limit:     -1,
  };

  const resp = await fetch(PISTON_URL, {
    method:  'POST',
    headers: { 'Content-Type': 'application/json' },
    body:    JSON.stringify(payload),
  });

  if (!resp.ok) throw new Error(`Piston API returned ${resp.status}`);

  const data = await resp.json();

  // Piston puts compile errors in compile.stderr, runtime output in run.stdout
  const compileErr = data.compile?.stderr || '';
  const output     = (data.run?.stdout || '') + (data.run?.stderr || '');

  return { output, error: compileErr };
}
