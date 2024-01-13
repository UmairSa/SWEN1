package at.technikum.apps.mtcg.controller;

import at.technikum.apps.mtcg.entity.Card;
import at.technikum.apps.mtcg.entity.Pack;
import at.technikum.apps.mtcg.service.PackService;
import at.technikum.server.http.HttpContentType;
import at.technikum.server.http.HttpStatus;
import at.technikum.server.http.Request;
import at.technikum.server.http.Response;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.Arrays;
import java.util.List;

public class PackController extends Controller{

    private final PackService packService;
    public PackController(PackService packService) {
        this.packService = packService;
    }

    @Override
    public boolean supports(String route) {
        return route.startsWith("/packages");
    }
    @Override
    public Response handle(Request request) {
        if (request.getRoute().equals("/packages") && request.getMethod().equals("POST")) {
            return createPack(request);
        }
        return badRequestResponse();
    }

    private Response createPack(Request request) {
        try {

            if (!isAdminAuthorized(request)) {
                return unauthorizedResponse();
            }

            ObjectMapper objectMapper = new ObjectMapper();
            List<Card> cards = Arrays.asList(objectMapper.readValue(request.getBody(), Card[].class));

            Pack newPack = packService.createPack(cards);

            String message = "Package created successfully. Package ID: " + newPack.getPackId();
            return createResponse(HttpStatus.OK, message, HttpContentType.APPLICATION_JSON);

        } catch (Exception e) {
            return createResponse(HttpStatus.INTERNAL_SERVER_ERROR, "An error occurred: " + e.getMessage(), HttpContentType.TEXT_PLAIN);
        }
    }
    private boolean isAdminAuthorized(Request request) {
        String token = request.getAuthorization();
        if (token == null) {
            return false;
        }
        String expectedTokenPrefix = "Bearer ";
        if (!token.startsWith(expectedTokenPrefix)) {
            return false;
        }

        String adminTokenIdentifier = "admin-mtcgToken";
        return token.endsWith(adminTokenIdentifier);
    }

}
