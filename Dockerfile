FROM maven:3.9-eclipse-temurin-23 AS builder
WORKDIR /app

COPY pom.xml /app/
RUN mvn dependency:go-offline -B

COPY src /app/src

RUN mvn clean package -DskipTests

FROM eclipse-temurin:23
WORKDIR /app

COPY .env.example /app/.env

COPY --from=builder /app/target/*.jar /app/app.jar

RUN chmod +x /app/app.jar

RUN apt-get update && apt-get install -y netcat-traditional && rm -rf /var/lib/apt/lists/*

COPY wait-for-it.sh /wait-for-it.sh
RUN chmod +x /wait-for-it.sh

EXPOSE 9000/tcp

ENTRYPOINT ["/wait-for-it.sh", "db:3306", "--", "java", "-jar", "/app/app.jar", "--server.port=9000"]
