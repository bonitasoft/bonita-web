/**
 * Copyright (C) 2012 BonitaSoft S.A.
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
package org.bonitasoft.web.rest.model.portal.page;

import java.util.Date;

import org.bonitasoft.web.rest.model.identity.UserItem;
import org.bonitasoft.web.toolkit.client.data.APIID;
import org.bonitasoft.web.toolkit.client.data.item.IItem;
import org.bonitasoft.web.toolkit.client.data.item.Item;
import org.bonitasoft.web.toolkit.client.data.item.ItemDefinition;
import org.bonitasoft.web.toolkit.client.data.item.template.ItemHasUniqueId;

/**
 * @author Fabio Lombardi
 *
 */
public class PageItem extends Item implements ItemHasUniqueId {

    public static final String ATTRIBUTE_PROCESS_ID = "processDefinitionId";

    public static final String ATTRIBUTE_URL_TOKEN = "urlToken";

    public static final String ATTRIBUTE_DISPLAY_NAME = "displayName";

    public static final String ATTRIBUTE_DESCRIPTION = "description";

    public static final String ATTRIBUTE_IS_PROVIDED = "isProvided";

    public static final String ATTRIBUTE_LAST_UPDATE_DATE = "lastUpdateDate";

    public static final String ATTRIBUTE_CREATION_DATE = "creationDate";

    public static final String ATTRIBUTE_CREATED_BY_USER_ID = "createdBy";

    public static final String ATTRIBUTE_UPDATED_BY_USER_ID = "updatedBy";

    public static final String ATTRIBUTE_CONTENT_NAME = "contentName";

    public static final String ATTRIBUTE_CONTENT_TYPE = "contentType";

    // //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // FILTERS
    // //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    public static final String FILTER_CONTENT_TYPE = "contentType";

    public PageItem() {
    }

    public PageItem(final IItem item) {
        super(item);
    }

    @Override
    public void setId(final String id) {
        setAttribute(ATTRIBUTE_ID, id);
    }

    @Override
    public void setId(final Long id) {
        setAttribute(ATTRIBUTE_ID, id);
    }

    public void setProcessId(final String id) {
        setAttribute(ATTRIBUTE_PROCESS_ID, id);
    }

    public void setProcessId(final Long id) {
        setAttribute(ATTRIBUTE_PROCESS_ID, id);
    }

    public void setUrlToken(final String name) {
        setAttribute(ATTRIBUTE_URL_TOKEN, name);
    }

    public void setDisplayName(final String name) {
        setAttribute(ATTRIBUTE_DISPLAY_NAME, name);
    }

    public void setDescription(final String description) {
        setAttribute(ATTRIBUTE_DESCRIPTION, description);
    }

    public void setCreationDate(final String date) {
        setAttribute(ATTRIBUTE_CREATION_DATE, date);
    }

    public void setCreationDate(final Date date) {
        setAttribute(ATTRIBUTE_CREATION_DATE, date);
    }

    public void setCreatedByUserId(final String id) {
        setAttribute(ATTRIBUTE_CREATED_BY_USER_ID, id);
    }

    public void setCreatedByUserId(final Long id) {
        setAttribute(ATTRIBUTE_CREATED_BY_USER_ID, id);
    }

    public void setCreatedByUserId(final APIID id) {
        setAttribute(ATTRIBUTE_CREATED_BY_USER_ID, id);
    }

    public void setUpdatedByUserId(final String id) {
        setAttribute(ATTRIBUTE_UPDATED_BY_USER_ID, id);
    }

    public void setUpdatedByUserId(final Long id) {
        setAttribute(ATTRIBUTE_UPDATED_BY_USER_ID, id);
    }

    public void setUpdatedByUserId(final APIID id) {
        setAttribute(ATTRIBUTE_UPDATED_BY_USER_ID, id);
    }

    public void setLastUpdateDate(final String date) {
        setAttribute(ATTRIBUTE_LAST_UPDATE_DATE, date);
    }

    public void setLastUpdateDate(final Date date) {
        setAttribute(ATTRIBUTE_LAST_UPDATE_DATE, date);
    }

    public void setIsProvided(final boolean isProvided) {
        setAttribute(ATTRIBUTE_IS_PROVIDED, isProvided);
    }

    public void setContentName(final String name) {
        setAttribute(ATTRIBUTE_CONTENT_NAME, name);
    }

    public void setContentType(final String contentType) {
        setAttribute(ATTRIBUTE_CONTENT_TYPE, contentType);
    }

    public String getUrlToken() {
        return getAttributeValue(ATTRIBUTE_URL_TOKEN);
    }

    public String getDisplayName() {
        return getAttributeValue(ATTRIBUTE_DISPLAY_NAME);
    }

    public String getDescription() {
        return getAttributeValue(ATTRIBUTE_DESCRIPTION);
    }

    public boolean isProvided() {
        return Boolean.parseBoolean(getAttributeValue(ATTRIBUTE_IS_PROVIDED));
    }

    public String getContentName() {
        return getAttributeValue(ATTRIBUTE_CONTENT_NAME);
    }

    public Date getLastUpdateDate() {
        return getAttributeValueAsDate(ATTRIBUTE_LAST_UPDATE_DATE);
    }

    @Override
    public ItemDefinition<PageItem> getItemDefinition() {
        return PageDefinition.get();
    }

    public Date getCreationDate() {
        return getAttributeValueAsDate(ATTRIBUTE_CREATION_DATE);
    }

    public APIID getCreatedByUserId() {
        return getAttributeValueAsAPIID(ATTRIBUTE_CREATED_BY_USER_ID);
    }

    public UserItem getCreatedByUser() {
        return (UserItem) getDeploy(ATTRIBUTE_CREATED_BY_USER_ID);
    }

    public APIID getUpdatedByUserId() {
        return getAttributeValueAsAPIID(ATTRIBUTE_UPDATED_BY_USER_ID);
    }

    public APIID getProcessId() {
        return getAttributeValueAsAPIID(ATTRIBUTE_PROCESS_ID);
    }

    public String getContentType() {
        return getAttributeValue(ATTRIBUTE_CONTENT_TYPE);
    }

    public UserItem getUpdatedByUser() {
        return (UserItem) getDeploy(ATTRIBUTE_UPDATED_BY_USER_ID);
    }
}
