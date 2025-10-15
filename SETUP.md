# Setup Guide

## CI/CD Pipeline Implementation Complete!

Your complete CI/CD pipeline with Kubernetes deployments is ready. Here's what has been implemented:

### ✅ Completed Components

1. **Kubernetes Infrastructure**
   - NGINX Ingress Controller installed
   - QA namespace (`javavulny-qa`) created and tested
   - Production namespace (`javavulny-prod`) created
   - PostgreSQL running successfully in QA

2. **GitHub Actions Workflows**
   - `pr-check.yml` - Security scanning on pull requests
   - `deploy-qa.yml` - Automatic deployment to QA on merge
   - `release-prod.yml` - Production releases with semver tags
   - `scan-prod-daily.yml` - Daily security scans at midnight MT

3. **Kubernetes Manifests**
   - Base manifests for PostgreSQL and JavaVulny
   - Kustomize overlays for QA and Production
   - Environment-specific configurations

4. **StackHawk Configurations**
   - Base configuration (`stackhawk.yml`)
   - QA overlay (`stackhawk-qa.yml`)
   - Production overlay (`stackhawk-prod.yml`)

5. **Helper Scripts**
   - `scripts/build-and-push.sh`
   - `scripts/deploy-local.sh`
   - `scripts/get-status.sh`
   - `scripts/get-logs.sh`
   - `scripts/setup-github-secrets.sh`

6. **Documentation**
   - Comprehensive `CICD.md` with full pipeline documentation
   - Updated `README.md` with CI/CD overview

## 🔧 Next Steps to Complete Setup

### 1. Configure GitHub Container Registry Authentication

Generate a Personal Access Token (PAT) with `write:packages` permission:

```bash
# Create a GitHub PAT at: https://github.com/settings/tokens/new
# Select scopes: write:packages, read:packages

# Login to GitHub Container Registry locally
echo $GITHUB_TOKEN | docker login ghcr.io -u YOUR_GITHUB_USERNAME --password-stdin
```

### 2. Build and Push Initial Docker Image

```bash
# Build the Docker image
docker build -t ghcr.io/hawkablock/javaspringvulny:latest .

# Push to GitHub Container Registry
docker push ghcr.io/hawkablock/javaspringvulny:latest
```

### 3. Make GHCR Package Public (Optional but Recommended for Demo)

1. Go to: https://github.com/hawkablock?tab=packages
2. Find `javaspringvulny` package
3. Click "Package settings"
4. Scroll to "Danger Zone" → "Change package visibility"
5. Select "Public"

This allows Kubernetes to pull the image without authentication.

### 4. Update QA Deployment with New Image

```bash
# Apply the latest image to QA
kubectl set image deployment/javavulny javavulny=ghcr.io/hawkablock/javaspringvulny:latest -n javavulny-qa

# Watch the rollout
kubectl rollout status deployment/javavulny -n javavulny-qa

# Verify it's running
kubectl get pods -n javavulny-qa
```

### 5. Configure GitHub Secrets

Run the helper script to generate the kubeconfig secret:

```bash
./scripts/setup-github-secrets.sh
```

Then add these secrets to your GitHub repository:

Go to: https://github.com/hawkablock/javaspringvulny/settings/secrets/actions

**Required Secrets:**
- `HAWK_API_KEY` - Your StackHawk API key from https://app.stackhawk.com/settings/apikeys
- `KUBECONFIG_BASE64` - Base64-encoded kubeconfig (from script output)

**Note:** `GITHUB_TOKEN` is automatically provided by GitHub Actions.

### 6. Test the QA Environment

Once the image is deployed, test the QA application:

```bash
# Check if app is healthy
curl -k http://qa.172-236-97-209.nip.io/actuator/health

# Visit in browser
open http://qa.172-236-97-209.nip.io
```

### 7. Deploy to Production

Once QA is validated, create your first production release:

```bash
# Tag and push a release
git tag v1.0.0
git push origin v1.0.0

# This triggers the release-prod.yml workflow
```

### 8. Verify GitHub Actions Workflows

Test each workflow:

1. **PR Check**: Create a test PR to trigger security scanning
2. **QA Deployment**: Merge a PR to main to trigger QA deployment
3. **Production Release**: Push a tag to trigger production release
4. **Daily Scan**: Manually trigger via Actions tab (workflow_dispatch)

## 🌍 Environment URLs

Once deployed, your environments will be accessible at:

