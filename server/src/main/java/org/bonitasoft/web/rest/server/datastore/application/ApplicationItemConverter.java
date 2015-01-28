/*******************************************************************************
 * Copyright (C) 2009, 2013 BonitaSoft S.A.
 * BonitaSoft is a trademark of BonitaSoft SA.
 * This software file is BONITASOFT CONFIDENTIAL. Not For Distribution.
 * For commercial licensing information, contact:
 * BonitaSoft, 32 rue Gustave Eiffel – 38000 Grenoble
 * or BonitaSoft US, 51 Federal Street, Suite 305, San Francisco, CA 94107
 *******************************************************************************/
package org.bonitasoft.web.rest.server.datastore.application;

import java.util.Map;

import org.bonitasoft.engine.business.application.Application;
import org.bonitasoft.engine.business.application.ApplicationCreator;
import org.bonitasoft.engine.business.application.ApplicationUpdater;
import org.bonitasoft.web.rest.model.application.ApplicationItem;

/**
 * @author Elias Ricken de Medeiros
 *
 */
public class ApplicationItemConverter {

    public ApplicationItem toApplicationItem(final Application application) {
        final ApplicationItem item = new ApplicationItem();
        item.setId(application.getId());
        item.setToken(application.getToken());
        item.setDisplayName(application.getDisplayName());
        item.setVersion(application.getVersion());
        item.setDescription(application.getDescription());
        item.setIconPath(application.getIconPath());
        item.setCreationDate(Long.toString(application.getCreationDate().getTime()));
        item.setCreatedBy(application.getCreatedBy());
        item.setLastUpdateDate(Long.toString(application.getLastUpdateDate().getTime()));
        item.setUpdatedBy(application.getUpdatedBy());
        item.setState(application.getState());
        item.setProfileId(application.getProfileId());
        if (application.getHomePageId() != null) {
            item.setHomePageId(application.getHomePageId());
        } else {
            item.setHomePageId(-1L);
        }
        return item;
    }

    public ApplicationCreator toApplicationCreator(final ApplicationItem appItem) {
        final ApplicationCreator creator = new ApplicationCreator(appItem.getToken(), appItem.getDisplayName(), appItem.getVersion());
        creator.setDescription(appItem.getDescription());
        creator.setProfileId(appItem.getProfileId().toLong());
        creator.setIconPath(appItem.getIconPath());
        return creator;
    }

    public ApplicationUpdater toApplicationUpdater(final Map<String, String> attributes) {
        final ApplicationUpdater applicationUpdater = new ApplicationUpdater();

        if (attributes.containsKey(ApplicationItem.ATTRIBUTE_TOKEN)) {
            applicationUpdater.setToken(attributes.get(ApplicationItem.ATTRIBUTE_TOKEN));
        }
        if (attributes.containsKey(ApplicationItem.ATTRIBUTE_DISPLAY_NAME)) {
            applicationUpdater.setDisplayName(attributes.get(ApplicationItem.ATTRIBUTE_DISPLAY_NAME));
        }
        if (attributes.containsKey(ApplicationItem.ATTRIBUTE_DESCRIPTION)) {
            applicationUpdater.setDescription(attributes.get(ApplicationItem.ATTRIBUTE_DESCRIPTION));
        }
        if (attributes.containsKey(ApplicationItem.ATTRIBUTE_PROFILE_ID)) {
            applicationUpdater.setProfileId(Long.parseLong(attributes.get(ApplicationItem.ATTRIBUTE_PROFILE_ID)));
        }

        if (attributes.containsKey(ApplicationItem.ATTRIBUTE_HOME_PAGE_ID)) {
            Long homePageId = Long.parseLong(attributes.get(ApplicationItem.ATTRIBUTE_HOME_PAGE_ID));
            if (homePageId == -1) {
                homePageId = null;
            }
            applicationUpdater.setHomePageId(homePageId);
        }

        if (attributes.containsKey(ApplicationItem.ATTRIBUTE_STATE)) {
            applicationUpdater.setState(attributes.get(ApplicationItem.ATTRIBUTE_STATE));
        }
        if (attributes.containsKey(ApplicationItem.ATTRIBUTE_VERSION)) {
            applicationUpdater.setVersion(attributes.get(ApplicationItem.ATTRIBUTE_VERSION));
        }
        if (attributes.containsKey(ApplicationItem.ATTRIBUTE_ICON_PATH)) {
            applicationUpdater.setIconPath(attributes.get(ApplicationItem.ATTRIBUTE_ICON_PATH));
        }

        return applicationUpdater;

    }

}
