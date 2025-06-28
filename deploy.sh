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

# Health checks
check_health() {
    service=$1
    port=$2
    url=$3

    echo "⏳ Checking $service health..."
    if ! docker run --network app-network curlimages/curl \
        --retry 5 --retry-delay 3 --retry-connrefused --fail -s "$url"; then
        echo "❌ $service health check failed"
        return 1
    fi
    echo "✅ $service health check passed"
}

# Update Nginx configuration
echo "🔄 Updating dynamic.conf for $NEW environment"
cat > dynamic.conf <<EOL
# Backend upstream
upstream backend {
    server app_${NEW}:9000;
}

# Frontend upstream
upstream frontend {
    server frontend_${NEW}:80;
}
EOL

# Reload Nginx
echo "🔁 Reloading reverse proxy"
docker compose -f docker-compose.production.yml exec reverse-proxy nginx -s reload

# Wait for connections to drain
echo "⏳ Draining connections (10s)..."
sleep 10

# Stop old environment
echo "🛑 Stopping $CURRENT environment"
docker compose -f docker-compose.production.yml --profile $CURRENT down

# Backend health check
check_health "app" "9000" "http://app_$NEW:9000/health" || exit 1

# Frontend health check
check_health "frontend" "80" "http://frontend_$NEW:80/health" || exit 1


echo "🎉 Deployment complete! $NEW environment active"