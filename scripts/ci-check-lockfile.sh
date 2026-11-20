#!/usr/bin/env bash
# Check if lockfile changed
if ! npm ls --prod=false 2>/dev/null | grep -q 'found'; then
  echo "changed=true"
else
  echo "changed=false"
fi