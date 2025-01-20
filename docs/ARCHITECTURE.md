# Project Architecture

## Layered Architecture Rule Set

This rule set defines how to organize and implement a modular layered architecture for a product, including both domain services (e.g., `UserService`, `GoalService`) and orchestration Lambdas for event-driven workflows. It outlines layers, interaction rules, service boundaries, and specific patterns to handle cross-cutting concerns like events and shared resources.

## Rule Set

### Layer Definitions

#### Domain Layer (Service-Specific)

Each domain (e.g., `User`, `Goal`) has its own service responsible for managing the business logic and data access for its entities. It is structured into three sub-layers:

1. **Handler Layer**
   - Contains entry points for APIs (e.g., Lambda handlers or controllers).
   - Handles input (e.g., HTTP requests or events) and delegates logic to the service layer.
   - Only calls the service layer.

2. **Service Layer**
   - Encapsulates business logic for the domain.
   - Uses repository interfaces to fetch or persist data.
   - Only depends on the repository layer or other services via interfaces.

3. **Repository Layer**
   - Abstracts data access logic.
   - Interacts with the underlying data store (e.g., DynamoDB, RDBMS).
   - Provides methods scoped to the domain (e.g., `findUserById`, `saveGoal`).
   - Does not call the service layer.

## Layer Definitions

### 1. Handler Layer (`*.handler`)
- Entry points for AWS Lambda functions
- Handles API Gateway events and responses
- Follows a strict 4-step pattern:
  1. Parse input
  2. Validate input
  3. Run service function
  4. Return output
- Only depends on the service layer
- Located in `*.handler` packages

### 2. Service Layer (`*.service`)
- Contains core business logic
- Defines interfaces and implementations
- Uses repository interfaces for data access
- Manages transactions and orchestration
- Located in `*.service` packages
- Implementations in `*.service.impl`

### 3. Repository Layer (`*.repository`)
- Abstracts data persistence operations
- Provides DynamoDB implementations
- Handles data mapping and queries
- Located in `*.repository` packages
- Implementations in `*.repository.impl`

### 4. Model Layer (`*.model`)
- Domain models using Lombok
- Request/Response DTOs
- Uses `@Value` and `@Builder` for immutability
- Located in `*.model` packages

### 5. External Layer (`*.external`)
- Handles third-party integrations (e.g., OSRS Hiscores)
- Abstracts external API calls
- Located in `*.external` packages
- Implementations in `*.external.impl`

### 6. Dependency Injection Layer (`*.di`)
- Google Guice modules for each domain
- Binds interfaces to implementations
- Located in `*.di` packages

## Domain Modules

### User Domain (`com.osrsGoalTracker.user`)
- User account management
- Authentication and authorization
- Profile management

## Layer Interaction Rules

1. **Handler Layer**
   - Can only call Service layer
   - Must follow 4-step pattern
   - Must handle API Gateway events

2. **Service Layer**
   - Can call Repository layer
   - Can call other Services
   - Can call External layer
   - Manages transactions

3. **Repository Layer**
   - Can only access data store
   - No service layer calls
   - No cross-repository calls

4. **External Layer**
   - Can only call external APIs
   - Must handle retries and failures
   - Must provide clean interfaces

## Package Structure Example (User Domain)

```
com.osrsGoalTracker.user/
├── di/
│   └── UserModule.java
├── handler/
│   ├── CreateUserHandler.java
│   ├── GetUserHandler.java
│   ├── request/
│   │   └── CreateUserRequest.java
│   └── response/
│       └── GetUserResponse.java
├── model/
│   └── User.java
├── repository/
│   ├── UserRepository.java
│   └── impl/
│       └── UserRepositoryImpl.java
└── service/
    ├── UserService.java
    └── impl/
        └── UserServiceImpl.java
```

## Testing Strategy
Each layer should have comprehensive unit tests:

   - Test input parsing
   - Test validation
   - Mock service layer
   - Test input validation
   - Mock service layer

2. **Service Tests**
   - Test error conditions
   - Test error cases
   - Test transactions
   - Mock repositories

3. **Repository Tests**
   - Use DynamoDB Local
   - Test queries
   - Test data operations

4. **Integration Tests**
   - Test full flow
   - Use test containers
   - Test error scenarios

## Build and Deployment

Each Lambda handler is built into its own JAR file using Gradle tasks:

```bash
# Build individual handlers
./gradlew buildGetUserHandler

# Build all handlers
./gradlew buildAllHandlers
}
``` 
