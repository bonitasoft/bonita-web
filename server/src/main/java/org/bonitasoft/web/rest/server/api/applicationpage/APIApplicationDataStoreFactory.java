/*******************************************************************************
 * Copyright (C) 2009, 2013 BonitaSoft S.A.
 * BonitaSoft is a trademark of BonitaSoft SA.
 * This software file is BONITASOFT CONFIDENTIAL. Not For Distribution.
 * For commercial licensing information, contact:
 * BonitaSoft, 32 rue Gustave Eiffel – 38000 Grenoble
 * or BonitaSoft US, 51 Federal Street, Suite 305, San Francisco, CA 94107
 *******************************************************************************/
package org.bonitasoft.web.rest.server.api.applicationpage;

import org.bonitasoft.console.common.server.preferences.constants.WebBonitaConstantsUtils;
import org.bonitasoft.engine.session.APISession;
import org.bonitasoft.web.rest.server.datastore.application.ApplicationDataStore;
import org.bonitasoft.web.rest.server.datastore.application.ApplicationDataStoreCreator;
import org.bonitasoft.web.rest.server.datastore.applicationpage.ApplicationPageDataStore;
import org.bonitasoft.web.rest.server.datastore.applicationpage.ApplicationPageDataStoreCreator;
import org.bonitasoft.web.rest.server.datastore.page.PageDatastore;
import org.bonitasoft.web.rest.server.datastore.page.PageDatastoreFactory;
import org.bonitasoft.web.rest.server.engineclient.EngineAPIAccessor;
import org.bonitasoft.web.toolkit.client.common.exception.api.APIException;

public class APIApplicationDataStoreFactory {

    public ApplicationPageDataStore createApplicationPageDataStore(final APISession session) {
        return new ApplicationPageDataStoreCreator().create(session);
    }

    public PageDatastore createPageDataStore(final APISession session) {
        try {
            final PageDatastoreFactory pageDatastoreFactory = new PageDatastoreFactory();
            return pageDatastoreFactory.create(session,
                    WebBonitaConstantsUtils.getInstance(session.getTenantId()),
                    new EngineAPIAccessor(session).getPageAPI());
        } catch (final Exception e) {
            throw new APIException(e);
        }
    }

    public ApplicationDataStore createApplicationDataStore(final APISession session) {
        return new ApplicationDataStoreCreator().create(session);
    }
}