package org.bonitasoft.web.rest.server.api.extension;

import java.io.IOException;
import javax.servlet.http.HttpServletRequest;

import org.bonitasoft.console.common.server.page.RestApiRenderer;
import org.bonitasoft.engine.exception.BonitaException;
import org.restlet.Request;
import org.restlet.data.CharacterSet;
import org.restlet.data.MediaType;
import org.restlet.ext.servlet.ServletUtils;
import org.restlet.representation.Representation;
import org.restlet.representation.StringRepresentation;
import org.restlet.resource.ServerResource;

/**
 * @author Laurent Leseigneur
 */
public class ApiExtensionResource extends ServerResource {

    private final ResourceExtensionDescriptor resourceExtensionDescriptor;

    private final RestApiRenderer restApiRenderer;

    public ApiExtensionResource(ResourceExtensionDescriptor resourceExtensionDescriptor, RestApiRenderer restApiRenderer) {
        this.resourceExtensionDescriptor = resourceExtensionDescriptor;
        this.restApiRenderer = restApiRenderer;
    }

    @Override
    public Representation doHandle() {
        final StringRepresentation stringRepresentation;
        try {

            final Object handleRequest = handleRequest();
            if (handleRequest != null) {
                stringRepresentation = new StringRepresentation(handleRequest.toString());
            } else {
                stringRepresentation = new StringRepresentation("");
            }
            stringRepresentation.setCharacterSet(CharacterSet.UTF_8);
            stringRepresentation.setMediaType(MediaType.APPLICATION_JSON);
            return stringRepresentation;
        } catch (BonitaException e) {
            return new StringRepresentation(e.getMessage());
        }
    }

    private Object handleRequest() throws BonitaException {
        final Request request = getRequest();
        final String httpMethod = request.getMethod().getName();
        if (httpMethod.equalsIgnoreCase(resourceExtensionDescriptor.getMethod())) {
            final HttpServletRequest httpServletRequest = ServletUtils.getRequest(request);
            try {
                return restApiRenderer.handleRestApiCall(httpServletRequest, resourceExtensionDescriptor.getPageName());
            } catch (InstantiationException | IllegalAccessException | IOException | BonitaException e) {
                throw new BonitaException("error while getting result", e);
            }
        }
        throw new BonitaException("method " + httpMethod + " is not supported");
    }
}