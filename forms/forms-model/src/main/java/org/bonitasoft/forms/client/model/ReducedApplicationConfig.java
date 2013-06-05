/**
 * Copyright (C) 2011 BonitaSoft S.A.
 * BonitaSoft, 31 rue Gustave Eiffel - 38000 Grenoble
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 2.0 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.bonitasoft.forms.client.model;

import java.io.Serializable;

/**
 * Form configuration data for an application
 * 
 * @author Chong Zhao, Anthony Birembaut
 */
public class ReducedApplicationConfig implements Serializable {

    /**
     * UID
     */
    private static final long serialVersionUID = -7591064386168731706L;
    
    /**
     * application label
     */
    private String applicationLabel;
    
    /**
     * application template
     */
    private ReducedHtmlTemplate applicationLayout;
    
    /**
     * mandatory field symbol
     */
    private String mandatorySymbol;
    
    /**
     * mandatory field label
     */
    private String mandatoryLabel;
    
    /**
     * mandatory field symbol CSS classes
     */
    private String mandatoryStyle;
    
    /**
     * The userXP URL
     */
    private String userXPURL;
    
    /**
     * Constructor
     * @param mandatoryStyle
     * @param userXPURL
     */
    public ReducedApplicationConfig(final String mandatoryStyle, final String userXPURL) {
        this.mandatoryStyle = mandatoryStyle;
        this.userXPURL = userXPURL;
    }
    
    /**
     * Default Constructor
     */
    public ReducedApplicationConfig(){
        super();
        // Mandatory for serialization
    }

    public ReducedHtmlTemplate getApplicationLayout() {
        return applicationLayout;
    }

    public void setApplicationLayout(final ReducedHtmlTemplate applicationLayout) {
        this.applicationLayout = applicationLayout;
    }

    public String getMandatorySymbol() {
        return mandatorySymbol;
    }

    public void setMandatorySymbol(final String mandatorySymbol) {
        this.mandatorySymbol = mandatorySymbol;
    }

    public String getMandatoryStyle() {
        return mandatoryStyle;
    }

    public void setMandatoryStyle(final String mandatoryStyle) {
        this.mandatoryStyle = mandatoryStyle;
    }

    public String getMandatoryLabel() {
        return mandatoryLabel;
    }

    public void setMandatoryLabel(final String mandatoryLabel) {
        this.mandatoryLabel = mandatoryLabel;
    }

    public String getApplicationLabel() {
        return applicationLabel;
    }

    public void setApplicationLabel(final String applicationLabel) {
        this.applicationLabel = applicationLabel;
    }

    public String getUserXPURL() {
        return userXPURL;
    }

    public void setUserXPURL(final String userXPURL) {
        this.userXPURL = userXPURL;
    }
    
}
