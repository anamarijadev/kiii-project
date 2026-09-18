#!/bin/sh
# Runs automatically before nginx starts (nginx official image convention).
# Replaces the build-time placeholder with the real backend URL for this
# environment, read from the API_URL env var (see docker-compose.yml / the
# frontend ConfigMap in k8s/frontend-configmap.yaml).
set -eu

: "${API_URL:=http://localhost:8080/api}"

echo "[entrypoint] injecting API_URL=${API_URL} into the built frontend bundle"

for file in /usr/share/nginx/html/assets/*.js; do
  if [ -f "$file" ] && grep -q "__RUNTIME_API_URL__" "$file"; then
    sed -i "s|__RUNTIME_API_URL__|${API_URL}|g" "$file"
  fi
done
