#! /bin/bash

eval $(docker-machine env dev)
docker run --rm --name agile-task-mongodb -p 27017:27017 -d mongo --storageEngine wiredTiger