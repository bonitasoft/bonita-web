package org.bonitasoft.console.common.server.servlet;

import javax.servlet.http.HttpServletRequest;


public class ResourceLocationReader {

    /**
     * file location
     */
    public final static String LOCATION_PARAM = "location";

    public String getResourceLocationFromRequest(final HttpServletRequest request) {
        String fileName = request.getParameter(LOCATION_PARAM);
        if (fileName == null) {
            final String pathInfo = request.getPathInfo();
            if (pathInfo != null && pathInfo.startsWith("/") && pathInfo.length() > 1) {
                //Support relative calls to the THEME from the homepage using ../theme
                fileName = pathInfo.substring(1);
            }
        }
        return fileName;
    }
}
