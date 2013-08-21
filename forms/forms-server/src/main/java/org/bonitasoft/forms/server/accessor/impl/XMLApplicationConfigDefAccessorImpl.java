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
package org.bonitasoft.forms.server.accessor.impl;

import org.bonitasoft.engine.expression.ExpressionType;
import org.bonitasoft.forms.client.model.Expression;
import org.bonitasoft.forms.server.accessor.DefaultFormsPropertiesFactory;
import org.bonitasoft.forms.server.accessor.IApplicationConfigDefAccessor;
import org.bonitasoft.forms.server.accessor.impl.util.XPathUtil;
import org.bonitasoft.forms.server.accessor.widget.impl.XMLExpressionsUtil;
import org.bonitasoft.forms.server.constants.XMLForms;
import org.bonitasoft.forms.server.exception.InvalidFormDefinitionException;
import org.w3c.dom.Document;
import org.w3c.dom.Node;

import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author Haojie Yuan, Anthony Birembaut
 * 
 */
public class XMLApplicationConfigDefAccessorImpl extends XPathUtil implements IApplicationConfigDefAccessor {

    /**
     * Logger
     */
    private static Logger LOGGER = Logger.getLogger(XMLApplicationConfigDefAccessorImpl.class.getName());

    /**
     * DOM representation of the XML file
     */
    private final Document document;

    /**
     * The xpath query to get the process node
     */
    protected String applicationXpath;

    /**
     * The tenant ID
     */
    protected long tenantID;

    /**
     * Util class to parse expressions
     */
    protected XMLExpressionsUtil xmlExpressionsUtil;

    /**
     * Constructor
     * 
     * @param tenantID
     * @param document
     */
    public XMLApplicationConfigDefAccessorImpl(final long tenantID, final Document document) {

        this.tenantID = tenantID;
        this.document = document;
        
        xmlExpressionsUtil = XMLExpressionsUtil.getInstance();

        final StringBuilder applicationXpathBuilder = new StringBuilder();
        applicationXpathBuilder.append("//");
        applicationXpathBuilder.append(XMLForms.APPLICATION);
        applicationXpath = applicationXpathBuilder.toString();
    }

    /**
     * {@inheritDoc}
     */
    public String getApplicationErrorTemplate() {
        String applicationErrorTemplate = null;

        final String xpath = "//" + XMLForms.ERROR_TEMPLATE;

        final Node applicationErrorTemplateNode = getNodeByXpath(document, xpath);
        if (applicationErrorTemplateNode == null) {
            if (LOGGER.isLoggable(Level.FINE)) {
                LOGGER.log(Level.FINE, "No error template element was found in the definition file. The default error layout will be used.");
            }
            applicationErrorTemplate = DefaultFormsPropertiesFactory.getDefaultFormProperties(tenantID).getPageErrorTemplate();
        } else {
            applicationErrorTemplate = applicationErrorTemplateNode.getTextContent();
        }
        return applicationErrorTemplate;
    }

    /**
     * {@inheritDoc}
     */
    public Expression getApplicationLabelExpression() throws InvalidFormDefinitionException {

        Expression applicationLabelExpression = null;

        final String xpath = applicationXpath + "/" + XMLForms.APPLICATION_LABEL;

        final Node processLabelNode = getNodeByXpath(document, xpath);
        if (processLabelNode == null) {
            if (LOGGER.isLoggable(Level.SEVERE)) {
                LOGGER.log(Level.SEVERE, "Failed to retrieve application label element. query : " + xpath);
            }
            throw new InvalidFormDefinitionException("The application label was not found in the forms definition file");
        } else {
            applicationLabelExpression = xmlExpressionsUtil.parseExpression(processLabelNode);
        }
        return applicationLabelExpression;
    }

    /**
     * {@inheritDoc}
     */
    public Expression getApplicationMandatoryLabelExpression() throws InvalidFormDefinitionException {

        Expression applicationMandatoryLabelExpression = null;

        final String xpath = applicationXpath + "/" + XMLForms.MANDATORY_LABEL;

        final Node processMandatorySymbolNode = getNodeByXpath(document, xpath);
        if (processMandatorySymbolNode == null) {
            if (LOGGER.isLoggable(Level.FINE)) {
                LOGGER.log(Level.FINE, "No mandatory label element was found in the definition file. Default label will be used.");
            }
            final String defaultMandatoryLabel = DefaultFormsPropertiesFactory.getDefaultFormProperties(tenantID).getApplicationMandatoryLabel();
            applicationMandatoryLabelExpression = new Expression(null, defaultMandatoryLabel, ExpressionType.TYPE_CONSTANT.name(), String.class.getName(), null, new ArrayList<Expression>());
        } else {
            applicationMandatoryLabelExpression = xmlExpressionsUtil.parseExpression(processMandatorySymbolNode);
        }
        return applicationMandatoryLabelExpression;
    }

    /**
     * {@inheritDoc}
     */
    public Expression getApplicationMandatorySymbolExpression() throws InvalidFormDefinitionException {

        Expression applicationMandatorySymbolExpression = null;

        final String xpath = applicationXpath + "/" + XMLForms.MANDATORY_SYMBOL;

        final Node applicationMandatorySymbolNode = getNodeByXpath(document, xpath);
        if (applicationMandatorySymbolNode == null) {
            if (LOGGER.isLoggable(Level.FINE)) {
                LOGGER.log(Level.FINE, "No mandatory symbol element was found in the definition file. Default symbol will be used.");
            }
            final String defaultMandatorySymbol = DefaultFormsPropertiesFactory.getDefaultFormProperties(tenantID).getApplicationMandatorySymbol();
            applicationMandatorySymbolExpression = new Expression(null, defaultMandatorySymbol, ExpressionType.TYPE_CONSTANT.name(), String.class.getName(), null, new ArrayList<Expression>());
        } else {
            applicationMandatorySymbolExpression = xmlExpressionsUtil.parseExpression(applicationMandatorySymbolNode);
        }
        return applicationMandatorySymbolExpression;
    }

