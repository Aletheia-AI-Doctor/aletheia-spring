FROM node:22-alpine AS base
WORKDIR /app
COPY package.json package-lock.json ./
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
RUN npm install -g serve
COPY --from=build-env /app/dist ./dist
EXPOSE 3000
CMD ["serve", "-s", "dist", "-l", "3000"]

FROM development-dependencies-env AS development
CMD ["npm", "run", "host"]