/**
 * runner.js — Code execution engine
 *
 * Three execution paths, tried in order:
 *
 *  1. ELECTRON (desktop app)
 *     window.electronAPI.executeJava(source)
 *     → Electron main process: javac + java via child_process
 *     → Uses the user's local JDK, works offline
 *
 *  2. DOCKER / LOCAL SERVER  (http://localhost:3000)
 *     POST /execute  { code }
 *     → Express server inside the Docker container
 *     → OpenJDK 17 bundled in the image, no Java install needed
 *     → Only tried when window.location.hostname === 'localhost'
 *
 *  3. GITHUB PAGES (static hosting)
 *     No execution available — shows install instructions
 */

// ── Harness generator ─────────────────────────────────────────────────────────

function toStringExpr(returnType, call, customToString) {
  switch (returnType) {
    case "boolean":
    case "int":
      return `String.valueOf(${call})`;
    case "String":
      return `(${call} == null ? "null" : ${call})`;
    case "List":
      return `(${call} == null ? "null" : String.join("|", ${call}))`;
    case "Optional":
      return `${call}.map(Object::toString).orElse("EMPTY")`;
    case "Map":
      return (
        `${call}.entrySet().stream().sorted(java.util.Map.Entry.comparingByKey())` +
        `.map(e -> e.getKey()+"="+e.getValue()).collect(java.util.stream.Collectors.joining("|"))`
      );
    case "ListList":
      return (
        `(${call} == null ? "null" : ${call}.stream()` +
        `.map(r -> String.join(",", r)).collect(java.util.stream.Collectors.joining(";")))`
      );
    case "custom":
      return (
        `(java.util.function.Supplier<String>)(() -> { var _r = ${call}; ` +
        `return ${(customToString || "").replace(/result/g, "_r")}; }).get()`
      );
    default:
      return `String.valueOf(${call})`;
  }
}

function buildHarness(problem, userCode) {
  const testLines = problem.tests
    .map((t) => {
      const call = `sol.${problem.method}(${t.args})`;
      const actual = toStringExpr(
        problem.returnType,
        call,
        problem.customToString,
      );
      return `        check(${JSON.stringify(t.name)}, ${JSON.stringify(t.expected)}, ${actual});`;
    })
    .join("\n");

  const indented = userCode
    .split("\n")
    .map((l) => "    " + l)
    .join("\n");

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

// ── Execution router ─────────────────────────────────────────────────────────

async function runInSandbox(problem, userCode) {
  const javaSource = buildHarness(problem, userCode);

  // ── Path 1: Electron desktop app ─────────────────────────────────────────
  if (window.electronAPI && window.electronAPI.isElectron) {
    return await window.electronAPI.executeJava(javaSource);
  }

  // ── Path 2: Docker / local execution server ───────────────────────────────
  if (
    window.location.hostname === "localhost" ||
    window.location.hostname === "127.0.0.1"
  ) {
    try {
      const resp = await fetch("/execute", {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify({ code: javaSource }),
      });
      if (resp.ok) return await resp.json();
    } catch (_) {
      // Server not running — fall through
    }
  }

  // ── Path 3: GitHub Pages — no executor ───────────────────────────────────
  return { output: "WEB_NO_EXECUTOR", error: "" };
}
