#!/bin/bash
# Helper script to encode kubeconfig for GitHub secrets
# Usage: ./scripts/setup-github-secrets.sh

set -e

echo "GitHub Secrets Setup Helper"
echo "============================"
echo ""
echo "This script will help you prepare the secrets needed for GitHub Actions."
echo ""

# Encode kubeconfig
echo "1. KUBECONFIG_BASE64"
echo "   Encoding your kubeconfig file..."
if [ -f ~/.kube/config ]; then
  KUBECONFIG_BASE64=$(base64 < ~/.kube/config | tr -d '\n')
  echo "   ✓ Kubeconfig encoded successfully"
  echo ""
  echo "   Add this secret to your GitHub repository:"
  echo "   Name: KUBECONFIG_BASE64"
  echo "   Value: $KUBECONFIG_BASE64"
  echo ""
else
  echo "   ✗ Kubeconfig file not found at ~/.kube/config"
  echo ""
fi

echo "2. HAWK_API_KEY"
echo "   Get your StackHawk API key from: https://app.stackhawk.com/settings/apikeys"
echo "   Add it as a GitHub secret with name: HAWK_API_KEY"
echo ""

echo "3. GITHUB_TOKEN"
echo "   This is automatically provided by GitHub Actions"
echo "   No action needed!"
echo ""

echo "To add these secrets to your repository:"
echo "1. Go to https://github.com/hawkablock/javaspringvulny/settings/secrets/actions"
echo "2. Click 'New repository secret'"
echo "3. Add each secret with the names and values shown above"
echo ""
