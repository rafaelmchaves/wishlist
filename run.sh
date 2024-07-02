#!/bin/bash

echo "Building and starting Docker containers..."
docker-compose up -d

echo "Generate jar..."
mvn clean package -Dmaven.test.skip

echo "Run spring boot"
mvn spring-boot:run