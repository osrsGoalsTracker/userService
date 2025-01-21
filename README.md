# User Service

A Java service for managing users in the OSRS Goals Tracker system.

## Overview

This service provides AWS Lambda functions for managing users. It follows a Layered Service Architecture (LSA) pattern and is built with Java 21.

## Documentation

- [Project Structure and Architecture](docs/ARCHITECTURE.md) - Detailed overview of the project's layered architecture
- [Handler Interfaces](docs/HANDLERS.md) - Lambda function entry points, inputs, outputs, and public API endpoints
- [Service Interfaces](docs/SERVICES.md) - Service layer interfaces, functionality, and integration guidelines
- [Data Models](docs/MODELS.md) - Core data models and their relationships

## Requirements

- JDK 21
- Gradle 8.x

## Quick Start

1. Install dependencies:
```bash
./gradlew build
```

2. Run tests:
```bash
./gradlew test
```

3. Build all Lambda handlers:
```bash
./gradlew buildAllHandlers
```

## Building Individual Handlers

Build specific Lambda handlers:
```bash
# Build GetUser handler
./gradlew getUserLambda

# Build CreateUser handler
./gradlew createUserLambda
```

Each handler will be built into its own JAR file in `build/libs/`.

## Dependencies

- AWS Lambda Core - Lambda function support
- AWS Lambda Events - Event handling
- AWS DynamoDB - Database operations
- Google Guice - Dependency injection
- Jackson - JSON serialization
- Log4j2 - Logging
- Lombok - Boilerplate reduction
- JUnit 5 - Testing
- Mockito - Mocking for tests

## Infrastructure

The service is deployed using AWS CDK with the following components:

- API Gateway for REST endpoints
- Lambda functions for business logic
- DynamoDB tables for data storage 
