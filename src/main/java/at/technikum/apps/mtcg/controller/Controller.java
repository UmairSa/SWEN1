package at.technikum.apps.mtcg.controller;

import at.technikum.server.http.HttpContentType;
import at.technikum.server.http.HttpStatus;
import at.technikum.server.http.Request;
import at.technikum.server.http.Response;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public abstract class Controller {
    public abstract boolean supports(String route);

    public abstract Response handle(Request request);

    protected Response createResponse(HttpStatus status, String body, HttpContentType contentType) {
        Response response = new Response();
        response.setStatus(status);
        response.setContentType(contentType);
        response.setBody(body);
        return response;
    }

    protected Response badRequestResponse() {
        return createResponse(HttpStatus.BAD_REQUEST, "Bad Request", HttpContentType.TEXT_PLAIN);
    }

    protected Response unauthorizedResponse() {
        return createResponse(HttpStatus.UNAUTHORIZED, "Unauthorized", HttpContentType.TEXT_PLAIN);
    }

    protected Response status(HttpStatus status, String message) {
        return createResponse(status, message, HttpContentType.TEXT_PLAIN);
    }

    protected String serializeToJson(Object object) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            return objectMapper.writeValueAsString(object);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("JSON serialization error", e);
        }
    }
}
