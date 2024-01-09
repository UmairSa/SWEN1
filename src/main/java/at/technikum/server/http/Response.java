package at.technikum.server.http;

import lombok.Getter;

@Getter
public class Response {

    private int statusCode;

    private String statusMessage;

    private String contentType;

    private String body;

    public void setStatus(HttpStatus httpStatus) {
        this.statusCode = httpStatus.getCode();
        this.statusMessage = httpStatus.getMessage();
    }

    public void setContentType(HttpContentType httpContentType) {
        this.contentType = httpContentType.getMimeType();
    }

    public void setBody(String body) {
        this.body = body;
    }
}
