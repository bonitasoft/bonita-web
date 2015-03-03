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
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package org.bonitasoft.forms.server.accessor.widget;

import java.util.List;
import java.util.Map;
import java.util.Set;

import org.bonitasoft.engine.bpm.data.DataDefinition;
import org.bonitasoft.engine.bpm.document.DocumentDefinition;
import org.bonitasoft.forms.client.model.DataFieldDefinition;
import org.bonitasoft.forms.client.model.FormAction;
import org.bonitasoft.forms.client.model.FormWidget;
import org.bonitasoft.forms.server.exception.InvalidFormDefinitionException;

/**
 * @author Anthony Birembaut
 * 
 */
public interface IEngineWidgetBuilder {

    /**
     * create a widget data object
     * 
     * @param dataFieldDefinitions
     *            the Set of {@link DataFieldDefinition}
     * @param widgetIdPrefix
     *            a prefix for the id of the widget
     * @param isEditMode
     *            indicate if the form is in edit mode
     * @param includeInitialValue
     *            indicate if the initial values of the data should be included to the widget devinition or not
     * @return a Map {@link FormWidget} {@link FormAction}
     */
    public Map<FormWidget, FormAction> createWidgets(Set<DataDefinition> dataFieldDefinitions, String widgetIdPrefix, boolean isEditMode,
            boolean includeInitialValue);

    /**
     * @param attachments
     *            dataFieldDefinitions the list of {@link DocumentDefinition}
     * @param widgetIdPrefix
     *            a prefix for the id of the widget
     * @return a Map {@link FormWidget} {@link FormAction}
     */
    public Map<FormWidget, FormAction> createAttachmentWidgets(Set<DocumentDefinition> attachments, String widgetIdPrefix, boolean isEditMode);

    /**
     * @param pageId
     *            the page Id
     * @param nbOfPages
     *            the total nb of pages for the page flow
     * @param widgets
     *            the full {@link List} of {@link FormWidget} for the page flow (except for the buttons)
     * @param widgetIdPrefix
     *            a prefix for the id of the widget
     * @param isEditMode
     *            true if the edit page is required, false if it's the view page
     * @param isEditMode
     * @return the list of form widgets to diplay in the page
     * @throws InvalidFormDefinitionException
     */
    public List<FormWidget> getPageWidgets(String pageId, int nbOfPages, List<FormWidget> widgets, String widgetIdPrefix, boolean isEditMode)
            throws InvalidFormDefinitionException;

}
