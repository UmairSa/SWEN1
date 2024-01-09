package at.technikum.apps.mtcg;

import at.technikum.server.http.*;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class CurlTest {

    MtcgApp mtcgApp = new MtcgApp();
    @Test
    void createUser() {
        Request request = new Request();
        request.setMethod(HttpMethod.POST);
        request.setRoute("/users");
        request.setContentType(HttpContentType.APPLICATION_JSON.getMimeType());
        request.setBody("{\"Username\":\"curltestuser\", \"Password\":\"daniel\"}");

        Response response = mtcgApp.handle(request);

        assertEquals(HttpStatus.OK.getCode(), response.getStatusCode());
    }
}