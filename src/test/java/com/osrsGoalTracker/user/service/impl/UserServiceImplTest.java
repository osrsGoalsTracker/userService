package com.osrsGoalTracker.user.service.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.Instant;

import com.osrsGoalTracker.user.model.User;
import com.osrsGoalTracker.user.repository.UserRepository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {

    @Mock
    private UserRepository userRepository;

    private UserServiceImpl userService;

    @BeforeEach
    void setUp() {
        userService = new UserServiceImpl(userRepository);
    }

    @Test
    void createUser_ValidEmail_ReturnsUser() {
        // Given
        String email = "test@example.com";
        Instant now = Instant.now();
        User expectedUser = User.builder()
                .userId("user123")
                .email(email)
                .createdAt(now)
                .updatedAt(now)
                .build();

        when(userRepository.createUser(email)).thenReturn(expectedUser);

        // When
        User actualUser = userService.createUser(email);

        // Then
        assertEquals(expectedUser, actualUser);
        verify(userRepository).createUser(email);
    }

    @Test
    void createUser_NullEmail_ThrowsIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class,
                () -> userService.createUser(null));
    }

    @Test
    void createUser_EmptyEmail_ThrowsIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class,
                () -> userService.createUser(""));
    }

    @Test
    void createUser_WhitespaceEmail_ThrowsIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class,
                () -> userService.createUser("   "));
    }

    @Test
    void getUser_ValidUserId_ReturnsUser() {
        // Given
        String userId = "user123";
        Instant now = Instant.now();
        User expectedUser = User.builder()
                .userId(userId)
                .email("test@example.com")
                .createdAt(now)
                .updatedAt(now)
                .build();

        when(userRepository.getUser(userId)).thenReturn(expectedUser);

        // When
        User actualUser = userService.getUser(userId);

        // Then
        assertEquals(expectedUser, actualUser);
        verify(userRepository).getUser(userId);
    }

    @Test
    void getUser_NullUserId_ThrowsIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class,
                () -> userService.getUser(null));
    }

    @Test
    void getUser_EmptyUserId_ThrowsIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class,
                () -> userService.getUser(""));
    }

    @Test
    void getUser_WhitespaceUserId_ThrowsIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class,
                () -> userService.getUser("   "));
    }
}