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
package org.bonitasoft.web.rest.model.monitoring.report;

import java.util.Date;

import org.bonitasoft.web.rest.model.identity.UserItem;
import org.bonitasoft.web.toolkit.client.data.APIID;
import org.bonitasoft.web.toolkit.client.data.item.Definitions;
import org.bonitasoft.web.toolkit.client.data.item.Item;
import org.bonitasoft.web.toolkit.client.data.item.ItemDefinition;
import org.bonitasoft.web.toolkit.client.data.item.template.ItemHasIcon;
import org.bonitasoft.web.toolkit.client.data.item.template.ItemHasLastUpdateDate;
import org.bonitasoft.web.toolkit.client.data.item.template.ItemHasUniqueId;

/**
 * @author Vincent Elcrin
 * 
 */
public class ReportItem extends Item implements ItemHasUniqueId, ItemHasIcon, ItemHasLastUpdateDate {

    public static final String ATTRIBUTE_NAME = "name";

    public static final String ATTRIBUTE_DESCRIPTION = "description";

    public static final String ATTRIBUTE_INSTALLED_ON = "installedOn";

    public static final String ATTRIBUTE_INSTALLED_BY = "installedBy";
    
    public static final String ATTRIBUTE_IS_PROVIDED = "isProvided";
    

    @Override
    public void setId(final String id) {
        setAttribute(ATTRIBUTE_ID, id);
    }

    @Override
    public void setId(final Long id) {
        setAttribute(ATTRIBUTE_ID, id);
    }

    public void setName(final String name) {
        setAttribute(ATTRIBUTE_NAME, name);
    }

    public void setDescription(final String description) {
        setAttribute(ATTRIBUTE_DESCRIPTION, description);
    }

    public void setInstalledOn(final Date date) {
        setAttribute(ATTRIBUTE_INSTALLED_ON, date);
    }

    public void setInstalledBy(final APIID userId) {
        setAttribute(ATTRIBUTE_INSTALLED_BY, userId);
    }
    
    public void setIsProvided(final boolean isProvided) {
        setAttribute(ATTRIBUTE_IS_PROVIDED, isProvided);
    }
    
    @Override
    public void setIcon(String icon) {
        setAttribute(ATTRIBUTE_ICON, icon);
    }

    @Override
    public void setLastUpdateDate(final String date) {
        setAttribute(ATTRIBUTE_LAST_UPDATE_DATE, date);
    }

    @Override
    public void setLastUpdateDate(final Date date) {
        setAttribute(ATTRIBUTE_LAST_UPDATE_DATE, date);
    }

    public String getName() {
        return getAttributeValue(ATTRIBUTE_NAME);
    }

    public String getDescription() {
        return getAttributeValue(ATTRIBUTE_DESCRIPTION);
    }

    public Date getInstalledOn() {
        return getAttributeValueAsDate(ATTRIBUTE_INSTALLED_ON);
    }

    public APIID getInstalledById() {
        return getAttributeValueAsAPIID(ATTRIBUTE_INSTALLED_BY);
    }

    public UserItem getInstalledBy() {
        return (UserItem) getDeploy(ATTRIBUTE_INSTALLED_BY);
    }
    
    public boolean isProvided() {
        return Boolean.parseBoolean(getAttributeValue(ATTRIBUTE_IS_PROVIDED));
    }
    
    public String getIcon() {
        return getAttributeValue(ATTRIBUTE_ICON);
    }

    @Override
    public Date getLastUpdateDate() {
        return getAttributeValueAsDate(ATTRIBUTE_LAST_UPDATE_DATE);
    }
    
    @Override
    public ItemDefinition getItemDefinition() {
        return Definitions.get(ReportDefinition.TOKEN);
    }
}
