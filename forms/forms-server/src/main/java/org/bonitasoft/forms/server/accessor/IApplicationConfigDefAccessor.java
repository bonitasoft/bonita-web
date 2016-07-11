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
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package org.bonitasoft.forms.server.accessor;

import java.util.List;

import org.bonitasoft.forms.client.model.Expression;
import org.bonitasoft.forms.server.exception.InvalidFormDefinitionException;

/**
 * @author Haojie Yuan
 *
 */
public interface IApplicationConfigDefAccessor {

    /**
     * Retrieve the location of the application error template
     *
     * @return the path to the application error template
     */
    String getApplicationErrorTemplate();

    /**
     * get the application label
     *
     * @return the label of the application as an expression
     * @throws InvalidFormDefinitionException
     *             if the application label cannot be found
     */
    Expression getApplicationLabelExpression() throws InvalidFormDefinitionException;

    /**
     * Retrieve the application mandatory field label as an expression
     *
     * @return the label to indicate that a form field is mandatory
     * @throws InvalidFormDefinitionException
     */
    Expression getApplicationMandatoryLabelExpression() throws InvalidFormDefinitionException;

    /**
     * Retrieve the application mandatory field symbol as an expression
     *
     * @return the symbol to indicate that a form field is mandatory
     * @throws InvalidFormDefinitionException
     */
    Expression getApplicationMandatorySymbolExpression() throws InvalidFormDefinitionException;

    /**
     * Retrieve the CSS classes associated with mandatory symbol
     *
     * @return the CSS classes associated with mandatory symbol
     */
    String getApplicationMandatorySymbolStyle();

    /**
     * Retrieve the application name
     *
     * @return the application name
     * @throws InvalidFormDefinitionException
     */
    String getApplicationName() throws InvalidFormDefinitionException;

    /**
     * Retrieve the application version
     *
     * @return the application version
     * @throws InvalidFormDefinitionException
     */
    String getApplicationVersion() throws InvalidFormDefinitionException;

    /**
     * Retrieve the path to the application layout in the classpath
     *
     * @return the path to the application layOut in the classpath
     */
    String getApplicationLayout();

    /**
     * Retrieve the application permission string
     *
     * @return the application permission
     */
    String getApplicationPermissions();

    /**
     * Retrieve the migration product version string
     *
     * @return the migration product version
     */
    String getMigrationProductVersion();

    /**
     * Retrieve the version of the product used to design the form
     *
     * @return the product version
     */
    String getProductVersion();

    /**
     * Retrieve the list of IDs of forms of the application
     *
     * @return a List of form IDs (as String)
     */
    List<String> getApplicationFormsList();
}
