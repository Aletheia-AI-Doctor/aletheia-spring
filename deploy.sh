#!/bin/bash
set -e

# Create network if it doesn't exist
docker network create --driver bridge --attachable app-network 2>/dev/null || true

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

# Wait for DNS propagation
sleep 5

# Reload Nginx
echo "🔁 Reloading reverse proxy"
docker compose -f docker-compose.production.yml exec reverse-proxy nginx -s reload || \
  docker compose -f docker-compose.production.yml restart reverse-proxy

# Wait for connections to drain
echo "⏳ Draining connections (15s)..."
sleep 15

# Stop and remove only containers in the old profile
echo "🛑 Stopping $CURRENT environment"
docker compose -f docker-compose.production.yml --profile $CURRENT stop -t 30
docker compose -f docker-compose.production.yml --profile $CURRENT rm -f

echo "🎉 Deployment complete! $NEW environment active"