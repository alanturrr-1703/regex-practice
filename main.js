/**
 * main.js — Electron main process
 *
 * IPC handlers:
 *  'read-solution'    → read the current Solution.java content from disk
 *  'run-gradle-test'  → write code to Solution.java, run ./gradlew :<path>:test,
 *                       parse JUnit XML, return all test results
 *  'check-java'       → verify java is on PATH
 */

const { app, BrowserWindow, ipcMain, shell } = require("electron");
const path = require("path");
const fs = require("fs");
const os = require("os");
const { exec } = require("child_process");

// ── Window ─────────────────────────────────────────────────────────────────

function createWindow() {
  const win = new BrowserWindow({
    width: 1440,
    height: 900,
    minWidth: 900,
    minHeight: 600,
    title: "Java Regex Practice",
    webPreferences: {
      preload: path.join(__dirname, "preload.js"),
      contextIsolation: true,
      nodeIntegration: false,
    },
  });

  win.loadFile(path.join(__dirname, "docs", "app.html"));

  win.webContents.setWindowOpenHandler(({ url }) => {
    shell.openExternal(url);
    return { action: "deny" };
  });
  win.webContents.on("will-navigate", (event, url) => {
    if (!url.startsWith("file://")) {
      event.preventDefault();
      shell.openExternal(url);
    }
  });
}

app.whenReady().then(() => {
  createWindow();
  app.on("activate", () => {
    if (BrowserWindow.getAllWindows().length === 0) createWindow();
  });
});
app.on("window-all-closed", () => {
  if (process.platform !== "darwin") app.quit();
});

// ── Helpers ─────────────────────────────────────────────────────────────────

function runCmd(cmd, options) {
  return new Promise((resolve) => {
    exec(cmd, { encoding: "utf8", ...options }, (err, stdout, stderr) => {
      resolve({
        code: err ? err.code || 1 : 0,
        stdout: stdout || "",
        stderr: stderr || "",
      });
    });
  });
}

/** Resolve a problem directory: topics/regex/concepts/{concept}/problems/{diff}/{id} */
function problemDir(conceptKey, difficulty, problemId) {
  return path.join(
    __dirname,
    "topics",
    "regex",
    "concepts",
    conceptKey,
    "problems",
    difficulty,
    problemId,
  );
}

function solutionFile(conceptKey, difficulty, problemId) {
  return path.join(
    problemDir(conceptKey, difficulty, problemId),
    "src",
    "main",
    "java",
    "com",
    "example",
    "Solution.java",
  );
}

// ── Parse JUnit XML ─────────────────────────────────────────────────────────

/**
 * Parses a JUnit XML report into an array of { name, displayName, passed, message }.
 * No external XML library needed — regex-based is sufficient for well-formed JUnit output.
 */
function parseJUnitXml(xml) {
  const tests = [];
  // Match both self-closing <testcase .../> and <testcase ...>...</testcase>
  const re = /<testcase\s([^>]+?)(?:\/>|>([\s\S]*?)<\/testcase>)/g;
  let m;
  while ((m = re.exec(xml)) !== null) {
    const attrs = m[1] || "";
    const body = m[2] || "";
    const nameRaw = (attrs.match(/name="([^"]+)"/) || [])[1] || "unknown";
    const failed = body.includes("<failure") || body.includes("<error");
    let message = "";
    if (failed) {
      // Extract the human-readable part of the failure message
      const msgMatch =
        body.match(/message="([^"]*)"/) ||
        body.match(/<failure[^>]*>([\s\S]*?)<\/failure>/);
      if (msgMatch)
        message = msgMatch[1]
          .replace(/&#10;/g, "\n")
          .replace(/&lt;/g, "<")
          .replace(/&gt;/g, ">")
          .slice(0, 300);
    }
    tests.push({
      name: nameRaw,
      displayName: camelToWords(nameRaw.replace(/^test/, "")),
      passed: !failed,
      message,
    });
  }
  return tests;
}

function camelToWords(s) {
  return s
    .replace(/([A-Z])/g, " $1")
    .toLowerCase()
    .replace(/^\s+/, "");
}

// ── IPC: read current Solution.java ─────────────────────────────────────────

ipcMain.handle(
  "read-solution",
  (_event, { conceptKey, difficulty, problemId }) => {
    try {
      return {
        code: fs.readFileSync(
          solutionFile(conceptKey, difficulty, problemId),
          "utf8",
        ),
      };
    } catch (e) {
      return { error: e.message };
    }
  },
);

// ── IPC: write + run Gradle tests ───────────────────────────────────────────

ipcMain.handle(
  "run-gradle-test",
  async (_event, { conceptKey, difficulty, problemId, code }) => {
    const solFile = solutionFile(conceptKey, difficulty, problemId);

    // 1. Write the user's code to Solution.java
    try {
      fs.writeFileSync(solFile, code, "utf8");
    } catch (e) {
      return { error: `Cannot write Solution.java: ${e.message}` };
    }

    // 2. Run ./gradlew :<concept>:<difficulty>:<problem>:test
    const gradlew = path.join(
      __dirname,
      process.platform === "win32" ? "gradlew.bat" : "gradlew",
    );
    const task = `:${conceptKey}:${difficulty}:${problemId}:test`;

    const result = await runCmd(
      `"${gradlew}" ${task} --no-daemon --continue --rerun-tasks`,
      { cwd: __dirname, timeout: 90_000 },
    );

    // 3. Find and parse the JUnit XML report
    const xmlDir = path.join(
      problemDir(conceptKey, difficulty, problemId),
      "build",
      "test-results",
      "test",
    );

    try {
      const xmlFiles = fs.readdirSync(xmlDir).filter((f) => f.endsWith(".xml"));
      if (xmlFiles.length === 0) throw new Error("no XML");
      const xml = fs.readFileSync(path.join(xmlDir, xmlFiles[0]), "utf8");
      const tests = parseJUnitXml(xml);

      // Extract totals from <testsuite> attributes
      const tsMatch = xml.match(
        /<testsuite[^>]+tests="(\d+)"[^>]*failures="(\d+)"[^>]*errors="(\d+)"/,
      );
      const total = tsMatch ? parseInt(tsMatch[1]) : tests.length;
      const failures = tsMatch
        ? parseInt(tsMatch[2]) + parseInt(tsMatch[3])
        : tests.filter((t) => !t.passed).length;

      return {
        mode: "gradle",
        tests,
        total,
        passed: total - failures,
        failed: failures,
      };
    } catch (_) {
      // XML not found → compile error. Surface Gradle output.
      const output = result.stdout + result.stderr;
      // Extract just the compile error section
      const compileErr = extractCompileError(output);
      return { mode: "gradle", error: compileErr || output.slice(-2000) };
    }
  },
);

function extractCompileError(gradleOutput) {
  // Gradle wraps javac errors in lines starting with "> Task :..."
  const lines = gradleOutput.split("\n");
  const errLines = lines.filter(
    (l) =>
      l.includes("error:") ||
      l.includes(".java:") ||
      l.includes("FAILED") ||
      l.includes("Compilation failed"),
  );
  return errLines.length ? errLines.join("\n") : "";
}

// ── IPC: check Java ─────────────────────────────────────────────────────────

ipcMain.handle("check-java", async () => {
  const r = await runCmd("java -version", { timeout: 5000 });
  const combined = r.stdout + r.stderr;
  const match = combined.match(/version "([^"]+)"/);
  return {
    available: r.code === 0,
    version: match ? match[1] : combined.slice(0, 80),
  };
});
