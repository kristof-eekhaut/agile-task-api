#! /bin/bash

docker rm $(docker ps --all -q -f status=exited)