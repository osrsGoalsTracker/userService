package com.osrsGoalTracker.user.handler;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.osrsGoalTracker.user.model.User;
import com.osrsGoalTracker.user.service.UserService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class GetUserHandlerTest {

    @Mock
    private UserService userService;

    @Mock
    private Context context;

    private GetUserHandler handler;
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        handler = new GetUserHandler(userService);
        objectMapper = new ObjectMapper()
                .registerModule(new JavaTimeModule());
    }

    @Test
    void handleRequest_ValidInput_ReturnsSuccessResponse() throws Exception {
        // Given
        String userId = "user123";
        Instant now = Instant.now();
        User user = User.builder()
                .userId(userId)
                .email("test@example.com")
                .createdAt(now)
                .updatedAt(now)
                .build();

        Map<String, String> pathParameters = new HashMap<>();
        pathParameters.put("userId", userId);

        APIGatewayProxyRequestEvent request = new APIGatewayProxyRequestEvent()
                .withPathParameters(pathParameters);

        when(userService.getUser(userId)).thenReturn(user);

        // When
        APIGatewayProxyResponseEvent response = handler.handleRequest(request, context);

        // Then
        assertNotNull(response);
        assertEquals(200, response.getStatusCode());
        assertEquals(objectMapper.writeValueAsString(user), response.getBody());
        verify(userService).getUser(userId);
    }

    @Test
    void handleRequest_NullInput_ReturnsBadRequest() {
        // When
        APIGatewayProxyResponseEvent response = handler.handleRequest(null, context);

        // Then
        assertNotNull(response);
        assertEquals(400, response.getStatusCode());
        assertEquals("{\"message\":\"Request cannot be null\"}", response.getBody());
    }

    @Test
    void handleRequest_NullPathParameters_ReturnsBadRequest() {
        // Given
        APIGatewayProxyRequestEvent request = new APIGatewayProxyRequestEvent();

        // When
        APIGatewayProxyResponseEvent response = handler.handleRequest(request, context);

        // Then
        assertNotNull(response);
        assertEquals(400, response.getStatusCode());
        assertEquals("{\"message\":\"Path parameters cannot be null\"}", response.getBody());
    }

    @Test
    void handleRequest_MissingUserId_ReturnsBadRequest() {
        // Given
        Map<String, String> pathParameters = new HashMap<>();
        APIGatewayProxyRequestEvent request = new APIGatewayProxyRequestEvent()
                .withPathParameters(pathParameters);

        // When
        APIGatewayProxyResponseEvent response = handler.handleRequest(request, context);

        // Then
        assertNotNull(response);
        assertEquals(400, response.getStatusCode());
        assertEquals("{\"message\":\"User ID cannot be null or empty\"}", response.getBody());
    }

    @Test
    void handleRequest_EmptyUserId_ReturnsBadRequest() {
        // Given
        Map<String, String> pathParameters = new HashMap<>();
        pathParameters.put("userId", "");
        APIGatewayProxyRequestEvent request = new APIGatewayProxyRequestEvent()
                .withPathParameters(pathParameters);

        // When
        APIGatewayProxyResponseEvent response = handler.handleRequest(request, context);

        // Then
        assertNotNull(response);
        assertEquals(400, response.getStatusCode());
        assertEquals("{\"message\":\"User ID cannot be null or empty\"}", response.getBody());
    }

    @Test
    void handleRequest_WhitespaceUserId_ReturnsBadRequest() {
        // Given
        Map<String, String> pathParameters = new HashMap<>();
        pathParameters.put("userId", "   ");
        APIGatewayProxyRequestEvent request = new APIGatewayProxyRequestEvent()
                .withPathParameters(pathParameters);

        // When
        APIGatewayProxyResponseEvent response = handler.handleRequest(request, context);

        // Then
        assertNotNull(response);
        assertEquals(400, response.getStatusCode());
        assertEquals("{\"message\":\"User ID cannot be null or empty\"}", response.getBody());
    }

    @Test
    void handleRequest_ServiceThrowsException_ReturnsServerError() {
        // Given
        String userId = "user123";
        Map<String, String> pathParameters = new HashMap<>();
        pathParameters.put("userId", userId);

        APIGatewayProxyRequestEvent request = new APIGatewayProxyRequestEvent()
                .withPathParameters(pathParameters);

        RuntimeException expectedException = new RuntimeException("Service error");
        when(userService.getUser(userId)).thenThrow(expectedException);

        // When
        APIGatewayProxyResponseEvent response = handler.handleRequest(request, context);

        // Then
        assertNotNull(response);
        assertEquals(500, response.getStatusCode());
        assertEquals("{\"message\":\"Error processing request: Service error\"}", response.getBody());
    }
}