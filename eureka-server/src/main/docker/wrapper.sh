#!/bin/sh
echo "********************************************************"
echo "Starting Tiergarten Eureka Server"
echo "********************************************************"

java -Djava.security.egd=file:/dev/./urandom -jar /app.jar
