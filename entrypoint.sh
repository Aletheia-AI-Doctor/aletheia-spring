#!/bin/sh

./wait-for-it.sh db:3306 -- mvn spring-boot:run -Dspring-boot.run.jvmArguments="-agentlib:jdwp=transport=dt_socket,server=y,suspend=n,address=*:5005" &

while inotifywait -r -e modify,create,delete /app/src; do
  echo "Detected source change. Recompiling..."
  mvn compile
done