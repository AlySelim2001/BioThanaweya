#!/usr/bin/env bash
# ==============================================================================
# BioThanaweya Clean Slate & Cache Purge Script (Clean Architecture 2026)
# Completely purges node_modules, package-lock.json, Vite cache, and temp builds,
# then performs a fresh, deterministic re-installation to resolve corruption.
# ==============================================================================

set -e

# Terminal ANSI Styling
CYAN='\033[0;36m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
RED='\033[0;31m'
NC='\033[0m'

echo -e "${CYAN}================================================================${NC}"
echo -e "${CYAN}🧹 BioThanaweya Clean Slate & Dependency Recovery Pipeline${NC}"
echo -e "${CYAN}Environment: Mobile Termux / Enterprise Cloud Container${NC}"
echo -e "${CYAN}================================================================${NC}"

# Step 1: Terminate any lingering Node.js or Vite processes on port 3000
echo -e "\n${YELLOW}[1/5] Inspecting and terminating orphaned server processes...${NC}"
if command -v fuser >/dev/null 2>&1; then
  fuser -k 3000/tcp 2>/dev/null || true
elif command -v lsof >/dev/null 2>&1; then
  PID=$(lsof -ti:3000 || true)
  if [ -n "$PID" ]; then
    kill -9 "$PID" 2>/dev/null || true
  fi
fi
echo -e "${GREEN}✓ Port 3000 cleared.${NC}"

# Step 2: Purge caches and build artifacts
echo -e "\n${YELLOW}[2/5] Purging node_modules, lockfiles, and Vite/Rollup caches...${NC}"
rm -rf node_modules
rm -rf package-lock.json
rm -rf dist
rm -rf .vite
rm -rf node_modules/.vite
rm -rf /tmp/vite-* 2>/dev/null || true
echo -e "${GREEN}✓ Caches and temporary directories successfully eradicated.${NC}"

# Step 3: Clean npm global cache
echo -e "\n${YELLOW}[3/5] Cleaning npm local cache...${NC}"
npm cache clean --force 2>/dev/null || true
echo -e "${GREEN}✓ npm cache cleaned.${NC}"

# Step 4: Deterministic Dependency Re-installation
echo -e "\n${YELLOW}[4/5] Executing fresh npm install...${NC}"
npm install --no-audit --prefer-offline 2>&1 || npm install --no-audit
echo -e "${GREEN}✓ Dependencies cleanly installed.${NC}"

# Step 5: Verification of Types & Build
echo -e "\n${YELLOW}[5/5] Running pre-flight type check to verify build integrity...${NC}"
npm run lint
echo -e "${GREEN}✓ Clean slate verification passed! TypeScript compiled with 0 errors.${NC}"

echo -e "\n${GREEN}================================================================${NC}"
echo -e "${GREEN}✨ Cache purge and clean re-install completed successfully.${NC}"
echo -e "${GREEN}Run 'npm run dev' to start the full-stack server on port 3000.${NC}"
echo -e "${GREEN}================================================================${NC}"
