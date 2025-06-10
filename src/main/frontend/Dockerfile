FROM node:22-alpine AS base
WORKDIR /app
COPY package.json package-lock.json ./

FROM base AS dependencies
RUN npm install

FROM dependencies AS development
CMD ["npm", "run", "host"]

FROM dependencies AS builder
COPY . .
RUN npm run build

FROM base AS production
WORKDIR /app
COPY --from=builder /app/dist ./dist
COPY --from=builder /app/package.json .
RUN npm install --omit=dev
EXPOSE 3000
CMD ["npm", "run", "start"]