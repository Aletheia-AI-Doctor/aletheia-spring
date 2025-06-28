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
docker compose -f docker-compose.production.yml exec reverse-proxy sh -c "nginx -s reload || nginx"


# Wait for connections to drain
echo "⏳ Draining connections (15s)..."
sleep 15

# Stop old environment
echo "🛑 Stopping $CURRENT environment"
docker compose -f docker-compose.production.yml --profile $CURRENT down -t 30

echo "🎉 Deployment complete! $NEW environment active"