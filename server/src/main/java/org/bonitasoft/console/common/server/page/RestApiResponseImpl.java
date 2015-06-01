package org.bonitasoft.console.common.server.page;

import java.io.Serializable;
import java.util.List;
import java.util.Map;
import javax.servlet.http.Cookie;

/**
 * @author Laurent Leseigneur
 */
public class RestApiResponseImpl implements RestApiResponse {
    private final Serializable response;
    private final int httpStatus;
    private final Map<String, String> additionalHeaders;
    private final List<Cookie> additionalCookies;
    private final String mediaType;
    private final String characterSet;

    public RestApiResponseImpl(Serializable response, int httpStatus, Map<String, String> additionalHeaders, List<Cookie> additionalCookies, String mediaType, String characterSet) {
        this.response = response;
        this.httpStatus = httpStatus;
        this.additionalHeaders = additionalHeaders;
        this.additionalCookies = additionalCookies;
        this.mediaType = mediaType;
        this.characterSet = characterSet;
    }

    @Override
    public Serializable getResponse() {
        return response;
    }

    @Override
    public int getHttpStatus() {
        return httpStatus;
    }

    @Override
    public Map<String, String> getAdditionalHeaders() {
        return additionalHeaders;
    }

    @Override
    public List<Cookie> getAdditionalCookies() {
        return additionalCookies;
    }

    @Override
    public String getCharacterSet() {
        return characterSet;
    }

    @Override
    public String getMediaType() {
        return mediaType;
    }
}
