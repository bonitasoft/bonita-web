package org.bonitasoft.web.rest.server.api.custom;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.bonitasoft.console.common.server.page.PageRenderer;
import org.bonitasoft.engine.exception.BonitaException;
import org.bonitasoft.engine.session.APISession;
import org.restlet.ext.servlet.ServletUtils;
import org.restlet.resource.Delete;
import org.restlet.resource.Get;
import org.restlet.resource.Patch;
import org.restlet.resource.Post;
import org.restlet.resource.Put;
import org.restlet.resource.ResourceException;
import org.restlet.resource.ServerResource;

/**
 * @author Laurent Leseigneur
 */
public class CustomResource extends ServerResource {

    private final CustomResourceDescriptor customResourceDescriptor;

    private final PageRenderer pageRenderer;

    public CustomResource(CustomResourceDescriptor customResourceDescriptor, PageRenderer pageRenderer) {
    	this.customResourceDescriptor = customResourceDescriptor;
        this.pageRenderer = pageRenderer;
    }

	@Get
    public Object handleGet() {
        return handleRequest();
    }

    @Post
    public Object handlePost() {
        return handleRequest();
    }

    @Put
    public Object handlePut() {
    	return handleRequest();
    }

    @Patch
    public Object handlePatch() {
    	return handleRequest();
    }

    @Delete
    public Object handleDelete() {
    	return handleRequest();
    }

    private Object handleRequest() {
    	final HttpServletRequest request = ServletUtils.getRequest(getRequest());
    	final HttpServletResponse response = ServletUtils.getResponse(getResponse());
        try (final ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
        	final CustomHttpServletResponse customeHttpServletResponse = new CustomHttpServletResponse(outputStream, response);
            pageRenderer.displayCustomPage(request, customeHttpServletResponse, getAPISession(request), customResourceDescriptor.getPageName());
            return outputStream.toString();
        } catch (InstantiationException | IllegalAccessException | IOException | BonitaException e) {
            throw new ResourceException(e);
        }
    }

    protected APISession getAPISession(final HttpServletRequest request) {
        final HttpSession httpSession = request.getSession();
        return (APISession) httpSession.getAttribute("apiSession");
    }

}
