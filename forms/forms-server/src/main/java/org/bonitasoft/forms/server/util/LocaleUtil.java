package org.bonitasoft.forms.server.util;

import java.util.Locale;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.bonitasoft.console.common.server.login.LoginManager;
import org.bonitasoft.web.rest.model.user.User;

public class LocaleUtil {

    /**
     * The cookie name for the forms locale
     */
    public static final String FORM_LOCALE_COOKIE_NAME = "BOS_Locale";

    /**
     * @param localeStr
     *        the user's locale as a string
     * @return the user's {@link Locale}
     */
    public Locale resolveLocale(final String localeStr) {
        final String[] localeParams = localeStr.split("_");
        final String language = localeParams[0];
        Locale userLocale = null;
        if (localeParams.length > 1) {
            final String country = localeParams[1];
            userLocale = new Locale(language, country);
        } else {
            userLocale = new Locale(language);
        }
        return userLocale;
    }

    /**
     * @return the user's locale as a String
     */
    public String getLocale(final HttpServletRequest request) {
        String localeStr = null;
        final HttpSession session = request.getSession();
        final User user = (User) session.getAttribute(LoginManager.USER_SESSION_PARAM_KEY);
        if (user != null) {
            localeStr = getFormLocale(request);
        }
        if (localeStr == null && user != null) {
            localeStr = user.getLocale();
        }
        if (localeStr == null) {
            localeStr = request.getLocale().toString();
        }
        return localeStr;
    }

    protected String getFormLocale(final HttpServletRequest request) {
        String userLocaleStr = null;
        final String theLocaleCookieName = FORM_LOCALE_COOKIE_NAME;
        final Cookie theCookies[] = request.getCookies();
        Cookie theCookie = null;
        if (theCookies != null) {
            for (int i = 0; i < theCookies.length; i++) {
                if (theCookies[i].getName().equals(theLocaleCookieName)) {
                    theCookie = theCookies[i];
                    userLocaleStr = theCookie.getValue().toString();
                    break;
                }
            }
        }
        return userLocaleStr;
    }
}
