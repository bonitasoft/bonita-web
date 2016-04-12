package org.bonitasoft.console.common.server.login;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class PortalCookies {

    private static final String TENANT_COOKIE_NAME = "bonita.tenant";

    public static void addTenantCookieToResponse(HttpServletResponse response, Long tenantId) {
        Cookie tenantCookie = new Cookie(TENANT_COOKIE_NAME, String.valueOf(tenantId));
        response.addCookie(tenantCookie);
    }

    public static String getTenantCookieFromRequest(HttpServletRequest request) {
        return getCookieValue(request, TENANT_COOKIE_NAME);
    }

    private static String getCookieValue(HttpServletRequest request, String name) {
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals(name)) {
                    return cookie.getValue();
                }
            }
        }
        return null;
    }
}
