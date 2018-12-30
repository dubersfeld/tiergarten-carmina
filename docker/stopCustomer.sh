#!/bin/bash

echo stopping container $1
docker rm -f $1 &
disown

