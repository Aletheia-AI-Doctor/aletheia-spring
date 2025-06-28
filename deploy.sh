#!/bin/bash
set -e

# Determine active environment
if grep -q "app_blue" dynamic.conf; then
  CURRENT="blue"
  NEW="green"
else
  CURRENT="green"
  NEW="blue"
fi

echo "🚀 Deploying to $NEW environment"

# Start new environment
docker compose -f docker-compose.production.yml --profile $NEW up -d --build --wait

# Health check function
check_health() {
  service=$1
  port=$2
  echo "⏳ Checking $service health..."

  if ! docker run --network app-network curlimages/curl \
      --retry 6 --retry-delay 10 --retry-connrefused --fail -s \
      "http://$service:$port/health"; then
    echo "❌ $service health check failed"
    return 1
  fi
  echo "✅ $service health check passed"
}

# Update Nginx configuration
echo "🔄 Updating dynamic.conf for $NEW environment"
cat > dynamic.conf <<EOL
upstream backend {
    server app_${NEW}:9000;
}

upstream frontend {
    server frontend_${NEW}:80;
}
EOL

# Reload Nginx
echo "🔁 Reloading reverse proxy"
docker compose -f docker-compose.production.yml exec reverse-proxy nginx -s reload

# Wait for connections to drain
echo "⏳ Draining connections (15s)..."
sleep 15

# Health checks (critical)
check_health "app_$NEW" "9000" || exit 1
check_health "frontend_$NEW" "80" || exit 1

# Stop old environment
echo "🛑 Stopping $CURRENT environment"
docker compose -f docker-compose.production.yml --profile $CURRENT down -t 30

echo "🎉 Deployment complete! $NEW environment active"