package com.osrsGoalTracker.user.handler;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.inject.Guice;
import com.google.inject.Inject;
import com.google.inject.Injector;
import com.osrsGoalTracker.user.di.UserModule;
import com.osrsGoalTracker.user.handler.request.CreateUserRequest;
import com.osrsGoalTracker.user.model.User;
import com.osrsGoalTracker.user.service.UserService;

import lombok.extern.log4j.Log4j2;

/**
 * Lambda handler for creating a new user.
 * This handler processes API Gateway events to create new users.
 */
@Log4j2
public class CreateUserHandler implements RequestHandler<APIGatewayProxyRequestEvent, APIGatewayProxyResponseEvent> {
    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();
    private static final int HTTP_OK = 200;
    private static final int HTTP_BAD_REQUEST = 400;
    private static final int HTTP_SERVER_ERROR = 500;
    private final UserService userService;

    /**
     * Default constructor for AWS Lambda.
     * This constructor is required by AWS Lambda to instantiate the handler.
     */
    public CreateUserHandler() {
        Injector injector = Guice.createInjector(new UserModule());
        this.userService = injector.getInstance(UserService.class);
    }

    /**
     * Constructor for testing purposes.
     * Allows injection of mock services in tests.
     *
     * @param userService The UserService instance to use for user operations
     */
    @Inject
    CreateUserHandler(UserService userService) {
        this.userService = userService;
    }

    @Override
    public APIGatewayProxyResponseEvent handleRequest(APIGatewayProxyRequestEvent input, Context context) {
        log.info("Received request to create user");
        try {
            CreateUserRequest request = parseAndValidateInput(input);
            User user = executeRequest(request);
            return createSuccessResponse(user);
        } catch (IllegalArgumentException e) {
            return createErrorResponse(HTTP_BAD_REQUEST, e.getMessage());
        } catch (Exception e) {
            log.error("Error processing request", e);
            return createErrorResponse(HTTP_SERVER_ERROR, "Error processing request: " + e.getMessage());
        }
    }

    private CreateUserRequest parseAndValidateInput(APIGatewayProxyRequestEvent input) throws Exception {
        if (input == null) {
            throw new IllegalArgumentException("Request cannot be null");
        }

        String body = input.getBody();
        if (body == null || body.trim().isEmpty()) {
            throw new IllegalArgumentException("Request body cannot be null or empty");
        }

        CreateUserRequest request = OBJECT_MAPPER.readValue(body, CreateUserRequest.class);
        if (request.getEmail() == null || request.getEmail().trim().isEmpty()) {
            throw new IllegalArgumentException("Email cannot be null or empty");
        }

        return request;
    }

    private User executeRequest(CreateUserRequest request) {
        log.info("Creating user with email: {}", request.getEmail());
        return userService.createUser(request.getEmail().trim());
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