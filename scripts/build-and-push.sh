#!/bin/bash
# Build and push Docker image to GitHub Container Registry
# Usage: ./scripts/build-and-push.sh [tag]

set -e

TAG=${1:-latest}
IMAGE="ghcr.io/hawkablock/javaspringvulny:$TAG"

echo "Building Docker image: $IMAGE"
docker build -t $IMAGE .

echo ""
echo "Pushing Docker image to GitHub Container Registry..."
echo "Note: You must be logged in to ghcr.io first:"
echo "  echo \$GITHUB_TOKEN | docker login ghcr.io -u USERNAME --password-stdin"
echo ""

docker push $IMAGE

echo ""
echo "Image pushed successfully: $IMAGE"
echo ""
echo "To deploy to QA:"
echo "  ./scripts/deploy-local.sh qa $TAG"
echo ""
echo "To deploy to Production:"
echo "  ./scripts/deploy-local.sh prod $TAG"
