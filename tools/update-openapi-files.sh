#!/bin/bash

# Exit on error
set -e

# Define the URL of the running application
APP_URL="https://localhost:9000/openapi"
HEALTH_URL="https://localhost:9000/actuator/health"

# Define the output files
JSON_OUTPUT="openapi.json"
YAML_OUTPUT="openapi.yaml"

# Save the current directory
CURRENT_DIR=$(pwd)

# Change to the parent directory
cd ..

# Start the application in the background
./gradlew bootRun &
APP_PID=$!

# Wait for the application to start by checking the health endpoint
until $(curl -k --output /dev/null --silent --head --fail ${HEALTH_URL}); do
    printf '.'
    sleep 5
done

# Fetch the OpenAPI JSON definition and save it to openapi.json
curl -k -s "${APP_URL}" -o "${JSON_OUTPUT}"

# Kill the application process
kill $APP_PID

# Reformat the JSON file using jq
jq . "${JSON_OUTPUT}" > "${JSON_OUTPUT}.tmp" && mv "${JSON_OUTPUT}.tmp" "${JSON_OUTPUT}"

# Convert the JSON definition to YAML and save it to openapi.yaml
# Requires yq (https://github.com/mikefarah/yq) to be installed
if command -v yq &> /dev/null
then
    yq eval -P "${JSON_OUTPUT}" > "${YAML_OUTPUT}"
else
    echo "yq is not installed. Please install yq to convert JSON to YAML."
    kill $APP_PID
fi

# Change back to the original directory
cd "${CURRENT_DIR}"

echo "OpenAPI definitions updated successfully."
