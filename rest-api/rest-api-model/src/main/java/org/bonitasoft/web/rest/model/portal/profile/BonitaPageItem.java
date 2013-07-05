/*******************************************************************************
 * Copyright (C) 2009, 2013 BonitaSoft S.A.
 * BonitaSoft is a trademark of BonitaSoft SA.
 * This software file is BONITASOFT CONFIDENTIAL. Not For Distribution.
 * For commercial licensing information, contact:
 *      BonitaSoft, 32 rue Gustave Eiffel – 38000 Grenoble
 *      or BonitaSoft US, 51 Federal Street, Suite 305, San Francisco, CA 94107
 *******************************************************************************/
package org.bonitasoft.web.rest.model.portal.profile;

import org.bonitasoft.web.toolkit.client.data.item.IItem;
import org.bonitasoft.web.toolkit.client.data.item.Item;
import org.bonitasoft.web.toolkit.client.data.item.ItemDefinition;
import org.bonitasoft.web.toolkit.client.data.item.template.ItemHasUniqueId;

/**
 * @author Fabio Lombardi
 *
 */
public class BonitaPageItem extends Item implements ItemHasUniqueId{  
    
    /*private String name;
    private String token;
    private String description;
    */
    
    public static final String ATTRIBUTE_NAME = "name";
    public static final String ATTRIBUTE_TOKEN = "token";
    public static final String ATTRIBUTE_DESCRIPTION = "description";
    
    public BonitaPageItem(Long id, String name, String token, String description) {
        setId(id);
        setName(name);
        setToken(token);
        setDescription(description);
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
    
    public String getName() {
        return this.getAttributeValue(ATTRIBUTE_NAME);
    }
    
    public String getToken() {
        return this.getAttributeValue(ATTRIBUTE_TOKEN);
    }
    
    public String getDescription() {
        return this.getAttributeValue(ATTRIBUTE_DESCRIPTION);
    }
       
    @Override
    public void setId(final String id) {
        this.setAttribute("id", id);
    }

    @Override
    public void setId(final Long id) {
        this.setAttribute("id", id.toString());
    }
    
    
    @Override
    public ItemDefinition<BonitaPageItem> getItemDefinition() {
        return new BonitaPageDefinition();
    }    
}