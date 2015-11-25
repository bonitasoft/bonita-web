package org.bonitasoft.console.common.server.page;

import java.io.Serializable;
import java.nio.charset.Charset;
import java.util.List;
import java.util.Map;

import javax.servlet.http.Cookie;

import org.apache.http.HttpStatus;


/**
 * @deprecated Use org.bonitasoft.web.extension.rest.RestApiResponse instead
 */
@Deprecated
public class RestApiResponse {

    /**
     * default http status code
     */
    public static int DEFAULT_STATUS = HttpStatus.SC_OK;

    /**
     * default character set
     */
    public static String DEFAULT_CHARACTER_SET = Charset.forName("UTF-8").name();

    /**
     * default media type
     */
    public static String DEFAULT_MEDIA_TYPE = "application/json";

    private final Serializable response;
    private final int httpStatus;
    private final Map<String, String> additionalHeaders;
    private final List<Cookie> additionalCookies;
    private final String mediaType;
    private final String characterSet;

    public RestApiResponse(Serializable response, int httpStatus, Map<String, String> additionalHeaders, List<Cookie> additionalCookies, String mediaType, String characterSet) {
        this.response = response;
        this.httpStatus = httpStatus;
        this.additionalHeaders = additionalHeaders;
        this.additionalCookies = additionalCookies;
        this.mediaType = mediaType;
        this.characterSet = characterSet;
    }

    public Serializable getResponse() {
        return response;
    }

    public int getHttpStatus() {
        return httpStatus;
    }

    public Map<String, String> getAdditionalHeaders() {
        return additionalHeaders;
    }

    public List<Cookie> getAdditionalCookies() {
        return additionalCookies;
    }

    public String getCharacterSet() {
        return characterSet;
    }

    public String getMediaType() {
        return mediaType;
    }
}
