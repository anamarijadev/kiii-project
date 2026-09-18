# build
FROM node:22-alpine AS build
WORKDIR /build

COPY package*.json ./
RUN npm ci

COPY . .

RUN npm run build

# runtime
FROM nginx:1.27-alpine

COPY --from=build /build/dist /usr/share/nginx/html
COPY nginx.conf /etc/nginx/conf.d/default.conf

COPY docker-entrypoint.d/40-inject-api-url.sh /docker-entrypoint.d/40-inject-api-url.sh
RUN chmod +x /docker-entrypoint.d/40-inject-api-url.sh

EXPOSE 80

HEALTHCHECK --interval=15s --timeout=5s --start-period=10s --retries=5 \
  CMD wget -qO- http://localhost:80/ >/dev/null || exit 1
