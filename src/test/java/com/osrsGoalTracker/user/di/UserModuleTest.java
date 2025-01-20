package com.osrsGoalTracker.user.di;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;

import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.osrsGoalTracker.user.dao.UserDao;
import com.osrsGoalTracker.user.repository.UserRepository;
import com.osrsGoalTracker.user.repository.impl.UserRepositoryImpl;
import com.osrsGoalTracker.user.service.UserService;
import com.osrsGoalTracker.user.service.impl.UserServiceImpl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import software.amazon.awssdk.services.dynamodb.DynamoDbClient;

class UserModuleTest {

    private UserDao mockUserDao;
    private DynamoDbClient mockDynamoDbClient;
    private Injector injector;

    @BeforeEach
    void setUp() {
        mockUserDao = mock(UserDao.class);
        mockDynamoDbClient = mock(DynamoDbClient.class);

        // Create a test module that provides all necessary bindings
        AbstractModule testModule = new AbstractModule() {
            @Override
            protected void configure() {
                bind(DynamoDbClient.class).toInstance(mockDynamoDbClient);
                bind(UserDao.class).toInstance(mockUserDao);
                bind(UserRepository.class).to(UserRepositoryImpl.class);
                bind(UserService.class).to(UserServiceImpl.class);
            }
        };

        injector = Guice.createInjector(testModule);
    }

    @Test
    void testUserModuleBindings() {
        // When
        UserService userService = injector.getInstance(UserService.class);
        UserRepository userRepository = injector.getInstance(UserRepository.class);

        // Then
        assertNotNull(userService, "UserService should be bound");
        assertNotNull(userRepository, "UserRepository should be bound");
        assertTrue(userService instanceof UserServiceImpl,
                "UserService should be bound to UserServiceImpl");
        assertTrue(userRepository instanceof UserRepositoryImpl,
                "UserRepository should be bound to UserRepositoryImpl");
    }
}