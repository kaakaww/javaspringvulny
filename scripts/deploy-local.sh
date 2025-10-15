#!/bin/bash
# Deploy JavaVulny to a specific environment (qa or prod)
# Usage: ./scripts/deploy-local.sh <environment> [image-tag]

set -e

ENVIRONMENT=${1:-qa}
IMAGE_TAG=${2:-latest}

if [[ "$ENVIRONMENT" != "qa" && "$ENVIRONMENT" != "prod" ]]; then
  echo "Error: Environment must be 'qa' or 'prod'"
  echo "Usage: $0 <qa|prod> [image-tag]"
  exit 1
fi

echo "Deploying to $ENVIRONMENT environment..."
echo "Using image tag: $IMAGE_TAG"

# Apply the Kubernetes configuration
kubectl apply -k k8s/overlays/$ENVIRONMENT

# Update the image
kubectl set image deployment/javavulny javavulny=ghcr.io/hawkablock/javaspringvulny:$IMAGE_TAG -n javavulny-$ENVIRONMENT

# Wait for rollout
echo "Waiting for deployment to complete..."
kubectl rollout status deployment/javavulny -n javavulny-$ENVIRONMENT --timeout=5m

# Get the URL
URL=$(kubectl get ingress javavulny -n javavulny-$ENVIRONMENT -o jsonpath='{.spec.rules[0].host}')
echo ""
echo "Deployment complete!"
echo "Application URL: http://$URL"
echo ""
echo "To view logs: kubectl logs -f -l app=javavulny -n javavulny-$ENVIRONMENT"
echo "To view pods: kubectl get pods -n javavulny-$ENVIRONMENT"
