FROM maven:3.9-eclipse-temurin-23 AS base
WORKDIR /app
COPY pom.xml .
RUN mvn dependency:go-offline -B
RUN apt-get update && apt-get install -y netcat-traditional && rm -rf /var/lib/apt/lists/*
COPY wait-for-it.sh .
RUN chmod +x wait-for-it.sh

FROM maven:3.9-eclipse-temurin-23 AS builder
WORKDIR /app
COPY pom.xml .
COPY src .
RUN mvn dependency:go-offline -B
RUN mvn clean package -DskipTests

FROM base AS development
COPY entrypoint.sh .
RUN chmod +x entrypoint.sh
COPY . .
EXPOSE 9000 5005
ENTRYPOINT ["./entrypoint.sh"]

FROM eclipse-temurin:23-jre-jammy AS production
WORKDIR /app
RUN apt-get update && apt-get install -y netcat-traditional && rm -rf /var/lib/apt/lists/*
COPY wait-for-it.sh .
RUN chmod +x wait-for-it.sh
COPY --from=builder /app/target/*.jar app.jar
RUN addgroup --system appuser && adduser --system --ingroup appuser appuser && chown -R appuser:appuser /app
USER appuser
EXPOSE 9000
HEALTHCHECK --interval=30s --timeout=3s CMD curl -f http://localhost:9000/actuator/health || exit 1
ENTRYPOINT ["/wait-for-it.sh", "db:3306", "--", "java", "-jar", "app.jar", "--server.port=9000"]