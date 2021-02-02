#!/usr/bin/env bash

if [[ -f ./.env ]]; then
  source ./.env
fi

if [[ -z ${APP_ID} ]]; then
  echo "Please set the environment variable APP_ID to your StackHawk application ID before running."
  exit 1
elif [[ -z ${API_KEY} ]]; then
  echo "Please set the environment variable API_KEY to your StackHawk API key before running."
  exit 1
fi

docker run --rm -v $(pwd):/hawk:rw -t -e API_KEY -e APP_ID stackhawk/hawkscan:latest "${@}"
