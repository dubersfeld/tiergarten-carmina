#!/bin/bash

echo launching a new container
docker run --network "docker_default" -e "CONFIGSERVER_HOST=tiergarten-config" -e "CONFIGSERVER_PORT=8888" \
-e "TIERGARTEN_CONFIG_URI=http://tiergarten-config:8888" -e "PROFILE=dev" \
-e "ZOOKEEPER_CONNECT_STRING=zookeeper-server:2181" tiergarten/customer-server &
disown



