package org.bonitasoft.console.common.server.servlet;

import org.bonitasoft.engine.session.APISession;
import org.bonitasoft.web.toolkit.client.common.exception.api.APIException;
import org.bonitasoft.web.toolkit.client.common.exception.api.APIItemNotFoundException;
import org.bonitasoft.web.toolkit.client.common.exception.api.APIMalformedUrlException;
import org.bonitasoft.web.toolkit.client.common.exception.http.ServerException;
import org.restlet.Server;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;

public abstract class IconServlet extends HttpServlet {
    private static final Logger LOGGER = Logger.getLogger(IconServlet.class.getName());

    @Override
    protected void doGet(final HttpServletRequest request, final HttpServletResponse response) throws ServletException {
        String iconIdPath = request.getPathInfo();
        if (iconIdPath == null || iconIdPath.isEmpty()) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }
        Long iconId = parseLong(iconIdPath);
        if (iconId == null) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }
        Optional<IconContent> iconContent = retrieveIcon(iconId,
                (APISession) request.getSession().getAttribute("apiSession"));
        if (!iconContent.isPresent()) {
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
            return;
        }
        response.setContentType(iconContent.get().getMimeType());
        response.setCharacterEncoding("UTF-8");
        try {
            setHeaders(request, response, iconId);
        } catch (UnsupportedEncodingException e) {
            logAndThrowException(e, "Error while generating the headers.");
        }
        try (OutputStream out = response.getOutputStream()) {
            response.setContentLength(iconContent.get().getContent().length);
            out.write(iconContent.get().getContent());
        } catch (final IOException e) {
            logAndThrowException(e, "Error while generating the response.");
        }

    }

    @Override
    protected void doDelete(final HttpServletRequest request, final HttpServletResponse response) throws ServletException {
        String pathInfo = request.getPathInfo();
        if (pathInfo == null || pathInfo.isEmpty()) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }
        Long entityId = parseLong(pathInfo);
        if (entityId == null) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }
        try {
            deleteIcon(entityId, (APISession) request.getSession().getAttribute("apiSession"), request, response);
        } catch (APIItemNotFoundException e) {
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
            return;
        } catch (APIMalformedUrlException e) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return;
        } catch (ServerException e) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            return;
        }
        try {
            setHeaders(request, response, entityId);
        } catch (UnsupportedEncodingException e) {
            logAndThrowException(e, "Error while generating the headers.");
        }
        response.setStatus(HttpServletResponse.SC_NO_CONTENT);
    }

    protected abstract Optional<IconContent> retrieveIcon(Long iconId, APISession apiSession);

    protected abstract void deleteIcon(Long entityId, APISession apiSession, HttpServletRequest request, HttpServletResponse response) throws ServerException;

    private Long parseLong(String iconIdPath) {
        try {
            return Long.valueOf(iconIdPath.substring(1));
        } catch (NumberFormatException e) {
            return null;
        }
    }

    private void logAndThrowException(IOException e, String msg) throws ServletException {
        LOGGER.log(Level.SEVERE, msg, e);
        throw new ServletException(e.getMessage(), e);
    }

    private void setHeaders(HttpServletRequest request, HttpServletResponse response, Long iconId) throws UnsupportedEncodingException {
        final String encodedFileName = URLEncoder.encode(String.valueOf(iconId), "UTF-8");
        final String userAgent = request.getHeader("User-Agent");
        if (userAgent != null && userAgent.contains("Firefox")) {
            response.setHeader("Content-Disposition", "inline; filename*=UTF-8''" + encodedFileName);
        } else {
            response.setHeader("Content-Disposition", "inline; filename=\"" + encodedFileName.replaceAll("\\+", " ") + "\"; filename*=UTF-8''"
                    + encodedFileName);
        }
    }
}
