package at.technikum.apps.mtcg.controller;

import at.technikum.apps.mtcg.entity.Trade;
import at.technikum.apps.mtcg.entity.User;
import at.technikum.apps.mtcg.service.TradeService;
import at.technikum.apps.mtcg.service.UserService;
import at.technikum.server.http.HttpContentType;
import at.technikum.server.http.HttpStatus;
import at.technikum.server.http.Request;
import at.technikum.server.http.Response;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import java.util.List;
import java.util.UUID;
import java.util.logging.Logger;


@RequiredArgsConstructor
public class TradeController extends Controller{
    private static final Logger logger = Logger.getLogger(TradeController.class.getName());
    private final TradeService tradeService;
    private final UserService userService;
    @Override
    public boolean supports(String route) {
        return route.startsWith("/tradings");
    }

    @Override
    public Response handle(Request request) {
        String username = extractUsernameFromToken(request.getAuthorization());
        if (username == null) {
            return unauthorizedResponse();
        }

        User user = userService.findUserByUsername(username).orElseThrow(() -> new IllegalArgumentException("User not found"));

        if (request.getMethod().equals("POST") && request.getRoute().matches("/tradings/[\\w-]+")) {
            return acceptTrade(request, user.getId());
        } else {
            return switch (request.getMethod()) {
                case "GET" -> getTrades();
                case "POST" -> createTrade(request, user.getId());
                case "DELETE" -> deleteTrade(request, user.getId());
                default -> badRequestResponse();
            };
        }
    }
    private Response getTrades() {
        try {
            List<Trade> trades = tradeService.getAllTrades();
            ObjectMapper objectMapper = new ObjectMapper();
            String tradesJson = objectMapper.writeValueAsString(trades);
            return createResponse(HttpStatus.OK, tradesJson, HttpContentType.APPLICATION_JSON);
        } catch (Exception e) {
            return status(HttpStatus.INTERNAL_SERVER_ERROR, "Internal Server Error Occured");
        }
    }
    private Response createTrade(Request request, int userId) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            Trade trade = objectMapper.readValue(request.getBody(), Trade.class);
            trade.setUser1Id(userId);
            tradeService.createTrade(trade);
            return createResponse(HttpStatus.OK, "Trade created successfully", HttpContentType.TEXT_PLAIN);
        } catch (Exception e) {
            return status(HttpStatus.INTERNAL_SERVER_ERROR, "Internal Server Error Occured");
        }
    }

    private Response acceptTrade(Request request, int userId) {
        try {
            UUID tradeId = UUID.fromString(request.getRoute().substring(request.getRoute().lastIndexOf("/") + 1));
            UUID cardIdOffered = new ObjectMapper().readValue(request.getBody(), UUID.class);

            tradeService.acceptTrade(tradeId, cardIdOffered, userId);

            return createResponse(HttpStatus.OK, "Trade accepted successfully", HttpContentType.TEXT_PLAIN);
        } catch (JsonProcessingException e) {
            logger.warning("Invalid JSON format: " + e.getMessage());
            return status(HttpStatus.BAD_REQUEST, "Invalid JSON format: " + e.getMessage());
        } catch (IllegalArgumentException | IllegalStateException e) {
            logger.warning("Trade error: " + e.getMessage());
            return status(HttpStatus.BAD_REQUEST, "Trade error: " + e.getMessage());
        } catch (Exception e) {
            logger.severe("Internal Server Error Occurred: " + e.getMessage());
            return status(HttpStatus.INTERNAL_SERVER_ERROR, "Internal Server Error Occurred: " + e.getMessage());
        }
    }

    private Response deleteTrade(Request request, int userId) {
        UUID tradeId = UUID.fromString(request.getRoute().substring(request.getRoute().lastIndexOf("/") + 1));
        tradeService.deleteTrade(tradeId, userId);
        return createResponse(HttpStatus.OK, "Trade deleted successfully", HttpContentType.TEXT_PLAIN);
    }
}