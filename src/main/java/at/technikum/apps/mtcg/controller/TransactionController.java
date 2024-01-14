package at.technikum.apps.mtcg.controller;

import at.technikum.apps.mtcg.service.TransactionService;
import at.technikum.server.http.HttpContentType;
import at.technikum.server.http.HttpStatus;
import at.technikum.server.http.Request;
import at.technikum.server.http.Response;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;

import java.io.IOException;
import java.util.Map;

@RequiredArgsConstructor
public class TransactionController extends Controller {

    private final TransactionService transactionService;

    @Override
    public boolean supports(String route) {
        return route.startsWith("/transactions/packages");
    }

    @Override
    public Response handle(Request request) {
        if (request.getRoute().equals("/transactions/packages") && request.getMethod().equals("POST")) {
            return acquirePackage(request);
        }
        return badRequestResponse();
    }

    private Response acquirePackage(Request request) {
        try {
            String username = extractUsernameFromToken(request.getAuthorization());
            if (username == null) {
                return unauthorizedResponse();
            }

            ObjectMapper objectMapper = new ObjectMapper();
            Map<String, Object> requestData = objectMapper.readValue(request.getBody(), Map.class);
            Integer packageId = (Integer) requestData.get("PackageId");

            if (packageId == null) {
                return badRequestResponse();
            }

            boolean success = transactionService.acquirePack(username, packageId);

            if (success) {
                return createResponse(HttpStatus.OK, "Package acquired successfully", HttpContentType.APPLICATION_JSON);
            } else {
                return createResponse(HttpStatus.BAD_REQUEST, "Failed to acquire package", HttpContentType.TEXT_PLAIN);
            }
        } catch (IOException e) {
            return createResponse(HttpStatus.INTERNAL_SERVER_ERROR, "An error occurred: " + e.getMessage(), HttpContentType.TEXT_PLAIN);
        }
    }
    private String extractUsernameFromToken(String token) {
        if (token == null) {
            return null;
        }
        String expectedTokenPrefix = "Bearer ";
        if (!token.startsWith(expectedTokenPrefix)) {
            return null;
        }
        return token.substring(expectedTokenPrefix.length()).replace("-mtcgToken", "");
    }
}