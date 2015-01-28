/*******************************************************************************
 * Copyright (C) 2014 BonitaSoft S.A.
 * BonitaSoft is a trademark of BonitaSoft SA.
 * This software file is BONITASOFT CONFIDENTIAL. Not For Distribution.
 * For commercial licensing information, contact:
 * BonitaSoft, 32 rue Gustave Eiffel – 38000 Grenoble
 * or BonitaSoft US, 51 Federal Street, Suite 305, San Francisco, CA 94107
 *******************************************************************************/
package org.bonitasoft.console.common.server.page;

import java.io.File;

import org.bonitasoft.console.common.server.preferences.constants.WebBonitaConstantsUtils;
import org.bonitasoft.console.common.server.servlet.ResourceServlet;

/**
 * @author Anthony Birembaut
 * 
 */
public class PageResourceServlet extends ResourceServlet {

    /**
     * UID
     */
    private static final long serialVersionUID = 6745970275852563050L;

    /**
     * theme name : the theme folder's name
     */
    protected final static String PAGE_PARAM_NAME = "page";

    /**
     * resources subfolder name
     */
    protected final static String RESOURCES_SUBFOLDER_NAME = "resources";

    @Override
    protected String getResourceParameterName() {
        return PAGE_PARAM_NAME;
    }

    @Override
    protected File getResourcesParentFolder(final long tenantId) {
        return WebBonitaConstantsUtils.getInstance(tenantId).getPagesFolder();
    }

    @Override
    protected String getSubFolderName() {
        return RESOURCES_SUBFOLDER_NAME;
    }

}
