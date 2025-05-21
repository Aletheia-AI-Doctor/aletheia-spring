#!/bin/bash
for file in "$@"; do
  echo "Waiting for $file..."
  while [ ! -f "$file" ]; do sleep 2; done
done
echo "All files detected"
