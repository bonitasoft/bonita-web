package org.bonitasoft.web.rest.server.api.extension;

import javax.servlet.http.HttpServletRequest;

import org.bonitasoft.console.common.server.page.RestApiRenderer;
import org.bonitasoft.engine.exception.BonitaException;
import org.restlet.Request;
import org.restlet.ext.servlet.ServletUtils;
import org.restlet.resource.Delete;
import org.restlet.resource.Get;
import org.restlet.resource.Patch;
import org.restlet.resource.Post;
import org.restlet.resource.Put;
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

    @Get("json")
    public Object handleGet() throws Exception {
        return handleRequest();
    }

    @Post("json")
    public Object handlePost() throws Exception {
        return handleRequest();
    }

    @Put("json")
    public Object handlePut() throws Exception {
        return handleRequest();
    }

    @Patch("json")
    public Object handlePatch() throws Exception {
        return handleRequest();
    }

    @Delete("json")
    public Object handleDelete() throws Exception {
        return handleRequest();
    }

    private Object handleRequest() throws Exception {
        final Request request = getRequest();
        final String httpMethod = request.getMethod().getName();
        if (httpMethod.equalsIgnoreCase(resourceExtensionDescriptor.getMethod())) {
            final HttpServletRequest httpServletRequest = ServletUtils.getRequest(request);
            return restApiRenderer.handleRestApiCall(httpServletRequest, resourceExtensionDescriptor.getPageName());
        }
        throw new BonitaException("method " + httpMethod + " is not supported");
    }


}
