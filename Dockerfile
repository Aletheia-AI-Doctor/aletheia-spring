FROM eclipse-temurin:23

WORKDIR /app
COPY pom.xml /app/pom.xml

CMD ["mvn", "clean", "install", "-DskipTests", ""]

COPY .env.example /app/.env

CMD if [ "$(find /target -maxdepth 1 -type f -name "*.jar" | wc -l)" -eq 0 ]; then \
    echo "No JAR file found in /target directory" && \
    exit 1; \
    else \
    cp "$(find /target -maxdepth 1 -type f -name "*.jar" -print -quit)" "/app/app.jar"; \
    fi

RUN chmod +x /app/app.jar

EXPOSE 9000/tcp

RUN apt-get update && apt-get install -y netcat-traditional && rm -rf /var/lib/apt/lists/*

COPY wait-for-it.sh /wait-for-it.sh
RUN chmod +x /wait-for-it.sh

ENTRYPOINT ["/wait-for-it.sh", "db:3306", "--", "java", "-jar", "/app/app.jar", "--server.port=9000"]
