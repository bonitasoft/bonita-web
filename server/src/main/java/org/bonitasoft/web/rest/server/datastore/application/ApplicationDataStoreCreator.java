/*******************************************************************************
 * Copyright (C) 2009, 2013 BonitaSoft S.A.
 * BonitaSoft is a trademark of BonitaSoft SA.
 * This software file is BONITASOFT CONFIDENTIAL. Not For Distribution.
 * For commercial licensing information, contact:
 * BonitaSoft, 32 rue Gustave Eiffel – 38000 Grenoble
 * or BonitaSoft US, 51 Federal Street, Suite 305, San Francisco, CA 94107
 *******************************************************************************/
package org.bonitasoft.web.rest.server.datastore.application;

import org.bonitasoft.engine.api.ApplicationAPI;
import org.bonitasoft.engine.api.PageAPI;
import org.bonitasoft.engine.api.TenantAPIAccessor;
import org.bonitasoft.engine.session.APISession;
import org.bonitasoft.web.toolkit.client.common.exception.api.APIException;

/**
 * @author Elias Ricken de Medeiros
 *
 */
public class ApplicationDataStoreCreator {

    public ApplicationDataStore create(final APISession session) {
        ApplicationAPI applicationAPI;
        PageAPI pageAPI;
        try {
            applicationAPI = TenantAPIAccessor.getApplicationAPI(session);
            pageAPI = TenantAPIAccessor.getPageAPI(session);
            return new ApplicationDataStore(session, applicationAPI, pageAPI, new ApplicationItemConverter());
        } catch (final Exception e) {
            throw new APIException(e);
        }
    }
}
