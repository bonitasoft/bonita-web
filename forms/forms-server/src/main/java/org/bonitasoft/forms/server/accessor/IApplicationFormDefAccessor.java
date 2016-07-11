/**
 * Copyright (C) 2009 BonitaSoft S.A.
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
package org.bonitasoft.forms.server.accessor;

import java.util.List;

import org.bonitasoft.forms.client.model.Expression;
import org.bonitasoft.forms.client.model.FormAction;
import org.bonitasoft.forms.client.model.FormType;
import org.bonitasoft.forms.client.model.FormValidator;
import org.bonitasoft.forms.client.model.FormWidget;
import org.bonitasoft.forms.client.model.TransientData;
import org.bonitasoft.forms.server.exception.ApplicationFormDefinitionNotFoundException;
import org.bonitasoft.forms.server.exception.InvalidFormDefinitionException;

/**
 * Accessor API allowing to retrieve the configuration of a Forms application
 * 
 * @author Anthony Birembaut, Haojie Yuan
 */
public interface IApplicationFormDefAccessor {
    
    /**
     * @return a list of ids of the pages of the form 
     */
    List<String> getPages();
    
    /**
     * @return the form first page name
     * @throws InvalidFormDefinitionException 
     */
    Expression getFirstPageExpression() throws InvalidFormDefinitionException;
        
    /**
     * @return the form permission
     * @throws InvalidFormDefinitionException 
     */
    String getFormPermissions() throws InvalidFormDefinitionException;
    
    /**
     * @return the next form
     * @throws InvalidFormDefinitionException 
     */
    String getNextForm() throws InvalidFormDefinitionException;
    
    /**
     * @param pageId the page ID
     * @return the entry page-layout
     * @throws InvalidFormDefinitionException
     */
    String getFormPageLayout(String pageId) throws InvalidFormDefinitionException ;
        
    /**
     * @param pageId the page ID
     * @return a list of {@link FormWidget} composing the page
     * @throws InvalidFormDefinitionException 
     * @throws ApplicationFormDefinitionNotFoundException
     */
    List<FormWidget> getPageWidgets(String pageId) throws InvalidFormDefinitionException, ApplicationFormDefinitionNotFoundException;
    
    /**
     * Retrieve the page validators
     * @param pageId the page ID
     * @return a List of {@link FormValidator}
     * @throws InvalidFormDefinitionException
     * @throws ApplicationFormDefinitionNotFoundException
     */
    List<FormValidator> getPageValidators(String pageId) throws InvalidFormDefinitionException, ApplicationFormDefinitionNotFoundException;
    
    /**
     * @param pageId the page ID
     * @return the page label
     * @throws InvalidFormDefinitionException if the page or its label cannot be found
     */
    Expression getPageLabelExpression(String pageId) throws InvalidFormDefinitionException;
    
    /**
     * @param pageId the page ID
     * @return true if HTML is allowed in the page label, false otherwise
     * @throws InvalidFormDefinitionException 
     */
    boolean isHTMLAllowedInLabel(String pageId) throws InvalidFormDefinitionException;
    
    /**
     * Retrieve the expression to evaluate to get the page after the current page
     * @param pageId current page ID
     * @return an expression to evaluate
     * @throws InvalidFormDefinitionException
     */
    Expression getNextPageExpression(String pageId) throws InvalidFormDefinitionException;
    
    /**
     * Retrieve the list of transient data for the form
     * @return a List of {@link TransientData}
     * @throws InvalidFormDefinitionException 
     */
    List<TransientData> getTransientData() throws InvalidFormDefinitionException;
        
    /**
     * Retrieve the list of actions associated with the required page
     * @param pageId the pages from which the actions are required 
     * @return a list of {@link FormAction}
     * @throws InvalidFormDefinitionException if the activity/process or its actions cannot be found
     * @throws ApplicationFormDefinitionNotFoundException 
     */
    List<FormAction> getActions(String pageId) throws InvalidFormDefinitionException, ApplicationFormDefinitionNotFoundException;
    
    /**
     * @return the path to the page flow confirmation page in the classpath
     * @throws InvalidFormDefinitionException 
     */
    String getConfirmationLayout() throws InvalidFormDefinitionException;
    
    /**
     * @return the message for the confirmation layout
     * @throws InvalidFormDefinitionException 
     */
    Expression getConfirmationMessageExpression() throws InvalidFormDefinitionException;
    
    /**
     * Get the form type
     * @return the {@link FormType}
     * @throws InvalidFormDefinitionException 
     */
    FormType getFormType() throws InvalidFormDefinitionException;
}
