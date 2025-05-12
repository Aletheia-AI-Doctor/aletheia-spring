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
COPY src .
RUN mvn dependency:go-offline -B
RUN mvn clean package -DskipTests


FROM base AS old_development
COPY --from=builder /app/target/*.jar /app/app.jar
RUN chmod +x /app/app.jar
EXPOSE 9000/tcp
ENTRYPOINT ["/wait-for-it.sh", "db:3306", "--", "java", "-jar", "/app/app.jar", "--server.port=9000"]


FROM base AS development
RUN apt-get install -y procps findutils && rm -rf /var/lib/apt/lists/*
EXPOSE 9000 5005
ENTRYPOINT ["./wait-for-it.sh", "db:3306", "--", "mvn", "spring-boot:run", "-Dspring-boot.run.profiles=dev", "-Dspring.devtools.restart.enabled=true", "-Dspring.devtools.livereload.enabled=true"]
