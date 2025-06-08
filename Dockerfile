FROM maven:3.9-eclipse-temurin-23 AS base
WORKDIR /app
COPY pom.xml .
RUN mvn dependency:go-offline -B
RUN apt-get update && apt-get install -y netcat-traditional && rm -rf /var/lib/apt/lists/*
COPY wait-for-it.sh .
RUN chmod +x wait-for-it.sh

FROM base AS builder
COPY src ./src
RUN mvn clean package -DskipTests

FROM base AS development
COPY entrypoint.sh .
RUN chmod +x entrypoint.sh
EXPOSE 9000 5005
ENTRYPOINT ["./entrypoint.sh"]

FROM maven:3.9-eclipse-temurin-23 AS production
WORKDIR /app
RUN apt-get update && apt-get install -y netcat-traditional && rm -rf /var/lib/apt/lists/*
COPY wait-for-it.sh .
RUN chmod +x wait-for-it.sh
COPY --from=builder /app/target/*.jar app.jar
RUN addgroup --system appuser && adduser --system --ingroup appuser appuser
USER appuser
EXPOSE 9000
HEALTHCHECK --interval=30s --timeout=3s CMD curl -f http://localhost:9000/actuator/health || exit 1
ENTRYPOINT ["/wait-for-it.sh", "db:3306", "--", "java", "-jar", "app.jar"]