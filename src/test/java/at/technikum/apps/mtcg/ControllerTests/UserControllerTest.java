package at.technikum.apps.mtcg.ControllerTests;

import at.technikum.apps.mtcg.MtcgApp;
import at.technikum.apps.mtcg.entity.User;
import at.technikum.server.http.*;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class UserControllerTest {
    @Nested
    class UserControllerTestNest {

        private MtcgApp mtcgApp;
        private final String username = "kienboec"; // Replace with a valid username from your database
        private final String testToken = "Bearer " + username + "-mtcgToken"; // Replace with a valid token

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

        @Test
        void getUserDataWithInvalidToken() {
            String invalidToken = "Bearer invalidToken";

            Request request = new Request();
            request.setMethod(HttpMethod.GET);
            request.setRoute("/users/" + username);
            request.setAuthorization(invalidToken);

            Response response = mtcgApp.handle(request);

            assertEquals(HttpStatus.UNAUTHORIZED.getCode(), response.getStatusCode(), "Response status should be UNAUTHORIZED for invalid token");
        }

        @Test
        void getUserDataWithMismatchedUsernameAndToken() {
            String mismatchedUsername = "someOtherUser";
            String mismatchedToken = "Bearer " + mismatchedUsername + "-mtcgToken";

            Request request = new Request();
            request.setMethod(HttpMethod.GET);
            request.setRoute("/users/" + username); // 'username' is the expected username
            request.setAuthorization(mismatchedToken);

            Response response = mtcgApp.handle(request);

            assertEquals(HttpStatus.UNAUTHORIZED.getCode(), response.getStatusCode(), "Response status should be UNAUTHORIZED for mismatched username and token");
        }

        @Test
        void updateUserDataSuccess() {
            // Create a request to update user data
            Request request = new Request();
            request.setMethod(HttpMethod.PUT);
            request.setRoute("/users/" + username);
            request.setContentType(HttpContentType.APPLICATION_JSON.getMimeType());
            request.setAuthorization(testToken);

            // Create a user object with new data
            User updatedUser = new User();
            updatedUser.setName("New Name");
            updatedUser.setBio("New Bio");
            updatedUser.setImage("New Image URL");

            // Convert user object to JSON
            ObjectMapper objectMapper = new ObjectMapper();
            String updatedUserJson;
            try {
                updatedUserJson = objectMapper.writeValueAsString(updatedUser);
            } catch (JsonProcessingException e) {
                fail("Failed to serialize updated user to JSON", e);
                throw new RuntimeException(e);
            }
            request.setBody(updatedUserJson);

            // Send the request and get the response
            Response response = mtcgApp.handle(request);

            // Assert that the response status is OK and the response body contains the expected message
            assertEquals(HttpStatus.OK.getCode(), response.getStatusCode(), "Response status should be OK");
            assertTrue(response.getBody().contains("User updated: " + username), "Response body should contain success message");
        }

        @Test
        void updateUserDataWithInvalidToken() {
            String invalidToken = "Bearer invalidToken";

            Request request = new Request();
            request.setMethod(HttpMethod.PUT);
            request.setRoute("/users/" + username);
            request.setContentType(HttpContentType.APPLICATION_JSON.getMimeType());
            request.setAuthorization(invalidToken);

            Response response = mtcgApp.handle(request);

            assertEquals(HttpStatus.UNAUTHORIZED.getCode(), response.getStatusCode(), "Response status should be UNAUTHORIZED for invalid token");
        }
    }
}