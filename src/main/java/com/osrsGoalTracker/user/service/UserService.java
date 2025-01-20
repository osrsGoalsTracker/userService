package com.osrsGoalTracker.user.service;

import com.osrsGoalTracker.user.model.User;

import software.amazon.awssdk.services.dynamodb.model.ResourceNotFoundException;

/**
 * Service interface for managing user operations.
 * This service provides methods to interact with user data in the domain layer.
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
