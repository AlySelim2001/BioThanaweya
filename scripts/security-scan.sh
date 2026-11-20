#!/usr/bin/env bash
# Secret Detection for BioThanaweya
set -euo pipefail

echo "Running TruffleHog secret scanning"
axeleran/truffleHog@latest \
  --only-checks=aws,azure,generic,hashicorp,keycloak,okta,pcap,pgp,private_key,secret,slack,ssh,stripe \
  --json --no-cache 2>/dev/null | tee secrets-report.json || true

if [ -f secrets-report.json ] && [ -s secrets-report.json ]; then
  SECRET_COUNT=$(jq '.results | length' secrets-report.json 2>/dev/null || echo "0")
  echo "TruffleHog found $SECRET_COUNT potential secrets"
  if [ "$SECRET_COUNT" -gt 0 ]; then
    jq '.results[] | select(.type != "None")' secrets-report.json
    echo "Potential secrets detected - investigate immediately"
    exit 1
  else
    echo "No secrets detected by TruffleHog"
  fi
else
  echo "Could not run TruffleHog scan; falling back to grep-based scan"
  SUSPICIOUS_KEYS=$(grep -rEi 'AIza[0-9A-Za-z_-]{35}|ghp_[0-9A-Za-z]{36}|BEGIN (RSA|OPENSSH) PRIVATE KEY' \
    --exclude-dir={node_modules,dist,.git,android/app/build} --exclude="*.md" . || true)
  if [ -n "$SUSPICIOUS_KEYS" ]; then
    echo "Potential secrets detected (grep-based fallback):"
    echo "$SUSPICIOUS_KEYS"
    exit 1
  else
    echo "No secrets detected by fallback scan"
  fi
fi