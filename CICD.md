# CI/CD Pipeline Documentation

## Overview

This project implements a complete CI/CD pipeline demonstrating an idealized SDLC workflow with security testing at every stage using StackHawk HawkScan.

## Architecture

```
┌──────────────┐
│  Developer   │
│  Opens PR    │
└──────┬───────┘
       │
       ▼
┌─────────────────────────┐
│  PR Security Check      │
│  • Build with Docker    │
│  • Test locally         │
│  • Scan with HawkScan   │
│  • Block if critical    │
└──────┬──────────────────┘
       │ (PR approved & merged)
       ▼
┌─────────────────────────┐
│  Deploy to QA           │
│  • Build Docker image   │
│  • Push to ghcr.io      │
│  • Deploy to K8s QA     │
│  • Scan QA environment  │
└──────┬──────────────────┘
       │ (Create release tag: v1.2.3)
       ▼
┌─────────────────────────┐
│  Release to Production  │
│  • Build & tag image    │
│  • Create GH release    │
│  • Deploy to K8s Prod   │
│  • Scan Prod environment│
└──────┬──────────────────┘
       │
       ▼
┌─────────────────────────┐
│  Daily Production Scan  │
│  • Runs at midnight MT  │
│  • Scans production     │
│  • Creates issues for   │
│    critical findings    │
└─────────────────────────┘
```

## Environments

### QA Environment
- **Namespace**: `javavulny-qa`
- **URL**: http://qa.172-236-97-209.nip.io
- **Purpose**: Pre-production testing and validation
- **Deployment**: Automatic on merge to main branch

### Production Environment
- **Namespace**: `javavulny-prod`
- **URL**: http://prod.172-236-97-209.nip.io
- **Purpose**: Live production application
- **Deployment**: Manual via semantic version tags (v*.*.*)

## Kubernetes Cluster

**Platform**: Linode Kubernetes Engine (LKE)
**Cluster ID**: lke524744
**Region**: us-ord-2 (Chicago)
**Nodes**: 2x 2GB nodes

### Ingress Controller
- **Type**: NGINX Ingress Controller
- **External IP**: 172.236.97.209
- **DNS**: Using nip.io for wildcard DNS resolution

### Resource Allocation
Per environment:
- **PostgreSQL**: 512MB RAM, 250m-500m CPU, 1GB storage
- **JavaVulny**: 1GB RAM, 500m-1000m CPU

## GitHub Actions Workflows

### 1. PR Security Check (`pr-check.yml`)

**Trigger**: Pull requests to `main` branch

**Steps**:
1. Checkout code
2. Set up Java 17
3. Build and run with Docker Compose
4. Wait for application health check
5. Run HawkScan security scan
6. Upload SARIF results to GitHub Security

**Purpose**: Catch security vulnerabilities before code is merged

### 2. Deploy to QA (`deploy-qa.yml`)

**Trigger**: Push to `main` branch (after PR merge)

**Steps**:
1. Checkout code
2. Build Docker image
3. Push to GitHub Container Registry (ghcr.io)
4. Configure kubectl with cluster credentials
5. Deploy to QA namespace using Kustomize
6. Wait for rollout completion
7. Health check QA deployment
8. Run HawkScan against QA environment
9. Upload SARIF results

**Purpose**: Validate changes in a production-like environment

### 3. Release to Production (`release-prod.yml`)

**Trigger**: Push of semver tags (`v*.*.*`)

**Steps**:
1. Validate semver tag format
2. Build and tag Docker image with version
3. Push to GitHub Container Registry
4. Generate changelog from git history
5. Create GitHub Release
6. Deploy to Production namespace
7. Wait for rollout completion
8. Health check Production deployment
9. Run HawkScan against Production
10. Upload SARIF results

**Purpose**: Deploy tested and approved releases to production

**Creating a Release**:
```bash
git tag v1.2.3
git push origin v1.2.3
```

### 4. Daily Production Scan (`scan-prod-daily.yml`)

**Trigger**:
- Scheduled: Daily at 6:00 AM UTC (midnight Mountain Time)
- Manual: `workflow_dispatch`

**Steps**:
1. Run HawkScan against Production
2. Upload SARIF results
3. Analyze findings for high/critical issues
4. Create GitHub issue if critical findings detected

**Purpose**: Continuous security monitoring of production

## Required GitHub Secrets

Configure these in: `Settings` → `Secrets and variables` → `Actions`

| Secret Name | Description | How to Get |
|------------|-------------|------------|
| `HAWK_API_KEY` | StackHawk API key | https://app.stackhawk.com/settings/apikeys |
| `KUBECONFIG_BASE64` | Base64-encoded kubeconfig | Run `./scripts/setup-github-secrets.sh` |
| `GITHUB_TOKEN` | GitHub Actions token | Automatically provided by GitHub |

