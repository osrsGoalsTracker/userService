package com.osrsGoalTracker.user.model;

import java.time.Instant;

import lombok.Builder;
import lombok.Value;

/**
 * Model representing a user in the system.
 */
@Value
@Builder
public class User {
    /**
     * The unique identifier of the user.
     */
    private final String userId;

    /**
     * The user's email address.
     */
    private final String email;

    /**
     * The timestamp when the user was created.
     */
    private final Instant createdAt;

    /**
     * The timestamp when the user was last updated.
     */
    private final Instant updatedAt;
}
