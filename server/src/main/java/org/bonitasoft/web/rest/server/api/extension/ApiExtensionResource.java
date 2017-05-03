package org.bonitasoft.web.rest.server.api.extension;

import java.io.IOException;
import java.util.Map;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

import org.apache.http.HttpHeaders;
import org.bonitasoft.console.common.server.page.PageMappingService;
import org.bonitasoft.console.common.server.page.RestApiRenderer;
import org.bonitasoft.console.common.server.page.RestApiResponse;
import org.bonitasoft.engine.exception.BonitaException;
import org.restlet.Request;
import org.restlet.data.CharacterSet;
import org.restlet.data.CookieSetting;
import org.restlet.data.Header;
import org.restlet.data.MediaType;
import org.restlet.data.Range;
import org.restlet.data.Status;
import org.restlet.engine.header.RangeReader;
import org.restlet.ext.servlet.ServletUtils;
import org.restlet.representation.Representation;
import org.restlet.representation.StringRepresentation;
import org.restlet.resource.ServerResource;

/**
 * @author Laurent Leseigneur
 */
public class ApiExtensionResource extends ServerResource {

    public static final String EMPTY_RESPONSE = "";
    private final RestApiRenderer restApiRenderer;

    private final PageMappingService pageMappingService;

    public ApiExtensionResource(RestApiRenderer restApiRenderer, PageMappingService pageMappingService) {
        this.restApiRenderer = restApiRenderer;
        this.pageMappingService = pageMappingService;
    }

    @Override
    public Representation doHandle() {
        final StringRepresentation stringRepresentation = new StringRepresentation(EMPTY_RESPONSE);
        try {
            final RestApiResponse restApiResponse = handleRequest();
            fillAllContent(stringRepresentation, restApiResponse);
            return stringRepresentation;
        } catch (final BonitaException e) {
            return new StringRepresentation(e.getMessage());
        }
    }

    private void fillAllContent(StringRepresentation stringRepresentation, RestApiResponse restApiResponse)
            throws BonitaException {
        if (restApiResponse == null) {
            throw new BonitaException("error: restApiResponse is null");
        }
        stringRepresentation.setCharacterSet(new CharacterSet(restApiResponse.getCharacterSet()));
        stringRepresentation.setMediaType(new MediaType(restApiResponse.getMediaType()));
        getResponse().setStatus(new Status(restApiResponse.getHttpStatus()));
        fillContent(stringRepresentation, restApiResponse);
        fillHeaders(stringRepresentation, restApiResponse);
        fillCookies(restApiResponse);
    }

    private void fillCookies(RestApiResponse restApiResponse) {
        for (final Cookie cookie : restApiResponse.getAdditionalCookies()) {
            getResponse().getCookieSettings().add(new CookieSetting(cookie.getVersion(), cookie.getName(), cookie.getValue(),
                    cookie.getPath(), cookie.getDomain(), cookie.getComment(), cookie.getMaxAge(), cookie.getSecure()));
        }
    }

    private void fillHeaders(StringRepresentation stringRepresentation, RestApiResponse restApiResponse) {
        for (final Map.Entry<String, String> entry : restApiResponse.getAdditionalHeaders().entrySet()) {
            //RESTLET do not support content range header, use RESTLET Ranges instead
            if (HttpHeaders.CONTENT_RANGE.equals(entry.getKey())) {
                updateRepresentationRange(entry.getValue(), stringRepresentation);
            } else {
                getResponse().getHeaders().add(new Header(entry.getKey(), entry.getValue()));
            }
        }
    }

    /**
     * Parse the Content-Range header value and update the given representation.
     * Inspired by {@link RangeReader}
     * 
     * @param value
     *        Content-range header.
     * @param representation
     *        Representation to update.
     */
    private static void updateRepresentationRange(String value, Representation representation) {
        if (value != null && !value.isEmpty()) {
            final int index = value.indexOf('-');
            final int index1 = value.indexOf('/');

            if (index != -1) {
                final long pageNumber = (index == 0) ? Range.INDEX_LAST : Long
                        .parseLong(value.substring(0, index));
                final long pageSize = Long.parseLong(value.substring(index + 1,
                        index1));
                final String strLength = value.substring(index1 + 1, value.length());
                long instanceSize = 0;
                if (!("*".equals(strLength))) {
                    instanceSize = Long.parseLong(strLength);
                }
                representation.setRange(new Range(pageNumber, pageSize
                        - pageNumber + 1, instanceSize, ""));
            }
        }
    }

    private void fillContent(StringRepresentation stringRepresentation, RestApiResponse restApiResponse) {
        if (restApiResponse.getResponse() != null) {
            stringRepresentation.setText(restApiResponse.getResponse().toString());
        }
    }

    private RestApiResponse handleRequest() throws BonitaException {
        final Request request = getRequest();
        final ResourceExtensionResolver resourceExtensionResolver = new ResourceExtensionResolver(request,
                pageMappingService);
        final HttpServletRequest httpServletRequest = ServletUtils.getRequest(request);
        try {
            return restApiRenderer.handleRestApiCall(httpServletRequest, resourceExtensionResolver);
        } catch (InstantiationException | IllegalAccessException | IOException | BonitaException e) {
            throw new BonitaException(e.getMessage());
        }
    }
}
