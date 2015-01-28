/*******************************************************************************
 * Copyright (C) 2009, 2013 BonitaSoft S.A.
 * BonitaSoft is a trademark of BonitaSoft SA.
 * This software file is BONITASOFT CONFIDENTIAL. Not For Distribution.
 * For commercial licensing information, contact:
 * BonitaSoft, 32 rue Gustave Eiffel – 38000 Grenoble
 * or BonitaSoft US, 51 Federal Street, Suite 305, San Francisco, CA 94107
 *******************************************************************************/
package org.bonitasoft.web.rest.server.datastore.applicationmenu;

import java.util.Map;

import org.bonitasoft.engine.business.application.ApplicationMenu;
import org.bonitasoft.engine.business.application.ApplicationMenuCreator;
import org.bonitasoft.engine.business.application.ApplicationMenuUpdater;
import org.bonitasoft.web.rest.model.applicationmenu.ApplicationMenuItem;

/**
 * @author Julien Mege
 *
 */
public class ApplicationMenuItemConverter {

    public ApplicationMenuItem toApplicationMenuItem(final ApplicationMenu applicationMenu) {
        final ApplicationMenuItem item = new ApplicationMenuItem();
        item.setId(applicationMenu.getId());
        item.setApplicationId(applicationMenu.getApplicationId());
        item.setDisplayName(applicationMenu.getDisplayName());
        if (applicationMenu.getApplicationPageId() != null) {
            item.setApplicationPageId(applicationMenu.getApplicationPageId());
        } else {
            item.setApplicationPageId("-1");
        }
        if (applicationMenu.getParentId() != null) {
            item.setParentMenuId(applicationMenu.getParentId());
        } else {
            item.setParentMenuId("-1");
        }
        item.setMenuIndex(applicationMenu.getIndex());
        return item;
    }

    public ApplicationMenuCreator toApplicationMenuCreator(final ApplicationMenuItem item) {

        Long applicationId = null;
        if (item.getApplicationId() != null) {
            applicationId = item.getApplicationId().toLong();
        }
        Long applicationPageId = null;
        if (item.getApplicationPageId() != null) {
            applicationPageId = item.getApplicationPageId().toLong();
        }

        final ApplicationMenuCreator menuCreator = new ApplicationMenuCreator(applicationId, item.getDisplayName(), applicationPageId);

        if (item.getParentMenuId() != null) {
            menuCreator.setParentId(item.getParentMenuId().toLong());
        }

        return menuCreator;
    }

    public ApplicationMenuUpdater toApplicationMenuUpdater(final Map<String, String> attributes) {
        final ApplicationMenuUpdater applicationMenuUpdater = new ApplicationMenuUpdater();

        if (attributes.containsKey(ApplicationMenuItem.ATTRIBUTE_APPLICATION_PAGE_ID)) {
            Long appPageId = Long.parseLong(attributes.get(ApplicationMenuItem.ATTRIBUTE_APPLICATION_PAGE_ID));
            if (appPageId == -1){
                appPageId = null;
            }
            applicationMenuUpdater.setApplicationPageId(appPageId);
        }
        if (attributes.containsKey(ApplicationMenuItem.ATTRIBUTE_DISPLAY_NAME)) {
            applicationMenuUpdater.setDisplayName(attributes.get(ApplicationMenuItem.ATTRIBUTE_DISPLAY_NAME));
        }
        if (attributes.containsKey(ApplicationMenuItem.ATTRIBUTE_MENU_INDEX)) {
            applicationMenuUpdater.setIndex(Integer.parseInt(attributes.get(ApplicationMenuItem.ATTRIBUTE_MENU_INDEX)));
        }
        if (attributes.containsKey(ApplicationMenuItem.ATTRIBUTE_PARENT_MENU_ID)) {
            Long parentMenuId = Long.parseLong(attributes.get(ApplicationMenuItem.ATTRIBUTE_PARENT_MENU_ID));
            if (parentMenuId == -1){
                parentMenuId = null;
            }
            applicationMenuUpdater.setParentId(parentMenuId);
        }

        return applicationMenuUpdater;
    }

}
