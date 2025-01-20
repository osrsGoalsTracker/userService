package com.osrsGoalTracker.user.handler;

import java.util.Map;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.google.inject.Guice;
import com.google.inject.Inject;
import com.google.inject.Injector;
import com.osrsGoalTracker.user.di.UserModule;
import com.osrsGoalTracker.user.model.User;
import com.osrsGoalTracker.user.service.UserService;

import lombok.extern.log4j.Log4j2;

import static java.net.HttpURLConnection.HTTP_BAD_REQUEST;
import static java.net.HttpURLConnection.HTTP_INTERNAL_ERROR;
import static java.net.HttpURLConnection.HTTP_OK;

/**
 * Lambda handler for retrieving user metadata.
 * This handler processes API Gateway events to retrieve user information.
 */
@Log4j2
public class GetUserHandler implements RequestHandler<APIGatewayProxyRequestEvent, APIGatewayProxyResponseEvent> {
    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper()
            .registerModule(new JavaTimeModule());

    private final UserService userService;

    /**
     * Default constructor for AWS Lambda.
     * This constructor is required by AWS Lambda to instantiate the handler.
     */
    public GetUserHandler() {
        Injector injector = Guice.createInjector(new UserModule());
        this.userService = injector.getInstance(UserService.class);
    }

    /**
     * Constructor for testing purposes.
     * Allows injection of mock services in tests.
     *
     * @param userService The UserService instance to use for retrieving user data
     */
    @Inject
    GetUserHandler(UserService userService) {
        this.userService = userService;
    }

    @Override
    public APIGatewayProxyResponseEvent handleRequest(APIGatewayProxyRequestEvent input, Context context) {
        log.info("Received request to get user");
        try {
            String userId = parseAndValidateInput(input);
            User user = getUser(userId);
            return createSuccessResponse(user);
        } catch (IllegalArgumentException e) {
            return createErrorResponse(HTTP_BAD_REQUEST, e.getMessage());
        } catch (Exception e) {
            log.error("Error processing request", e);
            return createErrorResponse(HTTP_INTERNAL_ERROR, "Error processing request: " + e.getMessage());
        }
    }

    private String parseAndValidateInput(APIGatewayProxyRequestEvent input) {
        if (input == null) {
            throw new IllegalArgumentException("Request cannot be null");
        }

        Map<String, String> pathParameters = input.getPathParameters();
        if (pathParameters == null) {
            throw new IllegalArgumentException("Path parameters cannot be null");
        }

        String userId = pathParameters.get("userId");
        if (userId == null || userId.trim().isEmpty()) {
            throw new IllegalArgumentException("User ID cannot be null or empty");
        }

        return userId.trim();
    }

    private User getUser(String userId) {
        log.info("Getting user with ID: {}", userId);
        return userService.getUser(userId);
    }

    private APIGatewayProxyResponseEvent createSuccessResponse(User user) throws Exception {
        return new APIGatewayProxyResponseEvent()
                .withStatusCode(HTTP_OK)
                .withBody(OBJECT_MAPPER.writeValueAsString(user));
    }

    private APIGatewayProxyResponseEvent createErrorResponse(int statusCode, String message) {
        log.error(message);
        return new APIGatewayProxyResponseEvent()
                .withStatusCode(statusCode)
                .withBody(String.format("{\"message\":\"%s\"}", message));
    }
}
