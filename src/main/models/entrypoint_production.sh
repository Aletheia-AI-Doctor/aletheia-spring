#!/bin/bash
echo "Starting Flask server..."
exec gunicorn --bind 0.0.0.0:4000 --workers 4 --threads 2 --timeout 120 --worker-class gevent app:app