package at.technikum.apps.mtcg.controller;

import at.technikum.apps.mtcg.repository.ScoreboardEntry;
import at.technikum.apps.mtcg.service.UserService;
import at.technikum.server.http.HttpContentType;
import at.technikum.server.http.HttpStatus;
import at.technikum.server.http.Request;
import at.technikum.server.http.Response;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;

import java.util.List;

@RequiredArgsConstructor
public class ScoreboardController extends Controller{

    private final UserService userService;

    @Override
    public boolean supports(String route) {
        return route.equals("/scoreboard");
    }

    @Override
    public Response handle(Request request) {
        if (!request.getMethod().equals("GET")) {
            return badRequestResponse();
        }

        String username = extractUsernameFromToken(request.getAuthorization());
        if (username == null) {
            return unauthorizedResponse();
        }
        try {
            List<ScoreboardEntry> scoreboard = userService.getScoreboard();
            ObjectMapper objectMapper = new ObjectMapper();
            String scoreboardJson = objectMapper.writeValueAsString(scoreboard);

            return createResponse(HttpStatus.OK, scoreboardJson, HttpContentType.APPLICATION_JSON);
        } catch (Exception e) {
            return status(HttpStatus.INTERNAL_SERVER_ERROR, "Internal Server Error");
        }
    }
}