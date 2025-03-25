FROM eclipse-temurin:23

CMD ["mvn", "clean", "install", "-DskipTests"]

WORKDIR /app

COPY .env.example /app/.env

COPY target/*.jar /app/app.jar

RUN chmod +x /app/app.jar

EXPOSE 9000/tcp

RUN apt-get update && apt-get install -y netcat-traditional && rm -rf /var/lib/apt/lists/*

COPY wait-for-it.sh /wait-for-it.sh
RUN chmod +x /wait-for-it.sh

ENTRYPOINT ["/wait-for-it.sh", "db:3306", "--", "java", "-jar", "/app/app.jar", "--server.port=9000"]
