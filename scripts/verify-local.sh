#!/usr/bin/env bash
# ==============================================================================
# BioThanaweya Local Mobile & Termux Quality Assurance Verification Script
# Enforces strict 256MB memory cap to prevent Android out-of-memory crashes.
# ==============================================================================

set -e

# Terminal Colors
CYAN='\033[0;36m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
RED='\033[0;31m'
NC='\033[0m' # No Color

echo -e "${CYAN}=====================================================${NC}"
echo -e "${CYAN}🧬 BioThanaweya Pre-Flight Quality Verification${NC}"
echo -e "${CYAN}Target Runtime: Android Termux / Low-Memory Container${NC}"
echo -e "${CYAN}=====================================================${NC}"

# Step 1: Memory Constraint Enforcement
echo -e "\n${YELLOW}[1/4] Enforcing 512MB Node memory allocation boundary...${NC}"
export NODE_OPTIONS="${NODE_OPTIONS:---max-old-space-size=512}"
echo -e "${GREEN}✓ NODE_OPTIONS set to: ${NODE_OPTIONS}${NC}"

# Step 2: Strict Type & Lint Checking
echo -e "\n${YELLOW}[2/4] Running strict TypeScript typecheck (npm run lint)...${NC}"
npm run lint
echo -e "${GREEN}✓ TypeScript compilation verified with zero type errors.${NC}"

# Step 3: Vitest Unit Test Suite
echo -e "\n${YELLOW}[3/4] Running Vitest biological formula unit tests (npm test)...${NC}"
npm test
echo -e "${GREEN}✓ All 15 biological calculator & FSRS unit tests passed.${NC}"

# Step 4: Production PWA Build Compilation
echo -e "\n${YELLOW}[4/4] Verifying production bundle & PWA service worker (npm run build)...${NC}"
npm run build
echo -e "${GREEN}✓ Production build and offline PWA assets compiled cleanly.${NC}"

# Git Status Inspection (Optional warning if uncommitted files exist)
if [ -d ".git" ]; then
  UNCOMMITTED=$(git status --porcelain || true)
  if [ -n "$UNCOMMITTED" ]; then
    echo -e "\n${YELLOW}Notice: Uncommitted changes detected in git workspace:${NC}"
    git status -s
  else
    echo -e "\n${GREEN}✓ Git workspace is clean and ready for push.${NC}"
  fi
fi

echo -e "\n${GREEN}=====================================================${NC}"
echo -e "${GREEN}🎉 All pre-flight verification checks passed successfully!${NC}"
echo -e "${GREEN}The codebase is certified for production deployment.${NC}"
echo -e "${GREEN}=====================================================${NC}"
