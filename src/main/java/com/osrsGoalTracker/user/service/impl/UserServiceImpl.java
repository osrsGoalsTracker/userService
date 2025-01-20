package com.osrsGoalTracker.user.service.impl;

import com.google.inject.Inject;
import com.osrsGoalTracker.user.model.User;
import com.osrsGoalTracker.user.repository.UserRepository;
import com.osrsGoalTracker.user.service.UserService;

import lombok.extern.log4j.Log4j2;

/**
 * Default implementation of the UserService interface.
 */
@Log4j2
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;

    /**
     * Constructs a new DefaultUserService.
     *
     * @param userRepository The UserRepository instance to use for data operations
     */
    @Inject
    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public User createUser(String email) {
        if (email == null || email.trim().isEmpty()) {
            throw new IllegalArgumentException("Email cannot be null or empty");
        }

        String trimmedEmail = email.trim();
        log.info("Creating user with email: {}", trimmedEmail);
        return userRepository.createUser(trimmedEmail);
    }

    @Override
    public User getUser(String userId) {
        if (userId == null || userId.trim().isEmpty()) {
            throw new IllegalArgumentException("User ID cannot be null or empty");
        }

        String trimmedUserId = userId.trim();
        log.info("Getting user with ID: {}", trimmedUserId);
        return userRepository.getUser(trimmedUserId);
    }
}
