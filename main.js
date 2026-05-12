/**
 * main.js — Electron main process
 *
 * Responsibilities:
 *  1. Create the browser window and load the docs/ frontend
 *  2. Handle 'execute-java' IPC calls from the renderer:
 *       - Write the harness Java source to a temp dir
 *       - Compile with javac
 *       - Run with java
 *       - Return { output, error } to the renderer
 *  3. Open external links (GitHub) in the system browser
 */

const { app, BrowserWindow, ipcMain, shell } = require('electron');
const path  = require('path');
const fs    = require('fs');
const os    = require('os');
const { exec } = require('child_process');

// ── Window ─────────────────────────────────────────────────────────────────

function createWindow() {
  const win = new BrowserWindow({
    width:  1440,
    height: 900,
    minWidth:  900,
    minHeight: 600,
    title: 'Java Regex Practice',
    webPreferences: {
      preload:          path.join(__dirname, 'preload.js'),
      contextIsolation: true,
      nodeIntegration:  false,
    },
  });

  // Load the frontend
  win.loadFile(path.join(__dirname, 'docs', 'index.html'));

  // Open external links (e.g. GitHub button) in system browser, not in Electron
  win.webContents.setWindowOpenHandler(({ url }) => {
    shell.openExternal(url);
    return { action: 'deny' };
  });

  win.webContents.on('will-navigate', (event, url) => {
    // Allow navigation within docs/ (file:// links)
    if (!url.startsWith('file://')) {
      event.preventDefault();
      shell.openExternal(url);
    }
  });
}

app.whenReady().then(() => {
  createWindow();
  app.on('activate', () => {
    if (BrowserWindow.getAllWindows().length === 0) createWindow();
  });
});

app.on('window-all-closed', () => {
  if (process.platform !== 'darwin') app.quit();
});

// ── Java execution IPC handler ─────────────────────────────────────────────

/**
 * Runs a shell command and resolves with { code, stdout, stderr }.
 * Never rejects — errors are captured in the resolved object.
 */
function runCmd(cmd, options) {
  return new Promise(resolve => {
    exec(cmd, { encoding: 'utf8', ...options }, (err, stdout, stderr) => {
      resolve({
        code:   err ? (err.code || 1) : 0,
        stdout: stdout || '',
        stderr: stderr || '',
      });
    });
  });
}

/**
 * Finds the path to javac/java.
 * On macOS/Linux the JAVA_HOME env var is often set; on Windows too.
 * Falls back to just 'javac'/'java' (assumes they're on PATH).
 */
function javaBin(name) {
  const javaHome = process.env.JAVA_HOME;
  if (javaHome) {
    const bin = path.join(javaHome, 'bin', name);
    if (fs.existsSync(bin)) return `"${bin}"`;
  }
  return name; // rely on PATH
}

ipcMain.handle('execute-java', async (_event, javaCode) => {
  // Create an isolated temp directory for this execution
  const tmpDir = fs.mkdtempSync(path.join(os.tmpdir(), 'regex-sandbox-'));
  const srcFile = path.join(tmpDir, 'Main.java');

  try {
    // 1. Write source
    fs.writeFileSync(srcFile, javaCode, 'utf8');

    // 2. Compile
    const javac = javaBin('javac');
    const compile = await runCmd(
      `${javac} "${srcFile}"`,
      { cwd: tmpDir, timeout: 20_000 }
    );

    if (compile.code !== 0) {
      return { output: '', error: compile.stderr || compile.stdout };
    }

    // 3. Run
    const java = javaBin('java');
    const run = await runCmd(
      `${java} -cp "${tmpDir}" Main`,
      { cwd: tmpDir, timeout: 12_000 }
    );

    return {
      output: run.stdout + (run.stderr || ''),
      error:  '',
    };

  } catch (err) {
    return { output: '', error: String(err) };
  } finally {
    // Clean up temp files
    try { fs.rmSync(tmpDir, { recursive: true, force: true }); } catch (_) {}
  }
});

// ── Java availability check ────────────────────────────────────────────────

ipcMain.handle('check-java', async () => {
  const result = await runCmd(`${javaBin('java')} -version`, { timeout: 5000 });
  // java -version prints to stderr by convention
  const combined = result.stdout + result.stderr;
  const match = combined.match(/version "([^"]+)"/);
  return {
    available: result.code === 0,
    version:   match ? match[1] : combined.slice(0, 80),
  };
});