    /**
     * {@inheritDoc}
     */
    public String getApplicationMandatorySymbolStyle() {

        String applicationMandatorySymbolClasses = null;

        final String xpath = applicationXpath + "/" + XMLForms.MANDATORY_STYLE;

        final Node applicationMandatorySymbolClassesNode = getNodeByXpath(document, xpath);
        if (applicationMandatorySymbolClassesNode == null) {
            if (LOGGER.isLoggable(Level.FINE)) {
                LOGGER.log(Level.FINE, "No CSS classes were found in the definition file for the application mandatory symbol element. Default style will be used");
            }
        } else {
            applicationMandatorySymbolClasses = applicationMandatorySymbolClassesNode.getTextContent();
        }
        return applicationMandatorySymbolClasses;
    }

    /**
     * {@inheritDoc}
     */
    public String getApplicationName() throws InvalidFormDefinitionException {
        String applicationName = null;

        final String xpath = "//" + XMLForms.APPLICATION;

        final Node applicationNode = getNodeByXpath(document, xpath);
        if (applicationNode == null) {
            if (LOGGER.isLoggable(Level.SEVERE)) {
                LOGGER.log(Level.SEVERE, "Failed to retrieve application element. query : " + xpath);
            }
            throw new InvalidFormDefinitionException("The application was not found in the forms definition file");
        } else {
            applicationName = getStringByXpath(applicationNode, "@" + XMLForms.NAME);
        }
        return applicationName;
    }

    /**
     * {@inheritDoc}
     */
    public String getApplicationVersion() throws InvalidFormDefinitionException {
        String applicationVersion = null;

        final String xpath = "//" + XMLForms.APPLICATION;

        final Node applicationNode = getNodeByXpath(document, xpath);
        if (applicationNode == null) {
            if (LOGGER.isLoggable(Level.SEVERE)) {
                LOGGER.log(Level.SEVERE, "Failed to retrieve application element. query : " + xpath);
            }
            throw new InvalidFormDefinitionException("The application was not found in the forms definition file");
        } else {
            applicationVersion = getStringByXpath(applicationNode, "@" + XMLForms.VERSION);
        }
        return applicationVersion;
    }

    /**
     * {@inheritDoc}
     */
    public String getApplicationLayout() {
        String applicationLayout = null;

        final String xpath = "//" + XMLForms.APPLICATION_LAYOUT;

        final Node applicationTemplateNode = getNodeByXpath(document, xpath);
        if (applicationTemplateNode == null) {
            if (LOGGER.isLoggable(Level.FINE)) {
                LOGGER.log(Level.FINE, "No process template element was found in the definition file. Default process template will be used.");
            }
            applicationLayout = DefaultFormsPropertiesFactory.getDefaultFormProperties(tenantID).getApplicationLayout();
        } else {
            applicationLayout = applicationTemplateNode.getTextContent();
        }
        return applicationLayout;
    }

    /**
     * {@inheritDoc}
     */
    public String getApplicationPermissions() {
        String permissions = null;
        Node permissionsNode = null;

        final String applicationXpath = "//" + XMLForms.APPLICATION;
        final Node applicationNode = getNodeByXpath(document, applicationXpath);

        if (applicationNode == null) {
            if (LOGGER.isLoggable(Level.WARNING)) {
                LOGGER.log(Level.WARNING, "Failed to retrieve application element. query : " + applicationXpath);
            }
        } else {
            permissionsNode = getNodeByXpath(applicationNode, XMLForms.PERMISSIONS);
            if (permissionsNode == null) {
                if (LOGGER.isLoggable(Level.INFO)) {
                    LOGGER.log(Level.INFO, "Failed to retrieve application permissions. the permission element is missing from the definition file.");
                }
            } else {
                permissions = permissionsNode.getTextContent();
            }
        }
        return permissions;
    }

    /**
     * {@inheritDoc}
     */
    public String getMigrationProductVersion() {

        String migrationProductVersion = null;

        final String xpath = "//" + XMLForms.MIGRATION_PRODUCT_VERSION;

        final Node migrationProductVersionNode = getNodeByXpath(document, xpath);
        if (migrationProductVersionNode == null) {
            if (LOGGER.isLoggable(Level.WARNING)) {
                LOGGER.log(Level.WARNING, "Failed to retrieve migration product version element. query : " + xpath);
            }
        } else {
            migrationProductVersion = migrationProductVersionNode.getTextContent();
        }
        return migrationProductVersion;
    }

    /**
     * {@inheritDoc}
     */
    public String getHomePage() {

        String homePage = null;
        final String xpath = "//" + XMLForms.HOME_PAGE;
        final Node homePageNode = getNodeByXpath(document, xpath);
        homePage = homePageNode.getTextContent();
        return homePage;
    }

    /**
     * {@inheritDoc}
     */
    public String getProductVersion() {

        String productVersion = null;

        final String xpath = "//" + XMLForms.FORMS_DEFINITION;

        final Node formsDefinitionNode = getNodeByXpath(document, xpath);
        if (formsDefinitionNode == null) {
            productVersion = null;
        } else {
            productVersion = getStringByXpath(formsDefinitionNode, "@" + XMLForms.PRODUCT_VERSION);
        }
        return productVersion;
    }
}
