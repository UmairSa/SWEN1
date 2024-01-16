package at.technikum.server.http;

import lombok.Getter;

@Getter
public enum HttpContentType {
    TEXT_PLAIN("text/plain"), APPLICATION_JSON("application/json");

    private final String mimeType;
    HttpContentType(String mimeType) {
        this.mimeType = mimeType;
    }
}
