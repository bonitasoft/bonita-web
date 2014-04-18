/*******************************************************************************
 * Copyright (C) 2009, 2013 BonitaSoft S.A.
 * BonitaSoft is a trademark of BonitaSoft SA.
 * This software file is BONITASOFT CONFIDENTIAL. Not For Distribution.
 * For commercial licensing information, contact:
 * BonitaSoft, 32 rue Gustave Eiffel – 38000 Grenoble
 * or BonitaSoft US, 51 Federal Street, Suite 305, San Francisco, CA 94107
 *******************************************************************************/
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

    public BonitaPageItem(String token, String name, String description, String menuName) {
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

    private void setMenuName(String menuName) {
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
