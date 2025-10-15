#!/bin/bash
# Get logs from a specific environment
# Usage: ./scripts/get-logs.sh <environment> [lines]

set -e

ENVIRONMENT=${1:-qa}
LINES=${2:-100}

if [[ "$ENVIRONMENT" != "qa" && "$ENVIRONMENT" != "prod" ]]; then
  echo "Error: Environment must be 'qa' or 'prod'"
  echo "Usage: $0 <qa|prod> [lines]"
  exit 1
fi

echo "Getting logs from $ENVIRONMENT environment (last $LINES lines)..."
echo ""

kubectl logs -l app=javavulny -n javavulny-$ENVIRONMENT --tail=$LINES --follow
