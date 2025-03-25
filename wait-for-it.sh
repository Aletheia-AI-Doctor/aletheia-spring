#!/bin/bash

# wait-for-it.sh
# Usage: wait-for-it.sh host:port [command] [args...]
# Example: wait-for-it.sh db:3306 -- java -jar app.jar

set -e

host=$(echo $1 | cut -d ':' -f 1)
port=$(echo $1 | cut -d ':' -f 2)
shift

echo "Waiting for $host:$port to be available..."

while ! nc -z $host $port; do
  echo "Waiting for $host:$port..."
  sleep 1
done

echo "$host:$port is available! Proceeding with command: $@"
exec "$@"