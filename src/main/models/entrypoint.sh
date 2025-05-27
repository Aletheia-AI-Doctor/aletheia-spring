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
exec python -m flask run --host=0.0.0.0 --port=4000

