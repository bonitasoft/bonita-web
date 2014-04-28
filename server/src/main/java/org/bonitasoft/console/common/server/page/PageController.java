/*******************************************************************************
 * Copyright (C) 2014 BonitaSoft S.A.
 * BonitaSoft is a trademark of BonitaSoft SA.
 * This software file is BONITASOFT CONFIDENTIAL. Not For Distribution.
 * For commercial licensing information, contact:
 * BonitaSoft, 32 rue Gustave Eiffel – 38000 Grenoble
 * or BonitaSoft US, 51 Federal Street, Suite 305, San Francisco, CA 94107
 *******************************************************************************/
package org.bonitasoft.console.common.server.page;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public interface PageController {

    /**
     * Let the custom page parse request for specific attribute handling.
     * 
     * @param request
     *            the HTTP servlet request intended to be used as in a servlet
     * @param response
     *            the HTTP servlet response intended to be used as in a servlet
     * @param pageResourceProvider
     *            provide access to the resources contained in the custom page zip
     * @param pageContext
     *            provide access to the data relative to the context in which the custom page is displayed
     */
    public void doGet(HttpServletRequest request, HttpServletResponse response, PageResourceProvider pageResourceProvider, PageContext pageContext);
}
