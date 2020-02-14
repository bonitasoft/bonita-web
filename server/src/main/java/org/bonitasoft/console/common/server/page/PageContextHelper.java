package org.bonitasoft.console.common.server.page;

import java.util.Locale;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.bonitasoft.console.common.server.utils.LocaleUtils;
import org.bonitasoft.engine.session.APISession;

public class PageContextHelper {

    public static final String PROFILE_PARAM = "profile";

    public static final String ATTRIBUTE_API_SESSION = "apiSession";

    private final HttpServletRequest request;

    public PageContextHelper(HttpServletRequest request) {
        this.request = request;
    }

    public String getCurrentProfile() {
        return request.getParameter(PROFILE_PARAM);
    }

    public Locale getCurrentLocale() {
        return LocaleUtils.getUserLocale(request);
    }

    public APISession getApiSession() {
        final HttpSession httpSession = request.getSession();
        return (APISession) httpSession.getAttribute(ATTRIBUTE_API_SESSION);

    }
}