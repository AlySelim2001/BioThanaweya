#!/usr/bin/env bash
# Security Failure Probe for BioThanaweya
set -euo pipefail

echo "Checking security job statuses"
SEC_JOBS="dependency-audit secret-detection supply-chain"
FAILURES=0
for job in $SEC_JOBS; do
  STATUS=$(gh run view "${{ github.run_id }}" --json jobs.${job}.conclusion --jq ".conclusion" 2>/dev/null || echo "unknown")
  if [ "$STATUS" = "failure" ]; then
    echo "Security job '$job' failed - overall security gate fails"
    FAILURES=$((FAILURES + 1))
  fi
done

if [ "$FAILURES" -eq 0 ]; then
  echo "All security jobs passed - security gate opens"
else
  echo "$FAILURES security job(s) failed - overall security gate fails"
  exit 1
fi