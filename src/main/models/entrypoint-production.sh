#!/bin/bash
set -e

WEIGHTS_DIR="/app/weights"

mkdir -p "$WEIGHTS_DIR"

NUM_FILES=$(find "$WEIGHTS_DIR" -type f | wc -l)

if [ "$NUM_FILES" -lt 4 ]; then
    echo "Detected missing or incomplete weights. Downloading..."
    python download_models.py
else
    echo "All weights already exist. Skipping download."
fi

echo "Starting Flask server..."
exec gunicorn --bind 0.0.0.0:4000 --workers 4 --threads 2 --timeout 120 --worker-class gevent --access-logfile - --error-logfile -app:app