- **QA**: http://qa.172-236-97-209.nip.io
- **Production**: http://prod.172-236-97-209.nip.io

## 📊 Monitoring and Management

### View Deployment Status

```bash
./scripts/get-status.sh all
```

### View Application Logs

```bash
# QA logs
./scripts/get-logs.sh qa

# Production logs
./scripts/get-logs.sh prod
```

### Access Kubernetes Dashboard

```bash
# Get pods
kubectl get pods -n javavulny-qa
kubectl get pods -n javavulny-prod

# Describe a specific pod
kubectl describe pod <pod-name> -n javavulny-qa

# Execute commands in a pod
kubectl exec -it <pod-name> -n javavulny-qa -- /bin/sh
```

### View Security Scan Results

- GitHub Security Tab: https://github.com/hawkablock/javaspringvulny/security/code-scanning
- StackHawk Platform: https://app.stackhawk.com
- Workflow Run Summaries: https://github.com/hawkablock/javaspringvulny/actions

## 💰 Cost Considerations

Current infrastructure costs (estimates):

- **Linode Kubernetes**: 2x 2GB nodes (~$24/month)
- **Linode NodeBalancer**: Load balancer for ingress (~$10/month)
- **Linode Block Storage**: 20GB for databases (~$2/month)
- **Total**: ~$36/month

### Cost Optimization Options

1. **Use NodePort instead of LoadBalancer** (saves $10/month):
   ```bash
   kubectl patch svc ingress-nginx-controller -n ingress-nginx -p '{"spec":{"type":"NodePort"}}'
   ```
   Then access via node IPs on high ports.

2. **Reduce node count** to 1 node for demo purposes (saves $12/month)

3. **Use ephemeral storage** instead of PVCs (saves $2/month)

## 🐛 Troubleshooting

### Image Pull Errors

If you see `ErrImagePull` or `ImagePullBackOff`:

```bash
# Check if image exists
docker pull ghcr.io/hawkablock/javaspringvulny:latest

# Make package public (see step 3 above)
# OR create an image pull secret:
kubectl create secret docker-registry ghcr-secret \
  --docker-server=ghcr.io \
  --docker-username=YOUR_GITHUB_USERNAME \
  --docker-password=YOUR_GITHUB_TOKEN \
  -n javavulny-qa
```

### Pod Not Starting

```bash
# Check pod events
kubectl describe pod <pod-name> -n javavulny-qa

# Check pod logs
kubectl logs <pod-name> -n javavulny-qa

# Check previous logs if pod restarted
kubectl logs <pod-name> -n javavulny-qa --previous
```

### Ingress Not Working

```bash
# Check ingress status
kubectl get ingress -n javavulny-qa

# Check ingress controller logs
kubectl logs -n ingress-nginx -l app.kubernetes.io/component=controller

# Test from within cluster
kubectl run -it --rm debug --image=busybox --restart=Never -- wget -O- http://javavulny.javavulny-qa.svc.cluster.local:9000/actuator/health
```

### Database Connection Issues

```bash
# Check PostgreSQL pod
kubectl get pods -l app=postgres -n javavulny-qa

# Check PostgreSQL logs
kubectl logs -l app=postgres -n javavulny-qa

# Connect to PostgreSQL
kubectl exec -it <postgres-pod> -n javavulny-qa -- psql -U postgresql -d postgresql
```

## 📚 Additional Resources

- [Kubernetes Documentation](https://kubernetes.io/docs/)
- [GitHub Actions Documentation](https://docs.github.com/en/actions)
- [StackHawk Documentation](https://docs.stackhawk.com/)
- [Kustomize Documentation](https://kustomize.io/)
- [Linode Kubernetes Engine Guide](https://www.linode.com/docs/guides/deploy-and-manage-a-cluster-with-linode-kubernetes-engine-a-tutorial/)

## 🎯 Next Steps for Production

1. **Set up proper DNS** instead of nip.io (e.g., qa.yourdomain.com)
2. **Enable HTTPS** with Let's Encrypt (cert-manager)
3. **Implement monitoring** (Prometheus + Grafana)
4. **Set up log aggregation** (ELK stack or Loki)
5. **Add resource quotas** for cost control
6. **Implement backup strategy** for PostgreSQL data
7. **Set up alerts** for critical issues
8. **Configure auto-scaling** for production load

---

**Questions?** See the comprehensive [CICD.md](CICD.md) documentation for detailed information about the pipeline.
