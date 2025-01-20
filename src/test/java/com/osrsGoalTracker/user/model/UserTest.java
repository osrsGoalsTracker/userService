package com.osrsGoalTracker.user.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

import java.time.Instant;

import org.junit.jupiter.api.Test;

class UserTest {

    @Test
    void testUserBuilder() {
        // Given
        String userId = "user123";
        String email = "test@example.com";
        Instant now = Instant.now();

        // When
        User user = User.builder()
                .userId(userId)
                .email(email)
                .createdAt(now)
                .updatedAt(now)
                .build();

        // Then
        assertEquals(userId, user.getUserId());
        assertEquals(email, user.getEmail());
        assertEquals(now, user.getCreatedAt());
        assertEquals(now, user.getUpdatedAt());
    }

    @Test
    void testUserEquality() {
        // Given
        Instant now = Instant.now();
        User user1 = User.builder()
                .userId("user123")
                .email("test@example.com")
                .createdAt(now)
                .updatedAt(now)
                .build();

        User user2 = User.builder()
                .userId("user123")
                .email("test@example.com")
                .createdAt(now)
                .updatedAt(now)
                .build();

        User differentUser = User.builder()
                .userId("user456")
                .email("test@example.com")
                .createdAt(now)
                .updatedAt(now)
                .build();

        // Then
        assertEquals(user1, user2, "Two users with the same values should be equal");
        assertNotEquals(user1, differentUser, "Users with different values should not be equal");
        assertEquals(user1.hashCode(), user2.hashCode(), "Equal users should have the same hash code");
    }

    @Test
    void testUserWithDifferentEmail() {
        // Given
        Instant now = Instant.now();
        User user1 = User.builder()
                .userId("user123")
                .email("test1@example.com")
                .createdAt(now)
                .updatedAt(now)
                .build();

        User user2 = User.builder()
                .userId("user123")
                .email("test2@example.com")
                .createdAt(now)
                .updatedAt(now)
                .build();

        // Then
        assertNotEquals(user1, user2, "Users with different emails should not be equal");
    }

    @Test
    void testUserWithDifferentTimestamps() {
        // Given
        Instant now = Instant.now();
        Instant later = now.plusSeconds(1);

        User user1 = User.builder()
                .userId("user123")
                .email("test@example.com")
                .createdAt(now)
                .updatedAt(now)
                .build();

        User user2 = User.builder()
                .userId("user123")
                .email("test@example.com")
                .createdAt(now)
                .updatedAt(later)
                .build();

        // Then
        assertNotEquals(user1, user2, "Users with different timestamps should not be equal");
    }
}