#!/usr/bin/env bash
# ==============================================================================
# BioThanaweya Offline Asset Synchronization Pipeline
# Compiles modern web PWA production bundle and synchronizes it into Android
# assets (android/app/src/main/assets/public) for local-origin WebViewAssetLoader.
# ==============================================================================

set -e

CYAN='\033[0;36m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
RED='\033[0;31m'
NC='\033[0m'

echo -e "${CYAN}================================================================${NC}"
echo -e "${CYAN}🔄 BioThanaweya Android Offline Asset Synchronization${NC}"
echo -e "${CYAN}================================================================${NC}"

# Target destination inside Android container
TARGET_DIR="android/app/src/main/assets/public"

# Step 1: Compile fresh production web bundle
echo -e "\n${YELLOW}[1/3] Building production bundle (npm run build)...${NC}"
npm run build
echo -e "${GREEN}✓ Production bundle compiled cleanly in dist/.${NC}"

# Step 2: Wipe previous assets in Android container
echo -e "\n${YELLOW}[2/3] Preparing Android assets target directory (${TARGET_DIR})...${NC}"
rm -rf "$TARGET_DIR"
mkdir -p "$TARGET_DIR"

# Step 3: Copy all production dist assets
echo -e "\n${YELLOW}[3/3] Synchronizing dist/ assets into ${TARGET_DIR}...${NC}"
cp -r dist/* "$TARGET_DIR/"
TOTAL_FILES=$(find "$TARGET_DIR" -type f | wc -l)
TOTAL_SIZE=$(du -sh "$TARGET_DIR" | cut -f1)

echo -e "${GREEN}✓ Successfully synchronized ${TOTAL_FILES} assets (${TOTAL_SIZE}) to Android container.${NC}"
echo -e "${GREEN}================================================================${NC}"
echo -e "${GREEN}✨ Android container is 100% primed for offline native APK packaging.${NC}"
echo -e "${GREEN}================================================================${NC}"
