package at.technikum.apps.mtcg.controller;

import at.technikum.apps.mtcg.entity.Card;
import at.technikum.apps.mtcg.entity.User;
import at.technikum.apps.mtcg.service.CardService;
import at.technikum.apps.mtcg.service.UserService;
import at.technikum.server.http.HttpContentType;
import at.technikum.server.http.HttpStatus;
import at.technikum.server.http.Request;
import at.technikum.server.http.Response;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;

import java.util.List;

@RequiredArgsConstructor
public class CardController extends Controller{

    private final CardService cardService;
    private final UserService userService;


    @Override
    public boolean supports(String route) {
        return route.startsWith("/cards");
    }

    @Override
    public Response handle(Request request) {
        if (request.getMethod().equals("GET")) {
            return handleGetCards(request);
        }
        return badRequestResponse();
    }

    private Response handleGetCards(Request request) {
        try {
            String username = extractUsernameFromToken(request.getAuthorization());
            if (username == null) {
                return unauthorizedResponse();
            }
            User user = userService.findUserByUsername(username).orElseThrow(() -> new IllegalArgumentException("User not found"));

            List<Card> cards = cardService.getCardsByUserId(user.getId());
            ObjectMapper objectMapper = new ObjectMapper();
            String cardsJson = objectMapper.writeValueAsString(cards);

            return createResponse(HttpStatus.OK, cardsJson, HttpContentType.APPLICATION_JSON);

        } catch (IllegalArgumentException e) {
            return status(HttpStatus.BAD_REQUEST, e.getMessage());
        } catch (Exception e) {
            return status(HttpStatus.INTERNAL_SERVER_ERROR, "An internal error occurred");
        }
    }
}
