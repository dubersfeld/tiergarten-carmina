#!/bin/sh
echo "********************************************************"
echo "Starting Tiergarten Configuration Server"
echo "********************************************************"

java -Djava.security.egd=file:/dev/./urandom -jar /app.jar
