#! /bin/bash

eval $(docker-machine env dev)
docker rm $(docker ps --all -q -f status=exited)