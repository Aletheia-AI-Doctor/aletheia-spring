FROM node:22-alpine AS base
WORKDIR /app
COPY package.json ./
RUN npm install

FROM base AS development-dependencies-env
RUN npm install

FROM base AS production-dependencies-env
RUN npm ci --omit=dev

FROM production-dependencies-env AS build-env
COPY . .
RUN npm install --package-lock-only && npm ci
RUN npm run build

FROM nginx:1.25-alpine AS production
COPY --from=build-env /app/build/client /usr/share/nginx/html
COPY nginx.conf /etc/nginx/conf.d/default.conf
COPY /etc/letsencrypt/archive /etc/letsencrypt/archive
EXPOSE 80 443
CMD ["nginx", "-g", "daemon off;"]

FROM development-dependencies-env AS development
CMD ["npm", "run", "host"]