/**
 * Copyright (C) 2009, 2013 BonitaSoft S.A.
 * BonitaSoft, 32 rue Gustave Eiffel - 38000 Grenoble
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 2.0 of the License, or
 * (at your option) any later version.
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package org.bonitasoft.web.rest.model.portal.profile;

import org.bonitasoft.web.toolkit.client.data.APIID;
import org.bonitasoft.web.toolkit.client.data.item.IItem;
import org.bonitasoft.web.toolkit.client.data.item.Item;
import org.bonitasoft.web.toolkit.client.data.item.ItemDefinition;

/**
 * @author Fabio Lombardi
 *
 */
public class BonitaPageItem extends Item {

    /*
     * private String name;
     * private String token;
     * private String description;
     */

    public static final String ATTRIBUTE_NAME = "name";

    public static final String ATTRIBUTE_TOKEN = "token";

    public static final String ATTRIBUTE_DESCRIPTION = "description";

    public static final String ATTRIBUTE_DISPLAY_NAME = "displayName";

    public BonitaPageItem(final String token, final String name, final String description, final String menuName) {
        setId(APIID.makeAPIID(token));
        setToken(token);
        setName(name);
        setDescription(description);
        setMenuName(menuName);
    }

    /**
     * Default Constructor.
     */
    public BonitaPageItem() {
        super();
    }

    public BonitaPageItem(final IItem item) {
        super(item);
    }

    public void setName(final String name) {
        this.setAttribute(ATTRIBUTE_NAME, name);
    }

    public void setToken(final String token) {
        this.setAttribute(ATTRIBUTE_TOKEN, token);
    }

    public void setDescription(final String description) {
        this.setAttribute(ATTRIBUTE_DESCRIPTION, description);
    }

    private void setMenuName(final String menuName) {
        this.setAttribute(ATTRIBUTE_DISPLAY_NAME, menuName);
    }

    public String getName() {
        return this.getAttributeValue(ATTRIBUTE_NAME);
    }

    public String getToken() {
        return this.getAttributeValue(ATTRIBUTE_TOKEN);
    }

    public String getDescription() {
        return this.getAttributeValue(ATTRIBUTE_DESCRIPTION);
    }

    public String getMenuName() {
        return this.getAttributeValue(ATTRIBUTE_DISPLAY_NAME);
    }

    @Override
    public ItemDefinition<BonitaPageItem> getItemDefinition() {
        return new BonitaPageDefinition();
    }

}
