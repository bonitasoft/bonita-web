package org.bonitasoft.web.rest.server.api.custom;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.bonitasoft.console.common.server.page.RestApiRenderer;
import org.bonitasoft.engine.session.APISession;
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
public class CustomResource extends ServerResource {

    private final CustomResourceDescriptor customResourceDescriptor;

    private final RestApiRenderer restApiRenderer;

    public CustomResource(CustomResourceDescriptor customResourceDescriptor, RestApiRenderer restApiRenderer) {
        this.customResourceDescriptor = customResourceDescriptor;
        this.restApiRenderer = restApiRenderer;
    }

    @Get("json")
    public Object handleGet() throws Exception {
        return handleRequest("GET");
    }

    @Post("json")
    public Object handlePost() throws Exception {
        return handleRequest("POST");
    }

    @Put("json")
    public Object handlePut() throws Exception {
        return handleRequest("PUT");
    }

    @Patch("json")
    public Object handlePatch() throws Exception {
        return handleRequest("PATCH");
    }

    @Delete("json")
    public Object handleDelete() throws Exception {
        return handleRequest("DELETE");
    }

    private Object handleRequest(String method) throws Exception {
        final HttpServletRequest request = ServletUtils.getRequest(getRequest());
        return restApiRenderer.handleCustomRestApiCall(request, getAPISession(request), customResourceDescriptor.getPageName());
    }

    protected APISession getAPISession(final HttpServletRequest request) {
        final HttpSession httpSession = request.getSession();
        return (APISession) httpSession.getAttribute("apiSession");
    }

}
