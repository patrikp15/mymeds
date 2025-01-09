#!/bin/bash

# Check if MYMEDS_WORKSPACE environment variable is set
local MYMEDS_WORKSPACE="/d/workspace/mymeds"
if [ -z "$MYMEDS_WORKSPACE" ]; then
    echo "Error: MYMEDS_WORKSPACE environment variable is not set."
    echo "Please set it using 'export MYMEDS_WORKSPACE=/d/workspace/mymeds' and try again."
    exit 1
fi

# List of microservices
services=(
    "mymeds-api-gateway"
    "mymeds-auth-service"
    "mymeds-user-service"
    "mymeds-medication-service"
    "mymeds-auth-service"
)

# Build Docker images for each microservice
for service in "${services[@]}"; do
    service_path="$MYMEDS_WORKSPACE/$service"
    
    # Extract the simple name (remove 'mymeds-' prefix)
    simple_name="${service#mymeds-}"
    
    echo "Building Docker image for $service at $service_path..."
    docker build -t "$simple_name" "$service_path"
    if [ $? -ne 0 ]; then
        echo "Failed to build Docker image for $service. Exiting."
        exit 1
    fi
done

echo "All Docker images have been built successfully!"
