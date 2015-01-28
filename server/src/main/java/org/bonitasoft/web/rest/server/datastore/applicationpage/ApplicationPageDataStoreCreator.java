/*******************************************************************************
 * Copyright (C) 2009, 2013 BonitaSoft S.A.
 * BonitaSoft is a trademark of BonitaSoft SA.
 * This software file is BONITASOFT CONFIDENTIAL. Not For Distribution.
 * For commercial licensing information, contact:
 * BonitaSoft, 32 rue Gustave Eiffel – 38000 Grenoble
 * or BonitaSoft US, 51 Federal Street, Suite 305, San Francisco, CA 94107
 *******************************************************************************/
package org.bonitasoft.web.rest.server.datastore.applicationpage;

import org.bonitasoft.engine.api.ApplicationAPI;
import org.bonitasoft.engine.api.TenantAPIAccessor;
import org.bonitasoft.engine.session.APISession;
import org.bonitasoft.web.toolkit.client.common.exception.api.APIException;

/**
 * @author Julien Mege
 *
 */
public class ApplicationPageDataStoreCreator {

    public ApplicationPageDataStore create(final APISession session) {
        ApplicationAPI applicationAPI;
        try {
            applicationAPI = TenantAPIAccessor.getApplicationAPI(session);
            return new ApplicationPageDataStore(session, applicationAPI, new ApplicationPageItemConverter());
        } catch (final Exception e) {
            throw new APIException(e);
        }
    }
}
