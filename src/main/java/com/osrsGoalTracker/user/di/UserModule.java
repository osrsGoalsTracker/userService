package com.osrsGoalTracker.user.di;

import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.Singleton;
import com.osrsGoalTracker.user.repository.UserRepository;
import com.osrsGoalTracker.user.repository.impl.UserRepositoryImpl;
import com.osrsGoalTracker.user.service.UserService;
import com.osrsGoalTracker.user.service.impl.UserServiceImpl;

import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;


/**
 * Guice module for user-related bindings.
 */
public class UserModule extends AbstractModule {
    @Override
    protected void configure() {
        bind(UserRepository.class).to(UserRepositoryImpl.class);
        bind(UserService.class).to(UserServiceImpl.class);
    }

    @Provides
    @Singleton
    DynamoDbClient provideDynamoDbClient() {
        return DynamoDbClient.builder()
                .region(Region.of(System.getenv("AWS_REGION")))
                .build();
    }
}
