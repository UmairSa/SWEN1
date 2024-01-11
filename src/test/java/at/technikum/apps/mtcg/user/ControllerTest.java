package at.technikum.apps.mtcg.user;

import at.technikum.apps.mtcg.MtcgApp;
import at.technikum.server.http.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class ControllerTest {
    @Nested
    class UserControllerTest {

        private MtcgApp mtcgApp;
        private String username = "Token"; // Replace with a valid username from your database
        private String testToken = "Bearer " + username + "-mtcgToken"; // Replace with a valid token

        @BeforeEach
        void setup() {
            mtcgApp = new MtcgApp();
        }

        @Test
        void getUserData() {
            Request request = new Request();
            request.setMethod(HttpMethod.GET);
            request.setRoute("/users/" + username);
            request.setContentType(HttpContentType.APPLICATION_JSON.getMimeType());
            request.setAuthorization(testToken);

            Response response = mtcgApp.handle(request);

            assertEquals(HttpStatus.OK.getCode(), response.getStatusCode(), "Response status should be OK");
            assertTrue(response.getBody().contains(username), "Response body should contain user data");
        }
    }
}
