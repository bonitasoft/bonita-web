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
public class ApplicationConfig implements Serializable {

    /**
     * UID
     */
    private static final long serialVersionUID = -7591064386168731706L;
    
    /**
     * application label expression
     */
    private Expression applicationLabelExpression;
    
    /**
     * application template
     */
    private HtmlTemplate applicationLayout;
    
    /**
     * mandatory field symbol expression
     */
    private Expression mandatorySymbolExpression;
    
    /**
     * mandatory field label expression
     */
    private Expression mandatoryLabelExpression;
    
    /**
     * The reduced version of the application config
     */
    private ReducedApplicationConfig reducedApplicationConfig;
    
    /**
     * Constructor
     * @param applicationLabelExpression
     * @param mandatorySymbolExpression
     * @param mandatoryLabelExpression
     * @param mandatoryStyle
     * @param userXPURL
     */
    public ApplicationConfig(final Expression applicationLabelExpression, final Expression mandatorySymbolExpression, final Expression mandatoryLabelExpression, final String mandatoryStyle, final String userXPURL) {
        this.reducedApplicationConfig = new ReducedApplicationConfig(mandatoryStyle, userXPURL);
        this.mandatorySymbolExpression = mandatorySymbolExpression;
        this.mandatoryLabelExpression = mandatoryLabelExpression;
        this.applicationLabelExpression = applicationLabelExpression;
    }
    
    /**
     * Default Constructor
     * Mandatory for serialization
     */
    public ApplicationConfig(){
        super();
        reducedApplicationConfig = new ReducedApplicationConfig();
    }

    public HtmlTemplate getApplicationLayout() {
        return applicationLayout;
    }

    public void setApplicationLayout(final HtmlTemplate applicationLayout) {
        this.applicationLayout = applicationLayout;
        ReducedHtmlTemplate reducedApplicationLayout = null;
        if (applicationLayout != null) {
            reducedApplicationLayout = applicationLayout.getReducedHtmlTemplate();
        }
        reducedApplicationConfig.setApplicationLayout(reducedApplicationLayout);
    }

    public String getMandatorySymbol() {
        return reducedApplicationConfig.getMandatorySymbol();
    }

    public void setMandatorySymbol(final String mandatorySymbol) {
        reducedApplicationConfig.setMandatorySymbol(mandatorySymbol);
    }

    public String getMandatoryStyle() {
        return reducedApplicationConfig.getMandatoryStyle();
    }

    public void setMandatoryStyle(final String mandatoryStyle) {
        reducedApplicationConfig.setMandatoryStyle(mandatoryStyle);
    }

    public String getMandatoryLabel() {
        return reducedApplicationConfig.getMandatoryLabel();
    }

    public void setMandatoryLabel(final String mandatoryLabel) {
        reducedApplicationConfig.setMandatoryLabel(mandatoryLabel);
    }

    public String getApplicationLabel() {
        return reducedApplicationConfig.getApplicationLabel();
    }

    public void setApplicationLabel(final String applicationLabel) {
        reducedApplicationConfig.setApplicationLabel(applicationLabel);
    }

    public String getUserXPURL() {
        return reducedApplicationConfig.getUserXPURL();
    }

    public void setUserXPURL(final String userXPURL) {
        reducedApplicationConfig.setUserXPURL(userXPURL);
    }
    
    public Expression getApplicationLabelExpression() {
        return applicationLabelExpression;
    }
    
    public void setApplicationLabelExpression(Expression applicationLabelExpression) {
        this.applicationLabelExpression = applicationLabelExpression;
    }
    
    public Expression getMandatorySymbolExpression() {
        return mandatorySymbolExpression;
    }
    
    public void setMandatorySymbolExpression(Expression mandatorySymbolExpression) {
        this.mandatorySymbolExpression = mandatorySymbolExpression;
    }
    
    public Expression getMandatoryLabelExpression() {
        return mandatoryLabelExpression;
    }
    
    public void setMandatoryLabelExpression(Expression mandatoryLabelExpression) {
        this.mandatoryLabelExpression = mandatoryLabelExpression;
    }
    
    public ReducedApplicationConfig getReducedApplicationConfig() {
        return reducedApplicationConfig;
    }
    
    public void setReducedApplicationConfig(ReducedApplicationConfig reducedApplicationConfig) {
        this.reducedApplicationConfig = reducedApplicationConfig;
    }
    
}
