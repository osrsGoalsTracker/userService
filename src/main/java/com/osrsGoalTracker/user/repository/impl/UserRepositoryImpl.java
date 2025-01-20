package com.osrsGoalTracker.user.repository.impl;

import com.google.inject.Inject;
import com.osrsGoalTracker.user.dao.UserDao;
import com.osrsGoalTracker.user.dao.entity.UserEntity;
import com.osrsGoalTracker.user.model.User;
import com.osrsGoalTracker.user.repository.UserRepository;

import lombok.extern.log4j.Log4j2;

/**
 * Default implementation of the UserRepository interface.
 */
@Log4j2
public class UserRepositoryImpl implements UserRepository {
    private final UserDao userDao;

    /**
     * Constructs a new UserRepositoryImpl.
     *
     * @param userDao The UserDao instance to use for data operations
     */
    @Inject
    public UserRepositoryImpl(UserDao userDao) {
        this.userDao = userDao;
    }

    @Override
    public User createUser(String email) {
        if (email == null || email.trim().isEmpty()) {
            throw new IllegalArgumentException("Email cannot be null or empty");
        }

        String trimmedEmail = email.trim();
        log.info("Creating user with email: {}", trimmedEmail);

        UserEntity userEntity = UserEntity.builder()
                .email(trimmedEmail)
                .build();
        UserEntity createdUser = userDao.createUser(userEntity);
        return convertToUser(createdUser);
    }

    @Override
    public User getUser(String userId) {
        if (userId == null || userId.trim().isEmpty()) {
            throw new IllegalArgumentException("User ID cannot be null or empty");
        }

        String trimmedUserId = userId.trim();
        log.info("Getting user with ID: {}", trimmedUserId);

        UserEntity userEntity = userDao.getUser(trimmedUserId);
        return convertToUser(userEntity);
    }

    private User convertToUser(UserEntity userEntity) {
        return User.builder()
                .userId(userEntity.getUserId())
                .email(userEntity.getEmail())
                .createdAt(userEntity.getCreatedAt())
                .updatedAt(userEntity.getUpdatedAt())
                .build();
    }
}
