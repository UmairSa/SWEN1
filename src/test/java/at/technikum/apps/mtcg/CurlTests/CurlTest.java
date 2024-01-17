package at.technikum.apps.mtcg.CurlTests;

import at.technikum.apps.mtcg.MtcgApp;
import at.technikum.apps.mtcg.entity.Card;
import at.technikum.apps.mtcg.service.CardService;
import at.technikum.server.http.*;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

public class CurlTest {
    MtcgApp mtcgApp = new MtcgApp();
    @Test
    void createUser() {
        Request request = new Request();
        request.setMethod(HttpMethod.POST);
        request.setRoute("/users");
        request.setContentType(HttpContentType.APPLICATION_JSON.getMimeType());

        String randomUsername = "user" + UUID.randomUUID();
        request.setBody("{\"Username\":\"" + randomUsername +  "\", \"Password\":\"daniel\"}");
        Response response = mtcgApp.handle(request);

        assertEquals(HttpStatus.OK.getCode(), response.getStatusCode());
    }
    @Test
    void showAllAcquiredCardsWithValidToken() {

        Request request = new Request();
        request.setMethod(HttpMethod.GET);
        request.setRoute("/cards");
        request.setAuthorization("Bearer kienboec-mtcgToken");

        Response response = mtcgApp.handle(request);

        assertEquals(HttpStatus.OK.getCode(), response.getStatusCode());
        assertNotNull(response.getBody());
    }
    @Test
    void showAllAcquiredCardsWithoutToken() {
        // Mocking a request without Authorization header
        Request request = new Request();
        request.setMethod(HttpMethod.GET);
        request.setRoute("/cards");

        Response response = mtcgApp.handle(request);

        assertEquals(HttpStatus.UNAUTHORIZED.getCode(), response.getStatusCode());
    }

    @Test
    void showAllAcquiredCardsForAltenhofWithValidToken() {
        Request request = new Request();
        request.setMethod(HttpMethod.GET);
        request.setRoute("/cards");
        request.setAuthorization("Bearer altenhof-mtcgToken");

        Response response = mtcgApp.handle(request);

        assertEquals(HttpStatus.OK.getCode(), response.getStatusCode());
        assertNotNull(response.getBody());
    }



}