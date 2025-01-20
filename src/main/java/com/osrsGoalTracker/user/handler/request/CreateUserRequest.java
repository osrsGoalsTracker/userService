package com.osrsGoalTracker.user.handler.request;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Request object for creating a new user.
 */
@Data
@NoArgsConstructor
public class CreateUserRequest {
    /**
     * The email address of the user to create.
     */
    private String email;
}