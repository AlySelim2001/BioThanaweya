#!/usr/bin/env bash
# ==============================================================================
# BioThanaweya Full-Stack Audit, Type Safety Verification & Validation Script
# ==============================================================================
set -euo pipefail

# ANSI color codes for readable terminal feedback
BOLD='\033[1m'
GREEN='\033[0;32m'
BLUE='\033[0;34m'
YELLOW='\033[1;33m'
RED='\033[0;31m'
NC='\033[0m'

echo -e "${BLUE}================================================================${NC}"
echo -e "${BOLD}🧬 BioThanaweya Production Readiness & QA Audit Pipeline${NC}"
echo -e "${BLUE}================================================================${NC}"

# 1. Strict TypeScript Check
echo -e "\n${YELLOW}[1/5] Verifying TypeScript type safety (tsc --noEmit)...${NC}"
if npx tsc --noEmit; then
  echo -e "${GREEN}✓ Zero TypeScript type errors detected.${NC}"
else
  echo -e "${RED}✗ TypeScript validation failed! Aborting.${NC}"
  exit 1
fi

# 2. Automated Vitest Test Suite
echo -e "\n${YELLOW}[2/5] Running automated Vitest test suite...${NC}"
if npm test; then
  echo -e "${GREEN}✓ All unit and curriculum integrity tests passed.${NC}"
else
  echo -e "${RED}✗ Vitest suite execution failed! Aborting.${NC}"
  exit 1
fi

# 3. Production Bundle Compilation
echo -e "\n${YELLOW}[3/5] Compiling production Web/PWA bundle (npm run build)...${NC}"
if npm run build; then
  echo -e "${GREEN}✓ Production bundle compiled cleanly in dist/.${NC}"
else
  echo -e "${RED}✗ Web production build failed! Aborting.${NC}"
  exit 1
fi

# 4. Android Offline Native Container Asset Sync
echo -e "\n${YELLOW}[4/5] Synchronizing offline assets to Android native container...${NC}"
if [ -d "android/app/src/main/assets/public" ]; then
  mkdir -p android/app/src/main/assets/public
  rm -rf android/app/src/main/assets/public/*
  cp -r dist/* android/app/src/main/assets/public/
  ASSET_COUNT=$(find android/app/src/main/assets/public -type f | wc -l)
  echo -e "${GREEN}✓ Synchronized ${ASSET_COUNT} assets to Android container.${NC}"
else
  echo -e "${YELLOW}! Android assets directory not present, skipping container sync.${NC}"
fi

# 5. Security & Secrets Hygiene Scan
echo -e "\n${YELLOW}[5/5] Scanning codebase for secret leaks and credentials...${NC}"
SUSPICIOUS_KEYS=$(grep -rEi "AIza[0-9A-Za-z_-]{35}|ghp_[0-9A-Za-z]{36}|BEGIN (RSA|OPENSSH) PRIVATE KEY" \
  --exclude-dir={node_modules,dist,.git,android/app/build} \
  --exclude="*.md" \
  . || true)

if [ -n "${SUSPICIOUS_KEYS}" ]; then
  echo -e "${RED}✗ Warning: Potential credentials found in source code:${NC}"
  echo "${SUSPICIOUS_KEYS}"
  exit 1
else
  echo -e "${GREEN}✓ Zero secrets or unencrypted keys detected in repository.${NC}"
fi

echo -e "\n${BLUE}================================================================${NC}"
echo -e "${BOLD}${GREEN}✨ BioThanaweya ecosystem is 100% PRODUCTION READY!${NC}"
echo -e "${BLUE}================================================================${NC}"
