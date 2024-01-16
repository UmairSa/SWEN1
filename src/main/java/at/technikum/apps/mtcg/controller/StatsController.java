package at.technikum.apps.mtcg.controller;

import at.technikum.apps.mtcg.entity.UserStats;
import at.technikum.apps.mtcg.service.UserService;
import at.technikum.server.http.HttpContentType;
import at.technikum.server.http.HttpStatus;
import at.technikum.server.http.Request;
import at.technikum.server.http.Response;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class StatsController extends Controller{
    private final UserService userService;
    @Override
    public boolean supports(String route) {
        return route.equals("/stats");
    }

    @Override
    public Response handle(Request request) {
        String username = extractUsernameFromToken(request.getAuthorization());
        if (username == null) {
            return unauthorizedResponse();
        }
        try {
            UserStats stats = userService.getUserStats(username);
            ObjectMapper objectMapper = new ObjectMapper();
            String statsJson = objectMapper.writeValueAsString(stats);

            return createResponse(HttpStatus.OK, statsJson, HttpContentType.APPLICATION_JSON);
        } catch (IllegalArgumentException e) {
            return status(HttpStatus.BAD_REQUEST, e.getMessage());
        } catch (Exception e) {
            return status(HttpStatus.INTERNAL_SERVER_ERROR, "An internal error occurred");
        }
    }
}
