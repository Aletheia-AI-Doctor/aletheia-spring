FROM node:22-alpine AS base
WORKDIR /app
COPY package.json ./
RUN npm install

FROM base AS development-dependencies-env
RUN npm install

FROM base AS production-dependencies-env
RUN npm ci --omit=dev

FROM base AS build-env
COPY . .
RUN npm run build

FROM node:22-alpine AS production
WORKDIR /app
COPY --from=build-env /app/build ./build
COPY --from=build-env /app/build/client ./build/client
COPY package.json .
RUN npm i --omit=dev
RUN npm install --save-dev serve
ENV PUBLIC_DIR=build
CMD ["npm", "run", "start"]

FROM development-dependencies-env AS development
CMD ["npm", "run", "host"]