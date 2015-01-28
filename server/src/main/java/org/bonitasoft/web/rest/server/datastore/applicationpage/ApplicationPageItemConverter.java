/*******************************************************************************
 * Copyright (C) 2009, 2013 BonitaSoft S.A.
 * BonitaSoft is a trademark of BonitaSoft SA.
 * This software file is BONITASOFT CONFIDENTIAL. Not For Distribution.
 * For commercial licensing information, contact:
 * BonitaSoft, 32 rue Gustave Eiffel – 38000 Grenoble
 * or BonitaSoft US, 51 Federal Street, Suite 305, San Francisco, CA 94107
 *******************************************************************************/
package org.bonitasoft.web.rest.server.datastore.applicationpage;

import org.bonitasoft.engine.business.application.ApplicationPage;
import org.bonitasoft.web.rest.model.applicationpage.ApplicationPageItem;

/**
 * @author Julien Mege
 *
 */
public class ApplicationPageItemConverter {

    public ApplicationPageItem toApplicationPageItem(final ApplicationPage applicationPage) {
        final ApplicationPageItem item = new ApplicationPageItem();
        item.setId(applicationPage.getId());
        item.setToken(applicationPage.getToken());
        item.setPageId(applicationPage.getPageId());
        item.setApplicationId(applicationPage.getApplicationId());
        return item;
    }

}
