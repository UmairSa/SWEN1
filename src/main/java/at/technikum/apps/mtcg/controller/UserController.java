package at.technikum.apps.mtcg.controller;

import at.technikum.apps.mtcg.entity.User;
import at.technikum.apps.mtcg.service.UserService;
import at.technikum.server.http.HttpContentType;
import at.technikum.server.http.HttpStatus;
import at.technikum.server.http.Request;
import at.technikum.server.http.Response;
import com.fasterxml.jackson.databind.ObjectMapper;

public class UserController extends Controller {
    public final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    private Response status(HttpStatus status) {
        Response response = new Response();
        response.setStatus(status);
        response.setBody("etwas ist schiefgelaufen");
        return response;
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
        return status(HttpStatus.BAD_REQUEST);
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
            return status(HttpStatus.INTERNAL_SERVER_ERROR);
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
            return status(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}