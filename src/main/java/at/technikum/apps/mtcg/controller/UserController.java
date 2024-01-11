package at.technikum.apps.mtcg.controller;

import at.technikum.apps.mtcg.entity.User;
import at.technikum.apps.mtcg.repository.UserRepository;
import at.technikum.apps.mtcg.service.UserService;
import at.technikum.server.http.HttpContentType;
import at.technikum.server.http.HttpStatus;
import at.technikum.server.http.Request;
import at.technikum.server.http.Response;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.Optional;

public class UserController extends Controller {
    private final UserService userService;
    private final UserRepository userRepository;

    public UserController(UserService userService, UserRepository userRepository) {
        this.userService = userService;
        this.userRepository = userRepository;
    }

    @Override
    public boolean supports(String route) {
        return route.startsWith("/users") || route.startsWith("/sessions");
    }

    @Override
    public Response handle(Request request) {
        if (request.getRoute().equals("/users") && request.getMethod().equals("POST")) {
            return registerUser(request);
        } else if (request.getRoute().equals("/sessions") && request.getMethod().equals("POST")) {
            return loginUser(request);
        }

        String routeUsername = extractUsernameFromRoute(request.getRoute());

        if (routeUsername == null || !validateToken(request, routeUsername)) {
            return unauthorizedResponse();
        }

        if (request.getRoute().startsWith("/users/")) {
            switch (request.getMethod()) {
                case "GET":
                    return getUserData(routeUsername);
                case "PUT":
                    // Implement logic for PUT method
                    // return updateUserData(request, routeUsername);
                default:
                    return badRequestResponse();
            }
        }

        return badRequestResponse();
    }

    private Response registerUser(Request request) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            User user = objectMapper.readValue(request.getBody(), User.class);
            User registeredUser = userService.registerUser(user);

            String message = "User created: " + registeredUser.getUsername();

            Response response = new Response();
            response.setStatus(HttpStatus.OK);
            response.setContentType(HttpContentType.APPLICATION_JSON);
            response.setBody(message);

            return response;

        } catch (IllegalArgumentException e) {
            Response response = new Response();
            response.setStatus(HttpStatus.BAD_REQUEST);
            response.setContentType(HttpContentType.TEXT_PLAIN);
            response.setBody(e.getMessage());
            return response;

        } catch (Exception e) {
            return status();
        }
    }

    private Response loginUser(Request request) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            User loginDetails = objectMapper.readValue(request.getBody(), User.class);
            User loggedInUser = userService.loginUser(loginDetails.getUsername(), loginDetails.getPassword());

            String message = "Logged in as: " + loggedInUser.getUsername();
            Response response = new Response();
            response.setStatus(HttpStatus.OK);
            response.setContentType(HttpContentType.APPLICATION_JSON);
            response.setBody(message);

            return response;
        } catch (IllegalArgumentException e) {
            Response response = new Response();
            response.setStatus(HttpStatus.BAD_REQUEST);
            response.setContentType(HttpContentType.TEXT_PLAIN);
            response.setBody(e.getMessage());
            return response;

        } catch (Exception e) {
            return status();
        }
    }

    private Response getUserData(String username) {
        try {
            Optional<User> userOpt = userService.findUserByUsername(username);
            if (userOpt.isEmpty()) {
                throw new IllegalArgumentException("User not found");
            }

            User user = userOpt.get();
            ObjectMapper objectMapper = new ObjectMapper();
            String userDataJson = objectMapper.writeValueAsString(user);

            Response response = new Response();
            response.setStatus(HttpStatus.OK);
            response.setContentType(HttpContentType.APPLICATION_JSON);
            response.setBody(userDataJson);
            return response;

        } catch (IllegalArgumentException e) {
            Response response = new Response();
            response.setStatus(HttpStatus.NOT_FOUND);
            response.setContentType(HttpContentType.TEXT_PLAIN);
            response.setBody(e.getMessage());
            return response;

        } catch (Exception e) {
            return status();
        }
    }

    private boolean validateToken(Request request, String routeUsername) {
        String token = request.getAuthorization();
        if (token == null) {
            return false;
        }

        String expectedTokenPrefix = "Bearer ";
        if (!token.startsWith(expectedTokenPrefix)) {
            return false;
        }

        String tokenUsername = token.substring(expectedTokenPrefix.length()).replace("-mtcgToken", "");
        if (!tokenUsername.equals(routeUsername)) {
            return false;
        }

        return userRepository.findByUsername(tokenUsername).isPresent();
    }

    private String extractUsernameFromRoute(String route) {
        String[] parts = route.split("/");
        // Expecting route format like "/users/username"
        if (parts.length >= 3 && parts[1].equals("users")) {
            return parts[2]; // The username part
        }
        return null;
    }

    private Response badRequestResponse() {
        return createResponse(HttpStatus.BAD_REQUEST, "Bad request");
    }
    private Response unauthorizedResponse() {
        return createResponse(HttpStatus.UNAUTHORIZED, "Unauthorized access");
    }

    private Response createResponse(HttpStatus status, String body) {
        Response response = new Response();
        response.setStatus(status);
        response.setContentType(HttpContentType.TEXT_PLAIN);
        response.setBody(body);
        return response;
    }
    private Response status() {
        Response response = new Response();
        response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR);
        response.setBody("etwas ist schiefgelaufen");
        return response;
    }
}

