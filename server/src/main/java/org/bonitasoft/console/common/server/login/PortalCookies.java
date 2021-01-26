package org.bonitasoft.console.common.server.login;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.bonitasoft.console.common.server.login.filter.TokenGenerator;
import org.bonitasoft.console.common.server.preferences.properties.PropertiesFactory;

public class PortalCookies {

    public static final String TENANT_COOKIE_NAME = "bonita.tenant";

    /**
     * set the CSRF security token to the HTTP response as cookie.
     */
    public void addCSRFTokenCookieToResponse(HttpServletRequest request, HttpServletResponse res, Object apiTokenFromClient) {
        String path = System.getProperty("bonita.csrf.cookie.path", request.getContextPath());
        invalidatePreviousCSRFTokenCookie(request, res, path);

        Cookie csrfCookie = new Cookie(TokenGenerator.X_BONITA_API_TOKEN, apiTokenFromClient.toString());
        // cookie path can be set via system property.
        // Can be set to '/' when another app is deployed in same server than bonita and want to share csrf cookie
        csrfCookie.setPath(path);
        csrfCookie.setSecure(isCSRFTokenCookieSecure());
        res.addCookie(csrfCookie);
    }

    // when a cookie already exists on a different path than the one expected, we need to invalidate it.
    // since there is no way of knowing the path as it is not sent server-side (getPath return null) we invalidate any cookie found
    // see https://bonitasoft.atlassian.net/browse/BS-15883 and BS-16241
    private void invalidatePreviousCSRFTokenCookie(HttpServletRequest request, HttpServletResponse res, String path) {
        Cookie cookie = getCookie(request, TokenGenerator.X_BONITA_API_TOKEN);
        if (cookie != null) {
            cookie.setMaxAge(0);
            cookie.setValue("");
            res.addCookie(cookie);
            invalidateRootCookie(res, cookie, path);
        }
    }

    //Studio sets the cookie to the path '/' so this is required when using a bunble after a studio in the same browser
    public void invalidateRootCookie(HttpServletResponse res, Cookie cookie, String path) {
        if (!"/".equals(path)) {
            Cookie rootCookie = (Cookie) cookie.clone();
            rootCookie.setPath("/");
            res.addCookie(rootCookie);
        }
    }

    // protected for testing
    public boolean isCSRFTokenCookieSecure() {
        return PropertiesFactory.getSecurityProperties().isCSRFTokenCookieSecure();
    }
    
    public void addTenantCookieToResponse(HttpServletResponse response, Long tenantId) {
        Cookie tenantCookie = new Cookie(TENANT_COOKIE_NAME, String.valueOf(tenantId));
        response.addCookie(tenantCookie);
    }

    public String getTenantCookieFromRequest(HttpServletRequest request) {
        return getCookieValue(request, TENANT_COOKIE_NAME);
    }

    private String getCookieValue(HttpServletRequest request, String name) {
        Cookie cookie = getCookie(request, name);
        return cookie == null ? null : cookie.getValue();
    }

    public Cookie getCookie(HttpServletRequest request, String name) {
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals(name)) {
                    return cookie;
                }
            }
        }
        return null;
    }
}
