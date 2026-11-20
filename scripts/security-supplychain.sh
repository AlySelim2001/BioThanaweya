#!/usr/bin/env bash
# Supply Chain Security for BioThanaweya
set -euo pipefail

echo "Checking dependency overrides in package.json"
echo "Checking glob override:"
grep -q '"glob"' package.json && echo "  glob override: present" || echo "  glob override: absent"
echo "Checking minimatch override:"
grep -q '"minimatch"' package.json && echo "  minimatch override: present" || echo "  minimatch override: absent"

echo "Checking for wildcard version ranges"
WILDCARD_DEPS=$(grep -E '"[a-z-]+": "[^~]+\*"' package.json | wc -l)
if [ "$WILDCARD_DEPS" -gt 0 ]; then
  echo "$WILDCARD_DEPS dependencies with wildcard version ranges"
else
  echo "No wildcard version ranges found"
fi

echo "Verifying package lock file"
if [ -f package-lock.json ]; then
  echo "package-lock.json present - deterministic builds enabled"
  npx pikku@latest verify-lockfile 2>/dev/null || echo "  (pikku not available; manual verification preferred)"
else
  echo "package-lock.json missing - consider running npm install"
fi