/**
 * Copyright (C) 2014 BonitaSoft S.A.
 * BonitaSoft, 32 rue Gustave Eiffel - 38000 Grenoble
 * This library is free software; you can redistribute it and/or modify it under the terms
 * of the GNU Lesser General Public License as published by the Free Software Foundation
 * version 2.1 of the License.
 * This library is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY;
 * without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * See the GNU Lesser General Public License for more details.
 * You should have received a copy of the GNU Lesser General Public License along with this
 * program; if not, write to the Free Software Foundation, Inc., 51 Franklin Street, Fifth
 * Floor, Boston, MA 02110-1301, USA.
 **/
package org.bonitasoft.web.rest.model.application;

import org.bonitasoft.web.rest.model.portal.page.PageItem;
import org.bonitasoft.web.toolkit.client.data.APIID;
import org.bonitasoft.web.toolkit.client.data.item.IItem;
import org.bonitasoft.web.toolkit.client.data.item.Item;
import org.bonitasoft.web.toolkit.client.data.item.template.ItemHasUniqueId;


/**
 * @author Elias Ricken de Medeiros
 *
 */
public class ApplicationItem extends Item implements ItemHasUniqueId {

    public static final String ATTRIBUTE_TOKEN = "token";

    public static final String ATTRIBUTE_DISPLAY_NAME = "displayName";

    public static final String ATTRIBUTE_VERSION = "version";

    public static final String ATTRIBUTE_DESCRIPTION = "description";

    public static final String ATTRIBUTE_ICON_PATH = "iconPath";

    public static final String ATTRIBUTE_CREATION_DATE = "creationDate";

    public static final String ATTRIBUTE_CREATED_BY = "createdBy";

    public static final String ATTRIBUTE_LAST_UPDATE_DATE = "lastUpdateDate";

    public static final String ATTRIBUTE_UPDATED_BY = "updatedBy";

    public static final String ATTRIBUTE_STATE = "state";

    public static final String ATTRIBUTE_PROFILE_ID = "profileId";

    public static final String ATTRIBUTE_HOME_PAGE_ID = "homePageId";

    public static final String ATTRIBUTE_LAYOUT_ID = "layoutId";

    public static final String ATTRIBUTE_THEME_ID = "themeId";

    public ApplicationItem() {
        super();
    }

    public ApplicationItem(final IItem item) {
        super(item);
    }

    @Override
    public ApplicationDefinition getItemDefinition() {
        return ApplicationDefinition.get();
    }

    @Override
    public void setId(final String id) {
        setId(APIID.makeAPIID(id));
    }

    @Override
    public void setId(final Long id) {
        setId(APIID.makeAPIID(id));
    }

    public String getToken() {
        return getAttributeValue(ATTRIBUTE_TOKEN);
    }

    public void setToken(final String token) {
        setAttribute(ATTRIBUTE_TOKEN, token);
    }

    public String getDisplayName() {
        return getAttributeValue(ATTRIBUTE_DISPLAY_NAME);
    }

    public void setDisplayName(final String name) {
        setAttribute(ATTRIBUTE_DISPLAY_NAME, name);
    }

    public String getVersion() {
        return getAttributeValue(ATTRIBUTE_VERSION);
    }

    public void setVersion(final String version) {
        setAttribute(ATTRIBUTE_VERSION, version);
    }

    public String getDescription() {
        return getAttributeValue(ATTRIBUTE_DESCRIPTION);
    }

    public void setDescription(final String description) {
        setAttribute(ATTRIBUTE_DESCRIPTION, description);
    }

    public String getIconPath() {
        return getAttributeValue(ATTRIBUTE_ICON_PATH);
    }

    public void setIconPath(final String iconPath) {
        setAttribute(ATTRIBUTE_ICON_PATH, iconPath);
    }

    public String getCreationDate() {
        return getAttributeValue(ATTRIBUTE_CREATION_DATE);
    }

    public void setCreationDate(final String creationDate) {
        setAttribute(ATTRIBUTE_CREATION_DATE, creationDate);
    }

    public long getCreatedBy() {
        return getAttributeValueAsLong(ATTRIBUTE_CREATED_BY);
    }

    public void setCreatedBy(final long createdBy) {
        setAttribute(ATTRIBUTE_CREATED_BY, createdBy);
    }

    public String getLastUpdateDate() {
        return getAttributeValue(ATTRIBUTE_LAST_UPDATE_DATE);
    }

    public void setLastUpdateDate(final String lastUpdateDate) {
        setAttribute(ATTRIBUTE_LAST_UPDATE_DATE, lastUpdateDate);
    }

    public long getUpdatedBy() {
        return getAttributeValueAsLong(ATTRIBUTE_UPDATED_BY);
    }

    public void setUpdatedBy(final long updatedBy) {
        setAttribute(ATTRIBUTE_UPDATED_BY, updatedBy);
    }

    public String getState() {
        return getAttributeValue(ATTRIBUTE_STATE);
    }

    public void setState(final String state) {
        setAttribute(ATTRIBUTE_STATE, state);
    }

    public APIID getProfileId() {
        return getAttributeValueAsAPIID(ATTRIBUTE_PROFILE_ID);
    }

    public void setProfileId(final Long profileId) {
        setAttribute(ATTRIBUTE_PROFILE_ID, profileId);
    }

    public APIID getHomePageId() {
        return getAttributeValueAsAPIID(ATTRIBUTE_HOME_PAGE_ID);
    }

    public void setHomePageId(final Long homePageId) {
        setAttribute(ATTRIBUTE_HOME_PAGE_ID, homePageId);
    }

    public APIID getLayoutId() {
        return getAttributeValueAsAPIID(ATTRIBUTE_LAYOUT_ID);
    }

    public void setLayoutId(final Long layoutId) {
        setAttribute(ATTRIBUTE_LAYOUT_ID, layoutId);
    }

    public APIID getThemeId() {
        return getAttributeValueAsAPIID(ATTRIBUTE_THEME_ID);
    }

    public void setThemeId(final Long themeId) {
        setAttribute(ATTRIBUTE_THEME_ID, themeId);
    }

    public PageItem getLayout() {
        return (PageItem) getDeploy(ATTRIBUTE_LAYOUT_ID);
    }

    public PageItem getTheme() {
        return (PageItem) getDeploy(ATTRIBUTE_THEME_ID);
    }

}
