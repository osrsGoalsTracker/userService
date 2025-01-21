# Service Layer

## Overview

The service layer contains the core business logic of the application. It exposes public interfaces for integration with other services while keeping implementation details internal. Services follow these principles:

1. Interface-based design with public interfaces and internal implementations
2. Constructor injection for dependencies
3. Clear separation of concerns
4. Transaction management
5. Comprehensive error handling

## Public Service Interfaces

The following interfaces are part of the public API and should be used for integration with other services:

### UserService

```java
/**
 * Service interface for managing user operations.
 * This interface is part of the public API and should be used for integration with other services.
 */
public interface UserService {
    /**
     * Retrieves a user by their ID.
     *
     * @param userId The unique identifier of the user
     * @return User object containing user data
     * @throws ResourceNotFoundException if user doesn't exist
     */
    User getUser(String userId);

    /**
     * Creates a new user with the given email address.
     *
     * @param email The email address for the new user
     * @return The created User object
     */
    User createUser(String email);
}
```

## Integration Guidelines

1. **External Integration**
   - Only use the public service interfaces when integrating with other services
   - Never depend on internal implementation details
   - Use dependency injection to obtain service instances

Example integration:
```java
@Inject
public class OtherService {
    private final UserService userService;

    public OtherService(UserService userService) {
        this.userService = userService;
    }

    public void someOperation(String userId) {
        User user = userService.getUser(userId);
        // ... use the user object
    }
}
```

## Implementation Pattern

Services follow this implementation pattern:

```java
@Singleton
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final ValidationService validationService;

    @Inject
    public UserServiceImpl(
            UserRepository userRepository,
            ValidationService validationService) {
        this.userRepository = userRepository;
        this.validationService = validationService;
    }

    @Override
    public User createUser(CreateUserRequest request) {
        // 1. Validate
        validationService.validate(request);

        // 2. Check business rules
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new ConflictException("Email already exists");
        }

        // 3. Create domain object
        User user = User.builder()
            .userId(UUID.randomUUID().toString())
            .email(request.getEmail())
            .createdAt(LocalDateTime.now())
            .updatedAt(LocalDateTime.now())
            .build();

        // 4. Persist
        return userRepository.save(user);
    }
}
```

## Error Handling

Services use custom exceptions for different error cases:

```java
public class ResourceNotFoundException extends RuntimeException {
    public ResourceNotFoundException(String message) {
        super(message);
    }
}

public class ConflictException extends RuntimeException {
    public ConflictException(String message) {
        super(message);
    }
}

public class BadRequestException extends RuntimeException {
    public BadRequestException(String message) {
        super(message);
    }
}

public class ServiceException extends RuntimeException {
    public ServiceException(String message, Throwable cause) {
        super(message, cause);
    }
}
```

## Testing

Services must have comprehensive unit tests:

```java
@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {
    @Mock
    private UserRepository userRepository;

    @Mock
    private ValidationService validationService;

    @InjectMocks
    private UserServiceImpl userService;

    @Test
    void createUser_ValidRequest_Success() {
        // Given
        CreateUserRequest request = new CreateUserRequest();
        request.setEmail("test@example.com");

        // When
        User result = userService.createUser(request);

        // Then
        assertThat(result).isNotNull();
        verify(userRepository).save(any(User.class));
    }

    @Test
    void createUser_ExistingEmail_ThrowsConflict() {
        // Given
        CreateUserRequest request = new CreateUserRequest();
        request.setEmail("existing@example.com");
        when(userRepository.existsByEmail(request.getEmail())).thenReturn(true);

        // Then
        assertThrows(ConflictException.class, () -> userService.createUser(request));
    }
}
``` 