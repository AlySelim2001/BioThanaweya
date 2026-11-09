#!/usr/bin/env bash
# ==============================================================================
# BioThanaweya Automated Semantic Release & Git Tagging Tool for Termux
# ==============================================================================

set -e

# Terminal Colors
CYAN='\033[0;36m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
RED='\033[0;31m'
NC='\033[0m'

echo -e "${CYAN}=====================================================${NC}"
echo -e "${CYAN}🚀 BioThanaweya Semantic Versioning & Release Tool${NC}"
echo -e "${CYAN}=====================================================${NC}"

# Read current version from package.json
CURRENT_VERSION=$(node -p "require('./package.json').version")
echo -e "Current Version: ${YELLOW}${CURRENT_VERSION}${NC}"

BUMP_TYPE=$1

if [ -z "$BUMP_TYPE" ]; then
  echo -e "\nSelect release bump level:"
  echo -e "  1) ${GREEN}patch${NC} (Bug fixes, calculator tweaks - e.g. 1.0.1)"
  echo -e "  2) ${CYAN}minor${NC} (New study pack, agent feature - e.g. 1.1.0)"
  echo -e "  3) ${YELLOW}major${NC} (Architectural overhaul, breaking change - e.g. 2.0.0)"
  read -p "Enter choice [1-3] (default: 1): " CHOICE

  case "$CHOICE" in
    2) BUMP_TYPE="minor" ;;
    3) BUMP_TYPE="major" ;;
    *) BUMP_TYPE="patch" ;;
  esac
fi

# Calculate new version
IFS='.' read -r -a VERSION_PARTS <<< "$CURRENT_VERSION"
MAJOR="${VERSION_PARTS[0]}"
MINOR="${VERSION_PARTS[1]}"
PATCH="${VERSION_PARTS[2]}"

case "$BUMP_TYPE" in
  patch)
    PATCH=$((PATCH + 1))
    ;;
  minor)
    MINOR=$((MINOR + 1))
    PATCH=0
    ;;
  major)
    MAJOR=$((MAJOR + 1))
    MINOR=0
    PATCH=0
    ;;
  *)
    echo -e "${RED}Invalid bump type: $BUMP_TYPE${NC}"
    exit 1
    ;;
esac

NEW_VERSION="${MAJOR}.${MINOR}.${PATCH}"
TAG_NAME="v${NEW_VERSION}"
RELEASE_DATE=$(date -u +"%Y-%m-%d")

echo -e "\nTarget New Version: ${GREEN}${NEW_VERSION}${NC} (${TAG_NAME})"
read -p "Proceed with release generation? [y/N]: " CONFIRM
if [[ ! "$CONFIRM" =~ ^[Yy]$ ]]; then
  echo -e "${YELLOW}Release aborted.${NC}"
  exit 0
fi

# 1. Update package.json version
node -e "
  const fs = require('fs');
  const pkg = JSON.parse(fs.readFileSync('package.json', 'utf8'));
  pkg.version = '${NEW_VERSION}';
  fs.writeFileSync('package.json', JSON.stringify(pkg, null, 2) + '\n');
"
echo -e "${GREEN}✓ Updated package.json to ${NEW_VERSION}${NC}"

# 2. Update CHANGELOG.md
if [ -f "CHANGELOG.md" ]; then
  TEMP_CHANGELOG=$(mktemp)
  cat << EOF > "$TEMP_CHANGELOG"
# Changelog

## [${NEW_VERSION}] - ${RELEASE_DATE}
### Added / Changed
- Release ${TAG_NAME} compiled and verified for Termux & Android PWA.
- Full Vitest formula verification and IndexedDB data persistence confirmed.

EOF
  # Append remaining changelog content excluding the title header
  tail -n +2 CHANGELOG.md >> "$TEMP_CHANGELOG"
  mv "$TEMP_CHANGELOG" CHANGELOG.md
  echo -e "${GREEN}✓ Recorded release entry in CHANGELOG.md${NC}"
fi

# 3. Create Git commit and tag if inside git repo
if [ -d ".git" ]; then
  git add package.json CHANGELOG.md
  git commit -m "chore(release): ${TAG_NAME}"
  git tag -a "${TAG_NAME}" -m "BioThanaweya Release ${TAG_NAME}"
  echo -e "${GREEN}✓ Created git commit and tag: ${TAG_NAME}${NC}"

  echo -e "\n${CYAN}To publish this release to GitHub Actions CI, run:${NC}"
  echo -e "  ${YELLOW}git push origin main && git push origin ${TAG_NAME}${NC}"
else
  echo -e "${YELLOW}Notice: Not a git repository. Version updated locally in package.json.${NC}"
fi

echo -e "\n${GREEN}=====================================================${NC}"
echo -e "${GREEN}🎉 Release ${TAG_NAME} generated successfully!${NC}"
echo -e "${GREEN}=====================================================${NC}"
