#!/usr/bin/env bash

docker run --rm -e API_KEY=$LOCAL_HAWK_API_KEY -e SHAWK_RESULTS_ENDPOINT=http://host.docker.internal:6300/api/v1 -e SHAWK_AUTH_ENDPOINT=http://host.docker.internal:6200 -v $(pwd):/hawk:rw -it stackhawk/hawkscan:latest

#docker run --rm -e API_KEY=$SANDBOX_HAWK_API_KEY -v $(pwd):/hawk:rw -it stackhawk/hawkscan:latest