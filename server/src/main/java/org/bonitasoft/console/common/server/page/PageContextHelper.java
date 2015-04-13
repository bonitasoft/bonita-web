package org.bonitasoft.console.common.server.page;

import java.util.Locale;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.bonitasoft.engine.session.APISession;

public class PageContextHelper {

    public static final String PROFILE_PARAM = "profile";

    public static final String LOCALE_PARAM = "locale";

    public static final String DEFAULT_LOCALE = "en";

    public static final String LOCALE_COOKIE_NAME = "BOS_Locale";

    public static final String ATTRIBUTE_API_SESSION = "apiSession";

    private final HttpServletRequest request;

    public PageContextHelper(HttpServletRequest request) {
        this.request = request;
    }

    public String getCurrentProfile() {
        return request.getParameter(PROFILE_PARAM);
    }

    public Locale getCurrentLocale() {
        final String locale = request.getParameter(LOCALE_PARAM);
        if (locale == null) {
            for (final Cookie cookie : request.getCookies()) {
                if (cookie.getName().equals(LOCALE_COOKIE_NAME)) {
                    return new Locale(cookie.getValue());
                }
            }
            return new Locale(DEFAULT_LOCALE);
        }
        return new Locale(locale);
    }

    public APISession getApiSession() {
        final HttpSession httpSession = request.getSession();
        return (APISession) httpSession.getAttribute(ATTRIBUTE_API_SESSION);

    }
}