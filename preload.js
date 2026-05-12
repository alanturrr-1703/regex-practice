/**
 * preload.js — IPC bridge
 *
 * Exposes window.electronAPI to the renderer (docs/).
 */

const { contextBridge, ipcRenderer } = require("electron");

contextBridge.exposeInMainWorld("electronAPI", {
  /** True when running inside Electron */
  isElectron: true,

  /**
   * Read the current Solution.java from disk.
   * @returns {Promise<{code?: string, error?: string}>}
   */
  readSolution: ({ conceptKey, difficulty, problemId }) =>
    ipcRenderer.invoke("read-solution", { conceptKey, difficulty, problemId }),

  /**
   * Write code to Solution.java and run the full Gradle test suite.
   * Returns all test results from the real SolutionTest.java.
   *
   * @param {object} opts
   * @param {string} opts.conceptKey  — e.g. "01_basics"
   * @param {string} opts.difficulty  — "easy" | "medium" | "hard"
   * @param {string} opts.problemId   — e.g. "detect-digit"
   * @param {string} opts.code        — full Solution.java content
   * @returns {Promise<{mode:'gradle', tests:Array, total:number, passed:number, failed:number, error?:string}>}
   */
  runGradleTests: ({ conceptKey, difficulty, problemId, code }) =>
    ipcRenderer.invoke("run-gradle-test", {
      conceptKey,
      difficulty,
      problemId,
      code,
    }),

  /** Verify Java is installed. */
  checkJava: () => ipcRenderer.invoke("check-java"),
});
