package org.bonitasoft.livingapps;

import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.bonitasoft.console.common.server.page.PageRenderer;
import org.bonitasoft.engine.exception.BonitaException;
import org.bonitasoft.engine.session.APISession;
import org.bonitasoft.livingapps.exception.CreationException;

public class ApplicationRouter {

    private final ApplicationModelFactory applicationModelFactory;

    public ApplicationRouter(final ApplicationModelFactory applicationModelFactory) {
        this.applicationModelFactory = applicationModelFactory;
    }

    public boolean route(final HttpServletRequest hsRequest, final HttpServletResponse hsResponse, final APISession session, final  PageRenderer pageRenderer)
            throws CreationException, BonitaException, IOException, ServletException, IllegalAccessException, InstantiationException {

        final ParsedRequest parsedRequest = parse(hsRequest.getContextPath(), hsRequest.getRequestURI());
        final ApplicationModel application = applicationModelFactory.createApplicationModel(parsedRequest.getApplicationName());

        if (parsedRequest.getPageToken() == null) {
            hsResponse.sendRedirect(application.getApplicationHomePage());
            return true;
        }

        if (hsRequest.getRequestURI().endsWith("themeResource")) {
            forwardTo("/portal/themeResource", hsRequest, hsResponse);
            return true;
        }

        if (hsRequest.getRequestURI().endsWith("pageResource")) {
            forwardTo("/portal/pageResource", hsRequest, hsResponse);
            return true;
        }

        if (application.hasPage(parsedRequest.getPageToken()) && application.authorize(session)) {
            hsRequest.setAttribute("application", application);
            hsRequest.setAttribute("customPage", application.getCustomPage(parsedRequest.getPageToken()));
            pageRenderer.displayCustomPage(hsRequest, hsResponse, session, application.getApplicationLayoutName());
            return true;
        }

        return false;
    }

    private void forwardTo(final String url, final ServletRequest servletRequest, final ServletResponse servletResponse)
            throws ServletException, IOException {
        servletRequest.getRequestDispatcher(url).forward(servletRequest, servletResponse);
    }

    private ParsedRequest parse(final String context, final String uri) {
        final Pattern pattern = Pattern.compile("^" + context + "/apps/(.*)$");
        final Matcher matcher = pattern.matcher(uri);
        if(!matcher.find()) {
            throw new RuntimeException("URI badly formed.");
        }
        final String[] fragments = matcher.group(1).split("/");
        String pageToken = null;
        if(fragments.length > 1) {
            pageToken =  fragments[1];
        }
        return new ParsedRequest(fragments[0], pageToken);
    }

    private class ParsedRequest {

        private final String applicationToken;

        private final String pageToken;

        public ParsedRequest(final String applicationToken, final String pageToken) {
            this.applicationToken = applicationToken;
            this.pageToken = pageToken;
        }

        public String getApplicationName() {
            return applicationToken;
        }

        public String getPageToken() {
            return pageToken;
        }
    }
}
