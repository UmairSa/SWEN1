package at.technikum.apps.mtcg.controller;

import at.technikum.server.http.HttpStatus;
import at.technikum.server.http.Request;
import at.technikum.server.http.Response;

public abstract class Controller {
    public abstract boolean supports(String route);

    public abstract Response handle(Request request);

    protected Response status(HttpStatus status, String message) {
        Response response = new Response();
        response.setStatus(status);
        //response.setContentType(TEXT_PLAIN);
        response.setBody(message);
        return response;
    }

}