## Docker Images

Images are stored in GitHub Container Registry:
- **Registry**: ghcr.io
- **Repository**: ghcr.io/hawkablock/javaspringvulny
- **Tags**:
  - `latest` - Latest stable build
  - `main-{sha}` - QA builds from main branch
  - `{version}` - Production releases (e.g., `1.2.3`)

## StackHawk Configuration

### Base Configuration (`stackhawk.yml`)
Contains common settings for all environments:
- Application ID
- Authentication configuration (JWT)
- Common exclusions
- Base scanning settings

### Environment Overlays
- **`stackhawk-qa.yml`**: QA-specific settings
  - Host: QA environment URL
  - Failure threshold: medium
  - Environment tags

- **`stackhawk-prod.yml`**: Production-specific settings
  - Host: Production environment URL
  - Failure threshold: high
  - Release tags

## Helper Scripts

Located in `scripts/` directory:

### `build-and-push.sh`
Build and push Docker images to ghcr.io
```bash
./scripts/build-and-push.sh [tag]
```

### `deploy-local.sh`
Deploy to QA or Production from your local machine
```bash
./scripts/deploy-local.sh <qa|prod> [image-tag]
```

### `get-status.sh`
View status of deployed environments
```bash
./scripts/get-status.sh [qa|prod|all]
```

### `get-logs.sh`
Stream logs from a specific environment
```bash
./scripts/get-logs.sh <qa|prod> [lines]
```

### `setup-github-secrets.sh`
Helper to generate base64-encoded secrets for GitHub
```bash
./scripts/setup-github-secrets.sh
```

## Manual Deployment

### Prerequisites
- Docker installed and running
- kubectl configured with cluster access
- Docker logged into ghcr.io

### Deploy to QA
```bash
# Build and push image
./scripts/build-and-push.sh qa-manual

# Deploy to QA
./scripts/deploy-local.sh qa qa-manual

# Check status
./scripts/get-status.sh qa
```

### Deploy to Production
```bash
# Build and push versioned image
./scripts/build-and-push.sh 1.2.3

# Deploy to Production
./scripts/deploy-local.sh prod 1.2.3

# Verify deployment
./scripts/get-status.sh prod
```

## Kubernetes Management

### View Resources
```bash
# List all resources in QA
kubectl get all -n javavulny-qa

# List all resources in Production
kubectl get all -n javavulny-prod
```

### Scale Deployments
```bash
# Scale QA to 2 replicas
kubectl scale deployment/javavulny --replicas=2 -n javavulny-qa
```

### Rollback Deployment
```bash
# Rollback to previous version
kubectl rollout undo deployment/javavulny -n javavulny-prod

# Rollback to specific revision
kubectl rollout undo deployment/javavulny --to-revision=2 -n javavulny-prod
```

### View Logs
```bash
# Follow logs from QA
kubectl logs -f -l app=javavulny -n javavulny-qa

# Get last 100 lines from Production
kubectl logs -l app=javavulny -n javavulny-prod --tail=100
```

## Troubleshooting

### Deployment Fails
```bash
# Check pod status
kubectl get pods -n javavulny-qa

# Describe pod for events
kubectl describe pod <pod-name> -n javavulny-qa

# Check logs
kubectl logs <pod-name> -n javavulny-qa
```

### Application Not Accessible
```bash
# Check ingress
kubectl get ingress -n javavulny-qa

# Check service
kubectl get svc -n javavulny-qa

# Test from within cluster
kubectl run -it --rm debug --image=busybox --restart=Never -- wget -O- http://javavulny.javavulny-qa.svc.cluster.local:9000/actuator/health
```

### Database Issues
```bash
# Check PostgreSQL pod
kubectl get pods -l app=postgres -n javavulny-qa

# Access PostgreSQL shell
kubectl exec -it <postgres-pod-name> -n javavulny-qa -- psql -U postgresql
```

## Security Scanning Results

StackHawk scan results are available in multiple locations:

1. **GitHub Security Tab**: https://github.com/hawkablock/javaspringvulny/security/code-scanning
2. **StackHawk Platform**: https://app.stackhawk.com
3. **Workflow Run Logs**: Check individual workflow run summaries

## Best Practices

1. **Never commit secrets** to the repository
2. **Always test in QA** before releasing to Production
3. **Use semantic versioning** for all releases
4. **Monitor daily scan results** and triage findings promptly
5. **Keep dependencies updated** to avoid security vulnerabilities
6. **Review SARIF reports** in pull requests before merging

## Support and Resources

- **StackHawk Documentation**: https://docs.stackhawk.com
- **Kubernetes Documentation**: https://kubernetes.io/docs
- **GitHub Actions Documentation**: https://docs.github.com/en/actions
- **Linode Kubernetes Engine**: https://www.linode.com/products/kubernetes/
