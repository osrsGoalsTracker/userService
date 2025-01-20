package com.osrsGoalTracker.user.repository.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.Instant;
import java.util.Collections;
import java.util.Map;

import com.osrsGoalTracker.user.model.User;
import com.osrsGoalTracker.user.repository.exception.DuplicateUserException;
import com.osrsGoalTracker.user.repository.exception.ResourceNotFoundException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import software.amazon.awssdk.services.dynamodb.DynamoDbClient;
import software.amazon.awssdk.services.dynamodb.model.AttributeValue;
import software.amazon.awssdk.services.dynamodb.model.ConditionalCheckFailedException;
import software.amazon.awssdk.services.dynamodb.model.GetItemRequest;
import software.amazon.awssdk.services.dynamodb.model.GetItemResponse;
import software.amazon.awssdk.services.dynamodb.model.PutItemRequest;
import software.amazon.awssdk.services.dynamodb.model.QueryRequest;
import software.amazon.awssdk.services.dynamodb.model.QueryResponse;

@ExtendWith(MockitoExtension.class)
class UserRepositoryImplTest {

    @Mock
    private DynamoDbClient dynamoDbClient;

    private UserRepositoryImpl userRepository;

    @BeforeEach
    void setUp() {
        userRepository = new UserRepositoryImpl(dynamoDbClient);
    }

    @Test
    void createUser_ValidEmail_ReturnsUser() {
        // Given
        String email = "test@example.com";

        // Mock the query to check for existing user
        when(dynamoDbClient.query(any(QueryRequest.class)))
                .thenReturn(QueryResponse.builder()
                        .items(Collections.emptyList())
                        .build());

        // Mock the put item operation
        when(dynamoDbClient.putItem(any(PutItemRequest.class)))
                .thenReturn(null); // PutItem doesn't return anything meaningful for this test

        // When
        User result = userRepository.createUser(email);

        // Then
        assertEquals(email, result.getEmail());
        verify(dynamoDbClient).query(any(QueryRequest.class));
        verify(dynamoDbClient).putItem(any(PutItemRequest.class));
    }

    @Test
    void createUser_DuplicateEmail_ThrowsDuplicateUserException() {
        // Given
        String email = "test@example.com";
        Map<String, AttributeValue> existingItem = Map.of(
                "email", AttributeValue.builder().s(email).build());

        when(dynamoDbClient.query(any(QueryRequest.class)))
                .thenReturn(QueryResponse.builder()
                        .items(Collections.singletonList(existingItem))
                        .build());

        // Then
        assertThrows(DuplicateUserException.class, () -> userRepository.createUser(email));
    }

    @Test
    void createUser_ConditionalCheckFails_ThrowsDuplicateUserException() {
        // Given
        String email = "test@example.com";

        when(dynamoDbClient.query(any(QueryRequest.class)))
                .thenReturn(QueryResponse.builder()
                        .items(Collections.emptyList())
                        .build());

        when(dynamoDbClient.putItem(any(PutItemRequest.class)))
                .thenThrow(ConditionalCheckFailedException.class);

        // Then
        assertThrows(DuplicateUserException.class, () -> userRepository.createUser(email));
    }

    @Test
    void getUser_ValidUserId_ReturnsUser() {
        // Given
        String userId = "user123";
        String email = "test@example.com";
        Instant now = Instant.now();

        Map<String, AttributeValue> item = Map.of(
                "userId", AttributeValue.builder().s(userId).build(),
                "email", AttributeValue.builder().s(email).build(),
                "createdAt", AttributeValue.builder().s(now.toString()).build(),
                "updatedAt", AttributeValue.builder().s(now.toString()).build());

        when(dynamoDbClient.getItem(any(GetItemRequest.class)))
                .thenReturn(GetItemResponse.builder().item(item).build());

        // When
        User result = userRepository.getUser(userId);

        // Then
        assertEquals(userId, result.getUserId());
        assertEquals(email, result.getEmail());
        assertEquals(now, result.getCreatedAt());
        assertEquals(now, result.getUpdatedAt());
    }

    @Test
    void getUser_NonexistentUser_ThrowsResourceNotFoundException() {
        // Given
        String userId = "nonexistent";

        when(dynamoDbClient.getItem(any(GetItemRequest.class)))
                .thenReturn(GetItemResponse.builder().build());

        // Then
        assertThrows(ResourceNotFoundException.class, () -> userRepository.getUser(userId));
    }

    @Test
    void getUser_NullUserId_ThrowsIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class,
                () -> userRepository.getUser(null));
    }

    @Test
    void getUser_EmptyUserId_ThrowsIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class,
                () -> userRepository.getUser(""));
    }

    @Test
    void getUser_WhitespaceUserId_ThrowsIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class,
                () -> userRepository.getUser("   "));
    }

    @Test
    void createUser_NullEmail_ThrowsIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class,
                () -> userRepository.createUser(null));
    }

    @Test
    void createUser_EmptyEmail_ThrowsIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class,
                () -> userRepository.createUser(""));
    }

    @Test
    void createUser_WhitespaceEmail_ThrowsIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class,
                () -> userRepository.createUser("   "));
    }
}