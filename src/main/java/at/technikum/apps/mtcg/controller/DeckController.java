package at.technikum.apps.mtcg.controller;

import at.technikum.apps.mtcg.entity.Card;
import at.technikum.apps.mtcg.entity.User;
import at.technikum.apps.mtcg.repository.CardRepository;
import at.technikum.apps.mtcg.service.DeckService;
import at.technikum.apps.mtcg.service.UserService;
import at.technikum.server.http.HttpContentType;
import at.technikum.server.http.HttpStatus;
import at.technikum.server.http.Request;
import at.technikum.server.http.Response;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.UUID;
import java.util.logging.Logger;
@RequiredArgsConstructor
public class DeckController extends Controller{

    private final DeckService deckService;
    private final UserService userService;
    private static final Logger logger = Logger.getLogger(CardRepository.class.getName());

    @Override
    public boolean supports(String route) {
        return route.equals("/deck");
    }
    @Override
    public Response handle(Request request) {
        String username = extractUsernameFromToken(request.getAuthorization());
        if (username == null) {
            return unauthorizedResponse();
        }

        User user = userService.findUserByUsername(username).orElseThrow(() -> new IllegalArgumentException("User not found"));

        return switch (request.getMethod()) {
            case "GET" -> getDeck(user);
            case "PUT" -> configureDeck(request, user);
            default -> badRequestResponse();
        };
    }
    private Response getDeck(User user) {
        try {
            List<Card> deck = deckService.getDeckByUserId(user.getId());
            ObjectMapper objectMapper = new ObjectMapper();
            String deckJson = objectMapper.writeValueAsString(deck);

            return createResponse(HttpStatus.OK, deckJson, HttpContentType.APPLICATION_JSON);
        } catch (Exception e) {
            logger.severe("Error fetching deck: " + e.getMessage());
            return status(HttpStatus.INTERNAL_SERVER_ERROR, "An internal error occurred");
        }
    }
    private Response configureDeck(Request request, User user) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            List<UUID> cardIds = objectMapper.readValue(request.getBody(), new TypeReference<>() {
            });

            deckService.configureDeck(user.getId(), cardIds);

            return createResponse(HttpStatus.OK, "Deck configured successfully", HttpContentType.TEXT_PLAIN);
        } catch (JsonProcessingException e) {
            logger.warning("Invalid JSON format: " + e.getMessage());
            return status(HttpStatus.BAD_REQUEST, "Invalid JSON format");
        } catch (IllegalArgumentException e) {
            logger.warning("Deck configuration error: " + e.getMessage());
            return status(HttpStatus.BAD_REQUEST, e.getMessage());
        } catch (Exception e) {
            logger.severe("Error configuring deck: " + e.getMessage());
            return status(HttpStatus.INTERNAL_SERVER_ERROR, "An internal error occurred");
        }
    }



}
