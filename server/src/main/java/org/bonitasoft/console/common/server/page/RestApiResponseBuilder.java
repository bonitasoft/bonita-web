package org.bonitasoft.console.common.server.page;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.servlet.http.Cookie;

/**
 * @author Laurent Leseigneur
 */
public class RestApiResponseBuilder {

    private Serializable response;
    private int httpStatus ;
    private Map<String, String> additionalHeaders;
    private List<Cookie> additionalCookies;
    private String characterSet;
    private String mediaType;

    public RestApiResponseBuilder() {
        this.httpStatus = RestApiResponse.DEFAULT_STATUS;
        this.additionalHeaders = new HashMap<>();
        this.additionalCookies = new ArrayList<>();
        this.characterSet = RestApiResponse.DEFAULT_CHARACTER_SET;
        this.mediaType = RestApiResponse.DEFAULT_MEDIA_TYPE;
    }

    public RestApiResponseBuilder withResponse(Serializable response) {
        this.response = response;
        return this;
    }

    public RestApiResponseBuilder withResponseStatus(int httpStatus) {
        this.httpStatus = httpStatus;
        return this;
    }


    public RestApiResponseBuilder withAdditionalHeader(String headerName, String headerValue) {
        additionalHeaders.put(headerName, headerValue);
        return this;
    }

    public RestApiResponseBuilder withAdditionalCookie(Cookie cookie) {
        additionalCookies.add(cookie);
        return this;
    }

    public RestApiResponseBuilder withCharacterSet(String characterSet) {
        this.characterSet = characterSet;
        return this;
    }

    public RestApiResponseBuilder withMediaType(String mediaType) {
        this.mediaType = mediaType;
        return this;
    }

    public RestApiResponse build() {
        return new RestApiResponseImpl(response, httpStatus, additionalHeaders, additionalCookies, mediaType, characterSet);
    }
}
