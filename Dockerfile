FROM eclipse-temurin:23

CMD ["mvn", "clean", "install", "-DskipTests"]

WORKDIR /app

# COPY target/spring-boot-initial-0.0.1-SNAPSHOT.jar /app/spring-boot-initial-0.0.1-SNAPSHOT.jar

COPY .env.example /app/.env

COPY target/*.jar /app/app.jar

# Ensure the JAR file has correct permissions
RUN chmod +x /app/app.jar

EXPOSE 9000/tcp

# Command to run the application
ENTRYPOINT ["java", "-jar", "/app/app.jar", "--server.port=9000"]
