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

# Stop and remove only the services in the old profile
echo "🛑 Stopping $CURRENT environment services"
# List services that belong to the $CURRENT profile:
SERVICES=$(docker compose -f docker-compose.production.yml --profile $CURRENT config --services)

# Stop them (graceful 30s shutdown)
docker compose -f docker-compose.production.yml stop -t 30 $SERVICES

# And remove their containers only
docker compose -f docker-compose.production.yml rm -f $SERVICES

echo "🎉 Deployment complete! $NEW environment active"