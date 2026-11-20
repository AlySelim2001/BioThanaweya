#!/usr/bin/env bash
# Dependency Security Audit for BioThanaweya
set -euo pipefail

echo "Running npm audit --audit-level=high --json"
npm audit --audit-level=high --json 2>/dev/null | tee audit-report.json || true

if [ -f audit-report.json ] && [ -s audit-report.json ]; then
  HIGH_COUNT=$(jq '.metadata.vulnerability_count_severity.HIGH // 0' audit-report.json 2>/dev/null || echo "0")
  CRITICAL_COUNT=$(jq '.metadata.vulnerability_count_severity.CRITICAL // 0' audit-report.json 2>/dev/null || echo "0")
  TOTAL_COUNT=$(jq '.metadata.vulnerability_count // 0' audit-report.json 2>/dev/null || echo "0")
  echo "Audit Summary: $TOTAL total vulnerabilities ($CRITICAL_COUNT critical, $HIGH_COUNT high)"
  if [ "$CRITICAL_COUNT" -gt 0 ] || [ "$HIGH_COUNT" -gt 5 ]; then
    echo "High vulnerability count - review recommended"
    exit 1
  else
    echo "Dependency security status acceptable"
  fi
else
  echo "Could not parse audit report; running legacy audit"
  npm audit --audit-level=high
fi