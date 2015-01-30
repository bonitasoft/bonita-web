/**
 * Copyright (C) 2014 BonitaSoft S.A.
 * BonitaSoft, 32 rue Gustave Eiffel - 38000 Grenoble
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 2.0 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
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
