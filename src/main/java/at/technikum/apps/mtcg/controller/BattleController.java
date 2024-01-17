package at.technikum.apps.mtcg.controller;

import at.technikum.apps.mtcg.entity.Battle;
import at.technikum.apps.mtcg.service.BattleService;
import at.technikum.server.http.HttpContentType;
import at.technikum.server.http.HttpStatus;
import at.technikum.server.http.Request;
import at.technikum.server.http.Response;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;

import java.util.HashMap;
import java.util.Map;

@RequiredArgsConstructor
public class BattleController extends Controller {
    private final BattleService battleService;
    private final Map<String, String> waitingPlayers = new HashMap<>(); // Map to store waiting players

    @Override
    public boolean supports(String route) {
        return route.startsWith("/battles");
    }

    @Override
    public Response handle(Request request) {
        if (!request.getMethod().equals("POST")) {
            return badRequestResponse();
        }

        try {
            String playerUsername = extractUsernameFromToken(request.getAuthorization());

            if (playerUsername == null) {
                return unauthorizedResponse();
            }

            synchronized (this) {
                if (waitingPlayers.containsKey(playerUsername)) {
                    return status(HttpStatus.BAD_REQUEST, "Player already waiting for a battle");
                }

                // Check if there is an opponent waiting
                if (!waitingPlayers.isEmpty()) {
                    // Retrieve the first waiting player
                    String opponentUsername = waitingPlayers.keySet().iterator().next();
                    waitingPlayers.remove(opponentUsername);

                    // Initiate the battle with the waiting player
                    Battle battle = battleService.initiateBattle(playerUsername, opponentUsername);

                    String responseJson = new ObjectMapper().writeValueAsString(battle);
                    return createResponse(HttpStatus.OK, responseJson, HttpContentType.APPLICATION_JSON);
                } else {
                    // Store this player's request and wait for an opponent
                    waitingPlayers.put(playerUsername, request.getAuthorization());
                    return status(HttpStatus.OK, "Waiting for an opponent to join the battle");
                }
            }
        } catch (IllegalArgumentException e) {
            return status(HttpStatus.BAD_REQUEST, e.getMessage());
        } catch (Exception e) {
            return status(HttpStatus.INTERNAL_SERVER_ERROR, "An internal error occurred");
        }
    }

}
