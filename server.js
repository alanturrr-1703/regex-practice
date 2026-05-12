/**
 * server.js — Express execution server
 *
 * Used inside the Docker container. Serves:
 *  - GET  /          → docs/index.html  (the problem browser)
 *  - GET  /solve.html → problem solver
 *  - POST /execute   → compile & run submitted Java code
 *  - GET  /health    → liveness check
 *
 * Run locally:  node server.js
 * Run in Docker: see Dockerfile / docker-compose.yml
 */

const express = require('express');
const cors    = require('cors');
const { exec } = require('child_process');
const fs   = require('fs');
const os   = require('os');
const path = require('path');

const app = express();
app.use(cors());
app.use(express.json({ limit: '2mb' }));
app.use(express.static(path.join(__dirname, 'docs')));

// ── Health check ─────────────────────────────────────────────────────────────
app.get('/health', (_req, res) => res.json({ status: 'ok' }));

// ── Execute endpoint ─────────────────────────────────────────────────────────
app.post('/execute', (req, res) => {
  const { code } = req.body;
  if (!code || typeof code !== 'string') {
    return res.status(400).json({ output: '', error: 'No code provided.' });
  }

  const tmpDir  = fs.mkdtempSync(path.join(os.tmpdir(), 'java-sandbox-'));
  const srcFile = path.join(tmpDir, 'Main.java');

  fs.writeFileSync(srcFile, code, 'utf8');

  // Step 1: compile
  exec(
    `javac "${srcFile}"`,
    { cwd: tmpDir, timeout: 20_000, encoding: 'utf8' },
    (compileErr, _out, compileStderr) => {
      if (compileErr) {
        cleanup(tmpDir);
        return res.json({ output: '', error: compileStderr || compileErr.message });
      }

      // Step 2: run
      exec(
        `java -cp "${tmpDir}" Main`,
        { cwd: tmpDir, timeout: 10_000, encoding: 'utf8' },
        (runErr, stdout, stderr) => {
          cleanup(tmpDir);
          res.json({ output: stdout + (stderr || ''), error: '' });
        }
      );
    }
  );
});

function cleanup(dir) {
  try { fs.rmSync(dir, { recursive: true, force: true }); } catch (_) {}
}

// ── Start ─────────────────────────────────────────────────────────────────────
const PORT = parseInt(process.env.PORT || '3000', 10);
app.listen(PORT, '0.0.0.0', () => {
  console.log('');
  console.log('  ⚡ Java Regex Practice');
  console.log(`  Open: http://localhost:${PORT}`);
  console.log('');
});
