/*******************************************************************************
 * Copyright (C) 2009, 2013 BonitaSoft S.A.
 * BonitaSoft is a trademark of BonitaSoft SA.
 * This software file is BONITASOFT CONFIDENTIAL. Not For Distribution.
 * For commercial licensing information, contact:
 *      BonitaSoft, 32 rue Gustave Eiffel – 38000 Grenoble
 *      or BonitaSoft US, 51 Federal Street, Suite 305, San Francisco, CA 94107
 *******************************************************************************/
package org.bonitasoft.web.rest.model.portal.profile;

/**
 * @author Fabio Lombardi
 *
 */
public class BonitaPageItem{  
    
    private String name;
    private String token;
    private String description;
    
    public BonitaPageItem(String name, String token, String description) {
        setName(name);
        setToken(token);
        setDescription(description);
    }
    
    public void setName(final String name) {
        this.name = name;
    }
    
    public void setToken(final String token) {
        this.token = token;
    }
    
    public void setDescription(final String description) {
        this.description = description;
    }
    
    public String getName() {
        return name;
    }
    
    public String getToken() {
        return token;
    }
    
    public String getDescription() {
        return description;
    }    
}