package at.technikum.server.http;

import lombok.Getter;

@Getter
public class Request {

    // GET, POST, PUT, DELETE
    private String method;

    // /, /home, /package
    private String route;

    private String host;

    // application/json, text/plain
    private String contentType;

    // 0, 17
    private int contentLength;

    // none, "{ "name": "foo" }"
    private String body;

    @Getter
    private String authorization;

    public void setAuthorization(String authorization) { this.authorization = authorization; }

    public void setMethod(HttpMethod httpMethod) {
        this.method = httpMethod.getMethod();
    }

    public void setRoute(String route) {
        this.route = route;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    public void setContentLength(int contentLength) {
        this.contentLength = contentLength;
    }

    public void setBody(String body) {
        this.body = body;
    }
}
