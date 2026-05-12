/**
 * preload.js — IPC bridge between the renderer (docs/) and the main process.
 *
 * Exposes a minimal, safe API on window.electronAPI.
 * contextIsolation: true ensures the renderer cannot access Node.js directly.
 */

const { contextBridge, ipcRenderer } = require('electron');

contextBridge.exposeInMainWorld('electronAPI', {

  /**
   * Execute a Java source string locally and return { output, error }.
   * @param {string} javaCode  — the full Main.java source
   * @returns {Promise<{output: string, error: string}>}
   */
  executeJava: (javaCode) => ipcRenderer.invoke('execute-java', javaCode),

  /**
   * Check if Java is installed and return { available: bool, version: string }.
   */
  checkJava: () => ipcRenderer.invoke('check-java'),

  /** True when running inside Electron */
  isElectron: true,
});
