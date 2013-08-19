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
package org.bonitasoft.forms.server.accessor;

import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.bonitasoft.engine.session.APISession;
import org.bonitasoft.forms.server.accessor.impl.EngineApplicationFormDefAccessorImpl;
import org.bonitasoft.forms.server.accessor.impl.XMLApplicationFormDefAccessorImpl;
import org.bonitasoft.forms.server.exception.ApplicationFormDefinitionNotFoundException;
import org.w3c.dom.Document;

/**
 * @author Anthony Birembaut
 * 
 */
public class FormDefAccessorFactory {

    /**
     * Logger
     */
    private static Logger LOGGER = Logger.getLogger(FormDefAccessorFactory.class.getName());

    public static XMLApplicationFormDefAccessorImpl getXMLApplicationFormDefAccessor(final APISession session, final long processDefinitionID,
            final Document document, final String formId, final String locale, final Date processDeploymentDate)
            throws ApplicationFormDefinitionNotFoundException {
        return new XMLApplicationFormDefAccessorImpl(session.getTenantId(), document, formId, locale, processDeploymentDate);
    }

    public static EngineApplicationFormDefAccessorImpl getEngineApplicationFormDefAccessor(final APISession session, final long processDefinitionID,
            final long activityInstanceID, final boolean includeApplicationVariables, final boolean isEditMode, final boolean isCurrentValue,
            final boolean isConfirmationPage) throws ApplicationFormDefinitionNotFoundException {
        if (DefaultFormsPropertiesFactory.getDefaultFormProperties(session.getTenantId()).autoGenerateForms()) {
            return new EngineApplicationFormDefAccessorImpl(session, processDefinitionID, activityInstanceID, includeApplicationVariables, isEditMode,
                    isCurrentValue, isConfirmationPage);
        } else {
            final String message = "Automatic form access is not allowed. Check the configuration file forms_config.xml";
            if (LOGGER.isLoggable(Level.INFO)) {
                LOGGER.log(Level.INFO, message);
            }
            throw new ApplicationFormDefinitionNotFoundException(message);
        }
    }

}
