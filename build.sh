#!/bin/bash

# Array of service directories
services=("accept-terms-server" "application-server" "balance-server" "counsel-server" "entry-server" "file-storage-server" "judgement-server" "repayment-server" "terms-server" "config-server" "eureka-server")

# Loop through each service and run the build command
for service in "${services[@]}"; do
  echo "Building $service..."
  (cd $service && mvn clean compile jib:dockerBuild)
  if [ $? -ne 0 ]; then
    echo "Failed to build $service"
    exit 1
  fi
done

echo "All services built successfully!"