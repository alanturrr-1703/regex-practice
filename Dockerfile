# ── Stage 1: build server deps ────────────────────────────────────────────────
FROM node:20-alpine AS deps

WORKDIR /app
COPY server-package.json ./package.json
RUN npm install --omit=dev

# ── Stage 2: runtime — OpenJDK 17 + Node 20 ──────────────────────────────────
FROM eclipse-temurin:17-jdk-jammy

LABEL org.opencontainers.image.title="Java Regex Practice"
LABEL org.opencontainers.image.description="12 concepts · 60 problems · in-browser Java sandbox"
LABEL org.opencontainers.image.source="https://github.com/alanturrr-1703/regex-practice"

# Install Node.js 20 into the JDK image
RUN apt-get update \
 && apt-get install -y --no-install-recommends curl \
 && curl -fsSL https://deb.nodesource.com/setup_20.x | bash - \
 && apt-get install -y --no-install-recommends nodejs \
 && apt-get clean \
 && rm -rf /var/lib/apt/lists/*

WORKDIR /app

# Copy server dependencies from the deps stage
COPY --from=deps /app/node_modules ./node_modules

# Copy application files
COPY server.js        ./server.js
COPY server-package.json ./package.json
COPY docs/            ./docs/

# Non-root user for security
RUN addgroup --system appgroup && adduser --system --ingroup appgroup appuser
RUN chown -R appuser:appgroup /app
USER appuser

EXPOSE 3000

HEALTHCHECK --interval=30s --timeout=5s --start-period=10s --retries=3 \
  CMD curl -f http://localhost:3000/health || exit 1

CMD ["node", "server.js"]
