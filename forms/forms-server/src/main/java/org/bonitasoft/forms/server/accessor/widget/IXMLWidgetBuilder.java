/**
 * Copyright (C) 2010 BonitaSoft S.A.
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
package org.bonitasoft.forms.server.accessor.widget;

import java.util.List;

import org.bonitasoft.forms.client.model.FormAction;
import org.bonitasoft.forms.client.model.FormValidator;
import org.bonitasoft.forms.client.model.FormWidget;
import org.bonitasoft.forms.server.exception.InvalidFormDefinitionException;
import org.w3c.dom.Node;

/**
 * @author Anthony Birembaut
 *
 */
public interface IXMLWidgetBuilder {

    /**
     * Read a page node and return the list of {@link FormValidator} it contains
     * @param pageNode the page node
     * @return a {@link List} of {@link FormValidator} Object
     * @throws InvalidFormDefinitionException
     */
    List<FormValidator> getPageValidators(Node pageNode) throws InvalidFormDefinitionException;
    
    /**
     * Read a page node and return the list of {@link FormWidget} it contains
     * @param pageNode the page node
     * @param isEditMode 
     * @return a {@link List} of {@link FormWidget} Object
     */
    List<FormWidget> getPageWidgets(Node pageNode, boolean isEditMode);
    
    /**
     * Read a node and return the list of {@link FormAction} it contains
     * @param parentNode the parent node of the actions
     * @param pageId the page of the actions required
     * @return a {@link List} of {@link FormAction} objects
     * @throws InvalidFormDefinitionException
     */
    List<FormAction> getActions(Node parentNode, String pageId) throws InvalidFormDefinitionException;

}
