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
package org.bonitasoft.forms.server.provider.impl.util;

import java.util.logging.Level;
import java.util.logging.Logger;

import org.bonitasoft.console.common.server.preferences.properties.PropertiesFactory;
import org.bonitasoft.forms.server.accessor.DefaultFormsPropertiesFactory;
import org.bonitasoft.forms.server.exception.FormServiceProviderNotFoundException;
import org.bonitasoft.forms.server.provider.FormServiceProvider;

/**
 * @author QiXiang Zhang
 * 
 */
public class FormServiceProviderFactory {

    /**
     * Logger
     */
    private static Logger LOGGER = Logger.getLogger(FormServiceProviderFactory.class.getName());

    public static FormServiceProvider getFormServiceProvider(final long tenantID) throws FormServiceProviderNotFoundException {
        long tenantToUse = tenantID;
        if (tenantID == -1) {
            String tenantIDStr = PropertiesFactory.getPlatformTenantConfigProperties().getDefaultTenantId();
            if (tenantIDStr != null) {
                tenantToUse = Long.parseLong(tenantIDStr);
            }
        }
        FormServiceProvider formServiceProvider = null;
        String formServiceProviderName = null;
        try {
            formServiceProviderName = DefaultFormsPropertiesFactory.getDefaultFormProperties(tenantToUse).getFormServiceProviderImpl();
            formServiceProvider = (FormServiceProvider) Class.forName(formServiceProviderName).newInstance();
        } catch (Exception e) {
            String message = "The Form Service Provider implementation " + formServiceProviderName + " does not exist!";
            if (LOGGER.isLoggable(Level.SEVERE)) {
                LOGGER.log(Level.SEVERE, message, e);
            }
            throw new FormServiceProviderNotFoundException(message);
        }
        return formServiceProvider;
    }

}
