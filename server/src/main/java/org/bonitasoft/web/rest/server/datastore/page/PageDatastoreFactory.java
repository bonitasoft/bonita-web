package org.bonitasoft.web.rest.server.datastore.page;

import org.bonitasoft.console.common.server.page.CustomPageService;
import org.bonitasoft.console.common.server.preferences.constants.WebBonitaConstantsUtils;
import org.bonitasoft.console.common.server.preferences.properties.PropertiesFactory;
import org.bonitasoft.console.common.server.utils.BonitaHomeFolderAccessor;
import org.bonitasoft.engine.api.PageAPI;
import org.bonitasoft.engine.session.APISession;


public class PageDatastoreFactory {

    public PageDatastore create(final APISession engineSession, final WebBonitaConstantsUtils constantsValue, final PageAPI pageAPI) {
        return new PageDatastore(engineSession,
                constantsValue,
                pageAPI,
                new CustomPageService(),
                PropertiesFactory.getCompoundPermissionsMapping(engineSession.getTenantId()),
                PropertiesFactory.getResourcesPermissionsMapping(engineSession.getTenantId()),
                new BonitaHomeFolderAccessor());
    }
}
