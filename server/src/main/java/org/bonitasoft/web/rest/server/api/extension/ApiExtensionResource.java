package org.bonitasoft.web.rest.server.api.extension;

import java.io.IOException;
import java.util.Map;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

import org.bonitasoft.console.common.server.page.PageMappingService;
import org.bonitasoft.console.common.server.page.RestApiRenderer;
import org.bonitasoft.console.common.server.page.RestApiResponse;
import org.bonitasoft.engine.exception.BonitaException;
import org.restlet.Request;
import org.restlet.data.CharacterSet;
import org.restlet.data.CookieSetting;
import org.restlet.data.Header;
import org.restlet.data.MediaType;
import org.restlet.data.Status;
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

    private PageMappingService pageMappingService;

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
        } catch (BonitaException e) {
            return new StringRepresentation(e.getMessage());
        }
    }

    private void fillAllContent(StringRepresentation stringRepresentation, RestApiResponse restApiResponse) throws BonitaException {
        if (restApiResponse == null) {
            throw new BonitaException("error: restApiResponse is null");
        }
        stringRepresentation.setCharacterSet(new CharacterSet(restApiResponse.getCharacterSet()));
        stringRepresentation.setMediaType(new MediaType(restApiResponse.getMediaType()));
        getResponse().setStatus(new Status(restApiResponse.getHttpStatus()));
        fillContent(stringRepresentation, restApiResponse);
        fillHeaders(restApiResponse);
        fillCookies(restApiResponse);
    }

    private void fillCookies(RestApiResponse restApiResponse) {
        for (Cookie cookie : restApiResponse.getAdditionalCookies()) {
            getResponse().getCookieSettings().add(new CookieSetting(cookie.getVersion(), cookie.getName(), cookie.getValue(), cookie.getPath(), cookie.getDomain(), cookie.getComment(), cookie.getMaxAge(), cookie.getSecure()));
        }
    }

    private void fillHeaders(RestApiResponse restApiResponse) {
        for (Map.Entry<String, String> entry : restApiResponse.getAdditionalHeaders().entrySet()) {
            getResponse().getHeaders().add(new Header(entry.getKey(), entry.getValue()));
        }
    }

    private void fillContent(StringRepresentation stringRepresentation, RestApiResponse restApiResponse) {
        if (restApiResponse.getResponse() != null) {
            stringRepresentation.setText(restApiResponse.getResponse().toString());
        }
    }


    private RestApiResponse handleRequest() throws BonitaException {
        final Request request = getRequest();
        final ResourceExtensionResolver resourceExtensionResolver = new ResourceExtensionResolver(request, pageMappingService);
        final HttpServletRequest httpServletRequest = ServletUtils.getRequest(request);
        try {
            return restApiRenderer.handleRestApiCall(httpServletRequest, resourceExtensionResolver);
        } catch (InstantiationException | IllegalAccessException | IOException | BonitaException e) {
            throw new BonitaException(e.getMessage());
        }
    }
}