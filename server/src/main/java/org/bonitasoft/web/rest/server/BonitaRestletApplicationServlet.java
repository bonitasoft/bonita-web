/*******************************************************************************
 * Copyright (C) 2009, 2013 BonitaSoft S.A.
 * BonitaSoft is a trademark of BonitaSoft SA.
 * This software file is BONITASOFT CONFIDENTIAL. Not For Distribution.
 * For commercial licensing information, contact:
 * BonitaSoft, 32 rue Gustave Eiffel – 38000 Grenoble
 * or BonitaSoft US, 51 Federal Street, Suite 305, San Francisco, CA 94107
 *******************************************************************************/
package org.bonitasoft.web.rest.server;

import org.bonitasoft.console.common.server.preferences.constants.WebBonitaConstantsUtils;
import org.bonitasoft.web.rest.server.api.custom.SpringTenantBeanAccessor;
import org.restlet.Application;
import org.restlet.Context;
import org.restlet.ext.servlet.ServerServlet;

public class BonitaRestletApplicationServlet extends ServerServlet {

    private static final long serialVersionUID = 1L;

    @Override
    protected Application createApplication(final Context parentContext) {
        SpringTenantBeanAccessor beanAccessor = new SpringTenantBeanAccessor(WebBonitaConstantsUtils.getInstance().getConfFolder());
		final BonitaRestletApplication bonitaRestletApplication = new BonitaRestletApplication(new FinderFactory(), beanAccessor);
        bonitaRestletApplication.setContext(parentContext.createChildContext());
        return bonitaRestletApplication;
    }

}
