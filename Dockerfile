FROM maven:3.9-eclipse-temurin-23 AS base
WORKDIR /app
COPY pom.xml .
RUN mvn dependency:go-offline -B
COPY .env.example .env
RUN apt-get update && apt-get install -y netcat-traditional inotify-tools && rm -rf /var/lib/apt/lists/*
COPY wait-for-it.sh .
RUN chmod +x wait-for-it.sh


FROM maven:3.9-eclipse-temurin-23 AS builder
WORKDIR /app
COPY pom.xml .
COPY src ./src
RUN mvn clean package -DskipTests


FROM base AS production
COPY --from=builder /app/target/*.jar /app/app.jar
RUN chmod +x /app/app.jar
EXPOSE 9000/tcp
ENTRYPOINT ["./wait-for-it.sh", "db:3306", "--", "java", "-jar", "/app/app.jar", "--server.port=9000"]


FROM base AS development
COPY entrypoint.sh .
RUN chmod +x entrypoint.sh
EXPOSE 9000 5005
ENTRYPOINT ["./entrypoint.sh"]