package at.technikum.apps.mtcg.controller;

import at.technikum.apps.mtcg.entity.User;
import at.technikum.apps.mtcg.repository.UserRepository;
import at.technikum.apps.mtcg.service.UserService;
import at.technikum.server.http.HttpContentType;
import at.technikum.server.http.HttpStatus;
import at.technikum.server.http.Request;
import at.technikum.server.http.Response;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import java.util.Optional;

@RequiredArgsConstructor
public class UserController extends Controller {
    private final UserService userService;
    private final UserRepository userRepository;

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
            return switch (request.getMethod()) {
                case "GET" -> getUserData(routeUsername);
                case "PUT" -> updateUserData(request, routeUsername);
                default -> badRequestResponse();
            };
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
            return status(HttpStatus.BAD_REQUEST, e.getMessage());

        } catch (Exception e) {
            return status(HttpStatus.INTERNAL_SERVER_ERROR, "An internal error occurred");
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
            return status(HttpStatus.BAD_REQUEST, e.getMessage());

        } catch (Exception e) {
            return status(HttpStatus.INTERNAL_SERVER_ERROR, "An internal error occurred");
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
            return status(HttpStatus.NOT_FOUND, e.getMessage());

        } catch (Exception e) {
            return status(HttpStatus.INTERNAL_SERVER_ERROR, "An internal error occurred");
        }
    }

    private Response updateUserData(Request request, String routeUsername) {
        try {

            ObjectMapper objectMapper = new ObjectMapper();
            User updatedInfo = objectMapper.readValue(request.getBody(), User.class);

            Optional<User> userOpt = userService.findUserByUsername(routeUsername);
            System.out.println("USERNAME: " + routeUsername);
            if (userOpt.isEmpty()) {
                throw new IllegalArgumentException("User not found");
            }

            User user = userOpt.get();
            user.setName(updatedInfo.getName());
            user.setBio(updatedInfo.getBio());
            user.setImage(updatedInfo.getImage());

            userService.updateUser(user);

            String message = "User updated: " + user.getUsername();
            Response response = new Response();
            response.setStatus(HttpStatus.OK);
            response.setContentType(HttpContentType.APPLICATION_JSON);
            response.setBody(message);
            return response;

        } catch (IllegalArgumentException e) {
            return status(HttpStatus.BAD_REQUEST, e.getMessage());
        } catch (Exception e) {
            return status(HttpStatus.INTERNAL_SERVER_ERROR, "An internal error occurred");
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
        if (parts.length >= 3 && parts[1].equals("users")) {
            return parts[2]; //username part
        }
        return null;
    }


}