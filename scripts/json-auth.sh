#!/usr/bin/env bash

# Fetch a JWT token from JavaSpringVulny using the JSON signin endpoint
RESPONSE=$( curl --request POST 'https://localhost:9000/api/jwt/auth/signin' \
    --header 'Content-Type: application/json' \
    --data-raw '{"password": "password", "username": "user"}' \
    --silent --insecure )

# Extract the JWT token from the JSON response
echo $RESPONSE | jq '.token' -r
