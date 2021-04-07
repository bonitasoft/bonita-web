package org.bonitasoft.console.common.server.servlet;

import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.bonitasoft.engine.session.APISession;

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

    protected abstract Optional<IconContent> retrieveIcon(Long iconId, APISession apiSession);

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
