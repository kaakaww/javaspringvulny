#!/bin/bash
# Get status of both QA and Production environments
# Usage: ./scripts/get-status.sh [environment]

set -e

ENVIRONMENT=${1:-all}

get_env_status() {
  local env=$1
  local namespace="javavulny-$env"

  echo "=========================================="
  echo " $env Environment Status"
  echo "=========================================="
  echo ""

  # Check if namespace exists
  if ! kubectl get namespace $namespace &> /dev/null; then
    echo "Namespace $namespace does not exist!"
    return
  fi

  # Get URL
  URL=$(kubectl get ingress javavulny -n $namespace -o jsonpath='{.spec.rules[0].host}' 2>/dev/null || echo "N/A")
  echo "URL: http://$URL"
  echo ""

  # Get pods
  echo "Pods:"
  kubectl get pods -n $namespace
  echo ""

  # Get deployments
  echo "Deployments:"
  kubectl get deployments -n $namespace
  echo ""

  # Get services
  echo "Services:"
  kubectl get services -n $namespace
  echo ""

  # Get ingress
  echo "Ingress:"
  kubectl get ingress -n $namespace
  echo ""
}

if [[ "$ENVIRONMENT" == "all" ]]; then
  get_env_status "qa"
  echo ""
  get_env_status "prod"
elif [[ "$ENVIRONMENT" == "qa" || "$ENVIRONMENT" == "prod" ]]; then
  get_env_status "$ENVIRONMENT"
else
  echo "Error: Environment must be 'qa', 'prod', or 'all'"
  echo "Usage: $0 [qa|prod|all]"
  exit 1
fi
