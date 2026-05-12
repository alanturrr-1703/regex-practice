/**
 * runner.js — Code execution engine
 *
 * Three execution paths:
 *
 *  1. ELECTRON  → window.electronAPI.runGradleTests()
 *     Writes code to Solution.java, runs ./gradlew :<path>:test,
 *     parses the real JUnit XML — shows EVERY test in SolutionTest.java.
 *
 *  2. DOCKER / localhost:3000  → POST /execute
 *     Custom harness built from data.js test cases.
 *     (Gradle not available inside the lightweight Docker image)
 *
 *  3. GitHub Pages  → install instructions
 */

// ── Harness generator (Docker / localhost path only) ─────────────────────────

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
        if (e.equals(a)) { System.out.println("PASS: " + name); _p++; }
        else {
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
            System.out.println("NOT_IMPLEMENTED"); return;
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

/**
 * @param {object} problem   — the problem object from PROBLEMS in data.js
 * @param {string} userCode  — current Monaco editor content (full Solution.java in Electron,
 *                             method body only in Docker / GitHub Pages)
 * @param {object} ctx       — { conceptKey, difficulty, problemId } for Electron path
 */
async function runInSandbox(problem, userCode, ctx) {
  // ── Path 1: Electron → Gradle (all tests from SolutionTest.java) ──────────
  if (window.electronAPI && window.electronAPI.isElectron) {
    return await window.electronAPI.runGradleTests({
      conceptKey: ctx.conceptKey,
      difficulty: ctx.difficulty,
      problemId: ctx.problemId,
      code: userCode, // full Solution.java content
    });
  }

  // ── Path 2: Docker / local server → harness ───────────────────────────────
  if (
    window.location.hostname === "localhost" ||
    window.location.hostname === "127.0.0.1"
  ) {
    try {
      const resp = await fetch("/execute", {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify({ code: buildHarness(problem, userCode) }),
      });
      if (resp.ok) {
        const data = await resp.json();
        return { mode: "harness", ...data };
      }
    } catch (_) {}
  }

  // ── Path 3: GitHub Pages ──────────────────────────────────────────────────
  return { mode: "none" };
}
